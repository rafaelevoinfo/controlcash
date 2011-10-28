package br.com.dreamsoft;

import java.util.Calendar;
import java.util.Locale;

import android.app.Application;
/**
 * Classe utilizada para guardar o valor da data.
 * @author rafael
 *
 */
public class ApplicationControlCash extends Application{
	private Calendar data;
	
	

	public ApplicationControlCash() {
		super();
		data = Calendar.getInstance(new Locale("pt","br"));
	}



	public Calendar getData() {
		return data;
	}



	public void setData(Calendar data) {
		this.data = data;
	}	
	
}
