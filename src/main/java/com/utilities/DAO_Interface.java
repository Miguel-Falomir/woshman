package com.utilities;

import java.util.List;

public interface DAO_Interface<T, K> {
    // T representa la clase del objeto entidad
    // K representa la clase del identificador de la entidad
    // Ambos pueden definirse arbitrariamente en cada clase <? implements DAO_Interface>
    boolean insert(T obj);

    boolean update(T obj);

    boolean delete(T obj);

    T search(K id);

    List<T> searchAll();
}