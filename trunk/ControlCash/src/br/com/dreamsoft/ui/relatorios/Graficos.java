package br.com.dreamsoft.ui.relatorios;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Graficos extends View {

	public Graficos(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public Graficos(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.LTGRAY);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setARGB(255, 0, 0, 255);
		
		canvas.drawRect(10,0,50,50,p);
		
	}
	
	

}
