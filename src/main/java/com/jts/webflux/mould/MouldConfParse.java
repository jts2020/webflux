package com.jts.webflux.mould;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class MouldConfParse {

    private static final ObjectMapper OBJ_MAPPER = new ObjectMapper();

    private static final Map<String, SoftReference<NodeConf>> CACHE_NODE_CFG_MAP = new ConcurrentHashMap<>();

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
                    if (Objects.nonNull(cfg)) {
                        SoftReference<NodeConf> nsr = new SoftReference<>(cfg);
                        CACHE_NODE_CFG_MAP.put(id, nsr);
                        log.info("CACHE_NODE_CFG_MAP put id [{}]", id);
                        return cfg;
                    }
                    log.warn("NodeConf is null.id [{}]", id);
                    return null;
                });
        Node body = getDataBody(nodeConf);
        return nodeConf;
    }

    public Node getDataBody(NodeConf nodeConf) {
        return Optional.ofNullable(nodeConf)
                .map(NodeConf::getDataBody)
                .orElse(null);
    }

    public Node getNode(String id, String path) {
        NodeConf root = getNodeConf(id);
        Node dataBodyNode = getDataBody(root);
        if (Objects.nonNull(path)) {
            String[] paths = path.split("/", -1);
            Node node = dataBodyNode;
            for (String pth : paths) {
                if (Objects.isNull(node)) {
                    return null;
                }
                node = getChildNode(node, pth);
            }
            return node;
        }
        return dataBodyNode;
    }

    private Node getChildNode(Node node, String key) {
        Node res = Optional.ofNullable(node)
                .map(n -> n.get(key))
                .filter(attr -> Objects.equals(Attribute.ARRAY_TYPE, attr.getType()))
                .map(Attribute::getBody)
                .orElse(null);
        log.info("Get key[{}] getBodyNpde {}", key, node);
        return res;
    }

}
