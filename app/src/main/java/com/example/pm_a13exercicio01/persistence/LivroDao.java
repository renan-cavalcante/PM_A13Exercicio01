package com.example.pm_a13exercicio01.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pm_a13exercicio01.model.Exemplar;
import com.example.pm_a13exercicio01.model.Livro;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDao implements  ILivroDao, ICrudDao<Livro> {

    private final Context context;
    private GenericDao dao;
    private SQLiteDatabase db;

    public LivroDao(Context context) {
        this.context = context;
    }

    @Override
    public void insert(Livro livro) throws SQLException {
        ContentValues cExemplar = getExemplar(livro);
        ContentValues cLivro = getLivro(livro);
        Long id = db.insert("exemplar", null, cExemplar);
        cLivro.put("exemplar_codigo",id);
        db.insert("livro", null, cLivro);

    }


    @Override
    public void update(Livro livro) throws SQLException {
        ContentValues cExemplar = getExemplar(livro);
        ContentValues cLivro = getLivro(livro);

        db.update("exemplar",cExemplar,"codigo = "+livro.getCodigo(),null);
        db.update("livro",cLivro,"codigo = "+livro.getCodigo(),null);
    }

    @Override
    public void delete(Livro livro) throws SQLException {
        db.delete("livro","codigo = "+livro.getCodigo(),null);// desnecessario ja que esta com delete cascate
        db.delete("exemplar","codigo = "+livro.getCodigo(),null);
    }

    @SuppressLint("Range")
    @Override
    public Livro FindById(Livro l )throws SQLException {
        Livro livro = null;
        String sql = "SELECT * FROM livro left join exemplar on livro.exemplar_codigo = exemplar.codigo where codigo ="+l.getCodigo();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            livro = new Livro();
            livro.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            livro.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            livro.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            livro.setEdicao(cursor.getInt(cursor.getColumnIndex("edicao")));
            livro.setISBN(cursor.getString(cursor.getColumnIndex("isbn")));
        }
        cursor.close();
        return livro;
    }

    @SuppressLint("Range")
    @Override
    public List<Livro> FindAll() throws SQLException {
        List<Livro> livros = null;
        String sql = "SELECT * FROM livro left join exemplar on livro.exemplar_codigo = exemplar.codigo ";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
            livros = new ArrayList<>();
        }
        while (!cursor.isAfterLast()){
            Livro livro = new Livro();
            livro.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            livro.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            livro.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            livro.setEdicao(cursor.getInt(cursor.getColumnIndex("edicao")));
            livro.setISBN(cursor.getString(cursor.getColumnIndex("isbn")));
            livros.add(livro);
            cursor.moveToNext();
        }
        cursor.close();
        return livros;
    }

    @Override
    public LivroDao open() throws SQLException {
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

    private ContentValues getLivro(Livro l){
        ContentValues c = new ContentValues();
        c.put("edicao",l.getEdicao());
        c.put("isbn",l.getISBN());
        return c;
    }
}
