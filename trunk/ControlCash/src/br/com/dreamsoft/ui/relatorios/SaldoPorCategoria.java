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
import android.widget.SimpleAdapter;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.ui.adapters.SaldosAdapter;
import br.com.dreamsoft.utils.Mensagens;

public class SaldoPorCategoria extends Activity {
	public static final String DATA = "data";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.lista);
		setTitle("Distribuição por categoria");

		buscaCategorias();
	}

	private void buscaCategorias() {
		String data = (String) getIntent().getExtras().get(DATA);
		
		ReceitaDao daoRec = Factory.createReceitaDao(this);
		DespesaDao daoDesp = Factory.createDespesaDao(this);
		CategoriaDao daoCat = Factory.createCategoriaDao(this);
		
		List<Receita> receitas = null;
		List<Despesa> despesas = null;
		
		HashMap<String, Double> valores= new HashMap<String,Double>();
		
		// busca todas as categorias
		List<Categoria> categorias = daoCat.buscarTodos();
		for(Categoria cat:categorias){
			double resultado = daoRec.buscarSaldoCategoria(cat.getId());
			if(resultado!=0){
				//salvo no hash o resultado
				valores.put(cat.getNome(), resultado);
				Log.w("ControlCash", String.valueOf(resultado));
			}
		}
		//PAREI AQUI ---->>>> DAQUI EM DIANTE TENHO QUE FAZER UM MODO DE EXIBIR O NOME DA CATEGORIA JUNTAMENTE COM SEU VALOR, AMBOS
		//SALVOS NO HASMAP
		
	}
}
