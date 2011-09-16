package br.com.dreamsoft.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.dreamsoft.dao.interfaces.BancoDados;
import br.com.dreamsoft.model.Categoria;

public class CategoriaDao implements BancoDados<Categoria> {

	private final String KEY_ID = "id";
	private final String KEY_NOME = "nome";

	private final String[] COLUNS = { KEY_ID, KEY_NOME };
	private final String DATABASE_TABLE = "categorias";
	private SQLiteDatabase db;

	public CategoriaDao(Context ctx) {
		super();
		this.db = ControlCashBD.getInstance(ctx);
	}

	public long cadastrar(Categoria rc) {
		this.db.beginTransaction();
		long id = -1;
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(KEY_NOME, rc.getNome());
			// grava no banco
			id = db.insert(DATABASE_TABLE, null, initialValues);
			if (id != -1) {
				this.db.setTransactionSuccessful();
			}
		} finally {
			this.db.endTransaction();
		}
		return id;
	}

	public boolean alterar(Categoria cat) {
		boolean result = false;
		this.db.beginTransaction();
		long id = 0;
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(KEY_NOME, cat.getNome());

			// atualiza no banco
			id = db.update(DATABASE_TABLE, initialValues, KEY_ID + "= ?",
					new String[] { String.valueOf(cat.getId()) });
			if (id != 0) {
				this.db.setTransactionSuccessful();
				result = true;
			}
		} finally {
			this.db.endTransaction();
		}
		return result;
	}

	public boolean deletar(Integer id) {
		boolean result = false;
		this.db.beginTransaction();

		try {
			// deleta no banco
			id = db.delete(DATABASE_TABLE, KEY_ID + "= ?",
					new String[] { String.valueOf(id) });
			if (id != 0) {
				this.db.setTransactionSuccessful();
				result = true;
			}
		} finally {
			this.db.endTransaction();
		}
		return result;
	}

	public List<Categoria> buscarTodos() {
		Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, null, null,
				null, null, null, null);
		List<Categoria> categorias = new ArrayList<Categoria>();
		// pega os index pelos nomes
		int indexId = cursor.getColumnIndex(KEY_ID);
		int indexNom = cursor.getColumnIndex(KEY_NOME);
		// pega os dados
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Categoria categoria = new Categoria();

			categoria.setId(cursor.getInt(indexId));
			categoria.setNome(cursor.getString(indexNom));

			categorias.add(categoria);
			cursor.moveToNext();

		}
		cursor.close();
		return categorias;
	}

	/**
	 * Busca a categoria pelo seu id
	 */
	public Categoria buscar(Integer id) {
		Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, KEY_ID + "= ?",
						new String[] { id.toString() }, null, null, null, null);
		// pega os index pelos nomes
		int indexId = cursor.getColumnIndex(KEY_ID);
		int indexNom = cursor.getColumnIndex(KEY_NOME);
		// pega os dados
		cursor.moveToFirst();
		Categoria categoria = new Categoria();

		categoria.setId(cursor.getInt(indexId));
		categoria.setNome(cursor.getString(indexNom));

		cursor.close();
		return categoria;

	}

	@Override
	public Categoria buscar(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

}
