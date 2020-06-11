package de.hdmstuttgart.financemanager.Helper;

import java.text.DecimalFormat;

public class CurrencyFormatter {

    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(number));
    }
}
