package com.jts.webflux.conf;

import com.jts.webflux.bo.Person;
import com.jts.webflux.dao.PersonDao;
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

@Configuration
public class DBInitDataConf {

    private static final Logger LOGGER = LogManager.getLogger(DBInitDataConf.class);

    @Bean
    public ApplicationRunner initDatabase(DatabaseClient client, PersonDao personDao) {

        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS PERSON;",
                "CREATE TABLE IF NOT EXISTS PERSON ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");

        statements.forEach(sql -> executeSql(client, sql)
                .doOnSuccess(count -> LOGGER.info("Schema created, rows updated: {}", () -> count))
                .doOnError(error -> LOGGER.error("got error : {}", error::getMessage))
                .subscribe()
        );

        return args -> getPerson()
                .flatMap(personDao::save)
                .subscribe(person -> LOGGER.info("User saved: {}", () -> person));
    }

    private Flux<Person> getPerson() {
        return Flux.just(new Person(null, "John", "Doe"), new Person(null, "Jane", "Doe"));
    }

    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
        return client.sql(sql).fetch().rowsUpdated();
    }
}
