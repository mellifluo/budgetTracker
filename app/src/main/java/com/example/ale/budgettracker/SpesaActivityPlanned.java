package com.example.ale.budgettracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
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

public class SpesaActivityPlanned extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    // UI references.
    private AutoCompleteTextView nomeSpesa;
    private EditText importoSpesa;
    private EditText numeroSpesa;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private boolean monthly, mod, sign;
    private int year, month, day;
    private int count = 1;
    private String id, newCat;
    private String address = "";
    private DBHelper dbh;
    private ArrayAdapter<String> adapter;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = new DBHelper(this);
        if (dbh.getTheme()==0) setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_spesa_planned);
        // Set up the login form.

        importoSpesa = (EditText) findViewById(R.id.prezzo2);
        numeroSpesa = (EditText) findViewById(R.id.repeat);
        nomeSpesa = (AutoCompleteTextView) findViewById(R.id.nome_spesa2);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        makeSpinner();
        spinner.setOnItemSelectedListener(this);

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
        if (extras.size() > 2){
            DataButton.setVisibility(View.GONE);
            mnameSignInButton2.setVisibility(View.VISIBLE);
            mnameRemoveButton.setVisibility(View.VISIBLE);
            mnameRemoveButton2.setVisibility(View.VISIBLE);
            numeroSpesa.setVisibility(View.GONE);
            String modifiedName = extras.getString("nameSpesa");
            nomeSpesa.setText(modifiedName);
            String modifiedAmount = extras.getString("amountSpesa");
            importoSpesa.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED
                    | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            modifiedAmount = removeLastChar(modifiedAmount);
            importoSpesa.setText(modifiedAmount);
            id = extras.getString("id");
            day = Integer.valueOf(extras.getString("day"));
            month = Integer.valueOf(extras.getString("month"));
            year = Integer.valueOf(extras.getString("year"));
            newCat = extras.getString("cat");
        }
        else {
            if (extras.getBoolean("month")) {
                numeroSpesa.setHint("Per quanti mesi");
                monthly = true;
            }
            else {
                monthly = false;
                numeroSpesa.setHint("Per quante settimane");
            }
        }
        sign = extras.getBoolean("sign");

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
                Intent mapIntent = new Intent(SpesaActivityPlanned.this, MapsActivity.class);
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
        String ncount = numeroSpesa.getText().toString();
        if (!numeroSpesa.getText().toString().equals("")){
            count = Integer.valueOf(numeroSpesa.getText().toString());
        }
        amount = String.format("%.2f", Float.valueOf(amount));
        amount = amount.replace(",", ".");
        if (sign) amount = "-" + amount;
        boolean cancel = false;
        View focusView = null;

        if (count > 30) {
            numeroSpesa.setError("Penso tu abbia esagerato!");
            focusView = numeroSpesa;
            cancel = true;
        }

        if (name.length() > 15 ) {
            nomeSpesa.setError("Troppo lunga!");
            focusView = nomeSpesa;
            cancel = true;
        }

        // Check for a valid amount, if the user entered one.
        if (!TextUtils.isEmpty(amount) && amount.length() > 10) {
            importoSpesa.setError("Penso tu abbia esagerato!");
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

        if (TextUtils.isEmpty(ncount) && !mod) {
            numeroSpesa.setError("Metti per quante volte");
            focusView = numeroSpesa;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            if (dbh.getExpanse(nomeSpesa.getText().toString(), String.valueOf(year), String.valueOf(month),
                    String.valueOf(day)).getCount() == 0) {
                float code = dbh.insertPlannedExpense(nomeSpesa.getText().toString(), amount,
                            String.valueOf(year), String.valueOf(month), String.valueOf(day), monthly,
                            count, newCat, "p", address);
                if (code != -1)
                    Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                if (mod) {
                    Toast.makeText(this, "Modifiche effettuate",
                            Toast.LENGTH_LONG);
                    dbh.modifyExpanseAll(name, amount, newCat, address, id);
                    finish();
                }
                else {
                    Toast.makeText(this, "Nome transazione e data già inserite, modificato l'ammontare",
                            Toast.LENGTH_LONG);
                    dbh.modifyExpanse(nomeSpesa.getText().toString(), amount,
                            String.valueOf(year), String.valueOf(month), String.valueOf(day), newCat, "p", address);
                    finish();
                }
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
        String name = nomeSpesa.getText().toString();
        dbh.removeExpanse(name,id);
        finish();
    }


    private void removeAttemptOne(){
        String name = nomeSpesa.getText().toString();
        dbh.removeOneExpanse(name,id, String.valueOf(year), String.valueOf(month), String.valueOf(day));
        finish();
    }

    public void onItemSelected(AdapterView<?> parent, final View view,
                               int pos, long id) {
        String selectedItem = (String) parent.getItemAtPosition(pos);
        if (selectedItem.equals("Nessuna categoria")) newCat = "";
        else if (selectedItem.equals("Aggiungi categoria")) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(SpesaActivityPlanned.this);
            final EditText input = new EditText(SpesaActivityPlanned.this);
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
        spinner = (Spinner) findViewById(R.id.spinner_cat2);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}
