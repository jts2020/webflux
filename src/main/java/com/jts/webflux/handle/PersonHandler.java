package com.jts.webflux.handle;

import com.jts.webflux.bo.Person;
import com.jts.webflux.dao.PersonDao;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class PersonHandler {

    private final DatabaseClient client;

    private final PersonDao personDao;

    public PersonHandler(DatabaseClient client, PersonDao personDao) {
        this.client = client;
        this.personDao = personDao;
    }

    public Mono<ServerResponse> helloPerson(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue("Hello Person!"));
    }

    public Mono<ServerResponse> webClient(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(WebClient.create("www.baidu.com")
                        .get()
                        .retrieve()
                        .bodyToFlux(String.class), String.class);
    }

    public Mono<ServerResponse> findByLastname(ServerRequest request) {
        String name = request.queryParam("name")
                .orElse("");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(personDao.findByLastname(name), Person.class);

    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        /*Flux<String> flux = Flux.fromArray(new String[]{"1111", "2222", "3333", "4444","5555"})
                .map(s -> {
                    try {
                        TimeUnit.SECONDS.sleep(2L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "my->data->" + s;
                });*/
        Flux<Person> flux = client.sql("SELECT * FROM PERSON")
                .fetch()
                .all()
                .map(row -> new Person(Integer.valueOf(Objects.toString(row.get("id"), "0")),
                        Objects.toString(row.get("firstname")), Objects.toString(row.get("lastname"))));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(flux, Person.class);

    }

}
