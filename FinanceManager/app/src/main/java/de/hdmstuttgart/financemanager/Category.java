package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class Category {

    public static ArrayList<String> categorySpinnerMain = new ArrayList<>();
    public static ArrayList<String> categorySpinnerDetail = new ArrayList<>();

    public static ArrayList<String> incomingTypeSpinnerMain = new ArrayList<>();
    public static ArrayList<String> incomingTypeSpinnerDetail = new ArrayList<>();

    //Führt den Block instant aus, sobald die Klasse aufgerufen wird
    static {
        categorySpinnerMain.add("Kategorie auswählen...");
        categorySpinnerMain.add("-----");
        categorySpinnerMain.add("Einkauf");
        categorySpinnerMain.add("Essen");
        categorySpinnerMain.add("Studium/Beruf");
        categorySpinnerMain.add("Freizeit");
        categorySpinnerMain.add("Gebühren");
        categorySpinnerMain.add("Sonstige");

        categorySpinnerDetail.add("Einkauf");
        categorySpinnerDetail.add("Essen");
        categorySpinnerDetail.add("Studium/Beruf");
        categorySpinnerDetail.add("Freizeit");
        categorySpinnerDetail.add("Gebühren");
        categorySpinnerDetail.add("Sonstige");

        incomingTypeSpinnerMain.add("Kategorie auswählen...");
        incomingTypeSpinnerMain.add("-----");
        incomingTypeSpinnerMain.add("Lohn/Gehalt");
        incomingTypeSpinnerMain.add("Geschenk");
        incomingTypeSpinnerMain.add("Sonstige");

        incomingTypeSpinnerDetail.add("Lohn/Gehalt");
        incomingTypeSpinnerDetail.add("Geschenk");
        incomingTypeSpinnerDetail.add("Sonstige");
    }
}
