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
 * A login screen that offers login via email/password.
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

        Button mEmailSignInButton = (Button) findViewById(R.id.aggiungi_spesa_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    private void attemptLogin() {

        // Reset errors.
        nomeSpesa.setError(null);
        importoSpesa.setError(null);

        // Store values at the time of the login attempt.
        String email = nomeSpesa.getText().toString();
        String password = importoSpesa.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && password.length() > 10) {
            importoSpesa.setError("Penso tu abbia esagerato, sborone");
            focusView = importoSpesa;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            nomeSpesa.setError("Metti il nome della spesa");
            focusView = nomeSpesa;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            importoSpesa.setError("Metti quanto ti è costata");
            focusView = importoSpesa;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            DBHelper dbh = new DBHelper(this);
            long code = dbh.insertNewExpense(nomeSpesa.getText().toString(), importoSpesa.toString());
            if (code != -1)
                Toast.makeText(this, "Inserimento effettuato", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}

