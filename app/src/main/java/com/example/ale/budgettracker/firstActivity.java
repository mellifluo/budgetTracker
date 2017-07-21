package com.example.ale.budgettracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class firstActivity extends AppCompatActivity {

    private EditText importoSpesa;
    private EditText nomePersona;
    private Calendar calendar;
    private boolean all = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        importoSpesa = (EditText) findViewById(R.id.budgetIniziale);
        nomePersona = (EditText) findViewById(R.id.inserisci_nome);
        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getPerson();
        if (cursor.moveToFirst()) {
            findViewById(R.id.domanda_nome).setVisibility(View.GONE);
            nomePersona.setVisibility(View.GONE);
            all = false;
        }

        Button aggiungiBudget = (Button) findViewById(R.id.aggiungi_budget);
        aggiungiBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    private void attemptLogin() {

        // Reset errors.
        importoSpesa.setError(null);
        nomePersona.setError(null);

        String nome = nomePersona.getText().toString();
        String amount = importoSpesa.getText().toString();
        amount = String.format("%.2f", Float.valueOf(amount));
        amount = amount.replace(",", ".");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Budget non valido");
            focusView = importoSpesa;
            cancel = true;
        }

        if (TextUtils.isEmpty(amount)) {
            importoSpesa.setError("Inserisci quanti soldi hai!");
            focusView = importoSpesa;
            cancel = true;
        }

        if (TextUtils.isEmpty(nome) && all) {
            importoSpesa.setError("Inserisci il nome!");
            focusView = importoSpesa;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            DBHelper dbh = new DBHelper(this);
            calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            float code = dbh.insertNewExpense("Budget mensile", amount,
                    year,
                    month,
                    day, "", "m", "");
            if (all) {
                code = dbh.insertNewPersona(nome,amount,true,true);
                String[] cats = getResources().getStringArray(R.array.array_category);
                for (int i = 0; i<cats.length; i++) code = dbh.insertNewCat(cats[i]);
            }
            if (code != -1 )
                Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
