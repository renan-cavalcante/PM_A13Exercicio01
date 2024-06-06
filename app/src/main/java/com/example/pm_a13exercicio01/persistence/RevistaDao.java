package com.example.pm_a13exercicio01.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pm_a13exercicio01.model.Exemplar;
import com.example.pm_a13exercicio01.model.Revista;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevistaDao implements  IRevistaDao, ICrudDao<Revista> {

    private final Context context;
    private GenericDao dao;
    private SQLiteDatabase db;

    public RevistaDao(Context context) {
        this.context = context;
    }

    @Override
    public void insert(Revista revista) throws SQLException {
        ContentValues cExemplar = getExemplar(revista);
        ContentValues cRevista = getRevista(revista);
        Long id = db.insert("exemplar", null, cExemplar);
        cRevista.put("exemplar_codigo",id);
        db.insert("revista", null, cRevista);

    }


    @Override
    public void update(Revista revista) throws SQLException {
        ContentValues cExemplar = getExemplar(revista);
        ContentValues cRevista = getRevista(revista);

        db.update("exemplar",cExemplar,"codigo = "+revista.getCodigo(),null);
        db.update("revista",cRevista,"codigo = "+revista.getCodigo(),null);
    }

    @Override
    public void delete(Revista revista) throws SQLException {
        db.delete("revista","codigo = "+revista.getCodigo(),null);// desnecessario ja que esta com delete cascate
        db.delete("exemplar","codigo = "+revista.getCodigo(),null);
    }

    @SuppressLint("Range")
    @Override
    public Revista FindById(Revista r) throws SQLException {
        Revista revista = null;
        String sql = "SELECT * FROM revista left join exemplar on revista.exemplar_codigo = exemplar.codigo where codigo ="+r.getCodigo();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            revista = new Revista();
            revista.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            revista.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            revista.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            revista.setISSN(cursor.getString(cursor.getColumnIndex("issn")));
        }
        cursor.close();
        return revista;
    }

    @SuppressLint("Range")
    @Override
    public List<Revista> FindAll() throws SQLException {
        List<Revista> revistas = null;
        String sql = "SELECT * FROM revista left join exemplar on revista.exemplar_codigo = exemplar.codigo ";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
            revistas = new ArrayList<>();
        }
        while (!cursor.isAfterLast()){
            Revista revista = new Revista();
            revista.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            revista.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            revista.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            revista.setISSN(cursor.getString(cursor.getColumnIndex("issn")));
            revistas.add(revista);
            cursor.moveToNext();
        }
        cursor.close();
        return revistas;
    }

    @Override
    public RevistaDao open() throws SQLException {
        dao = new GenericDao(context);
        db = dao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        dao.close();
    }

    private ContentValues getExemplar(Exemplar e){
        ContentValues c = new ContentValues();
        c.put("nome",e.getNome());
        c.put("qtdPaginas",e.getQtdPaginas());
        return c;
    }

    private ContentValues getRevista(Revista l){
        ContentValues c = new ContentValues();
        c.put("isbn",l.getISSN());
        return c;
    }
}
