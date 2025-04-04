package com.utilities;

import java.util.List;

public interface DAO<T, K> {

    void insert(T obj);

    void update(T obj);

    void delete(T obj);

    T search(K id);

    List<T> searchAll();

}