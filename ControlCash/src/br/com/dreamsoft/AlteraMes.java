package br.com.dreamsoft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.R;

public class AlteraMes extends Activity implements OnClickListener {

	private Spinner spAno;
	private EditText edtAno;
	private Button btnAlterar;
	
	public static final String ANO = "ano";
	public static final String MES = "mes";
	
	
	private String[] meses = new String[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecionar_mes);
		setTitle("Control Cash");

		spAno = (Spinner) findViewById(R.main.meses);
		edtAno = (EditText) findViewById(R.main.edtAno);
		btnAlterar = (Button) findViewById(R.main.btnAltAno);
 
		ArrayAdapter adpt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,meses);
		adpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spAno.setAdapter(adpt);
		
		btnAlterar.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int mesDefinido = -1;
		int anoDefinido = -1;
		//pega o mes
		mesDefinido = spAno.getSelectedItemPosition()+1;
				
		try {
			String ano = edtAno.getText().toString();
			if (ano.length() == 4) {
				anoDefinido = Integer.parseInt(ano);
				// cria intent que ira receber os valores de retorno
				Intent it = new Intent();
				it.putExtra(AlteraMes.MES, mesDefinido);
				it.putExtra(AlteraMes.ANO, anoDefinido);
				// seta o resultado
				setResult(Activity.RESULT_OK, it);				
			} else {
				setResult(Activity.RESULT_CANCELED,null);
				// throw new NumberFormatException();
				Mensagens.msgErro(1, AlteraMes.this);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Log.w("ControlCash", "Ano invalido");
			Mensagens.msgErro(1, this);
		}
		
		this.finish();

	}		
}
