package com.kimmin.faceemoji.server.dao;

import java.util.List;

/**
 * Created by fowafolo on 15/5/18.
 */
public interface GeneralDAO<T> {
    T queryById(String id);
    List<T> queryAll();
    void insert(T t);
    void delete(T t);
    void update(T t);
    void deleteById(String id);
}
