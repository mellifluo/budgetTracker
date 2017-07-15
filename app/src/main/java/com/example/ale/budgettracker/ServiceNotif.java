package com.example.ale.budgettracker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class ServiceNotif extends IntentService {
    public ServiceNotif()
    {
        super("ServiceNotif");
    }

    @Override
    protected void onHandleIntent(Intent i) {
        int n=0;
        DBHelper dbh = new DBHelper(this);
        while(true) {
            Cursor cursor = dbh.checkAlert();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String nomeNot = cursor.getString(cursor.getColumnIndex(dbh.COLUMN_EXPENSE_NAME));
                float amount = cursor.getFloat(cursor.getColumnIndex(dbh.COLUMN_AMOUNT));
                String sNot;
                if (amount < 0) sNot = "Ricordati che devi fare cassa!";
                else sNot = "Hai una spesa da fare!";
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setPriority(1)
                                .setSmallIcon(R.drawable.icon7)
                                .setContentTitle(nomeNot)
                                .setContentText(sNot);
                Intent resultIntent = new Intent(this, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(n, mBuilder.build());
                n++;
                cursor.moveToNext();
            }
            cursor.close();
            try {
                Thread.sleep(43200000);
            }
            catch (InterruptedException e) { }
        }
    }

    @Override
    public void onDestroy() {
    }
}