package de.hdmstuttgart.financemanager.Helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CurrencyFormatter {

    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00",  new DecimalFormatSymbols(Locale.US));
        return formatter.format(Double.parseDouble(number));
    }
}
