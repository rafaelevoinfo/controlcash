/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author rafael
 */
public class ControlCashBD {

	private static final String[] TABLE_CREATE;
	private static final String DATABASE_NAME = "ControlCash";
	private static final String[] DATABASE_TABLE = { "receitas", "despesas", "categorias" };
	private static final String[] DATABASE_UPDATE = { "ALTER TABLE categorias ADD id_export integer;" };
	private static final int DATABASE_VERSION = 2;
	private static final String TAG = "ControlCashBD";
	private SQLiteDatabase bd;
	private static ControlCashBD dao = new ControlCashBD();

	static {
		TABLE_CREATE = new String[] {
				"CREATE TABLE receitas (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, categoria INTEGER not null, nome varchar(200) not null, valor DOUBLE not null, date DATE not null);",
				"CREATE TABLE despesas (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, categoria INTEGER not null, nome varchar(200) not null, valor DOUBLE not null, date DATE not null);",
				"CREATE TABLE categorias (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) NOT NULL, id_export INTEGER);",
				"INSERT INTO categorias VALUES (1,'Salario',null);",
				"INSERT INTO categorias VALUES (2,'Lanches',null);" };
	}

	private ControlCashBD() {
	}

	public static SQLiteDatabase getInstance(Context ctx) {
		if (ControlCashBD.dao.bd == null || !ControlCashBD.dao.bd.isOpen()) {
			ControlCashBD.dao.bd = new DatabaseHelper(ctx).getWritableDatabase();
		}
		return ControlCashBD.dao.bd;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// for (String table : DATABASE_TABLE) {
			// db.execSQL("DROP TABLE IF EXISTS " + table);
			// }
			//
			// onCreate(db);

			if ((oldVersion == 1) && (newVersion == 2)) {
				for (String update : DATABASE_UPDATE) {
					db.execSQL(update);
				}
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			for (String table : TABLE_CREATE) {
				db.execSQL(table);
			}
		}
	}
}
