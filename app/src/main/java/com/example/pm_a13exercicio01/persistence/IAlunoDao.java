package com.example.pm_a13exercicio01.persistence;

import java.sql.SQLException;

public interface IAlunoDao {
    AlunoDao open() throws SQLException;
    void close();
}
