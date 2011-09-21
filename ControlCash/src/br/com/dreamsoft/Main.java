/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.ui.categoria.CadEdtCategoria;
import br.com.dreamsoft.ui.categoria.ListaCategorias;
import br.com.dreamsoft.ui.despesa.ListaDespesas;
import br.com.dreamsoft.ui.receita.ListaReceitas;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class Main extends Activity {

	private Button receitas;
	private Button despesas;
	private Button categoria;
	private TextView saldo;

	public void onResume() {
		super.onResume();
		atualizaSaldo();
	}

	public void atualizaSaldo() {
		ReceitaDao daoRec = Factory.createReceitaDao(this);
		DespesaDao daoDesp = Factory.createDespesaDao(this);

		double receitas = 0;
		double despesas = 0;

		try {
			for (Receita r : daoRec.buscarTodos()) {
				receitas += r.getValor();
			}
			for (Despesa desp : daoDesp.buscarTodos()) {
				despesas += desp.getValor();
			}

			double resultado = receitas - despesas;
			if(resultado < 0){
				saldo.setTextColor();
			}
			// formata o valor
			NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt",
					"br"));
			nf.setMaximumFractionDigits(2);
			try {
				saldo.setText(nf.format(resultado));
			} catch (Exception e) {
				e.printStackTrace();
				Mensagens.msgErro(2, this);
			}
			
			

		} catch (ParseException e) {
			Log.w("Erro", "Erro ao buscar os dados");
			Mensagens.msgErroBD(2, this);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Control Cash");

		receitas = (Button) findViewById(R.id.btnRec);
		despesas = (Button) findViewById(R.id.btnDesp);
		categoria = (Button) findViewById(R.id.btnCat);
		saldo = (TextView) findViewById(R.id.saldo);

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

		categoria.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(Main.this, ListaCategorias.class));
			}
		});
		
		atualizaSaldo();

	}
}
