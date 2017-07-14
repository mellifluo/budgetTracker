package com.example.ale.budgettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_BUDGET = "budget";
    public static final String TABLE_PERSON = "person";
    public static final String NAME_PERSON = "nomePersona";
    public static final String INITIAL_BUDGET = "amountPersona";
    public static final String ALERT = "alert";
    public static final String COLUMN_EXPENSE_NAME = "nomeSpesa";
    public static final String COLUMN_AMOUNT = "importoSpesa";
    private static int id = 0;
    public static final String ID = "ID";
    public static final String POSITION = "position";
	public static final String DATE_EXPANSE = "dateExpanse";
	public static final String YEAR_EXPANSE = "yearExpanse";
    public static final String MONTH_EXPANSE = "monthExpanse";
    public static final String DAY_EXPANSE = "dayExpanse";
    public static final String CATEGORY = "category";

    private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 5;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_BUDGET + "( "
            + ID + " int, "
            + DATE_EXPANSE + " text, "
            + CATEGORY + " text, "
            + POSITION + " text, "
			+ YEAR_EXPANSE + " text not null, "
            + MONTH_EXPANSE + " text not null, "
            + DAY_EXPANSE + " text not null, "
			+ COLUMN_EXPENSE_NAME	+ " text not null, "
			+ COLUMN_AMOUNT + " text not null," +
            "primary key (" + COLUMN_EXPENSE_NAME + ", " +
            YEAR_EXPANSE + ", " + MONTH_EXPANSE + ", " +
            DAY_EXPANSE + ") );";

    // Database creation sql statement
    private static final String PERSON_CREATE = "create table "
            + TABLE_PERSON + "( "
            + NAME_PERSON + " text primary key, "
            + INITIAL_BUDGET + " text , "
            + ALERT + " int " +
            ");";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(PERSON_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
	}

	public void removeAll() {
        getWritableDatabase().delete(DBHelper.TABLE_BUDGET, null, null);
        getWritableDatabase().delete(DBHelper.TABLE_PERSON, null, null);
	}

	public float insertNewPersona (String namePersona, String amountPersona) {
        ContentValues cv = new ContentValues();
        cv.put(NAME_PERSON, namePersona);
        cv.put(INITIAL_BUDGET, amountPersona);
        cv.put(ALERT, 1);
        float code = getWritableDatabase().insert(TABLE_PERSON, null, cv);
        return code;
    }


	public float insertNewExpense(String nameExpanse, String amountExpanse, String yearExpanse,
								 String monthExpanse, String dayExpanse, String category, String position) {
		ContentValues cv = new ContentValues();
        cv.put(ID, id);
        id++;
        if (monthExpanse.length() < 2) monthExpanse = "0" + monthExpanse;
        if (dayExpanse.length() < 2) dayExpanse = "0" + dayExpanse;
        String date = yearExpanse + "-" + monthExpanse + "-" + dayExpanse ;
		cv.put(DATE_EXPANSE, date);
        cv.put(CATEGORY, category);
        cv.put(POSITION, position);
        cv.put(YEAR_EXPANSE, yearExpanse);
        cv.put(MONTH_EXPANSE, monthExpanse);
        cv.put(DAY_EXPANSE, dayExpanse);
        cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
        cv.put(COLUMN_AMOUNT, amountExpanse);

		float code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
		return code;
	}

	public float insertPlannedExpense(String nameExpanse, String amountExpanse, String yearExpanse,
								  String monthExpanse, String dayExpanse, boolean monthly, int count, String category, String position) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(yearExpanse), Integer.valueOf(monthExpanse), Integer.valueOf(dayExpanse));
		ContentValues cv = new ContentValues();
		float code = -1;
		if (monthly){
			for (int i = 0; i<count; i++) {
                cv.put(ID, id);
                cv.put(CATEGORY, category);
                cv.put(POSITION, position);
                yearExpanse = String.valueOf(calendar.get(Calendar.YEAR));
                monthExpanse = String.valueOf(calendar.get(Calendar.MONTH)+1);
                dayExpanse = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                if (monthExpanse.length() < 2) monthExpanse = "0" + monthExpanse;
                if (dayExpanse.length() < 2) dayExpanse = "0" + dayExpanse;
                String date = yearExpanse + "-" + monthExpanse + "-" + dayExpanse  ;
                cv.put(DATE_EXPANSE, date);
                cv.put(YEAR_EXPANSE, yearExpanse);
                cv.put(MONTH_EXPANSE, monthExpanse);
                cv.put(DAY_EXPANSE, dayExpanse);
                cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
                cv.put(COLUMN_AMOUNT, amountExpanse);
                code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
                calendar.add(Calendar.MONTH,1);
			}
		}
		else {
			for (int i = 0; i<count; i++) {
                cv.put(ID, id);
                cv.put(CATEGORY, category);
                cv.put(POSITION, position);
                yearExpanse = String.valueOf(calendar.get(Calendar.YEAR));
                monthExpanse = String.valueOf(calendar.get(Calendar.MONTH)+1);
                dayExpanse = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                if (monthExpanse.length() < 2) monthExpanse = "0" + monthExpanse;
                if (dayExpanse.length() < 2) dayExpanse = "0" + dayExpanse;
                String date = yearExpanse + "-" + monthExpanse + "-" + dayExpanse ;
                cv.put(DATE_EXPANSE, date);
                cv.put(YEAR_EXPANSE, yearExpanse);
                cv.put(MONTH_EXPANSE, monthExpanse);
                cv.put(DAY_EXPANSE, dayExpanse);
                cv.put(COLUMN_EXPENSE_NAME, nameExpanse);
                cv.put(COLUMN_AMOUNT, amountExpanse);
                code = getWritableDatabase().insert(TABLE_BUDGET, null, cv);
                calendar.add(Calendar.WEEK_OF_MONTH,1);
            }
        }
        id++;
        return code;
	}

	public Cursor getBudget() {
		return getWritableDatabase().rawQuery("select * from " + TABLE_BUDGET + " order by " +
                DATE_EXPANSE + " asc limit 10 ", null);
	}

	public Cursor getOldBudget() {
        String selection = DATE_EXPANSE +" < date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select * from " + TABLE_BUDGET
                + " where " + selection + " order by " + DATE_EXPANSE + " desc ", null);
        return amountColumn;
    }

	public Cursor getExpanse(String exp, String year, String month, String day) {
        if (month.length() < 2) month = "0" + month;
        if (day.length() < 2) day = "0" + day;
        return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_EXPENSE_NAME + " =? and " +
                        YEAR_EXPANSE + " =? and " + MONTH_EXPANSE + " =? and " + DAY_EXPANSE + " =? "
                , new String[] {exp, year, month, day}, null, null, null);
    }

    public Cursor getExpanseByName(String exp) {
        return getWritableDatabase().query(TABLE_BUDGET, null, COLUMN_EXPENSE_NAME + " =?"
                , new String[] {exp}, null, null, null);
    }

    public Cursor getAllAddress() {
        return getWritableDatabase().rawQuery("select distinct " + COLUMN_EXPENSE_NAME + ", " + POSITION +
                " from " + TABLE_BUDGET + " where not position = '' ", null);
    }

	public void maxID() {
        Cursor cursor = getWritableDatabase().rawQuery("select max(ID) from budget",null);
        if (cursor.moveToFirst()) id = cursor.getInt(0)+1;
    }

    public float modifyExpanse(String exp, String am, String yearExpanse,
                              String monthExpanse, String dayExpanse, String category, String position) {
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        id++;
        cv.put(CATEGORY, category);
        if (monthExpanse.length() < 2) monthExpanse = "0" + monthExpanse;
        if (dayExpanse.length() < 2) dayExpanse = "0" + dayExpanse;
		String date = yearExpanse + "-" + monthExpanse + "-" + dayExpanse ;
		cv.put(DATE_EXPANSE, date);
        cv.put(POSITION, position);
        cv.put(YEAR_EXPANSE, yearExpanse);
        cv.put(MONTH_EXPANSE, monthExpanse);
        cv.put(DAY_EXPANSE, dayExpanse);
        cv.put(COLUMN_EXPENSE_NAME, exp);
        cv.put(COLUMN_AMOUNT, am);

        String selection = COLUMN_EXPENSE_NAME + " =? and " + YEAR_EXPANSE + " =? and " + MONTH_EXPANSE +
                " =? and " + DAY_EXPANSE + " =? ";
        String[] selectionArgs = { exp, yearExpanse, monthExpanse, dayExpanse };

        return getWritableDatabase().update(TABLE_BUDGET, cv, selection, selectionArgs );
    }


    public float modifyExpanseAll(String exp, String am, String idM) {
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        id++;
        cv.put(COLUMN_EXPENSE_NAME, exp);
        cv.put(COLUMN_AMOUNT, am);

        String selection = COLUMN_EXPENSE_NAME + " =? and " + ID + " =? ";
        String[] selectionArgs = { exp, idM };

        return getWritableDatabase().update(TABLE_BUDGET, cv, selection, selectionArgs );
    }

    public float removeExpanse(String exp, String id) {
        return getWritableDatabase().delete(TABLE_BUDGET, COLUMN_EXPENSE_NAME + "=? and " + ID + " =? "
                , new String[] {exp, id});
    }

    public float removeOneExpanse(String exp, String id, String year, String month, String day) {
        return getWritableDatabase().delete(TABLE_BUDGET, COLUMN_EXPENSE_NAME + "=? and " + ID + " =? and " +
                YEAR_EXPANSE + " =? and " + MONTH_EXPANSE + " =?  and " + DAY_EXPANSE + " =? "
                , new String[] {exp, id, year, month, day});
    }

    public float getTotal() {
        maxID();
        String selection = DATE_EXPANSE +" <= date('now')";
		Cursor cursor = getWritableDatabase().rawQuery("select sum( "+ COLUMN_AMOUNT +" ) from " + TABLE_BUDGET
                + " where " + selection, null);
        if (cursor.moveToFirst()){
            return cursor.getFloat(0);
        }
        else return 0;
	}

	public void checkMensile() {
        Cursor cursor = getWritableDatabase().rawQuery("select * from " + TABLE_BUDGET
                + " where nomeSpesa = 'Budget mensile' and dateExpanse = date('now', '-1 month') ", null);
        if (cursor.moveToFirst()) {
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            if (month.length() < 2) month = "0" + month;
            if (day.length() < 2) day = "0" + day;
            float x = modifyExpanse("Budget mensile", String.valueOf(getTotal()), year, month, day, "o", "");
            assert (x != -1);
        }
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
        if (month.length() < 2) month = "0" + month;
        year = "'" + year + "'";
        month = "'" + month + "'";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
				+ TABLE_BUDGET + " where " + YEAR_EXPANSE + " like " + year + " and " + MONTH_EXPANSE +
				" like " + month, null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

    public float getLossInAMonth(String year, String month) {
        if (month.length() < 2) month = "0" + month;
        year = "'" + year + "'";
        month = "'" + month + "'";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + YEAR_EXPANSE + " like " + year + " and " + MONTH_EXPANSE +
                " like " + month + " and " + COLUMN_AMOUNT + " like " + " '-%'", null);
        if (amountColumn.moveToFirst()){
            return amountColumn.getFloat(0);
        }
        else return 0;
    }

	public float getPosInAMonth(String year, String month) {
        if (month.length() < 2) month = "0" + month;
        year = "'" + year + "'";
        month = "'" + month + "'";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
				+ TABLE_BUDGET + " where " + YEAR_EXPANSE + " like " + year + " and " + MONTH_EXPANSE +
				" like " + month + " and " + COLUMN_AMOUNT + " not like " + " '-%'", null);
		if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

	public float getAmInADay(String year, String month, String day) {
        if (month.length() < 2) month = "0" + month;
        if (day.length() < 2) day = "0" + day;
        String date = "'" + year + "-" + month + "-" + day + "'";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + DATE_EXPANSE + " = " + date, null);
        if (amountColumn.moveToFirst()){
			return amountColumn.getFloat(0);
		}
		else return 0;
	}

    public float getTotalSummary() {
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float getTotalLoss() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " < 0 and " + selection , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float getTotalEarn() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select sum(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " > 0 and " + selection , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float maxLoss() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select min(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " < 0 and " + selection , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float maxEarn() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select max(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " > 0 and " + selection, null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float avgLoss() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select avg(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " < 0 and " + selection , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

    public float avgEarn() {
        String selection = DATE_EXPANSE +" <= date('now')";
        Cursor amountColumn = getWritableDatabase().rawQuery("select avg(" + COLUMN_AMOUNT + ") from "
                + TABLE_BUDGET + " where " + COLUMN_AMOUNT + " > 0 and " + selection , null);
        if (amountColumn.moveToFirst()) return amountColumn.getFloat(0);
        else return 0;
    }

	public Cursor getPerson() {
        return getWritableDatabase().rawQuery("select * from " + TABLE_PERSON, null);
    }

	public ArrayList<Cursor> getData(String Query){
		//get writable database
		SQLiteDatabase sqlDB = this.getWritableDatabase();
		String[] columns = new String[] { "message" };
		//an array list of cursor to save two cursors one has results from the query
		//other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2= new MatrixCursor(columns);
		alc.add(null);
		alc.add(null);

		try{
			String maxQuery = Query ;
			//execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);

			//add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });

			alc.set(1,Cursor2);
			if (null != c && c.getCount() > 0) {

				alc.set(0,c);
				c.moveToFirst();

				return alc ;
			}
			return alc;
		} catch(SQLException sqlEx){
			Log.d("printing exception", sqlEx.getMessage());
			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		} catch(Exception ex){
			Log.d("printing exception", ex.getMessage());

			//if any exceptions are triggered save the error message to cursor an return the arraylist
			Cursor2.addRow(new Object[] { ""+ex.getMessage() });
			alc.set(1,Cursor2);
			return alc;
		}
	}

}