package com.lemon.app.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class Data {
    @NotNull
    private UUID id;

    @NotNull
    private long createdAt;

    private long updatedAt;

    public Data() {
    }

    public Data(
            UUID id,
            long createdAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Data(
            UUID id,
            long createdAt,
            long updatedAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
