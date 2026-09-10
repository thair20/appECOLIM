package com.example.ecolim.model;

/**
 * Representa una fila de "collection_details" mientras el usuario arma la
 * recolección en pantalla (aún no se guarda en SQLite hasta el paso Confirmación).
 */
public class DetalleItem {
    public long idWasteType;
    public String wasteTypeName;
    public String categoryName;
    public double quantity;
    public String unit = "kg";
    public String observations = "";

    public DetalleItem(long idWasteType, String wasteTypeName, String categoryName, double quantity) {
        this.idWasteType = idWasteType;
        this.wasteTypeName = wasteTypeName;
        this.categoryName = categoryName;
        this.quantity = quantity;
    }
}
