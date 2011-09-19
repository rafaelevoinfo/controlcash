/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.despesa;

import java.text.ParseException;
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
import android.widget.ListView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.ui.adapters.DespesaAdapter;
import br.com.dreamsoft.ui.adapters.DespesaAdapter;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaDespesas extends ListActivity {

	private DespesaDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.dao = Factory.createDespesaDao(this);
		setTitle("Despesas cadastradas");
		// getListView().setBackgroundResource(R.drawable.background);
		getListView().setCacheColorHint(0x00000000);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();
	}

	private void refreshLista() {

		try {
			List<Despesa> lista = this.dao.buscarTodos();

			setListAdapter(new DespesaAdapter(this, lista));
		} catch (ParseException e) {
			e.printStackTrace();
			Mensagens.msgErro(1, this);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErroBD(2, this);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent it = new Intent(this, CadEdtDespesa.class);
		try {
			Despesa desp = (Despesa) l.getAdapter().getItem(position);
			it.putExtra(CadEdtDespesa.EDIT, true);
			it.putExtra(CadEdtDespesa.OBJ_DESP, desp);
			startActivity(it);
		} catch (ClassCastException e) {
			e.printStackTrace();
			Mensagens.msgErro(3, this);
		}

		
	}

	// protected void onActivityResult(int cod){
	//
	// }
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nova_despesa, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.addDespesa:
			Intent it = new Intent(this, CadEdtDespesa.class);
			startActivity(it);
			return true;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.del_despesa, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		//pega as informa��es sobre qual item foi clicado
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		boolean result = false;
		
		switch (item.getItemId()) {
			case R.id.btnDelDesp:		
				//pego o id do item selecionado atraves do info.id
				if(dao.deletar(Integer.parseInt(Long.toString(info.id)))){
					result= true;
					Mensagens.msgOkSemFechar(this);
					refreshLista();
				}else{
					Mensagens.msgErroBD(1, this);
				}
				break;
			default: Mensagens.msgErro(4, this);
				break;
			}
		return result;
	}
	
	
}
