package com.example.ale.budgettracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class firstActivity extends AppCompatActivity {

    private EditText importoSpesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        importoSpesa = (EditText) findViewById(R.id.budgetIniziale);

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

        String amount = importoSpesa.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Budget non valido");
            focusView = importoSpesa;
            cancel = true;
        }

        if (TextUtils.isEmpty(amount)) {
            importoSpesa.setError("Metti quanto");
            focusView = importoSpesa;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            DBHelper dbh = new DBHelper(this);
            long code = dbh.insertNewExpense("Budget iniziale", importoSpesa.getText().toString());
            if (code != -1)
                Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
