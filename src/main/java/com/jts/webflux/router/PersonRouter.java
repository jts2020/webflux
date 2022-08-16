package com.jts.webflux.router;

import com.jts.webflux.handle.PersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PersonRouter {

    @Bean
    public RouterFunction<ServerResponse> routeCity(PersonHandler personHandler) {
        return RouterFunctions.route()
                .GET("/hello", personHandler::helloPerson)
                .GET("/sse", personHandler::findAll)
                .GET("/web", personHandler::webClient)
                .GET("/find", personHandler::findByLastname)
                .build();
    }

}
