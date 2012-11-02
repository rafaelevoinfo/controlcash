package br.com.dreamsoft.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.planilha.ExportXls;

public class AdapterDaoPlanilha {
	public enum Tipo {
		RECEITA, DESPESA
	}

	public static List<ExportXls> buscarMes(Context ctx, String data, Tipo tipo) throws ParseException {
		switch (tipo) {
			case DESPESA:
				DespesaDao daoDesp = Factory.createDespesaDao(ctx);
				return copiar(daoDesp.buscarMes(data));
			case RECEITA:
				ReceitaDao daoRec = Factory.createReceitaDao(ctx);
				return copiar(daoRec.buscarMes(data));
			default:
				return new ArrayList<ExportXls>();// retorno uma lista vazia
		}

	}

	private static List<ExportXls> copiar(List<? extends Object> recDesp) {
		List<ExportXls> lista = new ArrayList<ExportXls>();
		for (Object obj : recDesp) {
			if (obj instanceof ExportXls) {
				lista.add((ExportXls) obj);
			}
		}

		return lista;
	}

}
