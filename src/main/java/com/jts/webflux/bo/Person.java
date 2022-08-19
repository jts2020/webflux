package com.jts.webflux.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("PERSON")
public class Person {
    @Id
    private Integer id;
    private String firstname;
    private String lastname;

}
