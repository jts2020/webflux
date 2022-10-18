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
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String reqId = serverWebExchange.getRequest().getPath().value().replaceAll("/", "-");
        ThreadContext.put("REQ-ID", reqId);
        ThreadContext.put("REQ-LEVEL", "INFO");
        return webFilterChain.filter(serverWebExchange)
                .doFinally(signalType -> ThreadContext.clearAll());
    }
}