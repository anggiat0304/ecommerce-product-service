package com.ecommerce.productservice.generics;

public interface ObjectSaver<T> {
    void saveSpecificInfo(T Object) throws Exception;
}
