package com.nor2code.springboot.thymeleafdemo.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtils {

    public static String formatAmount(Number amount, int decimalPlaces) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(' ');

        String pattern = decimalPlaces > 0 // Ez a feltétel vizsgálja, hogy a decimalPlaces (tizedesjegyek száma) nagyobb-e nullánál.
                ? "#,##0." + "0".repeat(decimalPlaces) // Ez a ternáris operátor, vagyis az if-else rövid formája. #,##0.ez ezres tagolást és egész számot jelent. Ehhez hozzáfűz annyi nullát, amennyi tizedesjegy kell
                : "#,##0"; // Ez az else ágat jelöli a ternáris operátorban. Ha decimalPlaces nulla vagy kisebb, akkor csak az egész szám formátumot adja, ezres tagolással, tizedesjegyek nélkül.

        DecimalFormat formatter = new DecimalFormat(pattern, symbols);
        return formatter.format(amount);
    }

    // Számlaszám formázása 4-es csoportokba
    public static String formatAccountNumber(String accountNumber) {
        return accountNumber.replaceAll("(.{4})(?=.)", "$1-"); //.{4} = 4 karaktert vesz, (?=.) = csak akkor illeszt, ha utána még van karakter (így az utolsó után nem tesz szóközt), $1 = 4 karakter + szóköz, itt kötőjel
    }
}
