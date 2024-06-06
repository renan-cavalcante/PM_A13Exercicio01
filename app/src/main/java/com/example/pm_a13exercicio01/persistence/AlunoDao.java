package com.example.pm_a13exercicio01.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pm_a13exercicio01.model.Aluno;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDao implements ICrudDao<Aluno>, IAlunoDao {

    private final Context context;
    private GenericDao dao;
    private SQLiteDatabase db;

    public AlunoDao(Context context){
        this.context = context;
    }

    @Override
    public AlunoDao open() throws SQLException {
        dao = new GenericDao(context);
        db = dao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        dao.close();
    }

    @Override
    public void insert(Aluno aluno) throws SQLException {
            ContentValues c = getaluno(aluno);
            db.insert("aluno", null, c);
    }

    @Override
    public void update(Aluno aluno) throws SQLException {
        ContentValues c = getaluno(aluno);
        db.update("aluno",c," ra = "+aluno.getRa(), null);
    }

    @Override
    public void delete(Aluno aluno) throws SQLException {
        ContentValues c = getaluno(aluno);
        db.delete("aluno","ra = "+aluno.getRa(),null);
    }

    @SuppressLint("Range")
    @Override
    public Aluno FindById(Aluno aluno) throws SQLException {
        Aluno a = null;
        String sql = "SELECET * FROM aluno where ra ="+aluno.getRa();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            a = new Aluno();

            a.setRa(cursor.getInt(cursor.getColumnIndex("ra")));
            a.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            a.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        }
        cursor.close();
        return a;
    }

    @SuppressLint("Range")
    @Override
    public List<Aluno> FindAll() throws SQLException {
        List<Aluno> alunos = null;
        String query = "SELECET * FROM aluno";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor != null){
            cursor.moveToFirst();
            alunos = new ArrayList<>();
        }
        while(!cursor.isAfterLast()){
            Aluno a = new Aluno();
            a.setRa(cursor.getInt(cursor.getColumnIndex("ra")));
            a.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            a.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            alunos.add(a);
            cursor.moveToNext();
        }
        cursor.close();
        return alunos;
    }

    private static ContentValues getaluno(Aluno a){
        ContentValues c = new ContentValues();
        c.put("ra", a.getRa());
        c.put("nome", a.getNome());
        c.put("email",a.getEmail());
        return c;
    }
}
