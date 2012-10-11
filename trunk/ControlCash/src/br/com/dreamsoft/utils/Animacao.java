package br.com.dreamsoft.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class Animacao {

	public static void addAnimacaoLista(ListView lv) {
		AnimationSet set = new AnimationSet(true);
		Animation anin = new AlphaAnimation(0.0f, 1.0f);
		anin.setDuration(100);
		set.addAnimation(anin);

		anin = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

		anin.setDuration(100);
		set.addAnimation(anin);

		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
		lv.setLayoutAnimation(controller);
	}

}
