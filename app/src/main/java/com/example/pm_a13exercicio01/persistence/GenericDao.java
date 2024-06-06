package com.example.pm_a13exercicio01.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "FUTEBOL.DB";
    private static final int DATABASE_VER = 1;

    private static final String CREATE_TABLE_EXEMPLAR =
            "CREATE TABLE exemplar (" +
                    "codigo INT NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(50) NOT NULL, " +
                    "qtdPaginas integer NOT NULL);";

    private static final String CREATE_TABLE_LIVRO =
            "CREATE TABLE livro (" +
                    "exemplar_codigo INT NOT NULL PRIMARY KEY, " +
                    "isbn CHAR(13) NOT NULL, " +
                    "edicao int NOT NULL, " +
                    "FOREIGN KEY (exemplar_codigo)  REFERENCES exemplar(codigo) ON DELETE CASCADE);";

    private static final String CREATE_TABLE_REVISTA =
            "CREATE TABLE revista (" +
                    "exemplar_codigo INT NOT NULL PRIMARY KEY, " +
                    "issn CHAR(8) NOT NULL, " +
                    "FOREIGN KEY (exemplar_codigo)  REFERENCES exemplar(codigo) ON DELETE CASCADE);";
    private static final String CREATE_TABLE_ALUNO =
            "CREATE TABLE aluno (" +
                    "ra INT NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "email varchar(50) NOT NULL);";

    private static final String CREATE_TABLE_ALUGUEL =
            "CREATE TABLE aluguel (" +
                    "data_retirada date NOT NULL PRIMARY KEY, " +
                    "aluno_ra int NOT NULL PRIMARY KEY, " +
                    "exempla_codigo int NOT NULL PRIMARY KEY, " +
                    "data_devolucao date not null," +
                    "FOREIGN KEY (aluno_ra)  REFERENCES aluno(ra)," +
                    "FOREIGN KEY (exemplar_codigo)  REFERENCES exemplar(codigo)" +
                    ");";

    public GenericDao(@Nullable Context context) {
        super(context, DATABASE,null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXEMPLAR);
        db.execSQL(CREATE_TABLE_LIVRO);
        db.execSQL(CREATE_TABLE_REVISTA);
        db.execSQL(CREATE_TABLE_ALUNO);
        db.execSQL(CREATE_TABLE_ALUGUEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS livro");
            db.execSQL("DROP TABLE IF EXISTS revista");
            db.execSQL("DROP TABLE IF EXISTS exemplar");
            db.execSQL("DROP TABLE IF EXISTS aluno");
            db.execSQL("DROP TABLE IF EXISTS aluguel");
            onCreate(db);
        }
    }
}
