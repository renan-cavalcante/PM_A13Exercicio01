package com.example.pm_a13exercicio01.persistence;

import java.sql.SQLException;

public interface IAluguelDao {
    AluguelDao open() throws SQLException;
    void close();
}
