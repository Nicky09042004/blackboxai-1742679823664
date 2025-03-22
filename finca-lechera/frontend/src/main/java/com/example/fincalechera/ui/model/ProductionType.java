package com.example.fincalechera.ui.model;

public enum ProductionType {
    DIARIA("Diaria"),
    SEMANAL("Semanal"),
    MENSUAL("Mensual");

    private final String displayName;

    ProductionType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}