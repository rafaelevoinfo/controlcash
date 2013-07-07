/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.receita;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.ReceitaDao;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class CadEdtReceita extends Activity {

	private Button btCad;
	private EditText nome;
	private EditText valor;
	private Spinner categoria;
	private DatePicker date;
	// atributos static usados para saber se esta sendo feita uma edi��o ou
	// cadastro
	public static String OBJ_REC = "receita";

	private ArrayAdapter<Categoria> catsAdp;
	private List<Categoria> categorias;

	private boolean flagEdt = false;
	private int idRec = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cad_edt_rec);
		setTitle(getString(R.string.receita));

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

		this.btCad = (Button) findViewById(R.id.cadastrar);
		this.nome = (EditText) findViewById(R.id.nome);
		this.valor = (EditText) findViewById(R.id.valor);
		this.categoria = (Spinner) findViewById(R.id.categoria);
		this.date = (DatePicker) findViewById(R.id.date);

		CategoriaDao daoCat = Factory.createCategoriaDao(this);
		this.categorias = daoCat.buscarTodos();
		Collections.sort(this.categorias, new Comparator<Categoria>() {
			public int compare(Categoria cat, Categoria cat2) {
				return cat.getNome().compareTo(cat2.getNome());
			}

		});

		this.catsAdp = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_item, this.categorias);
		// da uma enfeita na lista
		catsAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.categoria.setAdapter(this.catsAdp);

		Calendar dataBase = ((ApplicationControlCash) getApplication()).getData();
		// preenchendo o datePicker com a database do sistema
		this.date.updateDate(dataBase.get(Calendar.YEAR), dataBase.get(Calendar.MONTH),
				dataBase.get(Calendar.DAY_OF_MONTH));

		// verifica se esta editando
		if (getIntent().getExtras() != null) {
			flagEdt = true;
			Receita rc = null;
			try {
				rc = (Receita) getIntent().getExtras().get(OBJ_REC);

				this.idRec = rc.getId();

				this.nome.setText(rc.getNome());
				this.valor.setText(String.valueOf(rc.getValor()));
				// percorre todas as categorias para ver qual deve ser
				// selecionada
				for (int i = 0; i < this.categoria.getCount(); i++) {
					Categoria cat = (Categoria) this.categoria.getItemAtPosition(i);
					if (cat.getId() == rc.getCategoria().getId()) {
						this.categoria.setSelection(i);
						break;
					}
				}

				Calendar cal = Calendar.getInstance();
				cal.setTime(rc.getDate());

				this.date.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

				this.btCad.setText(getString(R.string.salvar));
			} catch (ClassCastException e) {
				e.printStackTrace();
				Mensagens.msgErro(1, this);
			}
		}

		addListeners();
	}

	public void addListeners() {
		this.btCad.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					ReceitaDao rDao = Factory.createReceitaDao(CadEdtReceita.this);

					Receita rc = new Receita();

					rc.setNome(nome.getText().toString());
					rc.setValor(Double.parseDouble(valor.getText().toString()));

					Categoria cat = (Categoria) categoria.getSelectedItem();
					rc.setCategoria(cat);

					Calendar cal = Calendar.getInstance();
					cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth());

					// seta a data
					rc.setDate(cal.getTime());

					if (!flagEdt) {
						// realiza o cadastro
						if (rDao.cadastrar(rc) != -1) {
							Mensagens.msgOk(CadEdtReceita.this);
						} else {
							throw new Exception();
						}
					} else {
						// realiza a alteracao
						rc.setId(CadEdtReceita.this.idRec);

						if (rDao.alterar(rc)) {
							Mensagens.msgOk(CadEdtReceita.this);
							// CadastraReceita.this.finish();
						} else {
							throw new Exception();
						}
					}

				} catch (Exception e) {
					Mensagens.msgErroBD(1, CadEdtReceita.this);
				}
			}
		});

	}
}
