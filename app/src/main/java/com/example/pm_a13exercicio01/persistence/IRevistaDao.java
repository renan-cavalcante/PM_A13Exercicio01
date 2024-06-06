package com.example.pm_a13exercicio01.persistence;

import java.sql.SQLException;

public interface IRevistaDao {
    RevistaDao open() throws SQLException;
    void close();
}
