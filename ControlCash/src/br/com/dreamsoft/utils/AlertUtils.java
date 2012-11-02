package br.com.dreamsoft.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import br.com.dreamsoft.R;

public class AlertUtils {

	public static final int OK = 1;
	public static final int CANCEL = 2;

	public enum OpcaoEscolhida {
		OK(1), CANCEL(2);

		private int value;

		private OpcaoEscolhida(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String getTextoValue() {
			return String.valueOf(value);
		}
	}

	public static void getTexto(Context ctx, final Handler handler, String title, int layout) {
		AlertDialog.Builder alerta = new AlertDialog.Builder(ctx);
		LayoutInflater inflater = LayoutInflater.from(ctx);
		final View view = inflater.inflate(layout, null);
		alerta.setView(view);
		alerta.setTitle(title);

		alerta.setPositiveButton(ctx.getString(R.string.positive_button), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String texto = "";
				EditText edtNome = (EditText) view.findViewById(R.id.edtEdit1);
				texto = edtNome.getText().toString();

				// avisando quem chamou q o user clicou no ok
				Message msg = new Message();
				msg.what = OpcaoEscolhida.OK.getValue();
				Bundle bundle = new Bundle();
				bundle.putString(OpcaoEscolhida.OK.getTextoValue(), texto);
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		});

		alerta.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				handler.sendEmptyMessage(OpcaoEscolhida.CANCEL.getValue());
			}
		});

		alerta.show();
	}
}
