/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.receita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.planilha.ListaExportar;
import br.com.dreamsoft.ui.adapters.ReceitaAdapter;
import br.com.dreamsoft.ui.movimentacao.ListaMovimentacao;
import br.com.dreamsoft.utils.AdapterDaoPlanilha.Tipo;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaReceitas extends ListaMovimentacao<Receita> {

	private ReceitaDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.dao = Factory.createReceitaDao(this);
		setTitle(getString(R.string.receitas_cadastradas));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent it = new Intent(ListaReceitas.this, CadEdtReceita.class);
				try {
					Receita rc = (Receita) lv.getAdapter().getItem(arg2);
					it.putExtra(CadEdtReceita.OBJ_REC, rc);
					startActivity(it);
				} catch (ClassCastException e) {
					e.printStackTrace();
					Mensagens.msgErro(3, ListaReceitas.this);
				}
			}

		});
	}

	@Override
	protected void refreshLista() {
		super.refreshLista();
		lv.setAdapter(new ReceitaAdapter(this, listaMov));
	}

	// protected void onActivityResult(int cod){
	//
	// }
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_receita, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.addReceita:
				Intent it = new Intent(this, CadEdtReceita.class);
				startActivity(it);
				return true;
			case R.id.exportPlanilha:
				it = new Intent(this, ListaExportar.class);
				it.putExtra(ListaExportar.TIPO, Tipo.RECEITA);
				startActivity(it);
				return true;
			case R.id.ordernar:
				showDialog(0);
				return true;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.del_receita, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		// pega as informa��es sobre qual item foi clicado
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		boolean result = false;

		switch (item.getItemId()) {
			case R.id.btnDelRec:
				// pego o id do item selecionado atraves do info.id
				if (dao.deletar(Integer.parseInt(Long.toString(info.id)))) {
					result = true;
					Mensagens.msgOkSemFechar(this);
					refreshLista();
				} else {
					Mensagens.msgErroBD(1, this);
				}
				break;
			default:
				Mensagens.msgErro(4, this);
				break;
		}
		return result;
	}

	@Override
	protected List<Receita> getMovimentacoes() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// pega a data que esta sendo usada
		// String date = sdf.format(Main.data.getTime());
		String date = sdf.format(((ApplicationControlCash) getApplication()).getData().getTime());
		try {
			return this.dao.buscarMes(date);
		} catch (ParseException e) {
			e.printStackTrace();
			Mensagens.msgErro(1, this);
		}
		return null;
	}

}
