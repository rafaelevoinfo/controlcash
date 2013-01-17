package br.com.dreamsoft.ui.movimentacao;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;
import br.com.dreamsoft.R;
import br.com.dreamsoft.model.Movimentacao;
import br.com.dreamsoft.utils.Animacao;
import br.com.dreamsoft.utils.Mensagens;

public abstract class ListaMovimentacao<T extends Movimentacao> extends Activity {
	protected enum Ordem {
		DATA, NOME, VALOR;
	}

	protected ListView lv;
	protected TextView tv;
	protected LayoutInflater inflater;
	protected List<T> listaMov;
	protected Ordem ordem;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.lista);

		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		lv = (ListView) findViewById(R.id.list);
		tv = (TextView) findViewById(R.id.saldo);
		tv.setTextColor(getResources().getColor(R.color.vermelho_escarlata));

		// getListView().setBackgroundResource(R.drawable.background);
		lv.setCacheColorHint(getResources().getColor(android.R.color.transparent));
		registerForContextMenu(lv);
		// faz a animacao da lista quando ela aparece
		Animacao.addAnimacaoLista(lv);
		// faz a animação na activity
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

		ordem = Ordem.DATA;
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLista();
	}

	protected void refreshLista() {
		listaMov = getMovimentacoes();

		double total = 0;
		for (T obj : listaMov) {
			total += obj.getValor();
		}

		// formata o valor
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		try {
			tv.setText(nf.format(total));
			// tv.refreshDrawableState();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagens.msgErro(2, this);
		}

		ordenarLista();

	}

	protected void ordenarLista() {
		switch (ordem) {
			case NOME:
				ordenarPorNome();
				break;
			case VALOR:
				ordenarPorValor();
				break;
			default:
				ordenarPorData();
		}
		// faz a animacao da lista quando ela aparece
		Animacao.addAnimacaoLista(lv);

	}

	protected void ordenarPorData() {
		Collections.sort(listaMov, new Comparator<T>() {
			public int compare(T mov, T mov2) {
				return mov.getDate().compareTo(mov2.getDate());
			}

		});
	}

	protected void ordenarPorValor() {
		Collections.sort(listaMov, new Comparator<T>() {
			public int compare(T mov, T mov2) {
				Double v1 = new Double(mov.getValor());
				Double v2 = new Double(mov2.getValor());
				return v1.compareTo(v2);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			return exibirOpcoesOrdernacao();
		} else {
			return null;
		}

	}

	protected Dialog exibirOpcoesOrdernacao() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.ordernar).setSingleChoiceItems(R.array.ordens, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case 1:// Nome
								ordem = Ordem.NOME;
								break;
							case 2:// Valor
								ordem = Ordem.VALOR;
								break;

							default:
								ordem = Ordem.DATA;
								break;
						}

						dialog.dismiss();
						refreshLista();
					}
				});

		return builder.create();
	}

	protected void ordenarPorNome() {
		Collections.sort(listaMov, new Comparator<T>() {
			public int compare(T mov, T mov2) {
				return mov.getNome().compareTo(mov2.getNome());
			}
		});
	}

	protected abstract List<T> getMovimentacoes();

}
