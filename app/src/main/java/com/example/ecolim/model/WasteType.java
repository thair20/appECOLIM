package com.example.ecolim.model;

/** Modelo que representa la tabla "waste_types" unida (JOIN) con "waste_categories". */
public class WasteType {
    public long idWasteType;
    public long idCategory;
    public String name;
    public String categoryName;
    public String description;
    public boolean isActive;

    public WasteType() { }

    public WasteType(long idWasteType, long idCategory, String name, String categoryName) {
        this.idWasteType = idWasteType;
        this.idCategory = idCategory;
        this.name = name;
        this.categoryName = categoryName;
    }
}
