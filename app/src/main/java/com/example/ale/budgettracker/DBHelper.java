package com.example.ale.budgettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TABLE_BUDGET = "budget";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_EXPENSE_NAME = "nomeSpesa";
	public static final String COLUMN_AMOUNT = "importoSpesa";
	
	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BUDGET + "( " + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_EXPENSE_NAME	+ " text not null, "
			+ COLUMN_AMOUNT + " integer not null);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
		onCreate(db);
	}

	
	public long insertNewExpense(String nameExpanse, String amountExpanse) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
		cv.put(COLUMN_AMOUNT, amountExpanse);
		
		long code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
		return code;
	}
	
	public Cursor getGrades() {
		return getWritableDatabase().query(TABLE_BUDGET, null, null, null, null, null, null);
	}
	
	public void deleteStudent(int id) {
		getWritableDatabase().delete(TABLE_BUDGET, COLUMN_ID + "=?", new String[] { String.valueOf(id) });
	}
	
	public Cursor get30() {
		return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_AMOUNT + "=?", new String[] { "30" }, null, null, null);
	}
}