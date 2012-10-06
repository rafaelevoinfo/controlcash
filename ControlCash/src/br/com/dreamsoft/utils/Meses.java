package br.com.dreamsoft.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import br.com.dreamsoft.R;

public class Meses {

	/**
	 * Converte um numero que representa um mes em uma string contendo o nome do
	 * Mês.
	 * 
	 * @param mes
	 *            - Valor numero de 0 a 11
	 * @param ctx
	 * @return
	 */
	public static String converterDiaMesToString(int mes, Context ctx) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		try {
			Date data = sdf.parse(String.valueOf(mes + 1));
			sdf.applyPattern("MMMM");
			String nomeMes = sdf.format(data);
			char[] letras = nomeMes.toCharArray();
			letras[0] = Character.toUpperCase(letras[0]);
			nomeMes = String.valueOf(letras);

			return nomeMes;
		} catch (ParseException e) {
			return ctx.getString(R.string.erro);
		}
	}

}
