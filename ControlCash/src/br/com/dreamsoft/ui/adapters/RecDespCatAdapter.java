/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.utils.Mensagens;

public class RecDespCatAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private HashMap<String, Double> map;
	private List<String> keys;
	private List<Double> valores;

	public RecDespCatAdapter(Context ctx, HashMap<String, Double> map) {

		this.map = map;
		valores = new ArrayList<Double>();
		keys = new ArrayList<String>();

		for (Double d : map.values()) {
			valores.add(d);
		}
		for (String key : map.keySet()) {
			keys.add(key);
		}

		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return map.size();
	}

	public Object getItem(int position) {
		return valores.get(position);
	}

	public long getItemId(int position) {
		return -1;
	}

	public View getView(int position, View v, ViewGroup vg) {
		// pega o nome da categoria e o saldo para esta categoria
		String categoria = keys.get(position);
		Double valor = valores.get(position);

		View view = inflater.inflate(android.R.layout.simple_list_item_2, null);
		// atualiza o nome na tela
		TextView tvNomeCat = (TextView) view.findViewById(android.R.id.text1);
		tvNomeCat.setText(categoria);
		// atualiza o saldo
		TextView saldo = (TextView) view.findViewById(android.R.id.text2);
		/*
		 * esse simbolo estranho faz add o simbolo monetario, nao usei um
		 * NumberFormat padrao pq ele coloca o valor negativo entre parenteses
		 */
		DecimalFormat df = new DecimalFormat("¤");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		try {
			saldo.setText(df.format(valor));
			if (valor < 0) {
				saldo.setTextColor(view.getResources().getColor(R.color.vermelho_escarlata));
			} else {
				saldo.setTextColor(view.getResources().getColor(R.color.royal_blue));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			Mensagens.msgErro(2, view.getContext());
		} catch (NullPointerException e) {
			e.printStackTrace();
			Mensagens.msgErro(5, view.getContext());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(-1, view.getContext());
		}

		return view;

	}
}
