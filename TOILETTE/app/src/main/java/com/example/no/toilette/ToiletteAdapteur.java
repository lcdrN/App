package com.example.no.toilette;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E146294Q on 25/03/16.
 */
public class ToiletteAdapteur extends ArrayAdapter<Toilette> {


    public ToiletteAdapteur(Activity context, ArrayList<Toilette> items) {
        super(context, R.layout.activity_main, items);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        // instantiation d’un View correspondant a notre fichier de layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        row = inflater.inflate(R.layout.toilettelayout, null);
        // personalisation de la vue
        Toilette toilette = getItem(position);
        System.out.println(toilette.distanceToString());
        TextView distance = (TextView) row.findViewById(R.id.nom);
        distance.setText(toilette.distanceToString());
        TextView adresse = (TextView) row.findViewById(R.id.adresse);
        adresse.setText(toilette.getAdresse());
        TextView commune = (TextView) row.findViewById(R.id.commune);
        commune.setText(toilette.getCommune());
        return (row);

    }

    public void sort() {
        super.sort(new Comparateur());
        notifyDataSetChanged();
    }


}