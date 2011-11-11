/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 
 * @author rafael
 */
public abstract class Mensagens {

	public static void msgOk(final Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Ok")
				.setPositiveButton("Ok", new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						((Activity) ctx).finish();
					}
				}).create();
		dialog.setMessage("Operação realizada com sucesso!");
		dialog.show();
	}

	public static void msgOkSemFechar(final Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Ok")
				.setPositiveButton("Ok", null).create();
		dialog.setMessage("Operação realizada com sucesso!");
		dialog.show();
	}

	public static void msgErroBD(int cod, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Erro")
				.setPositiveButton("Ok", null).create();
		switch (cod) {
			case 1:// erro ao cadastrar, alterar ou excluir
				dialog.setMessage("Erro ao completar a operação!");
				dialog.show();
				break;
			case 2:// erro ao buscar os dados
				dialog.setMessage("Erro ao buscar os dados!");
				dialog.show();
				break;
			case 3://viola��o de integridade
				dialog.setMessage("Existem receitas e/ou despesas ligadas a esta categoria! Impossível excluir.");
				dialog.show();
				break;

		}

	}

	public static void msgErro(int cod, Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		AlertDialog dialog = builder.setTitle("Erro")
				.setPositiveButton("Ok", null).create();
		switch (cod) {
			case 1:// formataçao
				dialog.setMessage("Erro ao formatar a data!");
				dialog.show();
				break;
			case 2:// formataçao
				dialog.setMessage("Erro ao formatar a moeda!");
				dialog.show();
				break;
			case 3:// Cast
				dialog.setMessage("Erro ao pegar o Objeto - Casting!");
				dialog.show();
				break;
			case 4:// Op��o de menu n�o encontrada
				dialog.setMessage("Nenhum menu com este id foi encontrado!");
				dialog.show();
				break;
			case 5:// Erro ao trocar a cor
				dialog.setMessage("Erro ao definir a cor dos textos!");
				dialog.show();
				break;
			case 6:// Erro ao converter a String
				dialog.setMessage("Digite um ano válido");
				dialog.show();
				break;
			default:
				dialog.setMessage("Erro desconhecido!");
				dialog.show();
				break;
		}
	}
}
