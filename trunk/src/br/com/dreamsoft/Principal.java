/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.dreamsoft.bd.DespesaDbAdapter;

/**
 *
 * @author rafael
 */
public class Principal extends Activity {

    private DespesaDbAdapter bdDespesa;
    private Button addDespesa;
    private TextView ultDesp;
    private EditText edtDesp;

    public Principal() {
       
    }

    private void inserido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("Inserido").
                setMessage("Inserido com sucesso!").create();
        dialog.show();
        
    }

    private void erro(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("Erro").
                setMessage("Erro ao inserir!").create();
        dialog.show();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

         addDespesa = (Button) findViewById(R.id.addDesp);
        ultDesp = (TextView) findViewById(R.id.ultDesp);
        edtDesp = (EditText) findViewById(R.id.edtDesp);

        bdDespesa = new DespesaDbAdapter(this);
        bdDespesa.open();

        Cursor desps = bdDespesa.fetchAllDespesas();
        //startManagingCursor(desps);
        if (desps.getCount() > 0) {
            desps.moveToLast();
            ultDesp.setText(desps.getString(2));
        }

        addDespesa.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (bdDespesa.createDespesa("Refeicao", edtDesp.getText().toString()) != -1) {
                    inserido();
                }else{
                    erro();
                }
            }
        });
        // ToDo add your GUI initialization code here        
    }
}
