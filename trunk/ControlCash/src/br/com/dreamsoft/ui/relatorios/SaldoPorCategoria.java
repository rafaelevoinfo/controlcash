package br.com.dreamsoft.ui.relatorios;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.ui.adapters.DespesaAdapter;
import br.com.dreamsoft.ui.adapters.RecDespCatAdapter;
import br.com.dreamsoft.ui.adapters.ReceitaAdapter;
import br.com.dreamsoft.ui.adapters.SaldosAdapter;
import br.com.dreamsoft.utils.Mensagens;

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
		
		lvReceitas = (ListView) findViewById(R.id.categoriasReceita);
		lvDespesas = (ListView) findViewById(R.id.categoriasDespesa);
		
		preencherListViews();
	}

	private void preencherListViews() {
		String data = (String) getIntent().getExtras().get(DATA);
		
		ReceitaDao daoRec = Factory.createReceitaDao(this);
		DespesaDao daoDesp = Factory.createDespesaDao(this);
		CategoriaDao daoCat = Factory.createCategoriaDao(this);
				
		
		HashMap<String, Double> categoriasRec= new HashMap<String,Double>();
		HashMap<String, Double> categoriasDesp= new HashMap<String,Double>();
		
		// busca todas as categorias
		List<Categoria> categorias = daoCat.buscarTodos();
		for(Categoria cat:categorias){
			double somaRec = daoRec.buscarSaldoCategoria(cat.getId(),data);
			double somaDesp = daoDesp.buscarSaldoCategoria(cat.getId(),data);
			if(somaRec!=0){
				//salvo no hash o resultado
				categoriasRec.put(cat.getNome(), somaRec);
Log.w("ControlCash", String.valueOf(somaRec));
			}
			if(somaDesp!=0){
				//salvo no hash o resultado
				categoriasDesp.put(cat.getNome(), somaDesp);
Log.w("ControlCash", String.valueOf(somaDesp));
			}
		}
		
		lvReceitas.setAdapter(new RecDespCatAdapter(this, categoriasRec));
		lvDespesas.setAdapter(new RecDespCatAdapter(this, categoriasDesp));
		//PAREI AQUI ---->>>> DAQUI EM DIANTE TENHO QUE FAZER UM MODO DE EXIBIR O NOME DA CATEGORIA JUNTAMENTE COM SEU VALOR, AMBOS
		//SALVOS NO HASMAP
		
	}
	
	
}