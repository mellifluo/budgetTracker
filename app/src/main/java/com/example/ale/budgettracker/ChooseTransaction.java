package com.example.ale.budgettracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ChooseTransaction extends AppCompatActivity {
    boolean sign = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper dbh = new DBHelper(this);
        if (dbh.getTheme()==0) setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_choose_transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CheckBox checkBox1 = (CheckBox) findViewById(R.id.check1);
        final CheckBox checkBox2 = (CheckBox) findViewById(R.id.check2);

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    checkBox2.setChecked(false);
                    sign = false;
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                    checkBox1.setChecked(false);
                    sign = true;
                }
            }
        });

        Button singola = (Button) findViewById(R.id.TransazioneSingola);
        singola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), SpesaActivity.class);
                selected.putExtra("sign", sign);
                view.getContext().startActivity(selected);
                finish();
            }
        });


        Button mensile = (Button) findViewById(R.id.TransazioneMensile);
        mensile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), SpesaActivityPlanned.class);
                selected.putExtra("month", true);
                selected.putExtra("sign", sign);
                view.getContext().startActivity(selected);
                finish();
            }
        });


        Button annuale = (Button) findViewById(R.id.TransazioneSettimanale);
        annuale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), SpesaActivityPlanned.class);
                selected.putExtra("month", false);
                selected.putExtra("sign", sign);
                view.getContext().startActivity(selected);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
