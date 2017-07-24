package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <ServiceH>
 * \brief
 * <Klasa nasledjuje Servis i sluzi da u pozadini izvrsava zadatke
 >
 *
 * Created on <DATE(24.05.2017)>
 *
 * @Author <Milorad Šipovac 1119/10>
 *
\notes
 * <DATE(dd.mm.yyyy)> <SHORT DESCRIPTION>
 *
\history
 *
 **********************************************************************/
public class ServiceH extends Service {



    private HTTPZahtev http;
    private long KOLIKO_CEKATI = 3600000;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        try {


            http = new HTTPZahtev(intent);
            Toast.makeText(ServiceH.this, R.string.automatsko_osvezavanje, Toast.LENGTH_SHORT).show();

            //Da se nakon svakih KOLIKO_CEKATI vremena ponovo pokrece
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (KOLIKO_CEKATI),
                    PendingIntent.getService(this, 0, new Intent(this, ServiceH.class), 0)
            );

            //Creating new thread for my service
            //Always write your long running tasks in a separate thread, to avoid ANR

            Thread t = new Thread(new Runnable() {
                public void run() {
                    http.getDataFromServer();
                }
            });
            t.start();
        } catch (Exception e) {
        }


        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {


        return null;
    }



}