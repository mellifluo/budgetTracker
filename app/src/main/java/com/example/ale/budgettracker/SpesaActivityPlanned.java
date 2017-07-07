package com.example.ale.budgettracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SpesaActivityPlanned extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView nomeSpesa;
    private EditText importoSpesa;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesa_planned);
        // Set up the login form.
        nomeSpesa = (AutoCompleteTextView) findViewById(R.id.nome_spesa);

        importoSpesa = (EditText) findViewById(R.id.prezzo);


        Button mnameRemoveButton = (Button) findViewById(R.id.rimuovi_spesa_button);
        mnameRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAttempt();
            }
        });
        mnameRemoveButton.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mnameRemoveButton.setVisibility(View.VISIBLE);
            String modifiedName = extras.getString("nameSpesa");
            nomeSpesa.setText(modifiedName);
            String modifiedAmount = extras.getString("amountSpesa");
            modifiedAmount = removeLastChar(modifiedAmount);
            importoSpesa.setText(modifiedAmount);
        }

        Button mnameSignInButton = (Button) findViewById(R.id.aggiungi_spesa_button);
        mnameSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button buttonPosition = (Button) findViewById(R.id.posizione_spesa_button);
        buttonPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpesaActivityPlanned.this, MapsActivity.class));
            }
        });

        calendar = Calendar.getInstance();
        int x = Calendar.YEAR;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
    private void attemptLogin() {

        // Reset errors.
        nomeSpesa.setError(null);
        importoSpesa.setError(null);

        String name = nomeSpesa.getText().toString();
        String amount = importoSpesa.getText().toString();
        amount = String.format("%.2f", Float.valueOf(amount));
        amount = amount.replace(",", ".");
        boolean cancel = false;
        View focusView = null;

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Penso tu abbia esagerato, sborone");
            focusView = importoSpesa;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            nomeSpesa.setError("Metti il nome della spesa");
            focusView = nomeSpesa;
            cancel = true;
        }

        if (TextUtils.isEmpty(amount)) {
            importoSpesa.setError("Metti quanto ti è costata");
            focusView = importoSpesa;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            DBHelper dbh = new DBHelper(this);
            if (dbh.getExpanse(nomeSpesa.getText().toString()).getCount() == 0) {
                float code = dbh.insertNewExpense(nomeSpesa.getText().toString(), amount,
                        String.valueOf(year), String.valueOf(month+1), String.valueOf(day), "p");
                if (code != -1)
                    Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                dbh.modifyExpanse(nomeSpesa.getText().toString(), amount,
                        String.valueOf(year), String.valueOf(month+1), String.valueOf(day), "p");
                finish();
            }

        }
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year = arg1;
                    month = arg2;
                    day = arg3;
                }
            };


    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void removeAttempt(){
        DBHelper dbh = new DBHelper(this);
        String name = nomeSpesa.getText().toString();
        dbh.removeExpanse(name);
        finish();
    }
}
