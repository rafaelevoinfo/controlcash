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
import android.util.Log;
import br.com.dreamsoft.dao.interfaces.Contas;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Despesa;

/**
 *
 * @author rafael
 */
public class DespesaDao implements Contas<Despesa> {

    private final String KEY_ID = "id";
    private final String KEY_NOME = "nome";
    private final String KEY_VALOR = "valor";
    private final String KEY_DATA = "date";
    private final String KEY_CATEGORIA = "categoria";
    private final String[] COLUNS = {KEY_ID, KEY_CATEGORIA, KEY_NOME, KEY_VALOR, KEY_DATA};
    private final String DATABASE_TABLE = "despesas";
    private SQLiteDatabase db;

    public DespesaDao(Context ctx) {
        this.db = ControlCashBD.getInstance(ctx);
    }

    public long cadastrar(Despesa rc) {
        this.db.beginTransaction();
        long id = -1;
        try {
        	 ContentValues initialValues = new ContentValues();
             
             initialValues.put(KEY_CATEGORIA, rc.getCategoria().getId());
             initialValues.put(KEY_NOME, rc.getNome());
             initialValues.put(KEY_VALOR, rc.getValor());
             SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
             try {
                 initialValues.put(KEY_DATA, sdf.format(rc.getDate()));
             	//initialValues.put(KEY_DATA, rc.getDate().toString());
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

    public boolean alterar(Despesa rc) {
        boolean result = false;
        this.db.beginTransaction();
        long id = 0;
        try {
        	ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CATEGORIA, rc.getCategoria().getId());
            initialValues.put(KEY_NOME, rc.getNome());
            initialValues.put(KEY_VALOR, rc.getValor());
           
            //SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    public List<Despesa> buscarTodos() throws ParseException {
        Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, null, null, null, null, null, null);
        List<Despesa> despesas = new ArrayList<Despesa>();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Despesa despesa = new Despesa();

            despesa.setId(cursor.getInt(indexId));
            Categoria cat = new Categoria();
            cat.setId(cursor.getInt(indexCat));
            despesa.setCategoria(cat);
            despesa.setNome(cursor.getString(indexNom));
            despesa.setValor(cursor.getDouble(indexVal));
            
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            despesa.setDate(date);

            despesas.add(despesa);
            cursor.moveToNext();

        }
        cursor.close();
        return despesas;
    }
    
    public List<Despesa> buscarIntervaloMes(String dataInicio, String dataFim) throws ParseException{
    	//cria a clausa where que faz a comparação entre os datas
    	String where = KEY_DATA+" BETWEEN date('"+dataInicio+"') AND date('"+dataFim+"')";
    	Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, where, null, null, null, null, null);
    	
        List<Despesa> despesas = new ArrayList<Despesa>();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Despesa despesa = new Despesa();

            despesa.setId(cursor.getInt(indexId));
            Categoria cat = new Categoria();
            cat.setId(cursor.getInt(indexCat));
            despesa.setCategoria(cat);
            despesa.setNome(cursor.getString(indexNom));
            despesa.setValor(cursor.getDouble(indexVal));
            
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            despesa.setDate(date);

            despesas.add(despesa);
            cursor.moveToNext();

        }
        cursor.close();
        return despesas;
        //return null;
    }

    public Despesa buscar(Integer id) throws ParseException{
        Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, KEY_ID + "= ?", new String[]{id.toString()}, null, null, null, null);
        Despesa despesa = new Despesa();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        despesa.setId(cursor.getInt(indexId));
        Categoria cat = new Categoria();
        cat.setId(cursor.getInt(indexCat));
        despesa.setCategoria(cat);
        despesa.setNome(cursor.getString(indexNom));
        despesa.setValor(cursor.getDouble(indexVal));
        
        //cria os formatadores da datas                                 
        SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
        //transforma a String em Date
        Date date =  sdfUSA.parse(cursor.getString(indexDat));
        //transforma o date em String e depois a String em Date
        date = sdfBRA.parse(sdfBRA.format(date));
        
        despesa.setDate(date);

        cursor.close();
        return despesa;

    }
    
    
    /**
     * Busca as despesas do mes da data passada
     * @param data
     * @return
     */
    public List<Despesa> buscarMes(String data) throws ParseException{
    	//cria a clausa where que faz a comparação entre os datas
    	String where = "strftime('%Y-%m',"+KEY_DATA+") = strftime('%Y-%m','"+data+"')";
    	Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, where, null, null, null, null, null);
        List<Despesa> despesas = new ArrayList<Despesa>();
        //pega os index pelos nomes
        int indexId = cursor.getColumnIndex(KEY_ID);
        int indexCat = cursor.getColumnIndex(KEY_CATEGORIA);
        int indexNom = cursor.getColumnIndex(KEY_NOME);
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        int indexDat = cursor.getColumnIndex(KEY_DATA);
        //pega os dados
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Despesa despesa = new Despesa();
            despesa.setId(cursor.getInt(indexId));
            Categoria cat = new Categoria();
            cat.setId(cursor.getInt(indexCat));
            despesa.setCategoria(cat);
            despesa.setNome(cursor.getString(indexNom));
            despesa.setValor(cursor.getDouble(indexVal));
            
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            despesa.setDate(date);

            despesas.add(despesa);
            cursor.moveToNext();

        }
        cursor.close();
        return despesas;    
    }

    public Despesa buscar(String nome) throws ParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Despesa buscar(Double valor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
