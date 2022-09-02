package com.jts.webflux.mould;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class Attribute {

    public static final String STRING_TYPE = "string";
    public static final String BIG_DECIMAL_TYPE = "bigDecimal";
    public static final String ARRAY_TYPE = "array";

    private Long order;
    private String type = STRING_TYPE;
    private String alias;
    private String defaultVal;
    private String desc;
    private Node body;
    private Map<String, Object> attr;
}
