package br.com.dreamsoft.model;

import java.io.Serializable;
import java.util.Date;

public abstract class Movimentacao implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int id;
	protected String nome;
	protected double valor;
	protected Date date;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
