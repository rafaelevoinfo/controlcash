/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.adapters;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.utils.Mensagens;

public class DespesaAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Despesa> despesas;

	public DespesaAdapter(Context ctx, List<Despesa> Despesas) {

		this.despesas = Despesas;

		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return despesas.size();
	}

	public Object getItem(int position) {
		return despesas.get(position);
	}

	public long getItemId(int position) {
		return despesas.get(position).getId();
	}

	public View getView(int position, View v, ViewGroup vg) {
		// recupera a Despesa
		Despesa desp = despesas.get(position);
		View view = inflater.inflate(R.layout.list_rec_desp, null);
		// atualiza o nome na tela
		TextView nome = (TextView) view.findViewById(R.id.desc);
		nome.setText(desp.getNome());
		// atualiza o valor
		TextView valor = (TextView) view.findViewById(R.id.valor);
		// NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt",
		// "br"));
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		try {
			valor.setText(nf.format(desp.getValor()));
			// troca a cor
			valor.setTextColor(view.getResources().getColor(R.color.vermelho_escarlata));
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

		// atualiza a data
		TextView data = (TextView) view.findViewById(R.id.date);
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		try {
			data.setText(df.format(desp.getDate()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(1, view.getContext());
		}

		desp = null;
		return view;

	}
}
