/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.dreamsoft.model.Categoria;

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
		View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
		// recupera a Categoria
		Categoria rec = categorias.get(position);

		// view.setLayoutParams(lp);
		// atualiza o nome na tela
		TextView nome = (TextView) view.findViewById(android.R.id.text1);
		// TextView nome = new TextView(view.getContext());
		nome.setText(rec.getNome());

		return view;

	}
}
