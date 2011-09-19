/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.dreamsoft.ui.categoria.CadEdtCategoria;
import br.com.dreamsoft.ui.categoria.ListaCategorias;
import br.com.dreamsoft.ui.despesa.ListaDespesas;
import br.com.dreamsoft.ui.receita.ListaReceitas;
import br.com.dreamsoft.utils.Mensagens;

/**
 *
 * @author rafael
 */
public class Main extends Activity {

    private Button receitas;
    private Button despesas;
    private Button categoria;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("Control Cash");

        receitas = (Button) findViewById(R.id.btnRec);
        despesas = (Button) findViewById(R.id.btnDesp);
        categoria = (Button) findViewById(R.id.btnCat);

        receitas.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                startActivity(new Intent(Main.this,ListaReceitas.class));
            }
        });
        despesas.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                startActivity(new Intent(Main.this,ListaDespesas.class));
            }
        });
        
        categoria.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                startActivity(new Intent(Main.this,ListaCategorias.class));
            }
        });

       
    }
}
