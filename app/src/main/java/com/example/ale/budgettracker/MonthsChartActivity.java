package com.example.ale.budgettracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthsChartActivity extends AppCompatActivity {
    private static DBHelper dbh;
    private static String month;
    private static String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DBHelper(this);
        if (dbh.getTheme()==0) setTheme(R.style.AppTheme2);
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
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
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


        Bundle extras = getIntent().getExtras();
        ArrayList sc = extras.getCharSequenceArrayList("cat");
        for (int i = 0; i<sc.size(); i++) {
            float v = 0;
            ArrayList<Entry> valueSetX = new ArrayList<>();
            if ( monthI % 2 == 1 && monthI != 11) {
                for (int j = 0; j<31; j++) {
                    v = dbh.getAmInADayC(year, month, String.valueOf(j), String.valueOf(sc.get(i))) + v;
                    valueSetX.add(new Entry(v, j));
                }
            }
            else {
                if (monthI == 2) {
                    for (int j = 0; j<28; j++) {
                        v = dbh.getAmInADayC(year, month, String.valueOf(j), String.valueOf(sc.get(i))) + v;
                        valueSetX.add(new Entry(v, j));
                    }
                }
                else {
                    for (int j = 0; j<30; j++) {
                        v = dbh.getAmInADayC(year, month, String.valueOf(j), String.valueOf(sc.get(i))) + v;
                        valueSetX.add(new Entry(v, j));
                    }
                }
            }
            LineDataSet lineDataSetX = new LineDataSet(valueSetX, String.valueOf(sc.get(i)));
            lineDataSetX.setColor(ColorTemplate.JOYFUL_COLORS[i]);
            lineDataSetX.setDrawValues(false);
            lineDataSetX.setDrawCircles(false);
            dataSets.add(lineDataSetX);
        }

        LineDataSet LineDataSet1 = new LineDataSet(valueSet1, "Totale");
        LineDataSet1.setColor(Color.BLUE);
        LineDataSet1.setDrawValues(false);
        LineDataSet1.setDrawCircles(false);
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