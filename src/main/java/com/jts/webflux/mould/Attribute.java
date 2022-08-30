package com.jts.webflux.mould;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private String name;
    private Long order;
    private String type;
    private String defaultVal;
    private String desc;
    private Map<String, Attribute> body;
    private Map<String, Object> attr;
}
