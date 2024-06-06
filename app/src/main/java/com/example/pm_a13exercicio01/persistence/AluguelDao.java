package com.example.pm_a13exercicio01.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pm_a13exercicio01.model.Aluguel;
import com.example.pm_a13exercicio01.model.Aluno;
import com.example.pm_a13exercicio01.model.Exemplar;
import com.example.pm_a13exercicio01.model.Livro;
import com.example.pm_a13exercicio01.model.Revista;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AluguelDao implements IAluguelDao, ICrudDao<Aluguel>{

    private final Context context;
    private GenericDao dao;
    private SQLiteDatabase db;

    public AluguelDao(Context context) {
        this.context = context;
    }

    @Override
    public AluguelDao open() throws SQLException {
        dao = new GenericDao(context);
        db = dao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        dao.close();
    }

    @Override
    public void insert(Aluguel aluguel) throws SQLException {
        ContentValues c = getAluguel(aluguel);
        db.insert("aluguel", null,c);
    }

    @Override
    public void update(Aluguel aluguel) throws SQLException {
        ContentValues c = getAluguel(aluguel);
        db.update("aluguel",c,
                "exmplar_codigo ="+aluguel.getExemplar().getCodigo() +
                "aluno_ra ="+aluguel.getAluno().getRa() +
                "data_retirada ="+aluguel.getDataRetirada(),null);
    }

    @Override
    public void delete(Aluguel aluguel) throws SQLException {
        db.delete("alugel","exmplar_codigo ="+aluguel.getExemplar().getCodigo() +
                "aluno_ra ="+aluguel.getAluno().getRa() +
                "data_retirada ="+aluguel.getDataRetirada(),null);
    }

    @SuppressLint("Range")
    @Override
    public Aluguel FindById(Aluguel aluguel) throws SQLException {
        Aluguel a = null;
        String query = "SELECT * FROM aluguel " +
                "join aluno on aluguel.aluno_ra = aluno.ra " +
                "join exemplar as e on e.codigo = aluguel.exemplar_codigo " +
                "left join revista as r on r.exemplar_codigo = e.codigo" +
                "left join livro as l on r.exemplar_codigo = e.codigo" +
                "where aluguel.exemplar_codigo ="+aluguel.getExemplar().getCodigo() +
                "aluguel.aluno_ra ="+aluguel.getAluno().getRa() +
                "aluguel.data_retirada ="+aluguel.getDataRetirada();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            Exemplar exemplar = instanciaExemplar(cursor);
            exemplar.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            exemplar.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            exemplar.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));

            Aluno aluno = new Aluno();
            aluno.setRa(cursor.getInt(cursor.getColumnIndex("codigo")));
            aluno.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));

            a.setDataDevolucao(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_devolucao"))));
            a.setDataRetirada(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_retirada"))));
            a.setAluno(aluno);
            a.setExemplar(exemplar);
            cursor.close();
        }
        return a;
    }

    @SuppressLint("Range")
    @Override
    public List<Aluguel> FindAll() throws SQLException {
        List<Aluguel> alugueis = null;
        String query = "SELECT * FROM aluguel " +
                "join aluno on aluguel.aluno_ra = aluno.ra " +
                "join exemplar as e on e.codigo = aluguel.exemplar_codigo " +
                "left join revista as r on r.exemplar_codigo = e.codigo" +
                "left join livro as l on r.exemplar_codigo = e.codigo";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null){
            cursor.moveToFirst();
            alugueis = new ArrayList<>();
        }
        while(!cursor.isAfterLast()){
            Aluguel a = new Aluguel();
            Exemplar exemplar = instanciaExemplar(cursor);
            exemplar.setQtdPaginas(cursor.getInt(cursor.getColumnIndex("qtdPaginas")));
            exemplar.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            exemplar.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));

            Aluno aluno = new Aluno();
            aluno.setRa(cursor.getInt(cursor.getColumnIndex("codigo")));
            aluno.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));

            a.setDataDevolucao(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_devolucao"))));
            a.setDataRetirada(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_retirada"))));
            a.setAluno(aluno);
            a.setExemplar(exemplar);
            alugueis.add(a);
            cursor.moveToNext();
        }
        cursor.close();
        return alugueis;
    }

    @SuppressLint("Range")
    private Exemplar instanciaExemplar(Cursor c){

        if(c.getString(c.getColumnIndex("issn")) != null){
            Revista r = new Revista();
            r.setISSN(c.getString(c.getColumnIndex("issn")));
            return r;
        }
        if(c.getString(c.getColumnIndex("isbn")) != null){
            Livro l = new Livro();
            l.setISBN(c.getString(c.getColumnIndex("isbn")));
            l.setEdicao(c.getInt(c.getColumnIndex("edicao")));
            return l;
        }
        return null;
    }

    private static ContentValues getAluguel(Aluguel a) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("exemplar_codigo", a.getExemplar().getCodigo());
        contentValues.put("aluno_ra", a.getAluno().getRa());
        contentValues.put("data_retirada", a.getDataRetirada().toString());
        contentValues.put("data_devolucao", a.getDataDevolucao().toString());
        return contentValues;
    }
}
