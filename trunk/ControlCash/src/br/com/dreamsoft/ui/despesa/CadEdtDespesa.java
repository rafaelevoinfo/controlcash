/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.despesa;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import br.com.dreamsoft.ApplicationControlCash;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.DespesaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.utils.Mensagens;

/**
 * 
 * @author rafael
 */
public class CadEdtDespesa extends Activity {

	private Button btCad;
	private EditText nome;
	private EditText valor;
	private Spinner categoria;
	private DatePicker date;
	private ImageButton ibtnCalc;
	private Animation anin;
	private Handler handler;
	// atributos static usados para saber se esta sendo feita uma edi��o ou
	// cadastro
	public static String OBJ_DESP = "despesa";

	private ArrayAdapter<Categoria> catsAdp;
	private List<Categoria> categorias;

	private boolean flagEdt = false;
	private int idRec = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cad_edt_desp);
		setTitle(getString(R.string.despesa));

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

		this.btCad = (Button) findViewById(R.id.cadastrarDesp);
		this.nome = (EditText) findViewById(R.id.nome);
		this.valor = (EditText) findViewById(R.id.valor);
		this.categoria = (Spinner) findViewById(R.id.categoria);
		this.date = (DatePicker) findViewById(R.id.date);
		ibtnCalc = (ImageButton) findViewById(R.id.ibtnCalc);
		anin = AnimationUtils.loadAnimation(this, R.anim.press_btn);
		handler = new Handler();

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
			Despesa desp = null;
			try {
				desp = (Despesa) getIntent().getExtras().get(OBJ_DESP);

				this.idRec = desp.getId();

				this.nome.setText(desp.getNome());
				this.valor.setText(String.valueOf(desp.getValor()));
				// percorre todas as categorias para ver qual deve ser
				// selecionada
				for (int i = 0; i < this.categoria.getCount(); i++) {
					Categoria cat = (Categoria) this.categoria.getItemAtPosition(i);
					if (cat.getId() == desp.getCategoria().getId()) {
						this.categoria.setSelection(i);
						break;
					}
				}

				// this.idCat = rc.getCategoria().getId();

				Calendar cal = Calendar.getInstance();
				cal.setTime(desp.getDate());

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
					DespesaDao dao = Factory.createDespesaDao(CadEdtDespesa.this);

					Despesa desp = new Despesa();

					desp.setNome(nome.getText().toString());
					desp.setValor(Double.parseDouble(valor.getText().toString()));

					Categoria cat = (Categoria) categoria.getSelectedItem();
					desp.setCategoria(cat);

					Calendar cal = Calendar.getInstance();
					cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth());

					// seta a data
					desp.setDate(cal.getTime());

					if (!flagEdt) {
						// realiza o cadastro
						if (dao.cadastrar(desp) != -1) {
							Mensagens.msgOk(CadEdtDespesa.this);
						} else {
							throw new Exception();
						}
					} else {

						// realiza a alteracao
						desp.setId(CadEdtDespesa.this.idRec);
						// TODO: Retirar essas mensagens e colocar um Toast
						if (dao.alterar(desp)) {
							Mensagens.msgOk(CadEdtDespesa.this);
							// CadastraDespesa.this.finish();
						} else {
							throw new Exception();
						}
					}

				} catch (Exception e) {
					Mensagens.msgErroBD(1, CadEdtDespesa.this);
				}
			}
		});

		// ibtnCalc.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// v.startAnimation(anin);
		// handler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// Expression exp = new Expression("2+5*2/2");
		// Mensagens.msgOkSemFechar(CadEdtDespesa.this, String.valueOf(exp.resolve()), 5000);
		// }
		// }, 250);
		//
		// }
		// });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
