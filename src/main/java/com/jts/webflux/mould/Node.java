package com.jts.webflux.mould;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Node extends LinkedHashMap<String, Attribute> {

    public static final Node EMPTY = new Node();

    public boolean exists(String key) {
        return Objects.nonNull(get(key));
    }
}
