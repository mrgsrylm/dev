package io.github.mrgsrylm.builder;

import lombok.SneakyThrows;

public abstract class BaseBuilder<T> {
    T data;

    @SneakyThrows
    public BaseBuilder(Class<T> clazz) {
        this.data = clazz.getDeclaredConstructor().newInstance();
    }

    public T build() {
        return data;
    }
}