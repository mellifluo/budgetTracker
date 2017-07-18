package com.example.ale.budgettracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via name/amount.
 */
public class SpesaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // UI references.
    private AutoCompleteTextView nomeSpesa;
    private EditText importoSpesa;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private String year, month, day, newCat;
    private String id;
    private String address = "";
    private boolean sign;
    private DBHelper dbh;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesa);
        // Set up the login form.
        nomeSpesa = (AutoCompleteTextView) findViewById(R.id.nome_spesa);

        importoSpesa = (EditText) findViewById(R.id.prezzo);

        dbh = new DBHelper(this);

        calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        Button mnameRemoveButton = (Button) findViewById(R.id.rimuovi_spesa_button);
        mnameRemoveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAttempt();
            }
        });
        mnameRemoveButton.setVisibility(View.INVISIBLE);

        Button mnameSignInButton = (Button) findViewById(R.id.aggiungi_spesa_button);
        mnameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        makeSpinner();
        spinner.setOnItemSelectedListener(this);

        Button DataButton = (Button) findViewById(R.id.data_spesa_button);

        Bundle extras = getIntent().getExtras();
        if (extras.size() > 1){
            DataButton.setVisibility(View.GONE);
            mnameRemoveButton.setVisibility(View.VISIBLE);
            String modifiedName = extras.getString("nameSpesa");
            nomeSpesa.setText(modifiedName);
            String modifiedAmount = extras.getString("amountSpesa");
            modifiedAmount = removeLastChar(modifiedAmount);
            importoSpesa.setText(modifiedAmount);
            importoSpesa.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            id = extras.getString("id");
            day = extras.getString("day");
            month = extras.getString("month");
            year = extras.getString("year");
            newCat = extras.getString("cat");
        }
        sign = extras.getBoolean("sign");

        Button buttonPosition = (Button) findViewById(R.id.posizione_spesa_button);
        buttonPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(SpesaActivity.this, MapsActivity.class);
                mapIntent.putExtra("menu", false);
                startActivityForResult(mapIntent, 5);
            }
        });
    }
    private void attemptLogin() {

        // Reset errors.
        nomeSpesa.setError(null);
        importoSpesa.setError(null);

        String name = nomeSpesa.getText().toString();
        String amount = importoSpesa.getText().toString();
        amount = String.format("%.2f", Float.valueOf(amount));
        if (sign) amount = "-" + amount;
        amount = amount.replace(",", ".");

        boolean cancel = false;
        View focusView = null;

        if (name.equals("Budget mensile")) {
            nomeSpesa.setError("Non puoi scrivere Budget mensile!");
            focusView = nomeSpesa;
            cancel = true;
        }

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Penso tu abbia esagerato");
            focusView = importoSpesa;
            cancel = true;
        }

        if (name.length() > 15 ) {
            nomeSpesa.setError("Troppo lunga!");
            focusView = nomeSpesa;
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
            if (dbh.getExpanse(nomeSpesa.getText().toString(), year, month, day).getCount() == 0) {
                float code = dbh.insertNewExpense(nomeSpesa.getText().toString(), amount,
                        String.valueOf(year), String.valueOf(month), String.valueOf(day), newCat, "o", address);
                if (code != -1)
                    Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                dbh.modifyExpanse(nomeSpesa.getText().toString(), amount,
                        String.valueOf(year), String.valueOf(month), String.valueOf(day), newCat, "o", address);
                finish();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && requestCode == 5 && data != null) {
                address = data.getStringExtra("position");
            }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            int amonth = Integer.valueOf(month);
            amonth--;
            month = String.valueOf(amonth);
            return new DatePickerDialog(this,
                    myDateListener, Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    year = String.valueOf(arg1);
                    month = String.valueOf(arg2+1);
                    day = String.valueOf(arg3);
                }
            };


    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void removeAttempt(){
        String name = nomeSpesa.getText().toString();
        dbh.removeOneExpanse(name,id, year, month, day);
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, final View view,
                               int pos, long id) {
        String selectedItem = (String) parent.getItemAtPosition(pos);
        if (selectedItem.equals("Nessuna categoria")) newCat = "";
        else if (selectedItem.equals("Aggiungi categoria")) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(SpesaActivity.this);
            final EditText input = new EditText(SpesaActivity.this);
            alert.setView(input);
            alert.setTitle("Inserisci la nuova categoria:");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString().trim();
                    if (value.length()<12) {
                        dbh.insertNewCat(value);
                        Snackbar.make(view, "Categoria aggiunta", Snackbar.LENGTH_LONG).show();
                        makeSpinner();
                    }
                    else Snackbar.make(view, "Categoria troppo lunga!", Snackbar.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            alert.show();

        }
        else newCat = selectedItem;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void makeSpinner(){
        ArrayList<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add("Nessuna categoria");
        Cursor cursor = dbh.getCat();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arraySpinner.add(cursor.getString(0));
            cursor.moveToNext();
        }
        arraySpinner.add("Aggiungi categoria");
        spinner = (Spinner) findViewById(R.id.spinner_cat);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

}

