/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.categoria;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
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
public class ListaCategorias extends Activity implements OnItemClickListener {

	private CategoriaDao dao;
	private ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.dao = Factory.createCategoriaDao(this);
		setTitle(getString(R.string.categorias_cadastradas));
		setContentView(R.layout.lista_categoria_relatorio);

		lv = (ListView) findViewById(R.id.lista_cat_rel);
		lv.setCacheColorHint(0x000000);
		lv.setOnItemClickListener(this);

		registerForContextMenu(lv);

		// LinearLayout ll = new LinearLayout(this);
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// lp.leftMargin = 45;
		//
		// ll.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_background));
		// // como estou usando um layout padrao do Android, aqui eu vou remover
		// a
		// // list do parent e então add ela no meu
		// // linearLayout, isto foi feito para que eu conseguise colocar a
		// margin.
		// ((FrameLayout) getListView().getParent()).removeAllViews();
		// // add a lista a meu linearLayout
		// ll.addView(getListView(), lp);
		// // dizendo ao Android para usar meu LinearLayout
		// setContentView(ll);

		Animacao.addAnimacaoLista(lv);

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();
	}

	private void refreshLista() {
		List<Categoria> lista = this.dao.buscarTodos();
		lv.setAdapter(new CategoriaAdapter(this, lista));

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent it = new Intent(this, CadEdtCategoria.class);
		Categoria cat = null;
		try {
			cat = (Categoria) lv.getAdapter().getItem(position);
		} catch (ClassCastException e) {
			e.printStackTrace();
			Mensagens.msgErro(3, this);
		}
		it.putExtra(CadEdtCategoria.OBJ_CAT, cat);
		startActivity(it);

	}

}
