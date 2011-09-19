/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.dao;

import android.content.Context;

/**
 *
 * @author rafael
 */
public class Factory {

    public static ReceitaDao createReceitaDao(Context ctx){
        return new ReceitaDao(ctx);
    }
    
    public static DespesaDao createDespesaDao(Context ctx){
        return new DespesaDao(ctx);
    }
    
    public static CategoriaDao createCategoriaDao(Context ctx){
    	return new CategoriaDao(ctx);
    }

}
