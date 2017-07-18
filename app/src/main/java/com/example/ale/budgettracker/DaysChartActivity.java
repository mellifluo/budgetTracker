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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DaysChartActivity extends AppCompatActivity {
     private static DBHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_days_chart);
        super.onCreate(savedInstanceState);
        LineChart chart = (LineChart) findViewById(R.id.chartGiorno);
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
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String Fday = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        Bundle extras = getIntent().getExtras();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if (extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            Fday = extras.getString("day");
            calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        }
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        float value = 0;
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 0));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 1));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 2));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 3));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 4));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 5));
        calendar.add(Calendar.DAY_OF_MONTH,1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        value = dbh.getAmInADay(year, month, String.valueOf(day)) + value;
        valueSet1.add(new Entry(value, 6));

        LineDataSet LineDataSet1 = new LineDataSet(valueSet1, "Totale");
        LineDataSet1.setColor(Color.BLUE);
        LineDataSet1.setDrawValues(false);
        LineDataSet1.setDrawCircles(false);
        dataSets.add(LineDataSet1);

        calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(Fday));
        ArrayList sc = extras.getCharSequenceArrayList("cat");
        value = 0;
        for (int i = 0; i<sc.size(); i++) {
            value = 0;
            ArrayList<Entry> valueSetX = new ArrayList<>();
            for (int j = 0; j<7; j++) {
                day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                value = dbh.getAmInADayC(year, month, String.valueOf(day),String.valueOf(sc.get(i))) + value;
                valueSetX.add(new Entry(value, j));
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
            LineDataSet lineDataSetX = new LineDataSet(valueSetX, String.valueOf(sc.get(i)));
            lineDataSetX.setColor(ColorTemplate.JOYFUL_COLORS[i]);
            lineDataSetX.setDrawValues(false);
            lineDataSetX.setDrawCircles(false);
            dataSets.add(lineDataSetX);
        }


        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month;
        String day;
        String date;
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
            calendar.set(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day));
        }

        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH));
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        date = day + "/" + month;
        xAxis.add(date);

        return xAxis;
    }
}
