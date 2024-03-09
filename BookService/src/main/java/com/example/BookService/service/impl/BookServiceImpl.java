package com.example.BookService.service.impl;

import com.example.BookService.entity.Book;
import com.example.BookService.exception.*;
import com.example.BookService.model.BookBuyModel;
import com.example.BookService.model.BookModel;
import com.example.BookService.model.BookResponseModel;
import com.example.BookService.model.InventoryModel;
import com.example.BookService.repository.BookRepository;
import com.example.BookService.service.BookService;
import com.netflix.appinfo.InstanceInfo;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.BookService.mapper.BookMapper.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    String inventoryGateway = "";

    public String callOtherServiceName(String serviceName){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if(instances.isEmpty()){
            return "no such services";
        }
        else{
            ServiceInstance instance = instances.get(0);
            String hostName = instance.getServiceId();
            if(hostName.equals(serviceName)){
                int port = instance.getPort();
                String url = "http://localhost:"+port+"/book-inventory";
                return url;
            }
            return null;
        }
    }
    @Override
    @Transactional
    public ResponseEntity<String> createBook(BookModel bookModel) {
        if (StringUtils.isBlank(bookModel.getBookName()) || StringUtils.isBlank(bookModel.getAuthorName())|| StringUtils.isBlank(bookModel.getGenre())  ||
                bookModel.getPrice() <= 0 ||
                bookModel.getQuantity() <= 0) {
            throw new BookValidationException("All fields (except bookId) must have a value and price and quantity should be greater then 0!!!");
        }

        if (bookRepository.existsByBookNameAndAuthorName(bookModel.getBookName(), bookModel.getAuthorName())) {
            throw new BookAlreadyExistsException("Book already exists with title: " + bookModel.getBookName() + " and author: " + bookModel.getAuthorName());
        }

        try {

            Book book = bookModelToBook(bookModel);
            Book savedBook = bookRepository.save(book);

            InventoryModel inventoryModel = new InventoryModel(savedBook.getId(), bookModel.getPrice(), bookModel.getQuantity());

            //Creating http header to send put request then send object and create the book
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<InventoryModel> requestEntity = new HttpEntity<>(inventoryModel, headers);
            inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
            String url = inventoryGateway + "/update/" + savedBook.getId();
            URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

            ResponseEntity<InventoryModel> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, InventoryModel.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                bookRepository.delete(savedBook);
                throw new InventoryCreationException("Failed to create inventory for book: " + savedBook.getId());

            }
            return ResponseEntity.ok("Book Created successfully!!! with id: "+ savedBook.getId());
        } catch (Exception ex) {
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }else throw new BookException(ex.getMessage());
        }

    }

    @Override
    public List<BookResponseModel> getAllBook() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) throw new BookNotAvailableException("No book available!!!");
        List<Long> bookIds = books.stream()
                .map(Book::getId)
                .toList();
        try {
            String queryParams = bookIds.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            queryParams = "ids=" + queryParams;
            inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
            String requestUrl = UriComponentsBuilder.fromUriString(inventoryGateway).query(queryParams)
                    .toUriString();

            ResponseEntity<List<InventoryModel>> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            List<InventoryModel> inventoryModelList = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && inventoryModelList != null) {
                List<Book> bookList = bookRepository.findAllById(bookIds);
                List<BookResponseModel> bookResponseModelList = new ArrayList<>();

                for (Book book : bookList) {
                    InventoryModel inventoryModel = inventoryModelList.stream()
                            .filter(inv -> inv.getInventoryId().equals(book.getId()))
                            .findFirst()
                            .orElse(null);

                    assert inventoryModel != null;
                    BookResponseModel bookResponseModel = bookToBookResponse(book, inventoryModel);
                    bookResponseModelList.add(bookResponseModel);
                }
                return bookResponseModelList;
            } else {
                throw new InventoryDataAccessException("Failed to retrieve inventory data for book IDs");
            }
        }catch (Exception ex) {
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }else throw new BookException(ex.getMessage());
        }

    }


    @Override
    public BookResponseModel getBookById(Long bookId) {
//        BookResponseModel bookResponseModel= new BookResponseModel();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found."));
        inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
        try {
            ResponseEntity<InventoryModel> responseEntity = restTemplate
                    .getForEntity(inventoryGateway+"/" + book.getId(), InventoryModel.class);
            //check with the status code
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new BookNotAvailableException("Book not available in inventory");
            }
            InventoryModel inventoryModel = responseEntity.getBody();

            if (inventoryModel == null) {
                throw new BookNotAvailableException("Inventory data not found for the book");
            }
            //map to Book response and return
            return bookToBookResponse(book, inventoryModel);
        }catch (Exception ex){
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }  else throw new BookNotAvailableException(ex.getMessage());
        }
    }

    @Override
    public BookModel updateBook(BookModel bookModel) {
        Book existingBook = bookRepository.findById(bookModel.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookModel.getBookId()));
        existingBook.setBookName(bookModel.getBookName());
        existingBook.setAuthorName(bookModel.getAuthorName());
        existingBook.setGenre(bookModel.getGenre());

        Book updatedBook = bookRepository.save(existingBook);

        InventoryModel newInventoryModel = new InventoryModel(updatedBook.getId(), bookModel.getPrice(), bookModel.getQuantity());

        try {//Creating http header to send put request then send object and create the book
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<InventoryModel> requestEntity = new HttpEntity<>(newInventoryModel, headers);
            inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
            String url = inventoryGateway + "/update/" + updatedBook.getId();
            URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

            ResponseEntity<InventoryModel> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, InventoryModel.class);

            if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
                throw new InventoryCreationException("Failed to create inventory for book: " + updatedBook.getId());

            }
            return bookModel;
        }catch (Exception ex) {
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }else throw new BookException( ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        bookRepository.delete(book);

        try {// Make a DELETE request to delete the book from the inventory service
            inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
            ResponseEntity<Void> response = restTemplate.exchange(
                    inventoryGateway + "/delete/" + bookId,
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );

            // Check the response status code
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new InventoryDeletionException("Failed to delete book from inventory: " + bookId);
            }
            return ResponseEntity.ok("Book deleted successfully!! with book ID: " + bookId);
        }catch (Exception ex) {
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }else throw new BookException( ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> buyBook(BookBuyModel bookBuyModel) {
        if (bookBuyModel.getBuyId() ==null || bookBuyModel.getQuantity()<=0 ){
            throw new BookException("Book id can not be null and quantity must be greater then 0!!");
        }

        Book book = bookRepository.findById(bookBuyModel.getBuyId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookBuyModel.getBuyId()));
        inventoryGateway = callOtherServiceName("INVENTORY-SERVICE");
        try {
            ResponseEntity<InventoryModel> responseEntity = restTemplate
                    .getForEntity(inventoryGateway + "/" + book.getId(), InventoryModel.class);
            //check with the status code
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new BookNotAvailableException("Book not available in inventory");
            }
            InventoryModel inventoryModel = responseEntity.getBody();

            assert inventoryModel != null;
            if (bookBuyModel.getQuantity() >= inventoryModel.getQuantity()) {
                return ResponseEntity.badRequest().body("Can not buy! Only " + inventoryModel.getQuantity() + " books Available!");
            }


            //Creating http header to send put request then send object and create the book
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            InventoryModel newInventoryModel = new InventoryModel(inventoryModel.getInventoryId(), inventoryModel.getPrice(), inventoryModel.getQuantity() - bookBuyModel.getQuantity());

            HttpEntity<InventoryModel> requestEntity = new HttpEntity<>(newInventoryModel, headers);

            String url = inventoryGateway + "/update/" + inventoryModel.getInventoryId();
            URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

            ResponseEntity<InventoryModel> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, InventoryModel.class);

            if (response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.CREATED) {
                throw new InventoryCreationException("Failed to buy book due to inventory update failure: " + inventoryModel.getInventoryId());
            }


            return ResponseEntity.ok("Book purchase successful!!!");
        }catch (Exception ex) {
            if (ex instanceof RestClientException){
                throw new InventoryDataAccessException("Unable to access to Inventory!!! "+ex.getMessage());
            }else throw new BookException( ex.getMessage());
        }
    }


}
