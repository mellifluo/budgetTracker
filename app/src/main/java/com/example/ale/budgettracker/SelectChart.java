package com.example.ale.budgettracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectChart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String year;
    private String month;
    private String day;
    private ArrayList<String> arraySpinner = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chart);

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

        Button thisYear = (Button) findViewById(R.id.annoChart);
        thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), LineChartActivity.class);
                selected.putExtra("year", year);
                view.getContext().startActivity(selected);
            }
        });


        Button thisMonth = (Button) findViewById(R.id.meseChart);
        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), MonthsChartActivity.class);
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
            if (!selectedItem.matches(".*\\d+.*") || Integer.valueOf(selectedItem)<35) {
                switch (selectedItem) {
                    case "Gennaio":
                        month = "0";
                    case "Febbraio":
                        month = "1";
                    case "Marzo":
                        month = "2";
                    case "Aprile":
                        month = "3";
                    case "Maggio":
                        month = "4";
                    case "Giugno":
                        month = "5";
                    case "Luglio":
                        month = "6";
                    case "Agosto":
                        month = "7";
                    case "Settembre":
                        month = "8";
                    case "Ottobre":
                        month = "9";
                    case "Novembre":
                        month = "10";
                    case "Dicembre":
                        month = "11";
                }
                checkthirtyone();
            } else {
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
        while (arraySpinner.isEmpty()) arraySpinner.remove(0);
        arraySpinner.add("Inserisci");
        if ( monthI % 2 == 0 && monthI != 10) {
            for (int i = 1; i<32; i++) {
                arraySpinner.add(String.valueOf(i));
            }
        }
        if (monthI != 28) {
            for (int i = 1; i<31; i++) {
                arraySpinner.add(String.valueOf(i));
            }
        }
        else {
            for (int i = 1; i<29; i++) {
                arraySpinner.add(String.valueOf(i));
            }
        }
    }


}