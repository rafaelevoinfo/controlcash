/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.categoria;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.ui.adapters.CategoriaAdapter;
import br.com.dreamsoft.utils.Animacao;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaCategorias extends ListActivity {

	private CategoriaDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.dao = Factory.createCategoriaDao(this);
		setTitle(getString(R.string.categorias_cadastradas));
		getListView().setCacheColorHint(0x00000000);
		registerForContextMenu(getListView());

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

		Animacao.addAnimacaoLista(getListView());

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();
	}

	private void refreshLista() {
		List<Categoria> lista = this.dao.buscarTodos();
		setListAdapter(new CategoriaAdapter(this, lista));

		/*
		 * try { List<Categoria> lista = this.dao.buscarTodos();
		 * List<HashMap<String, String>> listCat = new ArrayList<HashMap<String,
		 * String>>();
		 * 
		 * for (Categoria cat : lista) { HashMap<String, String> m = new
		 * HashMap<String, String>(); m.put("ID", String.valueOf(cat.getId()));
		 * m.put("NOME", cat.getNome()); listCat.add(m); } //array contendo o
		 * nome das chaves do meu hash String[] from = { "NOME" }; //array
		 * contendo o nome dos campos no layout que ir�o receber a os valores do
		 * hash int[] to = { android.R.id.text1 }; setListAdapter(new
		 * SimpleAdapter(this, listCat, android.R.layout.simple_list_item_1,
		 * from, to));
		 * 
		 * } catch (Exception e) { e.printStackTrace(); Mensagens.msgErroBD(2,
		 * this); }
		 */
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent it = new Intent(this, CadEdtCategoria.class);
		Categoria cat = null;
		try {
			cat = (Categoria) l.getAdapter().getItem(position);
		} catch (ClassCastException e) {
			e.printStackTrace();
			Mensagens.msgErro(3, this);
		}
		it.putExtra(CadEdtCategoria.OBJ_CAT, cat);
		startActivity(it);
		/*
		 * try { //pego o hash que guarda o id e o nome da categoria HashMap map
		 * = (HashMap) l.getAdapter().getItem(position);
		 * it.putExtra(CadEdtCategoria.EDIT, true); //crio um novo obj Categoria
		 * e passo ele para a proxima tela it.putExtra(CadEdtCategoria.OBJ_CAT,
		 * new
		 * Categoria(Integer.parseInt(map.get("ID").toString()),map.get("NOME"
		 * ).toString())); startActivity(it); } catch (ClassCastException e) {
		 * e.printStackTrace(); Mensagens.msgErro(3, this); }
		 */

	}

	// protected void onActivityResult(int cod){
	//
	// }
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nova_categoria, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addCategoria:
				Intent it = new Intent(this, CadEdtCategoria.class);
				startActivity(it);
				return true;

			default:
				return false;
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.del_categoria, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		// pega as informa��es sobre qual item foi clicado

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		boolean result = false;

		switch (item.getItemId()) { // pego o id do item selecionado atraves do
									// info.id
			case R.id.btnDelCat:
				if (dao.deletar(Integer.parseInt(Long.toString(info.id)))) {
					result = true;
					Mensagens.msgOkSemFechar(this);
					refreshLista();
				} else {
					Mensagens.msgErroBD(3, this);
				}
				break;
			default:
				Mensagens.msgErro(4, this);
				break;
		}

		return false;
	}
}
