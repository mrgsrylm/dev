package io.github.mrgsrylm.builder

abstract class BaseBuilder<T> {
    abstract var data: T;

    fun BaseBuilder(clazz: Class<T>) {
        this.data = clazz.getDeclaredConstructor().newInstance()
    }
    
    fun build(): T {
        return data
    }
}