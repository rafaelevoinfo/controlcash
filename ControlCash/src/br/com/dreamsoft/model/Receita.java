/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.model;

import java.io.Serializable;
import java.util.Date;

import android.content.Context;
import br.com.dreamsoft.R;
import br.com.dreamsoft.planilha.ExportXls;

/**
 * 
 * @author rafael
 */
public class Receita implements Serializable, ExportXls {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nome;
	private double valor;
	private Categoria categoria;
	private Date date;

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
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
