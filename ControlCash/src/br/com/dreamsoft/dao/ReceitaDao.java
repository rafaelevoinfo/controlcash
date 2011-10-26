/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
    private Context ctx;

    public ReceitaDao(Context ctx) {
        this.db = ControlCashBD.getInstance(ctx);
        this.ctx = ctx;
    }

    public long cadastrar(Receita rc) {
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

    public boolean alterar(Receita rc) {
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
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            receita.setDate(date);

            receitas.add(receita);
            cursor.moveToNext();

        }
        cursor.close();
        return receitas;
    }
    /**
     * Busca as receitas do mes da data passada
     * @param data - String no formato yyyy-MM-dd
     * @return
     */
    public List<Receita> buscarMes(String data) throws ParseException{
    	//cria a clausa where que faz a comparação entre os datas
    	String where = "strftime('%Y-%m',"+KEY_DATA+") = strftime('%Y-%m','"+data+"')";
    	Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, where, null, null, null, null, null);
    	//serve para buscar o nome das categorias
    	CategoriaDao daoCat = Factory.createCategoriaDao(ctx);
    	
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
            Categoria cat = daoCat.buscar(cursor.getInt(indexCat));
           
            receita.setCategoria(cat);
            receita.setNome(cursor.getString(indexNom));
            receita.setValor(cursor.getDouble(indexVal));
            
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            receita.setDate(date);

            receitas.add(receita);
            cursor.moveToNext();

        }
        cursor.close();
        return receitas;
        //return null;
    }
    
   /* public List<Receita> buscarIntervaloMes(String dataInicio, String dataFim) throws ParseException{
    	//cria a clausa where que faz a comparação entre os datas
    	String where = KEY_DATA+" BETWEEN date('"+dataInicio+"') AND date('"+dataFim+"')";
    	Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, where, null, null, null, null, null);
    	
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
            
            //cria os formatadores da datas                                 
            SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
            //transforma a String em Date
            Date date =  sdfUSA.parse(cursor.getString(indexDat));
            //transforma o date em String e depois a String em Date
            date = sdfBRA.parse(sdfBRA.format(date));
            
            receita.setDate(date);

            receitas.add(receita);
            cursor.moveToNext();

        }
        cursor.close();
        return receitas;
        //return null;
    }*/
    
    public Double buscarSaldoCategoria(int categoria){
    	//cria uma query que ja pega o resultado da subtração das receitas e despesas em uma mesma categoria
    	//OBS.: Na teoria nao deveria haver receitas e despesas cadastradas para a mesma categoria, mas o sistema permite isso, por isso trato isto aqui
    	String sql = "SELECT (rec - desp) as "+KEY_VALOR+" FROM (SELECT rec,desp FROM (SELECT SUM("+KEY_VALOR+") as rec FROM "+DATABASE_TABLE+" WHERE "+KEY_CATEGORIA+" = ?),(SELECT SUM("+KEY_VALOR+") as desp FROM despesas WHERE "+KEY_CATEGORIA+" = ?));";
    	Cursor cursor = this.db.rawQuery(sql,new String[]{String.valueOf(categoria),String.valueOf(categoria)});
    	      
        //pega os index pelos nomes
        
        int indexVal = cursor.getColumnIndex(KEY_VALOR);
        double resultado = 0.0;
        //pega os dados
        cursor.moveToFirst();
        resultado = cursor.getDouble(indexVal);  
        cursor.close();
        return resultado;
    }

    public Receita buscar(Integer id) throws ParseException {
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
        //cria os formatadores da datas                                 
        SimpleDateFormat sdfBRA = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfUSA = new SimpleDateFormat("yyyy-MM-dd");
        //transforma a String em Date
        Date date =  sdfUSA.parse(cursor.getString(indexDat));
        //transforma o date em String e depois a String em Date
        date = sdfBRA.parse(sdfBRA.format(date));
        
        receita.setDate(date);

        cursor.close();
        return receita;

    }

    public Receita buscar(String nome) throws ParseException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Receita buscar(Double valor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	
}
