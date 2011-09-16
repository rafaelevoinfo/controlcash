/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.categoria;

import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.utils.Mensagens;

/**
 *
 * @author rafael
 */
public class CadEdtCategoria extends Activity {

    private Button btCat;
    private EditText nome;
      
    //atributos static usados para saber se esta sendo feita uma edição ou cadastro
    public static String EDIT = "true";
    public static String OBJ_CAT = null;

    private boolean flagEdt = false;
    private int idCat = -1;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.cad_edt_cat);
        setTitle("Categoria");
              
        this.btCat = (Button) findViewById(R.id.btnCadCat);
        this.nome = (EditText) findViewById(R.id.nomeCat);                     
       
        //verifica se esta editando
        if (getIntent().getBooleanExtra(CadEdtCategoria.EDIT, false)) {
            flagEdt = true;
            Categoria cat = null;
            try {
                cat = (Categoria) getIntent().getExtras().get(OBJ_CAT);
            
                this.idCat = cat.getId();
                this.nome.setText(cat.getNome());
                
                this.btCat.setText("Editar");
            } catch (ClassCastException e) {
                e.printStackTrace();
                Mensagens.msgErro(3, this);
            }
        }

        this.btCat.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                try {
                    CategoriaDao catDao = Factory.createCategoriaDao(CadEdtCategoria.this);

                    Categoria cat = new Categoria();
                    cat.setNome(nome.getText().toString());
                  
                    
                    if (!flagEdt) {
                        //realiza o cadastro
                        if (catDao.cadastrar(cat) != -1) {
                            Mensagens.msgOk(CadEdtCategoria.this);
                        } else {
                            throw new SQLException();
                        }
                    }else{
                       //realiza a alteracao
                        cat.setId(CadEdtCategoria.this.idCat);
                        //rc.getCategoria().setId(CadEdtCategoria.this.idCat);
                        if (catDao.alterar(cat)) {
                            Mensagens.msgOk(CadEdtCategoria.this);
                            //CadastraCategoria.this.finish();
                        } else {
                            throw new SQLException();
                        }
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                    Mensagens.msgErroBD(1,CadEdtCategoria.this);
                }
            }
        });

    }
}
