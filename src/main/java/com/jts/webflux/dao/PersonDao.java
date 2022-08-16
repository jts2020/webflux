package com.jts.webflux.dao;

import com.jts.webflux.bo.Person;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PersonDao extends ReactiveCrudRepository<Person, String> {

    @Query("select id, firstname, lastname from PERSON c where c.lastname = :lastname")
    Flux<Person> findByLastname(String lastname);

}
