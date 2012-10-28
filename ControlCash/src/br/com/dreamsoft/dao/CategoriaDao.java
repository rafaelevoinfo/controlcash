package br.com.dreamsoft.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.dreamsoft.model.Categoria;
import br.com.dreamsoft.model.Despesa;
import br.com.dreamsoft.model.Receita;
import br.com.dreamsoft.utils.Mensagens;

public class CategoriaDao {

	private final String KEY_ID = "id";
	private final String KEY_NOME = "nome";
	private final String KEY_ID_EXPORT = "id_export";

	private final String[] COLUNS = { KEY_ID, KEY_NOME, KEY_ID_EXPORT };
	private final String DATABASE_TABLE = "categorias";
	private SQLiteDatabase db;
	private DespesaDao daoDesp;
	private ReceitaDao daoRec;
	private Context ctx;

	public CategoriaDao(Context ctx) {
		super();
		this.db = ControlCashBD.getInstance(ctx);
		this.daoDesp = Factory.createDespesaDao(ctx);
		this.daoRec = Factory.createReceitaDao(ctx);
		this.ctx = ctx;
	}

	public long cadastrar(Categoria cat) {
		this.db.beginTransaction();
		long id = -1;
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put(KEY_NOME, cat.getNome());
			if (cat.getIdExport() == 0) {
				initialValues.putNull(KEY_ID_EXPORT);
			} else {
				initialValues.put(KEY_ID_EXPORT, cat.getIdExport());
			}
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
			initialValues.put(KEY_ID_EXPORT, cat.getIdExport());

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
			// verifica se n�o existem receitas ou despesas cadastradas para
			// esta categoria, caso exista n�o permite a exclus�o
			List<Despesa> despesas = this.daoDesp.buscarPorCategoria(id);
			List<Receita> receitas = this.daoRec.buscarPorCategoria(id);
			if (despesas.size() < 1 && receitas.size() < 1) {// nao pode deletar
				// deleta no banco
				id = db.delete(DATABASE_TABLE, KEY_ID + "= ?", new String[] { String.valueOf(id) });
				if (id != 0) {
					this.db.setTransactionSuccessful();
					result = true;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Mensagens.msgErro(1, ctx);
		} finally {
			this.db.endTransaction();
		}
		return result;
	}

	public List<Categoria> buscarTodos() {
		Cursor cursor = this.db.query(true, DATABASE_TABLE, COLUNS, null, null, null, null, null, null);
		List<Categoria> categorias = new ArrayList<Categoria>();
		// pega os index pelos nomes
		int indexId = cursor.getColumnIndex(KEY_ID);
		int indexNom = cursor.getColumnIndex(KEY_NOME);
		int indexIdExport = cursor.getColumnIndex(KEY_ID_EXPORT);
		// pega os dados
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Categoria categoria = new Categoria();

			categoria.setId(cursor.getInt(indexId));
			categoria.setNome(cursor.getString(indexNom));
			categoria.setIdExport(cursor.getInt(indexIdExport));

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
		int indexIdExport = cursor.getColumnIndex(KEY_ID_EXPORT);

		// pega os dados
		cursor.moveToFirst();
		Categoria categoria = new Categoria();

		categoria.setId(cursor.getInt(indexId));
		categoria.setNome(cursor.getString(indexNom));
		categoria.setIdExport(cursor.getInt(indexIdExport));

		cursor.close();
		return categoria;

	}

}
