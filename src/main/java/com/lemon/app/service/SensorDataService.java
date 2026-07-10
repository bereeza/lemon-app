package com.lemon.app.service;

import com.lemon.app.model.Data;

@FunctionalInterface
public interface SensorDataService<T extends Data> {
    void process(T data) throws NumberFormatException;
}
