package br.com.dreamsoft.planilha;

import android.content.Context;

public interface ExportXls {
	public String[] getCabecalho(Context ctx);

	public Object[] getValores();

	public String getGrupo(Context ctx);

}
