package com.example.pm_a13exercicio01.persistence;

import java.sql.SQLException;

public interface ILivroDao {
    LivroDao open() throws SQLException;
    void close();
}
