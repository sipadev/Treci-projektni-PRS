package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <HTTPZahtev>
 *     InerClass HTTPThread
 * \brief
 * <Klasa služi da bi se povezala sa sajtom i preuzimala JSON obekte
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
public class HTTPZahtev {


    private static final String COMMENTS_URL =
            "https://newsapi.org/v1/articles?source=ars-technica&sortBy=top&apiKey=8fe851d4ca32483fb774df49ba7b7000";

    ResultReceiver resultReceiver;
    private static final String TAG_CVOR = "articles";
    private static final String TAG_NASLOV = "title";
    private static final String TAG_OPIS = "description";
    private static final String TAG_URL ="url";
    private static final String TAG_URL_TO_IMAGE ="urlToImage";
    private static final String TAG_TIME ="publishedAt";
    private static final int INTEGER_MAX=100;
    private Intent intent;
    HTTPZahtev(Intent intent){
        this.intent=intent;
        resultReceiver = intent.getParcelableExtra(MainActivity.KEY_ZA_SLANJE);
    }

    /************************************************************************/
    /**
     * @brief <Pokrece HttpThread>
     *
     * @param < HttpThread  > - <parameter description>


     *************************************************************************/
    public void getDataFromServer(){
        // Start Http request thread
        new HttpThread().start();
    }

    /************************************************************************/
    /**
     * @brief <Preuzima JSON objekte i smesta ih u bazu ako ih nema, proverava da li ima
     * manje od 100 elemenata u bazi,u suprotnom brise da bude 100,a zatim upisuje u bazu>
     *@param < JSON ob > - <parameter description>
     * @param < String  naslov> - <parameter description>
     * @param < String  opis > - <parameter description>
     * @param < String  url > - <parameter description>
     * @param < String  datum > - <parameter description>
     * @param < Drawable  slika > - <parameter description>
*
     *************************************************************************/
    public void parseResponse(String message) {
        ArrayList<Vesti> commentList = new ArrayList<>();

        try {

            JSONObject ob = new JSONObject(message);

            JSONArray comments = (JSONArray) ob.get(TAG_CVOR);





            for (int i = 0; i < comments.length(); i++) {
                JSONObject c = comments.getJSONObject(i);

                int id = i;
                String naslov = c.getString(TAG_NASLOV);

                String opis = c.getString(TAG_OPIS);

                String urlZaOtvaranje = c.getString(TAG_URL);
                String url = c.getString(TAG_URL_TO_IMAGE);
                 String datum= c.getString(TAG_TIME);

                Drawable slika = Imagehandler(url);
                Vesti vest = new Vesti(naslov, opis,slika, urlZaOtvaranje,stringUDatum(datum).getTime());

                synchronized (MainActivity.mDbHelper) {

                    if (!MainActivity.mDbHelper.isInBase(vest)) {

                        if(MainActivity.mDbHelper.size()==INTEGER_MAX)
                            MainActivity.mDbHelper.delete(MainActivity.mDbHelper.nastarijaVest());
                        MainActivity.mDbHelper.insert(vest);
                    }
                }

                // adding contact to contact list

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /************************************************************************/
    /**
     * @brief <Parsiranje stringa da bi se napravio obekat datum>
     *
     * @param < String vreme > - <Slika>
     * @param < int  h> - <parameter description>
     * @param < int m> - <parameter description>
     * @param < int s> - <parameter description>
     * @param < int d> - <parameter description>
     * @param < int mesec> - <parameter description>
     * @param < int g> - <parameter description>
     * @param < Date > - <parameter description>
     * @return <return Date>

     *************************************************************************/
    private Date stringUDatum(String s){
        String vreme=s.split("T")[1];
         vreme=vreme.split("Z")[0];
        String datum=s.split("T")[0];
        int h=Integer.parseInt(vreme.split(":")[0]);
        int m=Integer.parseInt(vreme.split(":")[1]);
        int ss=Integer.parseInt(vreme.split(":")[2]);

        int d=Integer.parseInt(datum.split("-")[2]);
        int mesec=Integer.parseInt(datum.split("-")[1]);
        int g=Integer.parseInt(datum.split("-")[0]);

        return new Date(g,mesec,d,h,m,ss);



    }

    /************************************************************************/
    /**
     * @brief <Kada se preuzme url ,koji je putanja do slike, pomogu ove metode dohvatimo tu sliku>
     *
     * @param < Drawable d > - <Slika>
     * @param < name> - <parameter description>
     * @param < name> - <parameter description>
     *
     * @return <return Drawable>

     *************************************************************************/
    protected Drawable Imagehandler(String url) {
        Drawable d;
        try {
            url=url.replaceAll(" ", "%20");
            InputStream is = (InputStream)this.fetch(url);
            d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e)
        {
            d=MainActivity.adapter.get(0).getSlika();
            return d;
        }
        catch (IOException e)
        {
            d=MainActivity.adapter.get(0).getSlika();
            return d;
        }
    }

    /************************************************************************/
    /**
     * @brief <Prima string(url adresu) i pravi obekat URL>
     *
     * @param < Drawable d > - <Slika>

     * @return <return content>

     *************************************************************************/
    protected Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    private class HttpThread extends Thread {

        // After call for background.start this run method call
        public void run() {

            String response = "";

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(COMMENTS_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String data;
                while ((data = bufferedReader.readLine()) != null) {
                    response += data + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } finally {
                urlConnection.disconnect();
            }

            parseResponse(response);
            try {
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.KEY_ZA_SLANJE, MainActivity.KEY_STUDENT_ID);

                resultReceiver.send(200, bundle);
            } catch (Exception e) {
            }
        }
    }

}
