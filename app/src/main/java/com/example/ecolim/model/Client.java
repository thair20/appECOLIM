package com.example.ecolim.model;

/** Modelo que representa la tabla "clients". */
public class Client {
    public long idClient;
    public String ruc;
    public String name;
    public String address;
    public String phone;
    public String email;
    public String contactPerson;
    public boolean isActive;

    public Client() { }

    public Client(long idClient, String name) {
        this.idClient = idClient;
        this.name = name;
    }
}
