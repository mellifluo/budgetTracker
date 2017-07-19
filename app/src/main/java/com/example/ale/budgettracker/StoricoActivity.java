package com.example.ale.budgettracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StoricoActivity extends AppCompatActivity {


    private List<Spesa> listItems;
    private rvAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DBHelper dbh;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DBHelper(this);
        if (dbh.getTheme()==0) setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_storico);

        rv = (RecyclerView)findViewById(R.id.rv2);
        rv.setHasFixedSize(true);
        rv.setVisibility(View.INVISIBLE);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        listItems = new ArrayList<>();

        adapter = new rvAdapter(listItems);
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        Cursor cursor = null;
        boolean flag = true;
        if (extras.size() == 2){
            setTitle("Archivio annuale");
            String year = extras.getString("year");
            if (extras.getCharSequenceArrayList("cat").size() != 0) {
                flag = false;
                listCards(year, "", "");
            }
            else cursor = dbh.getCardsInAYear(year);
        }
        else if (extras.size() == 3) {
            setTitle("Archivio mensile");
            String year = extras.getString("year");
            String month = extras.getString("month");
            if (extras.getCharSequenceArrayList("cat").size() != 0) {
                flag = false;
                listCards(year, month, "");
            }
            else cursor = dbh.getCardsInAMonth(year, month);
        }
        else {
            setTitle("Archivio settimanale");
            String year = extras.getString("year");
            String month = extras.getString("month");
            String day = extras.getString("day");
            if (extras.getCharSequenceArrayList("cat").size() != 0) {
                flag = false;
                listCards(year, month, day);
            }
            else cursor = dbh.getCardsInADay(year, month,day);
        }
        if (flag) {
            cursor.moveToFirst();
            listItems.clear();
            while (!cursor.isAfterLast()) {
                boolean haveToContinue = false;
                if (!haveToContinue) {
                    String nameNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
                    if (!nameNewSpesa.equals("Budget mensile")){
                        rv.setVisibility(View.VISIBLE);
                        rv.setBackgroundColor(Color.parseColor("#62727b"));
                        findViewById(R.id.storico_testo).setVisibility(View.GONE);
                        String dayNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.DAY_EXPANSE));
                        String yearNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.YEAR_EXPANSE));
                        String monthNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.MONTH_EXPANSE));
                        String amountNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
                        String categoryNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY));
                        String plNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.PLANNED));
                        String idNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
                        String posNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION));
                        Spesa newSpesa = new Spesa(nameNewSpesa, amountNewSpesa, yearNewSpesa, monthNewSpesa,
                                dayNewSpesa, plNewSpesa, categoryNewSpesa, idNewSpesa, posNewSpesa );
                        listItems.add(newSpesa);
                    }
                }
                cursor.moveToNext();
            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    private void listCards(String year, String month, String day) {
        if (month.equals("")) {
            Bundle extras = getIntent().getExtras();
            ArrayList sc = extras.getCharSequenceArrayList("cat");
            for (int i = 0; i<sc.size(); i++) {
                Cursor cursor = dbh.getCardsInAYearC(year,String.valueOf(sc.get(i)));
                visCards(cursor);
            }
        }
        else if (day.equals("")) {
            Bundle extras = getIntent().getExtras();
            ArrayList sc = extras.getCharSequenceArrayList("cat");
            for (int i = 0; i<sc.size(); i++) {
                Cursor cursor = dbh.getCardsInAMonthC(year,month,String.valueOf(sc.get(i)));
                visCards(cursor);
            }
        }
        else {
            Bundle extras = getIntent().getExtras();
            ArrayList sc = extras.getCharSequenceArrayList("cat");
            for (int i = 0; i<sc.size(); i++) {
                Cursor cursor = dbh.getCardsInADayC(year,month,day,String.valueOf(sc.get(i)));
                visCards(cursor);
            }
        }
    }

    private void visCards(Cursor cursor) {
        cursor.moveToFirst();
        listItems.clear();
        while (!cursor.isAfterLast()) {
            String nameNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
            rv.setVisibility(View.VISIBLE);
            rv.setBackgroundColor(Color.parseColor("#62727b"));
            findViewById(R.id.storico_testo).setVisibility(View.GONE);
            String dayNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.DAY_EXPANSE));
            String yearNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.YEAR_EXPANSE));
            String monthNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.MONTH_EXPANSE));
            String amountNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
            String categoryNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY));
            String plNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.PLANNED));
            String idNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
            String posNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.POSITION));
            Spesa newSpesa = new Spesa(nameNewSpesa, amountNewSpesa, yearNewSpesa, monthNewSpesa,
                    dayNewSpesa, plNewSpesa, categoryNewSpesa, idNewSpesa, posNewSpesa );
            listItems.add(newSpesa);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
        cursor.close();
    }

}
