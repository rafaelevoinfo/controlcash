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
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.utils.Mensagens;

public class ReceitaAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Receita> receitas;

	public ReceitaAdapter(Context ctx, List<Receita> receitas) {

		this.receitas = receitas;

		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return receitas.size();
	}

	public Object getItem(int position) {
		return receitas.get(position);
	}

	public long getItemId(int position) {
		return receitas.get(position).getId();
	}

	public View getView(int position, View v, ViewGroup vg) {
		// recupera a receita
		Receita rec = receitas.get(position);
		View view = inflater.inflate(R.layout.list_rec_desp, null);
		// atualiza o nome na tela
		TextView nome = (TextView) view.findViewById(R.id.desc);
		nome.setText(rec.getNome());
		// atualiza o valor
		TextView valor = (TextView) view.findViewById(R.id.valor);
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		try {
			valor.setText(nf.format(rec.getValor()));
			valor.setTextColor(view.getResources().getColor(R.color.royal_blue));
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
			data.setText(df.format(rec.getDate()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(1, view.getContext());
		}

		rec = null;
		return view;

	}
}
