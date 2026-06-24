package com.lemon.app.service;

import java.util.UUID;

@FunctionalInterface
public interface DataService<T> {
    void process(T data, UUID id, long timestamp) throws NumberFormatException;
}
