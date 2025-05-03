package com.utilities;

import java.util.List;

public interface DAO_Interface<T, K> {

    boolean insert(T obj);

    boolean update(T obj);

    boolean delete(T obj);

    T search(K id);

    List<T> searchAll();

}