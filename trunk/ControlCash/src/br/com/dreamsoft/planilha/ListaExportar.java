package br.com.dreamsoft.planilha;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.ui.adapters.ExportAdapter;
import br.com.dreamsoft.utils.AdapterDaoPlanilha;
import br.com.dreamsoft.utils.AdapterDaoPlanilha.Tipo;
import br.com.dreamsoft.utils.Animacao;
import br.com.dreamsoft.utils.Mensagens;

public class ListaExportar extends Activity {

	private ListView lv;
	private CheckBox chkTodas;
	private List<ExportXls> lista;
	private Button btnExportar;
	private Tipo tipo;

	public static String TIPO = "tipo";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setTitle(getString(R.string.export_receita_despesa));
		setContentView(R.layout.export_list);

		if (getIntent().getExtras().get(TIPO) != null) {
			tipo = (Tipo) getIntent().getExtras().get(TIPO);
		}

		lv = (ListView) findViewById(R.id.list);
		chkTodas = (CheckBox) findViewById(R.id.chkTodas);
		btnExportar = (Button) findViewById(R.id.btnExportar);

		// getListView().setBackgroundResource(R.drawable.background);
		lv.setCacheColorHint(getResources().getColor(android.R.color.transparent));
		registerForContextMenu(lv);
		// faz a animacao da lista quando ela aparece
		Animacao.addAnimacaoLista(lv);
		// faz a animação na activity
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String data = sdf.format(((ApplicationControlCash) getApplication()).getData().getTime());
			lista = AdapterDaoPlanilha.buscarMes(this, data, tipo);
		} catch (ParseException e) {
			Mensagens.msgErro(1, this);
		}

		lv.setAdapter(new ExportAdapter(this, lista));

		addEvents();
	}

	private void addEvents() {
		btnExportar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<ExportXls> listaAux = new ArrayList<ExportXls>();
				for (int i = 0; i < lista.size(); i++) {

					if (((ExportAdapter) lv.getAdapter()).isEscolhido(i)) {
						listaAux.add(lista.get(i));
					}
				}
				List<List<ExportXls>> dados = new ArrayList<List<ExportXls>>();
				if (listaAux.size() > 0)
					dados.add(listaAux);

				Planilha pn = new Planilha(ListaExportar.this, dados);
				pn.gerarPlanilha();
			}
		});

		chkTodas.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// marcado ou desmarcando todos
				for (int i = 0; i < lista.size(); i++) {
					ExportAdapter adapter = (ExportAdapter) lv.getAdapter();
					adapter.setEscolhido(i, isChecked);
				}
			}
		});
	}
}
