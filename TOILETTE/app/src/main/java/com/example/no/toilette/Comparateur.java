package com.example.no.toilette;

import java.util.Comparator;

/**
 * Created by Noé on 04/04/2016.
 */
public class Comparateur implements Comparator<Toilette> {
    @Override
    public int compare(Toilette t1, Toilette t2) {
        float libelle1 = t1.getDistance();
        float libelle2 = t2.getDistance();
        if (libelle1 >= libelle2) return 1;
        else return -1;
    }
}
