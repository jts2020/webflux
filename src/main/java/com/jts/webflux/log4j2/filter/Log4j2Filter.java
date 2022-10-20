package com.jts.webflux.log4j2.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class Log4j2Filter implements WebFilter {

    private static final String REQ_ID = "REQ-LOG-ID";
    private static final String REQ_LOG_LEVEL = "REQ-LOG-LEVEL";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String reqId = serverWebExchange.getRequest().getPath().value().replaceAll("/", "-");
        ThreadContext.put(REQ_ID, reqId);
        ThreadContext.put(REQ_LOG_LEVEL, "INFO");
        return webFilterChain.filter(serverWebExchange)
                .doFinally(signalType -> ThreadContext.clearAll());
    }
}