package br.com.dreamsoft.utils;

public class Meses{
	public static final String getMes(int mes){
		switch (mes) {
			case 0:
				return "Janeiro";
			case 1:
				return "Fevereiro";
			case 2:
				return "Março";
			case 3:
				return "Abril";
			case 4:
				return "Maio";
			case 5:
				return "Junho";
			case 6:
				return "Julho";
			case 7:
				return "Agosto";
			case 8:
				return "Setembro";
			case 9:
				return "Outubro";
			case 10:
				return "Novembro";
			case 11:
				return "Dezembro";
			default:
				return "Indefinido";
		}
	}
}
