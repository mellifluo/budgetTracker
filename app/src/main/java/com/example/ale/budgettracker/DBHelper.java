package com.example.ale.budgettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;


public class DBHelper extends SQLiteOpenHelper {

	public static final String TABLE_BUDGET = "budget";
	public static final String COLUMN_EXPENSE_NAME = "nomeSpesa";
    public static final String COLUMN_AMOUNT = "importoSpesa";
    public static final String YEAR_EXPANSE = "yearExpanse";
    public static final String MONTH_EXPANSE = "monthExpanse";
    public static final String DAY_EXPANSE = "dayExpanse";
    public static final String CATEGORY = "category";

	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BUDGET + "( "
            + CATEGORY + " text not null, "
            + YEAR_EXPANSE + " text not null, "
            + MONTH_EXPANSE + " text not null, "
            + DAY_EXPANSE + " text not null, "
			+ COLUMN_EXPENSE_NAME	+ " text not null primary key, "
			+ COLUMN_AMOUNT + " text not null);";

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


	public float insertNewExpense(String nameExpanse, String amountExpanse, String yearExpanse,
								 String monthExpanse, String dayExpanse, String category) {
		ContentValues cv = new ContentValues();
        cv.put(CATEGORY, category);
        cv.put(YEAR_EXPANSE, yearExpanse);
        cv.put(MONTH_EXPANSE, monthExpanse);
        cv.put(DAY_EXPANSE, dayExpanse);
        cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
        cv.put(COLUMN_AMOUNT, amountExpanse);

		float code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
		return code;
	}

	public Cursor getBudget() {
		return getWritableDatabase().query(TABLE_BUDGET, null, null, null, null, null, null);
	}

	public Cursor getOldBudget() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String selection = "( " + DAY_EXPANSE + " <= " + day + " and " + MONTH_EXPANSE + " <= " + month +
                " and " + YEAR_EXPANSE + " <= " + year + " ) or ( " + YEAR_EXPANSE + " <= " + year + " and "
                + MONTH_EXPANSE + " < " + month + " ) or (" + YEAR_EXPANSE + " < " + year + " )";
        Cursor amountColumn = getWritableDatabase().rawQuery("select * from " + TABLE_BUDGET
                + " where " + selection, null);
        return amountColumn;
    }

	public Cursor getExpanse(String exp) {
        return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_EXPENSE_NAME + " = ?", new String[] {exp}, null, null, null);
    }

	public Cursor getYear(String year) {
		return getWritableDatabase().query(TABLE_BUDGET, null, YEAR_EXPANSE + " = ?", new String[] {year}, null, null, null);
	}

    public float modifyExpanse(String exp, String am, String yearExpanse,
                              String monthExpanse, String dayExpanse, String category) {
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY, category);
        cv.put(YEAR_EXPANSE, yearExpanse);
        cv.put(MONTH_EXPANSE, monthExpanse);
        cv.put(DAY_EXPANSE, dayExpanse);
        cv.put(COLUMN_EXPENSE_NAME, exp);
        cv.put(COLUMN_AMOUNT, am);

        String selection = COLUMN_EXPENSE_NAME + " LIKE ?";
        String[] selectionArgs = { exp };

        return getWritableDatabase().update(TABLE_BUDGET, cv, selection, selectionArgs );
    }

    public float removeExpanse(String exp) {
        return getWritableDatabase().delete(TABLE_BUDGET, COLUMN_EXPENSE_NAME + "=?", new String[] {exp});
    }

    public float getTotal() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String selection = "( " + DAY_EXPANSE + " <= " + day + " and " + MONTH_EXPANSE + " <= " + month +
                " and " + YEAR_EXPANSE + " <= " + year + " ) or ( " + YEAR_EXPANSE + " <= " + year + " and "
                + MONTH_EXPANSE + " < " + month + " ) or (" + YEAR_EXPANSE + " < " + year + " )";
		Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from " + TABLE_BUDGET
                + " where " + selection, null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

	public float getTotalInAYear(String year) {
		Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from" +
				" " + TABLE_BUDGET + "where " + YEAR_EXPANSE + " = " + year , null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

	public float getTotalInAMonth(String year, String month) {
		Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
				+ TABLE_BUDGET + " where " + YEAR_EXPANSE + " = " + year + " and " + MONTH_EXPANSE +
				" = " + month, null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

    public float getLossInAMonth(String year, String month) {
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + YEAR_EXPANSE + " = " + year + " and " + MONTH_EXPANSE +
                " = " + month + " and " + COLUMN_AMOUNT + " like " + " '-%'", null);
        if (amountColumn.moveToFirst()){
            return amountColumn.getFloat(0);
        }
        else return 0;
    }

	public float getPosInAMonth(String year, String month) {
		Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
				+ TABLE_BUDGET + " where " + YEAR_EXPANSE + " = " + year + " and " + MONTH_EXPANSE +
				" = " + month + " and " + COLUMN_AMOUNT + " not like " + " '-%'", null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

	public float getAmInADay(String year, String month, String day) {
		Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
				+ TABLE_BUDGET + " where " + YEAR_EXPANSE + " = " + year + " and " + MONTH_EXPANSE +
				" = " + month + " and " + DAY_EXPANSE + " = " + day, null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

}