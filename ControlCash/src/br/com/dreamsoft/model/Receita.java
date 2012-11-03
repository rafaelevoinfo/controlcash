/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.model;

import java.io.Serializable;

import android.content.Context;
import br.com.dreamsoft.R;
import br.com.dreamsoft.planilha.ExportXls;

/**
 * 
 * @author rafael
 */
public class Receita extends Movimentacao implements Serializable, ExportXls {

	private static final long serialVersionUID = 1L;
	private Categoria categoria;

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String[] getCabecalho(Context ctx) {
		return new String[] { ctx.getString(R.string.nome), ctx.getString(R.string.id_export),
				ctx.getString(R.string.categoria), ctx.getString(R.string.data), ctx.getString(R.string.valor) };
	}

	@Override
	public Object[] getValores() {
		return new Object[] { nome, Integer.valueOf(categoria.getIdExport()), categoria.getNome(), date,
				Double.valueOf(valor) };
	}

	@Override
	public String getGrupo(Context ctx) {
		return ctx.getString(R.string.receitas);
	}

}
