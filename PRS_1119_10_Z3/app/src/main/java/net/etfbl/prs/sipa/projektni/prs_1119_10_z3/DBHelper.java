package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <DBHelper>
 * \brief
 * <Klasa sluzi za komunikaciju baze i aplikacije>
 *
 * Created on <DATE(22.05.2017)>
 *
 * @Author <Milorad Šipovac 1119/10>
 *
\notes
 * <DATE(dd.mm.yyyy)> <SHORT DESCRIPTION>
 *
\history
 *
 **********************************************************************/
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "students.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "clanci";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NASLOV = "naslov";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_OPIS = "opis";
    public static final String COLUMN_SLIKA = "slika";
    public static final String COLUMN_DATUM = "datum";

    public SQLiteDatabase mDb = null;

    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper() {
        super(null, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NASLOV + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_DATUM + " LONG, " +
                COLUMN_SLIKA + " BLOB, " +
                COLUMN_OPIS + " TEXT);");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private SQLiteDatabase getDb() {
        if(mDb == null) {
            mDb = getWritableDatabase();
        }
        return mDb;
    }


    /************************************************************************/
    /**
     * @brief <Upisuje u bazu podataka>
     *
     * @param < Sqlite db > - <>
     * @param < byte[] bitmapdata> - <sliku snimamo kao niz byte-ova,zato moramo pretvoriti Drawable u byte[]>
     * @param < name> - <parameter description>


     *************************************************************************/
    public void insert(Vesti vesti) {
        SQLiteDatabase db = getDb();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NASLOV, vesti.getNaslov());
        values.put(COLUMN_URL, vesti.getUrl());
        values.put(COLUMN_OPIS, vesti.getOpis());
        values.put(COLUMN_DATUM, vesti.getDate());

        Drawable d=vesti.getSlika();

        byte []bitmapdata=getBytesFromDrawable(d);
        values.put(COLUMN_SLIKA,bitmapdata);

        vesti.setId(db.insert(TABLE_NAME, null, values));
    }

    /************************************************************************/
    /**
     * @brief <Vraca broj elemenata u bazi podataka>
     *
     * @param < Sqlite db > - <>
     * @param < Cursor > - <sliku snimamo kao niz byte-ova,zato moramo pretvoriti Drawable u byte[]>

     * @return <return i>

     *************************************************************************/
    public long size() {
        SQLiteDatabase db = getDb();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_OPIS, null);

        if (cursor.getCount() <= 0) {
            return 0;
        }
        long i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            i++;
        }

        return i;
    }

    /************************************************************************/
    /**
     * @brief <Prima kao parametar arrayListu i tu array listu popunjava elementima iz baze>
     *
     * @param < Sqlite db > - <>
     * @param < byte[] bitmapdata> - <sliku snimamo kao niz byte-ova,zato moramo pretvoriti Drawable u byte[]>
     * @param < name> - <parameter description>
     *
     * @return <return content>

     *************************************************************************/
    public void fillArrayWithDB(ArrayList<Vesti> arrayList){
       arrayList.clear();
        SQLiteDatabase db = getDb();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_OPIS, null);
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            arrayList .add(createStudent(cursor));
        }


    }




    public boolean isInBase(Vesti vesti){
        SQLiteDatabase db = getDb();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_OPIS, null);
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
           if(vesti.equals(createStudent(cursor)))
               return true;
        return false;


    }

    public Vesti nastarijaVest() {
        SQLiteDatabase db = getDb();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_OPIS, null);
        int i = 0;
        cursor.moveToFirst();
        Vesti najmanji=createStudent(cursor);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            if(najmanji.getDate() < createStudent(cursor).getDate())
                najmanji=createStudent(cursor);


        return najmanji;
    }

    /************************************************************************/
    /**
     * @brief <Vraca obekat Vesti  ciji je id prosledjen kao parametar >
     *
     * @param < Sqlite db > - <>
     * @param < Cursor cursor> - <sliku snimamo kao niz byte-ova,zato moramo pretvoriti Drawable u byte[]>

     *
     * @return <return Vesti>

     *************************************************************************/
    public Vesti readVesti(long id) {
        SQLiteDatabase db = getDb();

        Cursor cursor = db.query(TABLE_NAME, null, "id = ?",
                new String[] { Long.toString(id) }, null, null, null);

        cursor.moveToFirst();
        return createStudent(cursor);
    }

    /************************************************************************/
    /**
     * @brief <niz bajtova primljenih kao parametar pretvara u Drawable obekat>
     * @return <return Drawable>

     *************************************************************************/

    public static Drawable getDrawableFromBytes(byte[] imageBytes) {

        if (imageBytes != null)
            return new BitmapDrawable(BitmapFactory.decodeByteArray(imageBytes,
                    0, imageBytes.length));
        else
            return null;
    }
    /************************************************************************/
    /**
     * @brief <Obekat dipa drawable pretvara u niz byte-ova>
     *
     * @param < byte[] byteArray > - <>
     * @return <return byte[]>

     *************************************************************************/
    public static byte[] getBytesFromDrawable(Drawable drawable){

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return  byteArray;
    }

    //Iz baze se vraca Cursor, pa moramo pretvoriti u Vesti

    private Vesti createStudent(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        String naslov = cursor.getString(cursor.getColumnIndex(COLUMN_NASLOV));
        String url = cursor.getString(cursor.getColumnIndex(COLUMN_URL));
        String opis = cursor.getString(cursor.getColumnIndex(COLUMN_OPIS));
        byte[] d=cursor.getBlob(cursor.getColumnIndex(COLUMN_SLIKA));
        Long datum = cursor.getLong(cursor.getColumnIndex(COLUMN_DATUM));
        Drawable drawable=(Drawable)getDrawableFromBytes(d);
        return new Vesti( id,naslov,opis,drawable,url,datum );
    }

    //brise vest iz baze na osnovu obekta Vest
    public void delete (Vesti vesti){
        SQLiteDatabase db =getWritableDatabase();
        db.delete(TABLE_NAME,"id = ?", new String[]{Long.toString(vesti.getId())});
    }
//brise vest iz baze na osnovu id
    public void delete (long id){
        SQLiteDatabase db =getWritableDatabase();
        db.delete(TABLE_NAME,"id = ?", new String[]{Long.toString(id)});
    }

}

