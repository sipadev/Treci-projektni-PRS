package net.etfbl.prs.sipa.projektni.prs_1119_10_z3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/****************************************************************************
 *
 * Copyright (c) 2016 Elektrotehnički fakultet
 * Patre 5, Banja Luka
 *
 * All Rights Reserved
 *
 * \file <ListAdapterVesti>
 * \brief
 * <Klasa nasledjuje BaseAdapter i napravljena je da bismo mogli prikazati vesti koje se nalaze u listi.
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
public class ListAdapterVesti extends BaseAdapter {

    private Context sadrzaj;
    public ArrayList<Vesti> lista;

    public ListAdapterVesti(Context context) {
        lista = new ArrayList<Vesti>();
        sadrzaj = context;
    }

    public ListAdapterVesti(Context context,ArrayList<Vesti> lista) {

        sadrzaj = context;
        this.lista=lista;
    }

/************************************************************************/
    /**
     * @brief <Metoda kao parametar prima obekat tipa Vesti i taj obekat dodaje u ArrazList pod nazivom lista >
     *
     * @param < Vesti stvar> - <Prosledjen kao parametar metode>
     *************************************************************************/
    public void prosledi(Vesti stvar) {
        lista.add(stvar);
    }




    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = lista.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return rv;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) sadrzaj.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item, null);
        }

        TextView name = (TextView) view.findViewById(R.id.naslov);
        TextView opis = (TextView) view.findViewById(R.id.opis);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);


        name.setText(lista.get(position).getNaslov());
        opis.setText(lista.get(position).getOpis());
        image.setImageDrawable(lista.get(position).getSlika());

        return view;
    }
}

