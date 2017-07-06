package com.example.ale.budgettracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;

//YEAR CHART ACTIVITY
public class LineChartActivity extends AppCompatActivity {
    private static DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        BarChart chart = (BarChart) findViewById(R.id.chart);
        chart.getAxisLeft().setStartAtZero(false);
        chart.getAxisRight().setStartAtZero(false);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);

    }

    private ArrayList<BarDataSet> getDataSet() {
        dbh = new DBHelper(this);
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();

        BarEntry x0 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(0)), 0);
        valueSet1.add(x0);
        BarEntry x1 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(1)), 1);
        valueSet1.add(x1);
        BarEntry x2 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(2)), 2);
        valueSet1.add(x2);
        BarEntry x3 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(3)), 3);
        valueSet1.add(x3);
        BarEntry x4 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(4)), 4);
        valueSet1.add(x4);
        BarEntry x5 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(5)), 5);
        valueSet1.add(x5);
        BarEntry x6 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(6)), 6);
        valueSet1.add(x6);
        BarEntry x7 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(7)), 7);
        valueSet1.add(x7);
        BarEntry x8 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(8)), 8);
        valueSet1.add(x8);
        BarEntry x9 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(9)), 9);
        valueSet1.add(x9);
        BarEntry x10 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(10)), 10);
        valueSet1.add(x10);
        BarEntry x11 = new BarEntry(dbh.getTotalInAMonth(year, String.valueOf(11)), 11);
        valueSet1.add(x11);

        BarEntry l0 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(0))*(-1), 0);
        valueSet2.add(l0);
        BarEntry l1 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(1))*(-1), 1);
        valueSet2.add(l1);
        BarEntry l2 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(2))*(-1), 2);
        valueSet2.add(l2);
        BarEntry l3 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(3))*(-1), 3);
        valueSet2.add(l3);
        BarEntry l4 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(4))*(-1), 4);
        valueSet2.add(l4);
        BarEntry l5 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(5))*(-1), 5);
        valueSet2.add(l5);
        BarEntry l6 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(6))*(-1), 6);
        valueSet2.add(l6);
        BarEntry l7 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(7))*(-1), 7);
        valueSet2.add(l7);
        BarEntry l8 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(8))*(-1), 8);
        valueSet2.add(l8);
        BarEntry l9 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(9))*(-1), 9);
        valueSet2.add(l9);
        BarEntry l10 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(10))*(-1), 10);
        valueSet2.add(l10);
        BarEntry l11 = new BarEntry(dbh.getLossInAMonth(year, String.valueOf(11))*(-1), 11);
        valueSet2.add(l11);

        BarEntry g0 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(0)), 0);
        valueSet3.add(g0);
        BarEntry g1 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(1)), 1);
        valueSet3.add(g1);
        BarEntry g2 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(2)), 2);
        valueSet3.add(g2);
        BarEntry g3 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(3)), 3);
        valueSet3.add(g3);
        BarEntry g4 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(4)), 4);
        valueSet3.add(g4);
        BarEntry g5 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(5)), 5);
        valueSet3.add(g5);
        BarEntry g6 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(6)), 6);
        valueSet3.add(g6);
        BarEntry g7 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(7)), 7);
        valueSet3.add(g7);
        BarEntry g8 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(8)), 8);
        valueSet3.add(g8);
        BarEntry g9 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(9)), 9);
        valueSet3.add(g9);
        BarEntry g10 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(10)), 10);
        valueSet3.add(g10);
        BarEntry g11 = new BarEntry(dbh.getPosInAMonth(year, String.valueOf(11)), 11);
        valueSet3.add(g11);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Totale");
        barDataSet1.setColor(Color.BLUE);
        barDataSet1.setDrawValues(false);

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Perdite");
        barDataSet2.setColor(Color.RED);
        barDataSet2.setDrawValues(false);

        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Guadagni");
        barDataSet3.setColor(Color.rgb(0, 155, 0));
        barDataSet3.setDrawValues(false);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("GEN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAG");
        xAxis.add("GIU");
        xAxis.add("LUG");
        xAxis.add("AGO");
        xAxis.add("SET");
        xAxis.add("OTT");
        xAxis.add("NOV");
        xAxis.add("DIC");
        return xAxis;
    }
}