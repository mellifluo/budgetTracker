package com.example.ale.budgettracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectChart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String year;
    private String month;
    private String day;
    private ArrayList<String> arraySpinner = new ArrayList<String>();
    private boolean history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        arraySpinner.add("Inserisci");

        Spinner s = (Spinner) findViewById(R.id.spinner_giorno);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter3);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_anno);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_anni, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_mese);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.array_mesi, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        s.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        history = extras.getBoolean("history");

        if (history){
            TextView domanda = (TextView) findViewById(R.id.DomandaGrafico);
            domanda.setText("Quale periodo vuoi vedere?");
            findViewById(R.id.correttavisione).setVisibility(View.GONE);
        }

        Button thisYear = (Button) findViewById(R.id.annoChart);
        thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected;
                if (history) selected = new Intent(view.getContext(), StoricoActivity.class);
                else selected = new Intent(view.getContext(), LineChartActivity.class);
                selected.putExtra("year", year);
                view.getContext().startActivity(selected);
            }
        });


        Button thisMonth = (Button) findViewById(R.id.meseChart);
        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected;
                if (history) selected = new Intent(view.getContext(), StoricoActivity.class);
                else selected = new Intent(view.getContext(), MonthsChartActivity.class);
                selected.putExtra("month", month);
                selected.putExtra("year", year);
                view.getContext().startActivity(selected);
            }
        });


        Button thisDay = (Button) findViewById(R.id.giornoChart);
        thisDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected;
                if (history) selected = new Intent(view.getContext(), StoricoActivity.class);
                else selected = new Intent(view.getContext(), DaysChartActivity.class);
                selected.putExtra("day", day);
                selected.putExtra("month", month);
                selected.putExtra("year", year);
                view.getContext().startActivity(selected);
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String selectedItem = (String) parent.getItemAtPosition(pos);
        if (!selectedItem.equals("Inserisci")) {
            if (!selectedItem.matches(".*\\d+.*")) {
                if (selectedItem.equals("Gennaio")) month = "1";
                if (selectedItem.equals("Febbraio")) month = "2";
                if (selectedItem.equals("Marzo")) month = "3";
                if (selectedItem.equals("Aprile")) month = "4";
                if (selectedItem.equals("Maggio")) month = "5";
                if (selectedItem.equals("Giugno")) month = "6";
                if (selectedItem.equals("Luglio")) month = "7";
                if (selectedItem.equals("Agosto")) month = "8";
                if (selectedItem.equals("Settembre")) month = "9";
                if (selectedItem.equals("Ottobre")) month = "10";
                if (selectedItem.equals("Novembre")) month = "11";
                if (selectedItem.equals("Dicembre")) month = "12";
                checkthirtyone();
            } else if (selectedItem.matches(".*\\d+.*") && Integer.valueOf(selectedItem)>1999) {
                year = selectedItem;
            }
            else {
                day = selectedItem;
            }
        }
        //view.getContext().startActivity(selected);
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void checkthirtyone() {
        int monthI = Integer.valueOf(month);
        arraySpinner = new ArrayList<String>();
        arraySpinner.add("Inserisci");
        if ( monthI % 2 == 1 && monthI != 11) {
            for (int i = 1; i<32; i++) {
                arraySpinner.add(String.valueOf(i));
            }
        }
        else {
            if (monthI == 2) {
                for (int i = 1; i<29; i++) {
                    arraySpinner.add(String.valueOf(i));
                }
            }
            else {
                for (int i = 1; i<31; i++) {
                    arraySpinner.add(String.valueOf(i));
                }
            }
        }
        Spinner s = (Spinner) findViewById(R.id.spinner_giorno);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter3);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }

}
