package br.com.dreamsoft.ui.gastos;

import java.util.Calendar;
import java.util.Locale;

import br.com.dreamsoft.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Gastos extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exibe_gastos);
		setTitle("Gastos por categoria");	

	}
	
	

}
