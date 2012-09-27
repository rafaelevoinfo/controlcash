package br.com.dreamsoft;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Meses;

public class AlteraMes extends Activity implements OnClickListener {

	private Spinner spAno;
	private EditText edtAno;
	private Button btnAlterar;

	public static final String ANO = "ano";
	public static final String MES = "mes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecionar_mes);
		setTitle(getString(R.string.app_name));

		spAno = (Spinner) findViewById(R.main.meses);
		edtAno = (EditText) findViewById(R.main.edtAno);
		btnAlterar = (Button) findViewById(R.main.btnAltAno);

		String[] meses = new String[12];
		for (int m = 0; m < 12; m++) {
			meses[m] = Meses.converterDiaMesToString(m, AlteraMes.this);
		}

		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, meses);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spAno.setAdapter(adpt);
		// seta o mes atual como padr�o
		// Calendar data = Calendar.getInstance(new Locale("pt","br"));
		spAno.setSelection(((ApplicationControlCash) getApplication()).getData()
				.get(Calendar.MONTH));
		edtAno.setText(String.valueOf(((ApplicationControlCash) getApplication()).getData().get(
				Calendar.YEAR)));
		// spAno.setSelection(Main.data.get(Calendar.MONTH));
		// edtAno.setText(String.valueOf(Main.data.get(Calendar.YEAR)));

		btnAlterar.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int mesDefinido = -1;
		int anoDefinido = -1;
		// pega o mes
		mesDefinido = spAno.getSelectedItemPosition();

		String ano = edtAno.getText().toString();
		if (ano.length() == 4) {
			try {
				anoDefinido = Integer.parseInt(ano);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Mensagens.msgErro(6, this);
			}
			// cria intent que ira receber os valores de retorno
			Intent it = new Intent();
			it.putExtra(AlteraMes.MES, mesDefinido);
			it.putExtra(AlteraMes.ANO, anoDefinido);
			// seta o resultado
			setResult(Activity.RESULT_OK, it);
			this.finish();
		} else {
			setResult(Activity.RESULT_CANCELED, null);
			// throw new NumberFormatException();
			// Log.w("ControlCash", "Ano inválido");
			Mensagens.msgErro(6, AlteraMes.this);
		}

	}
}
