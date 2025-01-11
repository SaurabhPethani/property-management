package com.cloud.property_gateway.limiter;

import com.cloud.property_gateway.config.Bucket;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimiter implements RateLimiter<Bucket> {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final int replenishRate = 5; // Requests per second
    private final int burstCapacity = 10; // Max capacity

    @Override
    public Mono<Response> isAllowed(String routeId, String userId) {
        Bucket bucket = buckets.computeIfAbsent(userId, key -> new Bucket(burstCapacity, replenishRate));
        synchronized (bucket) {
            long now = Instant.now().toEpochMilli();
            bucket.refill(now);
            int tokens = bucket.getTokens();

            if (tokens > 0) {
                bucket.setTokens(tokens-1);
                return Mono.just(new Response(true, Map.of()));
            } else {
                return Mono.just(new Response(false, Map.of()));
            }
        }
    }

    @Override
    public Map<String, Bucket> getConfig() {
        return buckets;
    }

    @Override
    public Class<Bucket> getConfigClass() {
        return Bucket.class;
    }

    @Override
    public Bucket newConfig() {
        return new Bucket(burstCapacity,replenishRate);
    }


}
