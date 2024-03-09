package com.example.apigateway.apigateway.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter1;
    @Autowired
    RoleBasedFilter roleFilter;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book", r -> r.path("/book-service/book/**")
                        .filters(f->f.filter(filter1)
                                     .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("CUSTOMER","ADMIN")))))
                        .uri("lb://book-service"))
                .route("book-buy", r -> r.path("/book-service/book/buy")
                        .filters(f->f.filter(filter1)
                                .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("CUSTOMER")))))
                        .uri("lb://book-service"))
                .route("book-create", r->r.path("/book-service/create/**")
                            .filters(f->f.filter(filter1)
                                        .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("ADMIN")))))
                            .uri("lb://book-service"))
                .route("book-update", r->r.path("/book-service/update/**")
                        .filters(f->f.filter(filter1)
                                .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("ADMIN")))))
                        .uri("lb://book-service"))
                .route("book-delete", r->r.path("/book-service/delete/**")
                        .filters(f->f.filter(filter1)
                                .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("ADMIN")))))
                        .uri("lb://book-service"))
                .route("inventory", r->r.path("/book-inventory/**")
                        .filters(f-> f.filter(filter1)
                                      .filter(roleFilter.apply(new RoleBasedFilter.Config(Arrays.asList("ADMIN")))))
                        .uri("lb://inventory-service"))

//
//                .route("auth-app", r -> r.path("/api/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://auth-app"))
                .build();
    }
}
