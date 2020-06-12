package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class PaymentMethods {

    public static ArrayList<String> methodSpinnerMain = new ArrayList<>();
    public static ArrayList<String> methodSpinnerDetail = new ArrayList<>();
    //Führt den Block instant aus, sobald die Klasse aufgerufen wird
    static {
        methodSpinnerMain.add("Zahlmethode wählen...");
        methodSpinnerMain.add("-----");
        methodSpinnerMain.add("EC");
        methodSpinnerMain.add("Bar");
        methodSpinnerMain.add("Bank");
        methodSpinnerMain.add("Kreditkarte");
        methodSpinnerMain.add("PayPal");
        methodSpinnerMain.add("Überweisung");
        methodSpinnerMain.add("Lastschrift");
        methodSpinnerMain.add("Sonstige");

        methodSpinnerDetail.add("EC");
        methodSpinnerDetail.add("Bar");
        methodSpinnerDetail.add("Bank");
        methodSpinnerDetail.add("Kreditkarte");
        methodSpinnerDetail.add("PayPal");
        methodSpinnerDetail.add("Überweisung");
        methodSpinnerDetail.add("Lastschrift");
        methodSpinnerDetail.add("Sonstige");
    }
}
