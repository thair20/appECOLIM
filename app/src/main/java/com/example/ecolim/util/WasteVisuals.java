package com.example.ecolim.util;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.ecolim.R;

/**
 * Mapea el nombre de una categoría de residuo (tabla waste_categories) a un
 * icono (emoji, sin necesidad de assets gráficos) y un color, para pintar los
 * círculos de las pantallas "Agregar Residuos" y "Detalle de Residuo".
 */
public final class WasteVisuals {

    private WasteVisuals() { }

    public static String iconFor(String categoryName) {
        if (categoryName == null) return "♻️";
        switch (categoryName) {
            case "Reciclable": return "♻️";
            case "Orgánico": return "🍎";
            case "Peligroso": return "☣️";
            case "Electrónico": return "💻";
            case "No aprovechable": return "🪵";
            default: return "♻️";
        }
    }

    public static int colorFor(Context context, String categoryName) {
        if (categoryName == null) return ContextCompat.getColor(context, R.color.dark_text);
        switch (categoryName) {
            case "Reciclable": return ContextCompat.getColor(context, R.color.cat_reciclable);
            case "Orgánico": return ContextCompat.getColor(context, R.color.cat_organico);
            case "Peligroso": return ContextCompat.getColor(context, R.color.cat_peligroso);
            case "Electrónico": return ContextCompat.getColor(context, R.color.cat_electronico);
            case "No aprovechable": return ContextCompat.getColor(context, R.color.cat_no_aprovechable);
            default: return ContextCompat.getColor(context, R.color.dark_text);
        }
    }
}
