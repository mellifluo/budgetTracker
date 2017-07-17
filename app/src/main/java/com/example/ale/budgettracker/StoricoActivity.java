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
    private static DBHelper dbh;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storico);

        dbh = new DBHelper(this);

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
        Cursor cursor;
        if (extras.size() == 1){
            String year = extras.getString("year");
            cursor = dbh.getCardsInAYear(year);
        }
        else if (extras.size() == 2) {
            String year = extras.getString("year");
            String month = extras.getString("month");
            cursor = dbh.getCardsInAMonth(year, month);
        }
        else {
            String year = extras.getString("year");
            String month = extras.getString("month");
            String day = extras.getString("day");
            cursor = dbh.getCardsInADay(year, month, day);
        }
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
                    String idNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
                    Spesa newSpesa = new Spesa(nameNewSpesa, amountNewSpesa, yearNewSpesa, monthNewSpesa,
                            dayNewSpesa, categoryNewSpesa, idNewSpesa );
                    listItems.add(newSpesa);
                }
            }
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
        cursor.close();
    }

}
