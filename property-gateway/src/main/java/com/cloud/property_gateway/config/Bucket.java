package com.cloud.property_gateway.config;

import java.time.Instant;

public class Bucket {
    private int tokens;
    private final int capacity;
    private final int replenishRate;
    private long lastRefillTimestamp;

    public Bucket(int capacity, int replenishRate) {
        this.tokens = capacity;
        this.capacity = capacity;
        this.replenishRate = replenishRate;
        this.lastRefillTimestamp = Instant.now().toEpochMilli();
    }

    public void refill(long currentTime) {
        long timeElapsed = currentTime - lastRefillTimestamp;
        int newTokens = (int) (timeElapsed / 1000) * replenishRate;
        tokens = Math.min(capacity, tokens + newTokens);
        lastRefillTimestamp = currentTime;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getReplenishRate() {
        return replenishRate;
    }

    public long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}
