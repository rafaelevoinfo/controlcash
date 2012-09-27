/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;
import br.com.dreamsoft.R;

/**
 * 
 * @author rafael
 */
public abstract class Mensagens {

	// TODO Adicionar todas as mensagens desta classe para o string.xml
	public static void msgOk(final Context ctx) {
		Toast.makeText(ctx, ctx.getString(R.string.operacao_ok), 0).show();
		((Activity) ctx).finish();
		// AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		// AlertDialog dialog = builder.setTitle("Ok")
		// .setPositiveButton("Ok", new OnClickListener() {
		//
		// public void onClick(DialogInterface arg0, int arg1) {
		// ((Activity) ctx).finish();
		// }
		// }).create();
		// dialog.setMessage("Operação realizada com sucesso!");
		// dialog.show();
	}

	public static void msgOkSemFechar(final Context ctx) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		// AlertDialog dialog = builder.setTitle("Ok").setPositiveButton("Ok",
		// null).create();
		// dialog.setMessage("Operação realizada com sucesso!");
		// dialog.show();
		Toast.makeText(ctx, ctx.getString(R.string.operacao_ok), 0).show();
	}

	public static void msgErroBD(int cod, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Erro").setPositiveButton("Ok", null).create();
		switch (cod) {
			case 1:// erro ao cadastrar, alterar ou excluir
				dialog.setMessage(ctx.getString(R.string.erro_completar_operacao));
				dialog.show();
				break;
			case 2:// erro ao buscar os dados
				dialog.setMessage(ctx.getString(R.string.erro_buscar_dados));
				dialog.show();
				break;
			case 3:// viola��o de integridade
				dialog.setMessage(ctx.getString(R.string.erro_valor_usado));
				dialog.show();
				break;

		}

	}

	public static void msgErro(int cod, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Erro").setPositiveButton("Ok", null).create();
		switch (cod) {
			case 1:// formataçao de data
				dialog.setMessage(ctx.getString(R.string.erro_formatar_data));
				dialog.show();
				break;
			case 2:// formataçao
				dialog.setMessage(ctx.getString(R.string.erro_formatar_moeda));
				dialog.show();
				break;
			case 3:// Cast
				dialog.setMessage(ctx.getString(R.string.erro_cast));
				dialog.show();
				break;
			case 4:// Op��o de menu n�o encontrada
				dialog.setMessage(ctx.getString(R.string.erro_menu_id));
				dialog.show();
				break;
			case 5:// Erro ao trocar a cor
				dialog.setMessage(ctx.getString(R.string.erro_definir_cor));
				dialog.show();
				break;
			case 6:// Erro ao converter a String
				dialog.setMessage(ctx.getString(R.string.erro_ano_invalido));
				dialog.show();
				break;
			default:
				dialog.setMessage(ctx.getString(R.string.erro_desconhecido));
				dialog.show();
				break;
		}
	}
}
