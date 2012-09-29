/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

	private ImageButton receitas;
	private ImageButton despesas;
	private ImageButton categoria;
	private ImageButton rels;
	private TextView saldo;
	private ImageButton addDesp;
	private ImageButton addRec;
	private TextView mesAtual;
	// public static Calendar data;
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
		setTitle(getString(R.string.app_name));

		daoRec = Factory.createReceitaDao(this);
		daoDesp = Factory.createDespesaDao(this);

		receitas = (ImageButton) findViewById(R.id.btnRec);
		despesas = (ImageButton) findViewById(R.id.btnDesp);
		rels = (ImageButton) findViewById(R.id.btnRelatorios);
		categoria = (ImageButton) findViewById(R.id.btnCat);
		saldo = (TextView) findViewById(R.id.saldo);

		addDesp = (ImageButton) findViewById(R.main.add_despesa);
		addRec = (ImageButton) findViewById(R.main.add_receita);
		mesAtual = (TextView) findViewById(R.main.mes);

		// data = Calendar.getInstance(new Locale("pt", "br"));
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
			this.mesAtual.setText(Meses.converterDiaMesToString(data.get(Calendar.MONTH), this) + "/"
					+ data.get(Calendar.YEAR));
		} else {
			this.mesAtual.setText(Meses.converterDiaMesToString(mesDefinido, this) + "/" + anoDefinido);
		}
		atualizaSaldo();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opcoes_main, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.trocaMes:
				// deve-se trocar o mes de atua��o
				startActivityForResult(new Intent(Main.this, AlteraMes.class), TROCAR_MES);

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
		}
	}

	public void atualizaSaldo() {

		double receitas = 0;
		double despesas = 0;

		List<Receita> listRec = null;
		List<Despesa> listDesp = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// java.text.DateFormat df = DateFormat.getDateFormat(this);

		String date = null;
		try {
			// pega a data e converte para o padr�o americano
			/*
			 * if (mesDefinido == -1 || anoDefinido == -1) { date =
			 * sdf.format(data.getTime()); } else { data.set(anoDefinido,
			 * mesDefinido, Calendar.DAY_OF_MONTH); date =
			 * sdf.format(data.getTime()); }
			 */
			date = sdf.format(data.getTime());

			listRec = daoRec.buscarMes(date);
			listDesp = daoDesp.buscarMes(date);
		} catch (ParseException e) {
			// e.printStackTrace();
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
		DecimalFormat df = new DecimalFormat("¤");// esse simbolo estranho faz
													// add o simbolo monetario
		// nao usei um NumberFormat padrao pq ele coloca o valor negativo entre
		// parenteses
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		try {
			saldo.setText(df.format(resultado));
			// saldo.setText("R$ " + nf.format(resultado));
		} catch (Exception e) {
			Log.w("ControlCash", "Erro ao formatar o saldo");
			Mensagens.msgErro(2, this);
		}

	}
}
