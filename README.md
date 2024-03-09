# Java Mid Term Project - Team (BJIT SEEKERS)
# Online Book Shop Micro-Service Project

This project is an online book shop application implemented using microservices architecture. It features an API Gateway for secure communication, 
a Discovery Server for service identification, a Cloud Config Server for centralized configuration, and microservices for authentication, 
book management, and inventory control. Together, these components create a scalable and efficient solution for an online book shop.


## Features
* User registration and login with JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling
* Do some Crud Operation of Book
* Inter Communication between different services


## Technologies
- Spring Boot
- Spring Boot
- Spring Security
- JPA
- Netflix Eureka
- JSON Web Tokens (JWT)
- Gradle
- MySQL


## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Gradle 3+
* MySql


To build and run the project, follow these steps:

* Login to mySQL driver with user root and and provide the respective user's specific password.
* add database name in mySql as "authentications", "book_service" and  "inventory_db_mid"
* Clone the repository: `https://github.com/BJITAcademy2021/Java_MidTermProject_BJITSeekers.git`
* Open the settings.gradle file and ensure that all the service modules are included in the build.
* Open the application.properties files for each service and configure the necessary settings, such as the Eureka server URL and database connection details.
* Gradle will automatically handle the dependencies and run configuration for each service module. It will use Spring Boot's embedded server to start the services.

### Access the Application
You can access the application through the API Gateway at http://localhost:8090
The API Gateway will route the requests to the appropriate microservices based on the configured routes.

## Architecture

Our sample microservices-based system consists of the following modules:
- **gateway-service** - This module utilizes Spring Cloud to run a Spring Boot application, acting as a proxy/gateway within our architecture. It enables secure communication between clients and microservices.
- **config-service** - The Config Service employs Spring Cloud Config Server in the native mode to serve as a configuration server. Configuration files are stored on the classpath, providing centralized configuration management for the microservices.
- **discovery-service** - Depending on the example, the Discovery Service uses Spring Cloud Netflix Eureka as an embedded discovery server. It facilitates dynamic service discovery, allowing microservices to locate and communicate with each other.
- **authentication-service** - This module is responsible for user registration and login functionalities. After successful login, it generates a JSON Web Token (JWT) to authenticate and authorize the user for subsequent requests.
- **book-service** -  The Book Service is a microservice that handles CRUD operations related to book management. It relies on the Inventory Service for tasks associated with inventory management.
- **inventory-service** - The Inventory Service is a microservice that provides CRUD operations for managing book inventory. It serves as a dependency for the Book Service, assisting in inventory-related tasks.


## Functions 

| Mapping | Function          | URL                                           | Permission  | Request Body Input                                          |
|---------|-------------------|-----------------------------------------------|-------------|-------------------------------------------------------------|
| POST    | User Registration | http://localhost:8090/auth-app/register    | ALL         | userName, email, password, roles 
| POST    | User Login        | http://localhost:8090/auth-app/login       | ALL         | email, password                                             |
| GET     | View All Books    | http://localhost:8090/book-service/book/all    | ADMIN, USER |                                                             |
| GET     | View One Book     | http://localhost:8090/book-service/book/{id}   | ADMIN, USER |                                                             |
| POST    | Create One Book   | http://localhost:8090/book-service/create      | ADMIN       | bookName, authorName, genre, price, quantity                |
| PATCH   | Update One Book   | http://localhost:8090/book-service/update/{id} | ADMIN       | bookName, authorName, genre, price, quantity                |
| DELETE  | Delete One Book   | http://localhost:8090/book-service/delete/{id} | ADMIN       |                                                             |
| PATCH   | Buy One Book      | http://localhost:8090/book-service/book/buy    | USER        | bookId, quantity   

##  Contributors

We are the 4 members who hvae given effort to make this project:

| Name          | Email           | Phone           |
| ------------- | --------------- | --------------- |
| Habibullah Howlader    | habibullah.howlader@bjitacademy.com | 01926 690376   |
| Nazmul Haque| nazmul.haque@bjitacademy.com|  01313 452233   |
| Jubayer Bin Hedayet |  jubayer.hedayet@bjitacademy.com|  01723 452193   |
| Alamin Hossain|  alamin.hossain@bjitacademy.com | 01843 452123   |

                                         
## Reference 

For Reference you can read all this documents.

* [Official Gradle documentation](https://docs.gradle.org)
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/gradle-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

