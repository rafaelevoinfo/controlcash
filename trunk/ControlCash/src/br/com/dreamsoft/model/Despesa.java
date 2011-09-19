/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rafael
 */
public class Despesa implements Serializable{
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


}
