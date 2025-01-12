package com.jeet.schedular.config;

import com.jeet.schedular.handler.HelloHandler;
import com.jeet.schedular.handler.ValidationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> route(HelloHandler handler) {
        return RouterFunctions
                .route(GET("/hello"), handler::sayHello)
                .andRoute(GET("/hello2"), handler::sayHello2)
                .andRoute(GET("/hello3"), handler::sayHelloWithQP);
    }

    @Bean
    public RouterFunction<ServerResponse> validateRoute(ValidationHandler handler) {
        return RouterFunctions
                .route(GET("/validate"), handler::validate)
                .andRoute(GET("/filter"), handler::parseQueryParams);
    }

}
