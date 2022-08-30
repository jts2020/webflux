package com.jts.webflux.mould;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Component
public class MouldConfParse {

    private static final ObjectMapper OBJ_MAPPER = new ObjectMapper();

    private static final Map<String, SoftReference<NodeConf>> CACHE_NODE_CFG_MAP = new HashMap<>();

    public NodeConf exec() {
        return getNodeConf("w484");
    }

    public NodeConf parseNodeConf() {
        try {
            File file = ResourceUtils.getFile("classpath:pkg/MapperMap.json");
            return OBJ_MAPPER.readValue(file, NodeConf.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NodeConf getNodeConf(String id) {
        SoftReference<NodeConf> sr = CACHE_NODE_CFG_MAP.get(id);
        NodeConf nodeConf = Optional.ofNullable(sr)
                .map(SoftReference::get)
                .orElseGet(() -> {
                    NodeConf cfg = parseNodeConf();
                    SoftReference nsr = new SoftReference<>(cfg);
                    CACHE_NODE_CFG_MAP.put(id, nsr);
                    log.info("CACHE_NODE_CFG_MAP put id [{}]", id);
                    return cfg;
                });
        Node body = getDataBody(nodeConf);
        log.info("id [{}]", exists(body, "id"));
        return nodeConf;
    }

    public Node getDataBody(NodeConf nodeConf) {
        return nodeConf.getDataBody();
    }

    public boolean exists(Node node, String key) {
        return Optional.ofNullable(node)
                .map(n -> n.get(key))
                .map(Objects::nonNull)
                .orElse(Boolean.FALSE);
    }

}
