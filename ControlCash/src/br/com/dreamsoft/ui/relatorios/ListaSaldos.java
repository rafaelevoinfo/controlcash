package br.com.dreamsoft.ui.relatorios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import br.com.dreamsoft.utils.Mensagens;

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
		setListAdapter(new SaldosAdapter(this, saldos));

		alterarLayout();
	}

	private void alterarLayout() {
		// alterando o layout para conseguir fazer o leftMargin
		LinearLayout ll = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 45;

		ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_background));
		// como estou usando um layout padrao do Android, aqui eu vou remover a
		// list do parent e então add ela no meu
		// linearLayout, isto foi feito para que eu conseguise colocar a margin.
		((FrameLayout) getListView().getParent()).removeAllViews();
		// add a lista a meu linearLayout
		ll.addView(getListView(), lp);
		// dizendo ao Android para usar meu LinearLayout
		setContentView(ll);
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
		it.putExtra(SaldoPorCategoria.DATA, saldo.getData());
		startActivity(it);
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

}
