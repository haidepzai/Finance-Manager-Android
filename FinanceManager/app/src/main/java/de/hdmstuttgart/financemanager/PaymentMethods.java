package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class PaymentMethods {

    public static ArrayList<String> MainSpinnerList = new ArrayList<>();
    public static ArrayList<String> DetailSpinnerList = new ArrayList<>();
    //Führt den Block instant aus, sobald die Klasse aufgerufen wird
    static {
        MainSpinnerList.add("Zahlmethode wählen...");
        MainSpinnerList.add("-----");
        MainSpinnerList.add("EC");
        MainSpinnerList.add("Bank");
        MainSpinnerList.add("Kreditkarte");
        MainSpinnerList.add("PayPal");
        MainSpinnerList.add("Überweisung");
        MainSpinnerList.add("Sonstige");

        DetailSpinnerList.add("EC");
        DetailSpinnerList.add("Bank");
        DetailSpinnerList.add("Kreditkarte");
        DetailSpinnerList.add("PayPal");
        DetailSpinnerList.add("Überweisung");
        DetailSpinnerList.add("Sonstige");

    }
}
