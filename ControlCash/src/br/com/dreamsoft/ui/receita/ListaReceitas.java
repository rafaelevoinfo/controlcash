/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.receita;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
import br.com.dreamsoft.Main;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.ui.adapters.ReceitaAdapter;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class ListaReceitas extends Activity {// extends ListActivity {

	private ReceitaDao dao;
	private ListView lv;
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.dao = Factory.createReceitaDao(this);
		setTitle("Receitas cadastradas");

		setContentView(R.layout.lista);

		lv = (ListView) findViewById(R.id.list);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(ListaReceitas.this, CadEdtReceita.class);
				try {
					Receita rc = (Receita) lv.getAdapter().getItem(arg2);
					it.putExtra(CadEdtReceita.EDIT, true);
					it.putExtra(CadEdtReceita.OBJ_REC, rc);
					startActivity(it);
				} catch (ClassCastException e) {
					e.printStackTrace();
					Mensagens.msgErro(3, ListaReceitas.this);
				}
			}

		});

		// getListView().setBackgroundResource(R.drawable.background);
		lv.setCacheColorHint(0x00000000);
		registerForContextMenu(lv);

		/*
		 * getListView().setCacheColorHint(0x00000000);
		 * registerForContextMenu(getListView());
		 */
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();

	}

	private void refreshLista() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		try {
			
			//String date = sdf.format(Main.data.getTime());
			String date = sdf.format(((ApplicationControlCash)getApplication()).getData().getTime());
			List<Receita> lista = this.dao.buscarMes(date);
			// setListAdapter(new ReceitaAdapter(this, lista));
			lv.setAdapter(new ReceitaAdapter(this, lista));

			tv = (TextView) findViewById(R.id.saldo);

			double total = 0;
			for (int i = 0; i < lv.getCount(); i++) {
				total += ((Receita) lv.getAdapter().getItem(i)).getValor();
			}
			// formata o valor
			NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt",
					"br"));
			nf.setMaximumFractionDigits(2);
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

	/*
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) { super.onListItemClick(l, v, position, id);
	 * 
	 * Intent it = new Intent(this, CadEdtReceita.class); try { Receita rc =
	 * (Receita) l.getAdapter().getItem(position);
	 * it.putExtra(CadEdtReceita.EDIT, true); it.putExtra(CadEdtReceita.OBJ_REC,
	 * rc); startActivity(it); } catch (ClassCastException e) {
	 * e.printStackTrace(); Mensagens.msgErro(3, this); }
	 * 
	 * 
	 * }
	 */

	// protected void onActivityResult(int cod){
	//
	// }
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nova_receita, menu);
		return true;
	}

	// chamado quando se clica em alguma opÃ§Ã£o do menu

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.addReceita:
				Intent it = new Intent(this, CadEdtReceita.class);
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
		inflater.inflate(R.menu.del_receita, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		// pega as informações sobre qual item foi clicado
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

}
