package com.example.ale.budgettracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;

//YEAR CHART ACTIVITY
public class MonthsChartActivity extends AppCompatActivity {
    private static DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_chart);
        LineChart chart = (LineChart) findViewById(R.id.chartMese);
        Toast.makeText(this, "Per una migliore visualizzazione ruotare lo schermo", Toast.LENGTH_LONG);

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
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            month = extras.getString("month");
        }
        ArrayList<LineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();
        ArrayList<Entry> valueSet2 = new ArrayList<>();
        ArrayList<Entry> valueSet3 = new ArrayList<>();

        for (int i=1; i<32; i++) {
            valueSet1.add(new Entry(dbh.getTotalInAMonth(year, String.valueOf(i)), i));
            valueSet2.add(new Entry(dbh.getLossInAMonth(year, String.valueOf(0)), 0));
            valueSet3.add(new Entry(dbh.getPosInAMonth(year, String.valueOf(0)), 0));
        }

        LineDataSet LineDataSet1 = new LineDataSet(valueSet1, "Totale");
        LineDataSet1.setColor(Color.BLUE);

        LineDataSet LineDataSet2 = new LineDataSet(valueSet2, "Perdite");
        LineDataSet2.setColor(Color.RED);

        LineDataSet LineDataSet3 = new LineDataSet(valueSet3, "Guadagni");
        LineDataSet3.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(LineDataSet1);
        dataSets.add(LineDataSet2);
        dataSets.add(LineDataSet3);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i=1; i<32; i++) {
            xAxis.add(String.valueOf(i));
        }
        return xAxis;
    }
}