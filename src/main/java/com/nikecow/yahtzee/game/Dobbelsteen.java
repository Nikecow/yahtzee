package com.nikecow.yahtzee.game;
import java.util.Random;

/**
 * Created by nicov on 30-5-2016.
 */
public class Dobbelsteen {
    private static final Random rand = new Random();
    private final int aantalOgen;

    public Dobbelsteen(int aantalOgen) {
        this.aantalOgen = aantalOgen;
    }

    public int werpDobbelsteen() {
        int worp = 0;
        return (worp = rand.nextInt(aantalOgen) + 1);
    }
}
