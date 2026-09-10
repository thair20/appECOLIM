package com.example.ecolim.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ECOLIM guarda las fechas en SQLite como texto en formato ISO "yyyy-MM-dd"
 * porque ese formato SÍ se puede comparar/ordenar correctamente con
 * operadores de texto (BETWEEN, ORDER BY) en SQLite. En pantalla, en
 * cambio, siempre se muestra en formato peruano "dd/MM/yyyy".
 */
public final class DateUtil {

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DISPLAY = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private DateUtil() { }

    public static String isoFromDate(Date date) {
        return ISO.format(date);
    }

    public static String displayFromDate(Date date) {
        return DISPLAY.format(date);
    }

    public static String isoToDisplay(String iso) {
        if (iso == null) return "";
        try {
            return DISPLAY.format(ISO.parse(iso));
        } catch (ParseException e) {
            return iso;
        }
    }

    public static String todayIso() {
        return ISO.format(new Date());
    }
}
