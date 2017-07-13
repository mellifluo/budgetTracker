package com.example.ale.budgettracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.R.id.toggle;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Spesa> listItems;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private rvAdapter adapter;
    private static DBHelper dbh;
    public String nomeUtente = "John Smith";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        listItems = new ArrayList<>();

        adapter = new rvAdapter(listItems);
        rv.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSpesa = new Intent(MainActivity.this, ChooseTransaction.class);
                startActivity(addSpesa);
            }
        });
        dbh = new DBHelper(this);
        dbh.checkMensile();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = dbh.getBudget();
        if (cursor.getCount() == 0){
            startActivity(new Intent(MainActivity.this, firstActivity.class));
        }
        cursor.moveToFirst();
        listItems.clear();
        while (!cursor.isAfterLast()) {
            boolean haveToContinue = false;
            if (!haveToContinue) {
                String nameNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXPENSE_NAME));
                String amountNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_AMOUNT));
                String yearNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.YEAR_EXPANSE));
                String monthNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.MONTH_EXPANSE));
                String dayNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.DAY_EXPANSE));
                String categoryNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY));
                String idNewSpesa = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
                Calendar calendarNow = Calendar.getInstance();
                Calendar calendar = new GregorianCalendar(Integer.valueOf(yearNewSpesa),
                        Integer.valueOf(monthNewSpesa)-1, Integer.valueOf(dayNewSpesa)+1);
                if (calendar.compareTo(calendarNow)>=0 || nameNewSpesa.equals("Budget mensile"))  {
                    Spesa newSpesa = new Spesa(nameNewSpesa, amountNewSpesa, yearNewSpesa, monthNewSpesa,
                            dayNewSpesa, categoryNewSpesa, idNewSpesa);
                    listItems.add(newSpesa);
                }
            }
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
        cursor.close();
        float totalBudget = dbh.getTotal();

        String totalBudgetToView = (String.valueOf(totalBudget)) + "€";

        TextView asd = (TextView) findViewById(R.id.textView2);
        asd.setText(totalBudgetToView);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Stai per cancellare tutti i dati");
            alertDialog.setMessage("Sei proprio sicuro?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbh.removeAll();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
            //Intent goSetting = new Intent(this, SettingsActivity.class);
            //startActivity(goSetting);
            return true;
        }

        if (id == R.id.database_view) {
            startActivity(new Intent(MainActivity.this, AndroidDatabaseManager.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, SelectChart.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, StoricoActivity.class));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
            mapIntent.putExtra("menu", true);
            startActivity(mapIntent);

        } else if (id == R.id.nav_send) {
            
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
