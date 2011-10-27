package br.com.dreamsoft.ui.relatorios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.model.Saldo;
import br.com.dreamsoft.ui.adapters.CategoriaAdapter;
import br.com.dreamsoft.ui.adapters.SaldosAdapter;
import br.com.dreamsoft.ui.categoria.CadEdtCategoria;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListaSaldos extends ListActivity {
	private ReceitaDao daoRec;
	private DespesaDao daoDesp;

	private List<Saldo> saldos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.relatorios_saldos);

		this.daoDesp = Factory.createDespesaDao(this);
		this.daoRec = Factory.createReceitaDao(this);
		this.saldos = new ArrayList<Saldo>();
		// Cria o calendar que irá fazer as operações com datas
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdfUS = new SimpleDateFormat("yyyy-MM-dd");

		List<Receita> receitas = null;
		List<Despesa> despesas = null;
		// volta 5 meses atrás
		for (int i = 0; i < 4; i++) {
			// formata as datas
			String data = sdfUS.format(cal.getTime());
			try {
				receitas = daoRec.buscarMes(data);
				despesas = daoDesp.buscarMes(data);
			} catch (ParseException e) {
				e.printStackTrace();
				Mensagens.msgErro(1, this);
				Log.w("ControlCash",
						"Erro ao formatar as datas na busca por mes");
			}

			saldos.add(calculaSaldo(receitas, despesas, data));
			cal.roll(Calendar.MONTH, false);
		}
		setListAdapter(new SaldosAdapter(this, saldos));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent it = new Intent(this, SaldoPorCategoria.class);
		Saldo saldo = null;
		try {
			saldo = (Saldo) l.getAdapter().getItem(position);
		} catch (ClassCastException e) {
			e.printStackTrace();
			Mensagens.msgErro(3, this);
		}
		it.putExtra(SaldoPorCategoria.DATA,saldo.getData());
		startActivity(it);
	}

	/**
	 * Calcula o saldo subtraindo o total das receitas do total das despesas
	 * 
	 * @param receitas
	 * @param despesas
	 * @param data
	 *            - Data indicando o mes da operação.
	 * @return
	 */
	private Saldo calculaSaldo(List<Receita> receitas, List<Despesa> despesas,
			String data) {
		double res = 0.0;
		for (Receita r : receitas) {
			res += r.getValor();
		}
		for (Despesa d : despesas) {
			res -= d.getValor();
		}

		Saldo saldo = new Saldo();
		saldo.setSaldo(res);
		saldo.setData(data);

		return saldo;

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();
	}

	private void refreshLista() {

	}

}
