package com.example.ale.budgettracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SummaryActivity extends AppCompatActivity {
    String pdfText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView nomeT = (TextView) findViewById(R.id.summary_nome);
        TextView initT = (TextView) findViewById(R.id.summary_initial);
        TextView totT = (TextView) findViewById(R.id.summary_totale);
        TextView speseT = (TextView) findViewById(R.id.summary_spese);
        TextView guadT = (TextView) findViewById(R.id.summary_guadagni);
        TextView medST = (TextView) findViewById(R.id.summary_mediaspesa);
        TextView medGT = (TextView) findViewById(R.id.summary_mediaguadagni);
        TextView maxST = (TextView) findViewById(R.id.summary_maxspesa);
        TextView maxGT = (TextView) findViewById(R.id.summary_maxguadagno);

        DBHelper dbh = new DBHelper(this);
        Cursor cursor = dbh.getPerson();
        cursor.moveToFirst();
        String namePerson = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_PERSON));
        nomeT.setText(namePerson);
        String initial_amount = cursor.getString(cursor.getColumnIndex(DBHelper.INITIAL_BUDGET));
        initT.setText("Budget iniziale: " + initial_amount + "€");
        cursor.close();

        totT.setText("Totale: " + String.valueOf(dbh.getTotal())+ "€");
        speseT.setText("Totale speso: " + String.valueOf(dbh.getTotalLoss())+ "€");
        guadT.setText("Totale guadagnato: " + String.valueOf(dbh.getTotalEarn())+ "€");
        medST.setText("Media spesa: " + String.valueOf(dbh.avgLoss())+ "€");
        medGT.setText("Media spesa: " + String.valueOf(dbh.avgEarn())+ "€");
        maxST.setText("Massima spesa: " + String.valueOf(dbh.maxLoss())+ "€");
        maxGT.setText("Massimo guadagno: " + String.valueOf(dbh.maxEarn())+ "€");
        pdfText = namePerson + "\n|Budget iniziale: " + initial_amount +
                "€\n|Totale: " + String.valueOf(dbh.getTotal()) +
                "€\n|Totale speso: " + String.valueOf(dbh.getTotalLoss()) +
                "€\n|Totale guadagnato: " + String.valueOf(dbh.getTotalEarn()) +
                "€\n|Media spesa: " + String.valueOf(dbh.avgLoss()) +
                "€\n|Media guadagno: " + String.valueOf(dbh.avgEarn()) +
                "€\n|Media spesa: " + String.valueOf(dbh.avgEarn()) +
                "€\n|Massima spesa: " + String.valueOf(dbh.maxLoss()) +
                "€\n|Massimo guadagno: " + String.valueOf(dbh.maxEarn()) + "€";

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fabPDF);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf(pdfText);
            }
        });
    }

    public void createPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "riassuntoBudget.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);
            Font font = new Font(Font.FontFamily.TIMES_ROMAN,30.0f);
            String[] arrayS = pdfText.split("\\|");
            //open the document
            doc.open();

            for (int i = 0; i<10; i++) {
                Paragraph p1 = new Paragraph(arrayS[i]);
                p1.setAlignment(Paragraph.ALIGN_LEFT);
                p1.setFont(font);
                //add paragraph to document
                doc.add(p1);
            }

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
            Toast.makeText(getApplicationContext(), "PDF creato con successo", Toast.LENGTH_LONG);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }


}
