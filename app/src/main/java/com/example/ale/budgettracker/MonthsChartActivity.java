package com.example.ale.budgettracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthsChartActivity extends AppCompatActivity {
    private static DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_chart);
        LineChart chart = (LineChart) findViewById(R.id.chartMese);
        chart.getAxisLeft().setStartAtZero(false);
        chart.getAxisRight().setStartAtZero(false);
        LineData data = new LineData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);

    }

    private ArrayList<LineDataSet> getDataSet() {
        dbh = new DBHelper(this);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String month = extras.getString("month");
            calendar.set(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(1));
        }
        ArrayList<LineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        float value = 0;
        for (int i=0; i<12; i++) {
            value = dbh.getTotalInAMonth(year, String.valueOf(i)) + value;
            valueSet1.add(new Entry(value, i));
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
        String[] mesi = getResources().getStringArray(R.array.array_mesi);
        for (int i=1; i<13; i++) {
            xAxis.add(String.valueOf(mesi[i].substring(0,3)));
        }
        return xAxis;
    }
}