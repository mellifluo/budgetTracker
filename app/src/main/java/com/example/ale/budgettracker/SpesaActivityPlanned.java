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
    private EditText numeroSpesa;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private boolean monthly;
    private boolean mod, set;
    private int year, month, day, count;
    private String id;
    private String Fyear, Fmonth, Fday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesa_planned);
        // Set up the login form.
        nomeSpesa = (AutoCompleteTextView) findViewById(R.id.nome_spesa2);

        importoSpesa = (EditText) findViewById(R.id.prezzo2);
        numeroSpesa = (EditText) findViewById(R.id.repeat);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Button mnameRemoveButton = (Button) findViewById(R.id.rimuovi_spesa_button2);
        mnameRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAttemptOne();
            }
        });
        mnameRemoveButton.setVisibility(View.INVISIBLE);

        Button mnameRemoveButton2 = (Button) findViewById(R.id.rimuovi_spesa_button3);
        mnameRemoveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAttempt();
            }
        });
        mnameRemoveButton2.setVisibility(View.INVISIBLE);

        Button mnameSignInButton2 = (Button) findViewById(R.id.aggiungi_spesa_button3);
        mnameSignInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod = true;
                attemptLogin();
            }
        });
        mnameSignInButton2.setVisibility(View.GONE);

        Button DataButton = (Button) findViewById(R.id.data_spesa_button2);

        Bundle extras = getIntent().getExtras();
        if (extras.size() > 1){
            DataButton.setVisibility(View.GONE);
            mnameSignInButton2.setVisibility(View.VISIBLE);
            mnameRemoveButton.setVisibility(View.VISIBLE);
            mnameRemoveButton2.setVisibility(View.VISIBLE);
            numeroSpesa.setVisibility(View.GONE);
            String modifiedName = extras.getString("nameSpesa");
            nomeSpesa.setText(modifiedName);
            String modifiedAmount = extras.getString("amountSpesa");
            modifiedAmount = removeLastChar(modifiedAmount);
            importoSpesa.setText(modifiedAmount);
            id = extras.getString("id");
            day = Integer.valueOf(extras.getString("day"));
            month = Integer.valueOf(extras.getString("month"));
            year = Integer.valueOf(extras.getString("year"));
        }
        else {
            if (extras.getBoolean("month")) {
                numeroSpesa.setText("12");
                monthly = true;
            }
            else {
                monthly = false;
                numeroSpesa.setText("4");
            }
        }

        Button mnameSignInButton = (Button) findViewById(R.id.aggiungi_spesa_button2);
        mnameSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod = false;
                attemptLogin();
            }
        });


        Button buttonPosition = (Button) findViewById(R.id.posizione_spesa_button2);
        buttonPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SpesaActivityPlanned.this, MapsActivity.class));
            }
        });
    }
    private void attemptLogin() {

        // Reset errors.
        nomeSpesa.setError(null);
        importoSpesa.setError(null);

        String name = nomeSpesa.getText().toString();
        String amount = importoSpesa.getText().toString();
        if (!numeroSpesa.getText().toString().equals("")){
            count = Integer.valueOf(numeroSpesa.getText().toString());
        }
        amount = String.format("%.2f", Float.valueOf(amount));
        amount = amount.replace(",", ".");
        boolean cancel = false;
        View focusView = null;

        if (count > 24) {
            numeroSpesa.setError("Massimo 24 volte!");
            focusView = numeroSpesa;
            cancel = true;
        }

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Penso tu abbia esagerato");
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
            if (!set) month++;
            if (dbh.getExpanse(nomeSpesa.getText().toString(), String.valueOf(year), String.valueOf(month),
                    String.valueOf(day)).getCount() == 0) {
                float code = dbh.insertPlannedExpense(nomeSpesa.getText().toString(), amount,
                            String.valueOf(year), String.valueOf(month), String.valueOf(day), monthly,
                            count, "p");
                if (code != -1)
                    Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                if (mod) {
                    Toast.makeText(this, "Modifiche effettuate",
                            Toast.LENGTH_LONG);
                    dbh.modifyExpanseAll(name, amount, id);
                    finish();
                }
                else {
                    Toast.makeText(this, "Nome transazione e data già inserite, modificato l'ammontare",
                            Toast.LENGTH_LONG);
                    dbh.modifyExpanse(nomeSpesa.getText().toString(), amount,
                            String.valueOf(year), String.valueOf(month), String.valueOf(day), "p");
                    finish();
                }
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
            set = true;
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
                    month = arg2+1;
                    day = arg3;
                }
            };


    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void removeAttempt(){
        DBHelper dbh = new DBHelper(this);
        String name = nomeSpesa.getText().toString();
        dbh.removeExpanse(name,id);
        finish();
    }


    private void removeAttemptOne(){
        DBHelper dbh = new DBHelper(this);
        String name = nomeSpesa.getText().toString();
        dbh.removeOneExpanse(name,id, String.valueOf(year), String.valueOf(month), String.valueOf(day));
        finish();
    }
}
