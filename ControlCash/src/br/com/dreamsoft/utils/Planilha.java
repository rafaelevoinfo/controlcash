package br.com.dreamsoft.utils;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import br.com.dreamsoft.R;
import br.com.dreamsoft.utils.Mensagens.Erros;

public class Planilha {
	private Context ctx;
	private WritableWorkbook planilha;

	public Planilha(Context ctx) {
		this.ctx = ctx;

	}

	public void createPlanilha() throws InterruptedException, RowsExceededException, WriteException,
			IOException {
		createFile();
		// while (planilha == null) {
		// // espera ate que o arquivo seja criado
		// synchronized (this) {
		// this.wait(1000 * 60);// espera um minuto
		// }
		// }
		// TODO; Criar alguma maneira de nao travar o sistema caso o usuario nao
		// informe o nome do arquivo no
		// metodo createFile;

	}

	private void createFile() {
		AlertDialog.Builder alerta = new AlertDialog.Builder(ctx);
		alerta.setTitle(ctx.getString(R.string.salvar));
		LayoutInflater inflater = LayoutInflater.from(ctx);
		final View view = inflater.inflate(R.layout.input_dialog, null);
		alerta.setView(view);

		alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String fileName = "";
				EditText edtNome = (EditText) view.findViewById(R.id.edtEdit1);
				if (edtNome.getText().toString().equals("")) {
					fileName = ctx.getString(R.string.planilha);
				} else {
					fileName = edtNome.getText().toString();
				}
				File file = null;
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					file = new File(ctx.getExternalFilesDir(null), fileName + ".xls");
				} else {
					file = new File(ctx.getDir("ControlCash", Context.MODE_PRIVATE), ctx
							.getString(R.string.planilha) + ".xls");
				}

				try {
					planilha = Workbook.createWorkbook(file);
					WritableSheet sheet = planilha.createSheet(ctx.getString(R.string.app_name), 0);
					sheet.addCell(new Label(0, 0, "Teste"));

					planilha.write();
					planilha.close();
				} catch (IOException e) {
					Mensagens.msgErro(Erros.CRIAR_FILE_PLANILHA, ctx);
				} catch (RowsExceededException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// Planilha.this.notifyAll();// usado para avisar q o
					// arquivo
					// foi criado.
				}

			}
		}).show();

		alerta.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Planilha.this.notifyAll();
			}
		});
	}
}
