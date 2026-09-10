package com.example.ecolim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda en memoria los datos de la recolección que el usuario está armando
 * (pantallas "Nueva Recolección" -> "Agregar Residuos" -> "Detalle de Residuo"
 * -> "Resumen" -> "Confirmación"). Se usa un singleton en vez de pasar todo por
 * Intent extras porque la lista de residuos crece dinámicamente.
 * Se limpia con reset() apenas se confirma o cancela la recolección.
 */
public class RecoleccionSession {

    private static RecoleccionSession instance;

    public long idEmployee;
    public long idClient;
    public String clientName;
    public long idLocation;
    public String locationName;
    public String date;       // dd/MM/yyyy
    public String startTime;  // hh:mm a
    public String observations = "";
    public final List<DetalleItem> items = new ArrayList<>();

    private RecoleccionSession() { }

    public static RecoleccionSession getInstance() {
        if (instance == null) {
            instance = new RecoleccionSession();
        }
        return instance;
    }

    public double getTotalKg() {
        double total = 0;
        for (DetalleItem item : items) {
            total += item.quantity;
        }
        return total;
    }

    public void reset() {
        idClient = 0;
        clientName = null;
        idLocation = 0;
        locationName = null;
        date = null;
        startTime = null;
        observations = "";
        items.clear();
    }
}
