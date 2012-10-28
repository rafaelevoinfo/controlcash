/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.categoria;

import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Mensagens.Infos;

/**
 * 
 * @author rafael
 */
public class CadEdtCategoria extends Activity {

	private Button btnCat;
	private EditText edtNome;
	private EditText edtIdExport;
	private ImageButton btnInfoIdExport;

	private Animation anin;
	private Handler handle;

	// atributos static usados para saber se esta sendo feita uma edi��o ou
	// cadastro
	public static String OBJ_CAT = "categoria";

	private boolean flagEdt = false;
	private int idCat = -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.cad_edt_cat);
		setTitle(getString(R.string.categoria));

		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		anin = AnimationUtils.loadAnimation(this, R.anim.press_btn);
		handle = new Handler();

		this.btnCat = (Button) findViewById(R.id.btnCadCat);
		this.edtNome = (EditText) findViewById(R.id.nomeCat);
		this.btnInfoIdExport = (ImageButton) findViewById(R.id.btnInfoIdExport);
		this.edtIdExport = (EditText) findViewById(R.id.edtIdExport);

		// verifica se esta editando
		if (getIntent().getExtras() != null) {
			flagEdt = true;
			Categoria cat = null;
			try {
				cat = (Categoria) getIntent().getExtras().get(OBJ_CAT);

				this.idCat = cat.getId();
				this.edtNome.setText(cat.getNome());
				if (cat.getIdExport() != 0) {
					this.edtIdExport.setText(String.valueOf(cat.getIdExport()));
				}

				this.btnCat.setText(getString(R.string.salvar));
			} catch (ClassCastException e) {
				e.printStackTrace();
				Mensagens.msgErro(3, this);
			}
		}

		configurarInfoButton();

		configurarAddButton();

	}

	private void configurarAddButton() {
		this.btnCat.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				CategoriaDao catDao = Factory.createCategoriaDao(CadEdtCategoria.this);

				Categoria cat = new Categoria();
				cat.setNome(edtNome.getText().toString());
				if (!edtIdExport.getText().toString().trim().equals("")) {
					cat.setIdExport(Integer.parseInt(edtIdExport.getText().toString()));
				}
				try {
					if (!flagEdt) {
						// realiza o cadastro
						if (catDao.cadastrar(cat) != -1) {
							Mensagens.msgOk(CadEdtCategoria.this);
						} else {
							throw new SQLException();
						}
					} else {
						// realiza a alteracao
						cat.setId(CadEdtCategoria.this.idCat);
						// rc.getCategoria().setId(CadEdtCategoria.this.idCat);
						if (catDao.alterar(cat)) {
							Mensagens.msgOk(CadEdtCategoria.this);
							// CadastraCategoria.this.finish();
						} else {
							throw new SQLException();
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
					Mensagens.msgErroBD(1, CadEdtCategoria.this);
				}
			}
		});

	}

	private void configurarInfoButton() {

		this.btnInfoIdExport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnInfoIdExport.startAnimation(anin);
				handle.postDelayed(new Runnable() {
					@Override
					public void run() {
						Mensagens.msgInfo(Infos.INFO_ID_EXPORT, CadEdtCategoria.this);
					}
				}, 250);

			}
		});

	}
}
