/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.despesa;

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
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.planilha.ListaExportar;
import br.com.dreamsoft.ui.adapters.DespesaAdapter;
import br.com.dreamsoft.ui.movimentacao.ListaMovimentacao;
import br.com.dreamsoft.utils.AdapterDaoPlanilha.Tipo;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaDespesas extends ListaMovimentacao<Despesa> {
	private DespesaDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.dao = Factory.createDespesaDao(this);
		setTitle(getString(R.string.despesas_cadastradas));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent it = new Intent(ListaDespesas.this, CadEdtDespesa.class);
				try {
					Despesa desp = (Despesa) lv.getAdapter().getItem(position);
					it.putExtra(CadEdtDespesa.OBJ_DESP, desp);
					startActivity(it);
				} catch (ClassCastException e) {
					e.printStackTrace();
					Mensagens.msgErro(3, ListaDespesas.this);
				}
			}

		});
	}

	@Override
	protected void refreshLista() {
		super.refreshLista();
		lv.setAdapter(new DespesaAdapter(this, listaMov));
	}

	@Override
	protected List<Despesa> getMovimentacoes() {
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

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_despesa, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent it = null;
		switch (item.getItemId()) {

			case R.id.addDespesa:
				it = new Intent(this, CadEdtDespesa.class);
				startActivity(it);
				return true;
			case R.id.exportPlanilha:
				it = new Intent(this, ListaExportar.class);
				it.putExtra(ListaExportar.TIPO, Tipo.DESPESA);
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
		inflater.inflate(R.menu.del_despesa, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		// pega as informa��es sobre qual item foi clicado
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		boolean result = false;

		switch (item.getItemId()) {
			case R.id.btnDelDesp:
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

}
