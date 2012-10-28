/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.model;

import java.io.Serializable;

/**
 * 
 * @author rafael
 */
public class Categoria implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int idExport;
	private String nome;

	public Categoria() {
		super();
	}

	public Categoria(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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

	@Override
	public String toString() {
		return this.nome;
	}

	public int getIdExport() {
		return idExport;
	}

	public void setIdExport(int idExport) {
		this.idExport = idExport;
	}

}
