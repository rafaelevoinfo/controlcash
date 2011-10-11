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
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.utils.Mensagens;

public class CategoriaAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Categoria> categorias;

    public CategoriaAdapter(Context ctx, List<Categoria> categorias) {

        this.categorias = categorias;

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return categorias.size();
    }

    public Object getItem(int position) {
        return categorias.get(position);
    }

    public long getItemId(int position) {
        return categorias.get(position).getId();
    }

    public View getView(int position, View v, ViewGroup vg) {
    	//recupera a Categoria
        Categoria rec = categorias.get(position);
        //View view = inflater.inflate(R.layout.list_rec_desp, null);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        //atualiza o nome na tela
        TextView nome = (TextView) view.findViewById(android.R.id.text1);
        nome.setText(rec.getNome());
               
        return view;

    }
}
