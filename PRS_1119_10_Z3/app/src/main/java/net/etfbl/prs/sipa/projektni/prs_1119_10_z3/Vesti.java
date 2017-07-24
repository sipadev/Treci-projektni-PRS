package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <ListAdapterVesti>
 * \brief
 <Klasa napravljena da služi kao model
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
import android.graphics.drawable.Drawable;



public class Vesti {

    private Drawable slika;
    private String naslov;
    private String opis;
    private String url;



    private long id;
    private long date;

    public Vesti(Long id, String naslov, String opis,Drawable slika,String url,long date) {
        this.slika = slika;
        this.naslov = naslov;
        this.opis = opis;
        this.url = url;
        this.id = id;
        this.date = date;
    }


    public Vesti( String naslov, String opis,Drawable slika,String url,long date) {
        this.slika = slika;
        this.naslov = naslov;
        this.opis = opis;
        this.url=url;
        this.date=date;
    }
    public long getDate() {
        return  date;
    }
    public boolean equals(Vesti vesti){
    return this.url.equals(vesti.getUrl());
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {

        return url;
    }




    public Drawable getSlika() {
        return slika;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getOpis() {
        return opis;
    }
}
