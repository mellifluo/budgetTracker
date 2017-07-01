package com.example.ale.budgettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TABLE_BUDGET = "budget";
	public static final String COLUMN_EXPENSE_NAME = "nomeSpesa";
	public static final String COLUMN_AMOUNT = "importoSpesa";

	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 3;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BUDGET + "( "
			+ COLUMN_EXPENSE_NAME	+ " text not null primary key, "
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

	public void removeAll() {
		getWritableDatabase().delete(DBHelper.TABLE_BUDGET, null, null);
	}


	public long insertNewExpense(String nameExpanse, String amountExpanse) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
		cv.put(COLUMN_AMOUNT, amountExpanse);

		long code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
		return code;
	}

	public Cursor getBudget() {
		return getWritableDatabase().query(TABLE_BUDGET, null, null, null, null, null, null);
	}

	public Cursor getExpanse(String exp) {
        return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_EXPENSE_NAME + " = ?", new String[] {exp}, null, null, null);
    }

    public long modifyExpanse(String exp, String am) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXPENSE_NAME, exp);
        cv.put(COLUMN_AMOUNT, am);

        String selection = COLUMN_EXPENSE_NAME + " LIKE ?";
        String[] selectionArgs = { exp };

        return getWritableDatabase().update(TABLE_BUDGET, cv, selection, selectionArgs );
    }

    public long removeExpanse(String exp){
        return getWritableDatabase().delete(TABLE_BUDGET, COLUMN_EXPENSE_NAME + "=?", new String[] {exp});
    }

	/*tengo perché sia mai che mi servano
	public void deleteStudent(int id) {
		getWritableDatabase().delete(TABLE_BUDGET, COLUMN_ID + "=?", new String[] { String.valueOf(id) });
	}

	public Cursor get30() {
		return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_AMOUNT + "=?", new String[] { "30" }, null, null, null);
	}
	*/
}