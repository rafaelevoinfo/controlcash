/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.ui.categoria.ListaCategorias;
import br.com.dreamsoft.ui.despesa.CadEdtDespesa;
import br.com.dreamsoft.ui.despesa.ListaDespesas;
import br.com.dreamsoft.ui.receita.CadEdtReceita;
import br.com.dreamsoft.ui.receita.ListaReceitas;
import br.com.dreamsoft.ui.relatorios.ListaSaldos;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Meses;

/**
 * 
 * @author rafael
 */
public class Main extends Activity {

	private Button receitas;
	private Button despesas;
	private Button categoria;
	private Button rels;
	private TextView saldo;
	private ImageButton addDesp;
	private ImageButton addRec;
	private TextView mesAtual;
	//public static Calendar data;
	private Calendar data;
	private int mesDefinido = -1;
	private int anoDefinido = -1;

	ReceitaDao daoRec;
	DespesaDao daoDesp;

	private final int TROCAR_MES = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Control Cash");			
		
		daoRec = Factory.createReceitaDao(this);
		daoDesp = Factory.createDespesaDao(this);

		receitas = (Button) findViewById(R.id.btnRec);
		despesas = (Button) findViewById(R.id.btnDesp);
		rels = (Button) findViewById(R.id.btnRelatorios);
		categoria = (Button) findViewById(R.id.btnCat);
		saldo = (TextView) findViewById(R.id.saldo);

		addDesp = (ImageButton) findViewById(R.main.add_despesa);
		addRec = (ImageButton) findViewById(R.main.add_receita);
		mesAtual = (TextView) findViewById(R.main.mes);

		//data = Calendar.getInstance(new Locale("pt", "br"));
		data = ((ApplicationControlCash) getApplication()).getData();

		receitas.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(Main.this, ListaReceitas.class));
			}
		});
		despesas.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(Main.this, ListaDespesas.class));
			}
		});
		
		rels.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(Main.this, ListaSaldos.class));
			}
		});

		categoria.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(Main.this, ListaCategorias.class));
			}
		});

		addRec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Main.this, CadEdtReceita.class));

			}
		});

		addDesp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Main.this, CadEdtDespesa.class));

			}
		});

		atualizaSaldo();

	}

	public void onResume() {
		super.onResume();
		if (mesDefinido == -1 || anoDefinido == -1) {
			this.mesAtual.setText(Meses.getMes(data.get(Calendar.MONTH))+"/"+data.get(Calendar.YEAR));
		} else {
			this.mesAtual.setText(Meses.getMes(mesDefinido)+"/"+anoDefinido);
		}
		atualizaSaldo();
	}

	/*
	 * public enum Meses{
	 * JANEIRO(1),FEVEREIRO(2),MARCO(3),ABRIL(4),MAIO(5),JUNHO(6),
	 * JULHO(7),AGOSTO(8),SETEMBRO(9),OUTUBRO(10),NOVEMBRO(11),DEZEMBRO(12);
	 * 
	 * 
	 * private Meses(int m){ switch(m){ case 1: mes = "Janeiro"; case 2: mes =
	 * "Fevereiro"; case 3: mes = "Março"; case 4: mes = "Abril"; case 5: mes =
	 * "Maio"; case 6: mes = "Junho"; case 7: mes = "Julho"; case 8: mes =
	 * "Agosto"; case 9: mes = "Setembro"; case 10: mes = "Outubro"; case 11:
	 * mes = "Novembro"; case 12: mes = "Dezembro"; default: mes = "Indefinido";
	 * } } private String mes(){ return mes; } private String mes = ""; }
	 */

	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opcoes_main, menu);
		return true;
	}

	// chamado quando se clica em alguma opÃ§Ã£o do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.trocaMes:
				// deve-se trocar o mes de atuação
				startActivityForResult(new Intent(Main.this, AlteraMes.class),
						TROCAR_MES);

				return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent it) {
		// verifica se esse é o resultado da chamada do trocar mes e se tudo
		// ocorreu bem
		if (requestCode == TROCAR_MES && resultCode == RESULT_OK) {
			Bundle params = it != null ? it.getExtras() : null;
data.set(params.getInt(AlteraMes.ANO), params.getInt(AlteraMes.MES), Calendar.DAY_OF_MONTH);
			//mesDefinido = params.getInt(AlteraMes.MES);
			//anoDefinido = params.getInt(AlteraMes.ANO);

			// atualizaSaldo();
		}
	}

	public void atualizaSaldo() {

		double receitas = 0;
		double despesas = 0;

		List<Receita> listRec = null;
		List<Despesa> listDesp = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = null;
		try {
			// pega a data e converte para o padrão americano
			/*if (mesDefinido == -1 || anoDefinido == -1) {
				date = sdf.format(data.getTime());
			} else {
				data.set(anoDefinido, mesDefinido, Calendar.DAY_OF_MONTH);
				date = sdf.format(data.getTime());
			}*/
			date = sdf.format(data.getTime());
			
			listRec = daoRec.buscarMes(date);
			listDesp = daoDesp.buscarMes(date);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.w("ControlCash", "Erro ao buscar os dados");
			Mensagens.msgErroBD(2, this);
		}
		if (listRec != null) {
			for (Receita r : listRec) {
				receitas += r.getValor();
			}
		}

		if (listDesp != null) {
			for (Despesa d : listDesp) {
				despesas += d.getValor();
			}
		}

		double resultado = receitas - despesas;
		if (resultado < 0) {
			saldo.setTextColor(Color.RED);
		} else {
			saldo.setTextColor(getResources().getColor(R.color.azul_claro));
		}
		// formata o valor
		NumberFormat nf = NumberFormat
				.getNumberInstance(new Locale("pt", "br"));
		// DecimalFormat nf = new DecimalFormat("0.##");
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);

		try {
			// saldo.setText(nf.format(resultado));
			saldo.setText("R$ " + nf.format(resultado));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(2, this);
		}

	}

}
