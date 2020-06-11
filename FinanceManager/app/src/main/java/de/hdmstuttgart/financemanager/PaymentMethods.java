package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class PaymentMethods {

   public static ArrayList<String> SpinnerList = new ArrayList<>();
    //Führt den Block instant aus, sobald die Klasse aufgerufen wird
    static {
        SpinnerList.add("Wählen Sie eine Zahlmethode...");
        SpinnerList.add("EC");
        SpinnerList.add("Bank");
        SpinnerList.add("Kreditkarte");
        SpinnerList.add("PayPal");
        SpinnerList.add("Überweisung");
        SpinnerList.add("Sonstige");
    }
}
