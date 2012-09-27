/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.despesa;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.widget.TextView;
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.ui.adapters.DespesaAdapter;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaDespesas extends Activity {

	private DespesaDao dao;
	private ListView lv;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.dao = Factory.createDespesaDao(this);
		setTitle(getString(R.string.despesas_cadastradas));
		setContentView(R.layout.lista);

		lv = (ListView) findViewById(R.id.list);
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

		// getListView().setBackgroundResource(R.drawable.background);
		lv.setCacheColorHint(0x00000000);
		registerForContextMenu(lv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();

	}

	private void refreshLista() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// pega a data que esta sendo usada
			// String date = sdf.format(Main.data.getTime());
			String date = sdf.format(((ApplicationControlCash) getApplication()).getData()
					.getTime());
			List<Despesa> lista = this.dao.buscarMes(date);
			lv.setAdapter(new DespesaAdapter(this, lista));

			tv = (TextView) findViewById(R.id.saldo);

			double total = 0;
			for (int i = 0; i < lv.getCount(); i++) {
				total += ((Despesa) lv.getAdapter().getItem(i)).getValor();
			}
			// formata o valor
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);
			try {
				tv.setText(nf.format(total));
			} catch (Exception e) {
				e.printStackTrace();
				Mensagens.msgErro(2, this);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			Mensagens.msgErro(1, this);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErroBD(2, this);
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
