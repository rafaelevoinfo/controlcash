/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

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
	private static final String DIR_BACKUP = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
			+ "/";
	private static final String BACKUP_FILE_NAME = "ControlCashBkp.bd";

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

	public static String realizarBackup(Context ctx) throws FileNotFoundException, IOException {
		if (ControlCashBD.dao.bd.isOpen()) {
			close();
		}

		File fileBd = new File(Environment.getDataDirectory() + "/data/" + ctx.getPackageName() + "/databases/"
				+ DATABASE_NAME);
		File dirBackup = new File(DIR_BACKUP);
		File fileBackup = new File(DIR_BACKUP + BACKUP_FILE_NAME);

		if (!dirBackup.exists()) {
			dirBackup.mkdirs();
		} else {
			dirBackup.delete();
			dirBackup.mkdirs();
		}

		if (!fileBackup.exists()) {
			fileBackup.createNewFile();
		} else {
			fileBackup.delete();
			fileBackup.createNewFile();
		}

		FileChannel inBd = new FileInputStream(fileBd).getChannel();
		FileChannel outBd = new FileOutputStream(fileBackup).getChannel();

		try {
			inBd.transferTo(0, inBd.size(), outBd);
		} finally {
			if (inBd != null)
				inBd.close();
			if (outBd != null)
				outBd.close();
		}

		ControlCashBD.dao.bd = getInstance(ctx);

		return fileBackup.getAbsolutePath();
	}

	public static void realizarRestore(Context ctx) throws FileNotFoundException, IOException {
		if (ControlCashBD.dao.bd.isOpen()) {
			close();
		}

		File fileBd = new File(Environment.getDataDirectory() + "/data/" + ctx.getPackageName() + "/databases/"
				+ DATABASE_NAME);
		File fileBackup = new File(DIR_BACKUP + BACKUP_FILE_NAME);

		FileChannel outBd = new FileOutputStream(fileBd).getChannel();
		FileChannel inBd = new FileInputStream(fileBackup).getChannel();

		try {
			inBd.transferTo(0, inBd.size(), outBd);
		} finally {
			if (inBd != null)
				inBd.close();
			if (outBd != null)
				outBd.close();
		}

		ControlCashBD.dao.bd = getInstance(ctx);

	}

	public static SQLiteDatabase getInstance(Context ctx) {
		if (ControlCashBD.dao.bd == null || !ControlCashBD.dao.bd.isOpen()) {
			ControlCashBD.dao.bd = new DatabaseHelper(ctx).getWritableDatabase();
		}
		return ControlCashBD.dao.bd;
	}

	public static void close() {
		ControlCashBD.dao.bd.close();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			if ((oldVersion == 1) && (newVersion == 2)) {
				for (String update : DATABASE_UPDATE) {
					db.execSQL(update);
				}
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// File fileBd = new File(Environment.getDataDirectory() + "/data/br.com.dreamsoft/databases/" +
			// DATABASE_NAME);
			for (String table : TABLE_CREATE) {
				db.execSQL(table);
			}

		}
	}
}
