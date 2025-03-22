package com.example.fincalechera.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cow {
    private String id;
    private String nombre;
    private String raza;
    private int edad;
    private String fechaNacimiento;
    private String numeroIdentificacion;
    private String estado; // Activa, Inactiva, etc.
    private String observaciones;
    
    // Constructor con campos principales
    public Cow(String id, String nombre, String raza, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
    }
}