package br.com.dreamsoft.model;

import java.io.Serializable;
import java.util.Date;

public class Saldo implements Serializable{
	private double saldo;
	private String data;//yyyy-MM-dd
	
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	

}
