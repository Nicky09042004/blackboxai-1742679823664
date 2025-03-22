package com.example.fincalechera.ui.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class MilkProduction {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty cowId = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> fecha = new SimpleObjectProperty<>();
    private final DoubleProperty cantidad = new SimpleDoubleProperty();
    private final ObjectProperty<ProductionType> tipoProduccion = new SimpleObjectProperty<>();
    private final StringProperty observaciones = new SimpleStringProperty();
    private final StringProperty cowInfo = new SimpleStringProperty();

    public MilkProduction() {
    }

    public MilkProduction(String id, String cowId, LocalDateTime fecha, double cantidad, 
                         ProductionType tipoProduccion, String observaciones, String cowInfo) {
        setId(id);
        setCowId(cowId);
        setFecha(fecha);
        setCantidad(cantidad);
        setTipoProduccion(tipoProduccion);
        setObservaciones(observaciones);
        setCowInfo(cowInfo);
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

    // Cow ID
    public String getCowId() {
        return cowId.get();
    }
    public StringProperty cowIdProperty() {
        return cowId;
    }
    public void setCowId(String cowId) {
        this.cowId.set(cowId);
    }

    // Fecha
    public LocalDateTime getFecha() {
        return fecha.get();
    }
    public ObjectProperty<LocalDateTime> fechaProperty() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha.set(fecha);
    }

    // Cantidad
    public double getCantidad() {
        return cantidad.get();
    }
    public DoubleProperty cantidadProperty() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    // Tipo de Producción
    public ProductionType getTipoProduccion() {
        return tipoProduccion.get();
    }
    public ObjectProperty<ProductionType> tipoProduccionProperty() {
        return tipoProduccion;
    }
    public void setTipoProduccion(ProductionType tipoProduccion) {
        this.tipoProduccion.set(tipoProduccion);
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

    // Cow Info (for display purposes)
    public String getCowInfo() {
        return cowInfo.get();
    }
    public StringProperty cowInfoProperty() {
        return cowInfo;
    }
    public void setCowInfo(String cowInfo) {
        this.cowInfo.set(cowInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MilkProduction that = (MilkProduction) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Producción: %s - %s - %.2f L", 
            getCowInfo(), getFecha(), getCantidad());
    }
}