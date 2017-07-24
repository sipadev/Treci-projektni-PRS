package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <AutoStart>
 * \brief
 * <Klasa nasledjuje BroadcastReceiver da bi preko manifest fajla da na svakih sat vremena
 * da osvežava podatke>
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
public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in=new Intent(context, ServiceH.class);
        in.setFlags(1);

        context.startService(in);
    }
}