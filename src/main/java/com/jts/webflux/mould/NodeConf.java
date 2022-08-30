package com.jts.webflux.mould;

import lombok.Data;

@Data
public class NodeConf {
    private String id;
    private String name;
    private Node dataHeader;
    private Node dataBody;
}
