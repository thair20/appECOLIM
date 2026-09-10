package com.example.ecolim.model;

/**
 * Modelo que representa la tabla "employees" del modelo de datos.
 * Nota: el campo "password" NO está en el diagrama entidad-relación original;
 * se agregó como mínimo necesario para poder validar el inicio de sesión,
 * ya que el modelo entregado no incluía credenciales de autenticación.
 */
public class Employee {
    public long idEmployee;
    public String dni;
    public String firstName;
    public String lastName;
    public String phone;
    public String email;
    public String password;
    public String role;
    public boolean isActive;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
