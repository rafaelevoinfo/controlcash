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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.model.Movimentacao;
import br.com.dreamsoft.planilha.ExportXls;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Mensagens.Erros;

public class ExportAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ExportXls> valores;
	private boolean[] escolhidos;

	public ExportAdapter(Context ctx, List<ExportXls> valores) {
		this.valores = valores;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		escolhidos = new boolean[valores.size()];
	}

	public int getCount() {
		return valores.size();
	}

	public Object getItem(int position) {
		return valores.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public boolean isEscolhido(int position) {
		return escolhidos[position];
	}

	public void setEscolhido(int position, boolean escolhido) {
		escolhidos[position] = escolhido;
		notifyDataSetChanged();
	}

	public View getView(int position, View v, ViewGroup vg) {
		Movimentacao mv = null;
		View view = inflater.inflate(R.layout.export_list_row, null);
		// recupera a Despesa ou receita
		if (valores.get(position) instanceof Movimentacao) {
			mv = (Movimentacao) valores.get(position);
			// atualiza o nome na tela
			TextView nome = (TextView) view.findViewById(R.id.desc);
			nome.setText(mv.getNome());

			CheckBox chk = (CheckBox) view.findViewById(R.id.chkEscolhido);
			final int pos = position;
			chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					escolhidos[pos] = isChecked;
				}
			});

			chk.setChecked(escolhidos[position]);

			// atualiza o valor
			TextView valor = (TextView) view.findViewById(R.id.valor);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);

			// atualiza a data
			TextView data = (TextView) view.findViewById(R.id.date);
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

			try {
				valor.setText(nf.format(mv.getValor()));
				data.setText(df.format(mv.getDate()));
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

		} else {
			Mensagens.msgErro(Erros.MOVIMENTACAO_INVALIDA, view.getContext());
		}
		return view;
	}
}
