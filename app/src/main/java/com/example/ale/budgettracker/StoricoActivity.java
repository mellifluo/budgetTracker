package com.example.ale.budgettracker;

import android.content.Intent;
import android.database.Cursor;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbh = new DBHelper(this);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv2);
        rv.setHasFixedSize(true);

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
        Cursor cursor = dbh.getOldBudget();
        cursor.moveToFirst();
        listItems.clear();
        while (!cursor.isAfterLast()) {
            boolean haveToContinue = false;
            findViewById(R.id.storico_testo).setVisibility(View.GONE);
            if (!haveToContinue) {
                String nameNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
                if (!nameNewSpesa.equals("Budget mensile")){
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }
}
