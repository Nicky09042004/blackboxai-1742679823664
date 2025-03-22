package com.example.fincalechera.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilkProduction {
    private String id;
    private String cowId;
    private LocalDateTime fecha;
    private double cantidad;
    private ProductionType tipoProduccion;
    private String observaciones;
    
    // Constructor con campos principales
    public MilkProduction(String id, String cowId, LocalDateTime fecha, double cantidad, ProductionType tipoProduccion) {
        this.id = id;
        this.cowId = cowId;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipoProduccion = tipoProduccion;
    }
}