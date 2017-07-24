package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_ZA_SLANJE = "resReceiver";
    public static final String KEY_ZA_SLANJE_SERVISA = "ser";
    public static DBHelper mDbHelper;
    ListView list;
    public static  ArrayList<Vesti> adapter;
    private MyResultReceiver resultReceiver = null;
    Intent intent;
    public static long NAJNOVIJI_CLANAK=0;
    private HTTPZahtev http;

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter=new ArrayList<Vesti>();

        mDbHelper = new DBHelper(MainActivity.this);
        mDbHelper.fillArrayWithDB(adapter);
        list = (ListView) findViewById(R.id.listView);
        resultReceiver=new MyResultReceiver(new Handler());



        SharedPreferences preferences=getSharedPreferences("ActivityPREF",Context.MODE_PRIVATE);
        if(preferences.getBoolean("activity_executed",false)){


        }else{
            SharedPreferences.Editor ed = preferences.edit();
            ed.putBoolean("activity_executed", true);

            Intent in = new Intent(MainActivity.this, ServiceH.class);
            in.putExtra(KEY_ZA_SLANJE, resultReceiver);
            startService(in);
            ed.commit();
        }
        refreshList();
        //Klikom na sadrzaj list view-a , otvara se browser
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((Vesti) adapter.get(position)).getUrl()));
                startActivity(browserIntent);


            }
        });


        //Klik na dugme osvezi pokrece osvezavanje nezavisno od servisa
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                callService();


            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            stopService(intent);
        }catch (Exception e){}
    }

    /************************************************************************/
    /**
     * @brief <Pokrece servis>
     *
     * @param < Intent intent > - <parameter description>


     *************************************************************************/

    private void callService () {

        Toast.makeText(MainActivity.this, R.string.osvezavanje, Toast.LENGTH_SHORT).show();
        intent = new Intent(MainActivity.this, ServiceH.class);
        intent.putExtra(KEY_ZA_SLANJE, resultReceiver);
        //startService(intent);
        http = new HTTPZahtev(intent);
        http.getDataFromServer();
    }

    /****************************************************************************
     *
     * Copyright (c) 2016 Elektrotehnički fakultet
     * Patre 5, Banja Luka
     *
     * All Rights Reserved
     *
     * \file <MyResultReceiver>
     * \brief
     * <Klasa nasledjuje ResultReciever i sluzi da prima rezultat kada se osvezi baza, tako
     * da se rezultati mogu prikazati u list view
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
    class MyResultReceiver extends ResultReceiver {

        private Context context = null;

        protected void setParentContext (Context context) {
            this.context = context;
        }

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult (int resultCode, Bundle resultData) {

            // Code to process resultData here

            refreshList();


        }
    }

    //osvezava listu
    public synchronized void refreshList(){

            mDbHelper.fillArrayWithDB(adapter);
            sort(adapter);
            int broj = 0;
            list.setAdapter(new ListAdapterVesti(getApplicationContext(),
                    adapter));
    }

//Sortira niz
    public void sort(ArrayList<Vesti> array){

        Vesti temp;
        for(int i=0;i<array.size();i++)
            for(int j=i;j<array.size();j++)
                if(array.get(i).getDate()<array.get(j).getDate()) {
                    temp = array.get(i);
                    array.set(i,array.get(j));
                    array.set(j,temp);

                }

    }





}
