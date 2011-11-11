/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dreamsoft.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author rafael
 */
public class ControlCashBD {

    private static final String[] TABLE_CREATE;
    private static final String DATABASE_NAME = "ControlCash";
    private static final String[] DATABASE_TABLE = {"receitas", "despesas", "categorias", "balanco"};
    private static final int DATABASE_VERSION = 4;
    private static final String TAG = "ControlCashBD";
    private SQLiteDatabase bd;
    private static ControlCashBD dao = new ControlCashBD();

    static {
        TABLE_CREATE = new String[]{"CREATE TABLE receitas (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, categoria INTEGER not null, nome varchar(200) not null, valor DOUBLE not null, date DATE not null);",
                    "CREATE TABLE despesas (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, categoria INTEGER not null, nome varchar(200) not null, valor DOUBLE not null, date DATE not null);",
                    "CREATE TABLE categorias (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, nome VARCHAR(100) NOT NULL);",
                    "CREATE TABLE balanco (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, date DATE NOT NULL, saldo DOUBLE NOT NULL);"};
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
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            for (String table : DATABASE_TABLE) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }

            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String table : TABLE_CREATE) {
                db.execSQL(table);
            }
        }
    }
}
