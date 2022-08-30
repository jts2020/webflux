package com.jts.webflux.conf;

import com.jts.webflux.bo.Person;
import com.jts.webflux.dao.PersonDao;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
public class DBInitDataConf {

    @Bean
    public ApplicationRunner initDatabase(DatabaseClient client, PersonDao personDao) {

        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS PERSON;",
                "CREATE TABLE IF NOT EXISTS PERSON ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");

        statements.forEach(sql -> executeSql(client, sql)
                .doOnSuccess(count -> log.info("Schema created, rows updated: {}", () -> count))
                .doOnError(error -> log.error("got error : {}", error::getMessage))
                .subscribe()
        );

        return args -> getPerson()
                .flatMap(personDao::save)
                .subscribe(person -> log.info("User saved: {}", () -> person));
    }

    private Flux<Person> getPerson() {
        return Flux.just(new Person(null, "John", "Doe"), new Person(null, "Jane", "Doe"));
    }

    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
        return client.sql(sql).fetch().rowsUpdated();
    }
}
