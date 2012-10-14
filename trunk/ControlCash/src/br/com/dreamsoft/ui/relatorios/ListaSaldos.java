package br.com.dreamsoft.ui.relatorios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.model.Saldo;
import br.com.dreamsoft.ui.adapters.SaldosAdapter;
import br.com.dreamsoft.utils.Animacao;
import br.com.dreamsoft.utils.Mensagens;

public class ListaSaldos extends Activity implements OnItemClickListener {
	private ReceitaDao daoRec;
	private DespesaDao daoDesp;
	private ListView lv;

	private List<Saldo> saldos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.relatorios_saldos);
		setContentView(R.layout.lista_categoria_relatorio);

		lv = (ListView) findViewById(R.id.lista_cat_rel);
		// impede que o fundo fique branco durante a animação
		lv.setCacheColorHint(0x000000);
		lv.setOnItemClickListener(this);

		this.daoDesp = Factory.createDespesaDao(this);
		this.daoRec = Factory.createReceitaDao(this);
		this.saldos = new ArrayList<Saldo>();
		// pega a data setada para o sistema
		// Calendar cal = (Calendar)Main.data.clone();
		Calendar cal = (Calendar) ((ApplicationControlCash) getApplication()).getData().clone();
		SimpleDateFormat sdfUS = new SimpleDateFormat("yyyy-MM-dd");

		List<Receita> receitas = null;
		List<Despesa> despesas = null;
		// volta 6 meses atr�s
		for (int i = 0; i < 6; i++) {
			// formata as datas
			String data = sdfUS.format(cal.getTime());
			try {
				receitas = daoRec.buscarMes(data);
				despesas = daoDesp.buscarMes(data);
			} catch (ParseException e) {
				e.printStackTrace();
				Mensagens.msgErro(1, this);
				Log.w("ControlCash", "Erro ao formatar as datas na busca por mes");
			}

			saldos.add(calculaSaldo(receitas, despesas, data));
			cal.add(Calendar.MONTH, -1);

		}
		lv.setAdapter(new SaldosAdapter(this, saldos));

		Animacao.addAnimacaoLista(lv);
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	/**
	 * Calcula o saldo subtraindo o total das receitas do total das despesas
	 * 
	 * @param receitas
	 * @param despesas
	 * @param data
	 *            - Data indicando o mes da opera��o.
	 * @return
	 */
	private Saldo calculaSaldo(List<Receita> receitas, List<Despesa> despesas, String data) {
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
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent it = new Intent(this, SaldoPorCategoria.class);
		Saldo saldo = null;
		try {
			saldo = (Saldo) lv.getAdapter().getItem(position);
		} catch (ClassCastException e) {
			e.printStackTrace();
			Mensagens.msgErro(3, this);
		}
		it.putExtra(SaldoPorCategoria.DATA, saldo.getData());
		startActivity(it);

	}

}
