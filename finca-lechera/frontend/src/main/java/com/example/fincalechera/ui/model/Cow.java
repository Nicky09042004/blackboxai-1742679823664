package com.example.fincalechera.ui.model;

import javafx.beans.property.*;

public class Cow {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty numeroIdentificacion = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty raza = new SimpleStringProperty();
    private final IntegerProperty edad = new SimpleIntegerProperty();
    private final StringProperty fechaNacimiento = new SimpleStringProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty observaciones = new SimpleStringProperty();

    public Cow() {
    }

    public Cow(String id, String numeroIdentificacion, String nombre, String raza, 
               int edad, String fechaNacimiento, String estado, String observaciones) {
        setId(id);
        setNumeroIdentificacion(numeroIdentificacion);
        setNombre(nombre);
        setRaza(raza);
        setEdad(edad);
        setFechaNacimiento(fechaNacimiento);
        setEstado(estado);
        setObservaciones(observaciones);
    }

    // ID
    public String getId() {
        return id.get();
    }
    public StringProperty idProperty() {
        return id;
    }
    public void setId(String id) {
        this.id.set(id);
    }

    // Número de Identificación
    public String getNumeroIdentificacion() {
        return numeroIdentificacion.get();
    }
    public StringProperty numeroIdentificacionProperty() {
        return numeroIdentificacion;
    }
    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion.set(numeroIdentificacion);
    }

    // Nombre
    public String getNombre() {
        return nombre.get();
    }
    public StringProperty nombreProperty() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    // Raza
    public String getRaza() {
        return raza.get();
    }
    public StringProperty razaProperty() {
        return raza;
    }
    public void setRaza(String raza) {
        this.raza.set(raza);
    }

    // Edad
    public int getEdad() {
        return edad.get();
    }
    public IntegerProperty edadProperty() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad.set(edad);
    }

    // Fecha de Nacimiento
    public String getFechaNacimiento() {
        return fechaNacimiento.get();
    }
    public StringProperty fechaNacimientoProperty() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento.set(fechaNacimiento);
    }

    // Estado
    public String getEstado() {
        return estado.get();
    }
    public StringProperty estadoProperty() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    // Observaciones
    public String getObservaciones() {
        return observaciones.get();
    }
    public StringProperty observacionesProperty() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getNumeroIdentificacion(), getNombre());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cow cow = (Cow) o;
        return getId() != null && getId().equals(cow.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}