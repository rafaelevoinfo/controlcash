/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.adapters;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.model.Saldo;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Meses;

public class SaldosAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Saldo> saldos;
	private Context ctx;

	public SaldosAdapter(Context ctx, List<Saldo> saldos) {

		this.saldos = saldos;
		this.ctx = ctx;

		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return saldos.size();
	}

	public Object getItem(int position) {
		return saldos.get(position);
	}

	/**
	 * Retorna posi��o da lista que cont�m este saldo
	 */
	public long getItemId(int position) {
		return saldos.indexOf(saldos.get(position));
	}

	public View getView(int position, View v, ViewGroup vg) {
		// pega o saldo
		Saldo saldo = saldos.get(position);

		View view = inflater.inflate(android.R.layout.simple_list_item_2, null);
		// preenche com o mes
		TextView tvMes = (TextView) view.findViewById(android.R.id.text1);
		SimpleDateFormat sdfUS = new SimpleDateFormat("yyyy-MM-dd");

		try {
			tvMes.setText(Meses.converterDiaMesToString(sdfUS.parse(saldo.getData()).getMonth(),
					ctx));
		} catch (java.text.ParseException e1) {
			e1.printStackTrace();
			Mensagens.msgErro(1, view.getContext());
		}

		TextView tvSaldo = (TextView) view.findViewById(android.R.id.text2);
		if (saldo.getSaldo() < 0) {
			tvSaldo.setTextColor(Color.RED);
		} else {
			tvSaldo.setTextColor(view.getResources().getColor(R.color.azul_claro));
		}
		/*
		 * esse simbolo estranho faz add o simbolo monetario nao usei um
		 * NumberFormat padrao pq ele coloca o valor negativo entre parenteses
		 */
		DecimalFormat df = new DecimalFormat("¤");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		try {
			// saldo.setText(nf.format(resultado));
			tvSaldo.setText(df.format(saldo.getSaldo()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(2, view.getContext());
		}

		return view;

	}
}
