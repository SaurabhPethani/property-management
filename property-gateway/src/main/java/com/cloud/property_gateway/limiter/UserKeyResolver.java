package com.cloud.property_gateway.limiter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserKeyResolver implements KeyResolver {
    @Override
    public Mono<String> resolve(org.springframework.web.server.ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}

