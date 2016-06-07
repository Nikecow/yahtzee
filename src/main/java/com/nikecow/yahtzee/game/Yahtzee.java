package com.nikecow.yahtzee.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicov on 30-5-2016.
 */


public class Yahtzee {
    public static final int maxAantalRonden = 13;
    public static final int aantalStenen = 5;
    public static final int aantalOgen = 6;
    public int maxAantalWorpen = 3;
    private int huidigAantalWorpen;
    private int huidigeRonde;
    private int totaal;
    private int aantalExtraYahtzee;
    private ArrayList<Dobbelsteen> dobbelStenen = new ArrayList<>();
    private List<Integer> worpen = new ArrayList<>(Collections.nCopies(aantalStenen, 0));
    private List<Integer> selectedCategories = new ArrayList<>();
    private ArrayList<Integer> scores = new ArrayList<>(Collections.nCopies(maxAantalRonden, 0));

    public Yahtzee() {
        for (int i = 0; i < aantalStenen; i++) {
            dobbelStenen.add(new Dobbelsteen(aantalOgen));
        }
    }

    private static List<Integer> berekenFrequentie(List<Integer> worpen) {
        List<Integer> frequenties = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            //  System.out.println("index: " + i +"maal: " +(Collections.frequency(worpen, i)));
            frequenties.add(Collections.frequency(worpen, i));
        }
        return frequenties;
    }

    public List<Integer> gooiDobbelstenen(ArrayList<Integer> excluded) {
        for (int i = 0; i < dobbelStenen.size(); i++) {
            if (!excluded.contains(i)) {
                worpen.set(i, dobbelStenen.get(i).werpDobbelsteen());
            }
        }
        huidigAantalWorpen++;
        //System.out.println(huidigAantalWorpen);
        return worpen;
    }

    public void selectCategory(Integer buttonID) {
        yahtzeeRoll();
        selectedCategories.add(buttonID);
        berekenTotaal();
    }

    private void yahtzeeRoll() {
        List<Integer> frequenties = berekenFrequentie(worpen);
        if (selectedCategories.contains(11) && frequenties.contains(5) && scores.get(11) > 0) { // Alleen bonus als Yahtzee niet 0 is.
            aantalExtraYahtzee++;
            scores.set(11, 50 + (aantalExtraYahtzee * 100));
            // System.out.println("Extra yahtzee");
        }
    }

    public List<Integer> getSelectedCategories() {
        return selectedCategories;
    }

    public int getHuidigAantalWorpen() {
        return (huidigAantalWorpen);
    }

    public void resetHuidigAantalWorpen() {
        huidigAantalWorpen = 0;
    }

    private void resetHuidigRonde() {
        huidigeRonde = 0;
    }

    public int getHuidigRonde() {
        return (huidigeRonde);
    }

    public void addHuidigRonde() {
        huidigeRonde++;
    }

    private void resetTotaal() {
        totaal = 0;
    }

    private void resetSelectedCategories() {
        selectedCategories.clear();
    }

    public ArrayList<Integer> berekenWorp(List<Integer> worpen) {
        List<Integer> frequenties = berekenFrequentie(worpen);
        berekenCategorie(frequenties);
        return getScores();
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public int getTotaal() {
        return totaal;
    }

    private void berekenTotaal() {
        int upperScore = 0;
        int lowerScore = 0;
        int bonus = 0;
        totaal = 0;
        for (int i = 0; i < selectedCategories.size(); i++) {
            Integer cat = selectedCategories.get(i);
            if (cat < 6) {
                upperScore += scores.get(cat);
            } else {
                lowerScore += scores.get(cat);
                //System.out.println("Cat: " + cat + " score: " + scores.get(cat));
            }
        }
        // Bonus upper score
        if (upperScore >= 63) {
            bonus = 35;
        }
        totaal = (upperScore + lowerScore + bonus);
        //System.out.println("Upper: " + upperScore + " Lower: " + lowerScore + " Bonus: " + bonus + " Total: " + totaal);
    }

    public void resetGame() {
        resetHuidigAantalWorpen();
        resetHuidigRonde();
        resetTotaal();
        resetSelectedCategories();
        resetAantalYahtzee();
    }

    private void resetAantalYahtzee() {
        aantalExtraYahtzee = 0;
    }

    private void berekenCategorie(List<Integer> frequenties) {
        int enen = frequenties.get(1);
        int tweeen = frequenties.get(2);
        int drieen = frequenties.get(3);
        int vieren = frequenties.get(4);
        int vijven = frequenties.get(5);
        int zessen = frequenties.get(6);
        int highestFreq = Collections.max(frequenties);
        boolean jokerYahtzee = false;
        // Bereken som van ogen
        int total = worpen.stream().mapToInt(Integer::intValue).sum();

        if (selectedCategories.contains(11) && frequenties.contains(5)) { // De Yahtzee bonussesn (joker en +100)
            int upperEquivalent = frequenties.indexOf(5) - 1; // Joker mag alleen als equivalente categorie al ingevuld is
            if (selectedCategories.contains(upperEquivalent)) {
                jokerYahtzee = true;
                System.out.println("joker: " + jokerYahtzee);
            }

        }

        for (int i = 0; i < scores.size(); i++) {
            // Wijzig niet de behouden categorieen behalve yahtzee;
            if (selectedCategories.contains(i)) {
                // System.out.println("bewaar: " + i);
                continue;
            }
            switch (i) {
                case 0: // Enen
                    scores.set(i, enen);
                    break;
                case 1: // Tweeen
                    scores.set(i, tweeen * 2);
                    break;
                case 2: // Drieen
                    scores.set(i, drieen * 3);
                    break;
                case 3: // Vieren
                    scores.set(i, vieren * 4);
                    break;
                case 4: // Vijven
                    scores.set(i, vijven * 5);
                    break;
                case 5: // Zessen
                    scores.set(i, zessen * 6);
                    break;
                case 6: // Drie dezelfde
                    if (highestFreq >= 3) {
                        scores.set(i, total);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 7: // Carre
                    if (highestFreq >= 4) {
                        scores.set(i, total);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 8: // Full house
                    if (jokerYahtzee || frequenties.containsAll(Arrays.asList(2, 3))) {
                        scores.set(i, 25);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 9: // Kleine straat
                    if (jokerYahtzee || (Collections.frequency(frequenties, 0) <= 3 && ((enen >= 1 && tweeen >= 1 && drieen >= 1 && vieren >= 1) || (tweeen >= 1 && drieen >= 1 && vieren >= 1 && vijven >= 1) || (drieen >= 1 && vieren >= 1 && vijven >= 1 && zessen >= 1)))) {
                        scores.set(i, 30);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 10: // Grote straat
                    if (jokerYahtzee || (Collections.frequency(frequenties, 0) <= 2 && (enen == 0 || zessen == 0))) {
                        scores.set(i, 40);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 11:// Yahtzee
                    if (frequenties.contains(5)) {
                        scores.set(i, 50);
                    } else {
                        scores.set(i, 0);
                    }
                    break;
                case 12: // Chance
                    scores.set(i, total);
                    break;
            }
        }
    }
}

