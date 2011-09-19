/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.adapters;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
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
    	//recupera a Despesa
        Despesa rec = despesas.get(position);
        View view = inflater.inflate(R.layout.list_rec_desp, null);
        //atualiza o nome na tela
        TextView nome = (TextView) view.findViewById(R.id.desc);
        nome.setText(rec.getNome());
        //atualiza o valor
        TextView valor = (TextView) view.findViewById(R.id.valor);
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "br"));
        nf.setMaximumFractionDigits(2);
        try {
            valor.setText(nf.format(rec.getValor()));
        } catch (Exception e) {
            e.printStackTrace();
            Mensagens.msgErro(2,view.getContext());
        }

        //atualiza a data  
        TextView data = (TextView) view.findViewById(R.id.date);
        SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            data.setText(sdf.format(rec.getDate()));
        } catch (Exception e) {
            e.printStackTrace();
            Mensagens.msgErro(1, view.getContext());
        }

        rec = null;
        return view;

    }
}
