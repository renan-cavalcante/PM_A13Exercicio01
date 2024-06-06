package com.example.pm_a13exercicio01.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ICrudDao<T> {
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    T FindById(T t) throws SQLException;
    List<T> FindAll() throws SQLException;
}
