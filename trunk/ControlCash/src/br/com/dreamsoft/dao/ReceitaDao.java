/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.dreamsoft.dao.interfaces.Contas;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Receita;

/**
 *
 * @author rafael
 */
public class ReceitaDao implements Contas<Receita> {

    private final String KEY_ID = "id";
    private final String KEY_NOME = "nome";
    private final String KEY_VALOR = "valor";
    private final String KEY_DATA = "date";
    private final String KEY_CATEGORIA = "categoria";
    private final String[] COLUNS = {KEY_ID, KEY_CATEGORIA, KEY_NOME, KEY_VALOR, KEY_DATA};
    private final String DATABASE_TABLE = "receitas";
    private SQLiteDatabase db;

    public ReceitaDao(Context ctx) {
        this.db = ControlCashBD.getInstance(ctx);
    }

    public long cadastrar(Receita rc) {
        this.db.beginTransaction();
        long id = -1;
        try {
            ContentValues initialValues = new ContentValues();
            
            initialValues.put(KEY_CATEGORIA, rc.getCategoria().getId());
            initialValues.put(KEY_NOME, rc.getNome());
            initialValues.put(KEY_VALOR, rc.getValor());
            SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                initialValues.put(KEY_DATA, sdf.format(rc.getDate()));
            } catch (Exception e) {
                return -1L;
            }
            //grava no banco
            id = db.insert(DATABASE_TABLE, null, initialValues);           
            if (id != -1) {
                this.db.setTransactionSuccessful();
            }
        } finally {
            this.db.endTransaction();            
        }
        return id;
    }

    public boolean alterar(Receita rc) {
        boolean result = false;
        this.db.beginTransaction();
        long id = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CATEGORIA, rc.getCategoria().getId());
            initialValues.put(KEY_NOME, rc.getNome());
            initialValues.put(KEY_VALOR, rc.getValor());
           
            SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                initialValues.put(KEY_DATA, sdf.format(rc.getDate()));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            //atualiza no banco
            id = db.update(DATABASE_TABLE, initialValues, KEY_ID + "= ?", new String[]{String.valueOf(rc.getId())});
            if (id != 0) {
                this.db.setTransactionSuccessful();
                result = true;
            }
        } finally {
            this.db.endTransaction();            
        }
        return result;
    }

    public boolean deletar(Integer id) {
        boolean result = false;
        this.db.beginTransaction();

        try {
            //deleta no banco
            id = db.delete(DATABASE_TABLE, KEY_ID + "= ?", new String[]{String.valueOf(id)});
            if (id != 0) {
                this.db.setTransactionSuccessful();
                result = true;
            }
        } finally {
            this.db.endTransaction();            
        }
        return result;
    }

    public List<Receita> buscarTodos() throws ParseException {
        Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, null, null, null, null, null, null);
        List<Receita> receitas = new ArrayList<Receita>();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Receita receita = new Receita();

            receita.setId(cursor.getInt(indexId));
            Categoria cat = new Categoria();
            cat.setId(cursor.getInt(indexCat));
            receita.setCategoria(cat);
            receita.setNome(cursor.getString(indexNom));
            receita.setValor(cursor.getDouble(indexVal));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(cursor.getString(indexDat));
            
            receita.setDate(date);

            receitas.add(receita);
            cursor.moveToNext();

        }
        cursor.close();
        return receitas;
    }
    
    public Receita buscarMes(int mes){
    	String[] data = new String[]{};
    	Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, KEY_DATA+"=?", new String[], null, null, null, null);
        List<Receita> receitas = new ArrayList<Receita>();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Receita receita = new Receita();

            receita.setId(cursor.getInt(indexId));
            Categoria cat = new Categoria();
            cat.setId(cursor.getInt(indexCat));
            receita.setCategoria(cat);
            receita.setNome(cursor.getString(indexNom));
            receita.setValor(cursor.getDouble(indexVal));
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(cursor.getString(indexDat));
            
            receita.setDate(date);

            receitas.add(receita);
            cursor.moveToNext();

        }
        cursor.close();
        return receitas;    	
    }

    public Receita buscar(Integer id) {
        Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, KEY_ID + "= ?", new String[]{id.toString()}, null, null, null, null);
        Receita receita = new Receita();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        receita.setId(cursor.getInt(indexId));
        Categoria cat = new Categoria();
        cat.setId(cursor.getInt(indexCat));
        receita.setCategoria(cat);
        receita.setNome(cursor.getString(indexNom));
        receita.setValor(cursor.getDouble(indexVal));
        receita.setDate(new Date(cursor.getString(indexDat)));

        cursor.close();
        return receita;

    }

    public Receita buscar(String nome) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Receita buscar(Double valor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
