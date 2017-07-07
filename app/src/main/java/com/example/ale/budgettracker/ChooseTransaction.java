package com.example.ale.budgettracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseTransaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_transaction);

        Button singola = (Button) findViewById(R.id.TransazioneSingola);
        singola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), SpesaActivity.class);
                view.getContext().startActivity(selected);
                finish();
            }
        });


        Button mensile = (Button) findViewById(R.id.TransazioneMensile);
        mensile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), MonthsChartActivity.class);
                selected.putExtra("month", true);
                view.getContext().startActivity(selected);
                finish();
            }
        });


        Button annuale = (Button) findViewById(R.id.TransazioneSettimanale);
        annuale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selected = new Intent(view.getContext(), DaysChartActivity.class);
                selected.putExtra("month", false);
                view.getContext().startActivity(selected);
                finish();
            }
        });

    }
}
