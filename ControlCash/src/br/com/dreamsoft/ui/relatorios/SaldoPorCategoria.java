package br.com.dreamsoft.ui.relatorios;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.ui.adapters.RecDespCatAdapter;
import br.com.dreamsoft.utils.Planilha;

public class SaldoPorCategoria extends Activity {
	public static final String DATA = "data";

	private ListView lvReceitas;
	private ListView lvDespesas;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.lista_saldo_categoria);
		setTitle(R.string.relatorios_saldos_categorias);

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

		lvReceitas = (ListView) findViewById(R.id.categoriasReceita);
		lvReceitas.setCacheColorHint(getResources().getColor(android.R.color.transparent));
		lvDespesas = (ListView) findViewById(R.id.categoriasDespesa);
		lvDespesas.setCacheColorHint(getResources().getColor(android.R.color.transparent));

		preencherListViews();

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.exportar, menu);
		return true;
	}

	// chamado quando se clica em alguma opção do menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.exportPlanilha:
				// TODO: Refazer isto aqui. fiz assim somente para teste
				Planilha pn = new Planilha(this);
				try {
					pn.createPlanilha();
				} catch (RowsExceededException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return true;
			default:
				return false;
		}

	}

	private void preencherListViews() {
		String data = (String) getIntent().getExtras().get(DATA);

		ReceitaDao daoRec = Factory.createReceitaDao(this);
		DespesaDao daoDesp = Factory.createDespesaDao(this);
		CategoriaDao daoCat = Factory.createCategoriaDao(this);

		HashMap<String, Double> categoriasRec = new HashMap<String, Double>();
		HashMap<String, Double> categoriasDesp = new HashMap<String, Double>();

		// busca todas as categorias
		List<Categoria> categorias = daoCat.buscarTodos();
		for (Categoria cat : categorias) {
			double somaRec = daoRec.buscarSaldoCategoria(cat.getId(), data);
			double somaDesp = daoDesp.buscarSaldoCategoria(cat.getId(), data);
			if (somaRec != 0) {
				// salvo no hash o resultado
				categoriasRec.put(cat.getNome(), somaRec);
				// Log.w("ControlCash", String.valueOf(somaRec));
			}
			if (somaDesp != 0) {
				// salvo no hash o resultado
				categoriasDesp.put(cat.getNome(), somaDesp);
				// Log.w("ControlCash", String.valueOf(somaDesp));
			}
		}

		lvReceitas.setAdapter(new RecDespCatAdapter(this, categoriasRec));
		lvDespesas.setAdapter(new RecDespCatAdapter(this, categoriasDesp));
		// PAREI AQUI ---->>>> DAQUI EM DIANTE TENHO QUE FAZER UM MODO DE EXIBIR
		// O NOME DA CATEGORIA JUNTAMENTE COM SEU VALOR, AMBOS
		// SALVOS NO HASMAP

	}

}
