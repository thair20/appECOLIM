package com.example.ecolim.model;

/** Modelo que representa la tabla "locations". */
public class Location {
    public long idLocation;
    public long idClient;
    public String name;
    public String address;
    public String reference;
    public double latitude;
    public double longitude;
    public boolean isActive;

    public Location() { }

    public Location(long idLocation, long idClient, String name) {
        this.idLocation = idLocation;
        this.idClient = idClient;
        this.name = name;
    }
}
