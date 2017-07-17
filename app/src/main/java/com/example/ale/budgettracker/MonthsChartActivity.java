package com.example.ale.budgettracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthsChartActivity extends AppCompatActivity {
    private static DBHelper dbh;
    private static String month;
    private static String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_chart);

        LineChart chart = (LineChart) findViewById(R.id.chartMese);
        chart.getAxisLeft().setStartAtZero(false);
        chart.getAxisRight().setStartAtZero(false);
        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            month = extras.getString("month");
            year = extras.getString("year");
            calendar.set(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(1));
        }
        LineData data = new LineData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);

    }

    private ArrayList<LineDataSet> getDataSet() {
        dbh = new DBHelper(this);

        ArrayList<LineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        float value = 0;
        int monthI = Integer.valueOf(month);
        if ( monthI % 2 == 1 && monthI != 11) {
            for (int i = 0; i<31; i++) {
                value = dbh.getAmInADay(year, month, String.valueOf(i)) + value;
                valueSet1.add(new Entry(value, i));
            }
        }
        else {
            if (monthI == 2) {
                for (int i = 0; i<28; i++) {
                    value = dbh.getAmInADay(year, month, String.valueOf(i)) + value;
                    valueSet1.add(new Entry(value, i));
                }
            }
            else {
                for (int i = 0; i<30; i++) {
                    value = dbh.getAmInADay(year, month, String.valueOf(i)) + value;
                    valueSet1.add(new Entry(value, i));
                }
            }
        }

        LineDataSet LineDataSet1 = new LineDataSet(valueSet1, "Totale");
        LineDataSet1.setColor(Color.BLUE);
        LineDataSet1.setDrawValues(false);
        LineDataSet1.setDrawCircles(false);
        dataSets = new ArrayList<>();
        dataSets.add(LineDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        int monthI = Integer.valueOf(month);
        if ( monthI % 2 == 1 && monthI != 11) {
            for (int i = 1; i<32; i++) {
                xAxis.add(String.valueOf(i));
            }
        }
        else {
            if (monthI == 2) {
                for (int i = 1; i<29; i++) {
                    xAxis.add(String.valueOf(i));
                }
            }
            else {
                for (int i = 1; i<31; i++) {
                    xAxis.add(String.valueOf(i));
                }
            }
        }
        return xAxis;
    }

}