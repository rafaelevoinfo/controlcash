/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.ui.despesa;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.dreamsoft.R;
import br.com.dreamsoft.dao.CategoriaDao;
import br.com.dreamsoft.dao.Factory;
import br.com.dreamsoft.dao.DespesaDao;
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
    //atributos static usados para saber se esta sendo feita uma edi��o ou cadastro
    public static String EDIT = "true";
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
        setTitle("Despesa");
                
        this.btCad = (Button) findViewById(R.id.cadastrarDesp);
        this.nome = (EditText) findViewById(R.id.nome);
        this.valor = (EditText) findViewById(R.id.valor);
        this.categoria = (Spinner) findViewById(R.id.categoria);
        this.date = (DatePicker) findViewById(R.id.date);
        
        CategoriaDao daoCat = Factory.createCategoriaDao(this);
        this.categorias = daoCat.buscarTodos();


        this.catsAdp = new ArrayAdapter<Categoria>(this,android.R.layout.simple_spinner_item , this.categorias);
        //da uma enfeita na lista
        catsAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.categoria.setAdapter(this.catsAdp);
        
        //verifica se esta editando
        if (getIntent().getBooleanExtra(CadEdtDespesa.EDIT, false)) {
            flagEdt = true;
            Despesa rc = null;
            try {
                rc = (Despesa) getIntent().getExtras().get(OBJ_DESP);
                
                this.idRec = rc.getId();                
                
                this.nome.setText(rc.getNome());
                this.valor.setText(String.valueOf(rc.getValor()));
                //percorre todas as categorias para ver qual deve ser selecionada
                for(int i=0; i <this.categoria.getCount();i++){
                	if(this.categoria.getItemIdAtPosition(i) == rc.getCategoria().getId()){                		   
                		this.categoria.setSelection(i);
                		break;
                	}
                }
                
                //this.idCat = rc.getCategoria().getId();
                                
                Calendar cal = Calendar.getInstance(new Locale("pt","br"));
                cal.setTime(rc.getDate());
                            
                this.date.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

                this.btCad.setText("Editar");
            } catch (ClassCastException e) {
                e.printStackTrace();
                Mensagens.msgErro(1, this);
            }
        }

        this.btCad.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                try {
                    DespesaDao dao = Factory.createDespesaDao(CadEdtDespesa.this);

                    Despesa desp = new Despesa();

                    desp.setNome(nome.getText().toString());
                    desp.setValor(Double.parseDouble(valor.getText().toString()));

                    Categoria cat = new Categoria();
                    cat.setId(Integer.parseInt(String.valueOf(categoria.getSelectedItemId())));                                        
                    desp.setCategoria(cat);
                    
                    Calendar cal = Calendar.getInstance(new Locale("pt","br"));                    
                    cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth());                   
                                                        
                    //seta a data
                    desp.setDate(cal.getTime());
                    
                    if (!flagEdt) {
                        //realiza o cadastro
                        if (dao.cadastrar(desp) != -1) {
                            Mensagens.msgOk(CadEdtDespesa.this);                           
                        } else {
                            throw new Exception();
                        }
                    }else{
                       //realiza a alteracao
                        desp.setId(CadEdtDespesa.this.idRec);                    
                        
                        if (dao.alterar(desp)) {
                            Mensagens.msgOk(CadEdtDespesa.this);
                            //CadastraDespesa.this.finish();
                        } else {
                            throw new Exception();
                        }
                    }


                } catch (Exception e) {
                   Mensagens.msgErroBD(1, CadEdtDespesa.this);
                }
            }
        });

    }
}
