package com.example.ale.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    String id;
    String year;
    String month;
    String day;
    String catExtra;
    String cat;
    String pos;
    boolean sign;
    String modifiedAmount;
    String modifiedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper dbh = new DBHelper(this);
        if (dbh.getTheme()==0) setTheme(R.style.NoActionBar2);
        setContentView(R.layout.activity_details);

        TextView detam = (TextView) findViewById(R.id.detail_amount);
        TextView detdt = (TextView) findViewById(R.id.detail_date);
        TextView detpl = (TextView) findViewById(R.id.detail_plan);
        TextView detct = (TextView) findViewById(R.id.detail_category);

        Bundle extras = getIntent().getExtras();
        modifiedName = extras.getString("nameSpesa");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(modifiedName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        modifiedAmount = extras.getString("amountSpesa");
        detam.setText("Ammontare: " + modifiedAmount);
        id = extras.getString("id");
        day = extras.getString("day");
        month = extras.getString("month");
        year = extras.getString("year");
        detdt.setText("Data: " + day + "/" + month + "/" + year);
        sign = extras.getBoolean("sign");
        catExtra = extras.getString("category");
        String typeS;
        if (modifiedAmount.contains("-")) typeS = "spesa";
        else typeS = "profitto";
        if (catExtra.equals("p")) detpl.setText("Tipo di "+ typeS + ": Ricorrente");
        else detpl.setText("Tipo di " + typeS + ": Occasionale");
        cat = extras.getString("cat");
        if (cat.equals("")){
            detct.setVisibility(View.GONE);
        }
        else detct.setText("Categoria: " + cat);
        pos = extras.getString("pos");
        if (!pos.equals("")) {
            FloatingActionButton fabpos = (FloatingActionButton) findViewById(R.id.detail_posbtn);
            fabpos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mapIntent = new Intent(DetailsActivity.this, MapsActivity.class);
                    mapIntent.putExtra("menu", true);
                    mapIntent.putExtra("det", pos);
                    mapIntent.putExtra("namedet", modifiedName);
                    startActivity(mapIntent);
                }
            });
        }
        else findViewById(R.id.layout_of_detpos).setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catExtra.equals("p")) {
                    Intent modifySpesa = new Intent(DetailsActivity.this, SpesaActivityPlanned.class);
                    modifySpesa.putExtra("day", day);
                    modifySpesa.putExtra("month", month);
                    modifySpesa.putExtra("year", year);
                    modifySpesa.putExtra("id", id);
                    modifySpesa.putExtra("category", catExtra);
                    modifySpesa.putExtra("cat", cat);
                    modifySpesa.putExtra("nameSpesa", modifiedName);
                    modifySpesa.putExtra("amountSpesa", modifiedAmount);
                    startActivity(modifySpesa);
                    finish();
                }
                else {
                    Intent modifySpesa = new Intent(DetailsActivity.this, SpesaActivity.class);
                    modifySpesa.putExtra("day", day);
                    modifySpesa.putExtra("month", month);
                    modifySpesa.putExtra("year", year);
                    modifySpesa.putExtra("id", id);
                    modifySpesa.putExtra("category", catExtra);
                    modifySpesa.putExtra("cat", cat);
                    modifySpesa.putExtra("nameSpesa", modifiedName);
                    modifySpesa.putExtra("amountSpesa", modifiedAmount);
                    startActivity(modifySpesa);
                    finish();
                }
            }
        });
    }

}
