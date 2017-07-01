package com.example.ale.budgettracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via name/amount.
 */
public class SpesaActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView nomeSpesa;
    private EditText importoSpesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spesa);
        // Set up the login form.
        nomeSpesa = (AutoCompleteTextView) findViewById(R.id.nome_spesa);

        importoSpesa = (EditText) findViewById(R.id.prezzo);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String modifiedName = extras.getString("nameSpesa");
            nomeSpesa.setText(modifiedName);
            String modifiedAmount = extras.getString("amountSpesa");
            importoSpesa.setText(modifiedAmount);
        }

        Button mnameSignInButton = (Button) findViewById(R.id.aggiungi_spesa_button);
        mnameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mnameRemoveButton = (Button) findViewById(R.id.rimuovi_spesa_button);
        mnameRemoveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAttempt();
            }
        });

    }
    private void attemptLogin() {

        // Reset errors.
        nomeSpesa.setError(null);
        importoSpesa.setError(null);

        String name = nomeSpesa.getText().toString();
        String amount = importoSpesa.getText().toString();

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
                long code = dbh.insertNewExpense(nomeSpesa.getText().toString(), importoSpesa.getText().toString());
                if (code != -1)
                    Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
                else Toast.makeText(this, "Errore nell'inserimento", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                dbh.modifyExpanse(nomeSpesa.getText().toString(), importoSpesa.getText().toString());
                finish();
            }

        }
    }

    private void removeAttempt(){
        DBHelper dbh = new DBHelper(this);
        String name = nomeSpesa.getText().toString();
        dbh.removeExpanse(name);
        finish();
    }

}

