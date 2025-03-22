package com.example.fincalechera.service;

import com.example.fincalechera.exception.ResourceNotFoundException;
import com.example.fincalechera.model.MilkProduction;
import com.example.fincalechera.model.ProductionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MilkProductionService {
    
    private final ExcelStorageService excelStorageService;
    private final CowService cowService;
    private List<MilkProduction> productions;

    @Autowired
    public MilkProductionService(ExcelStorageService excelStorageService, CowService cowService) {
        this.excelStorageService = excelStorageService;
        this.cowService = cowService;
        this.productions = new ArrayList<>();
    }

    public List<MilkProduction> getAllProductions() {
        return productions;
    }

    public MilkProduction getProductionById(String id) {
        return productions.stream()
                .filter(prod -> prod.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Producción no encontrada con ID: " + id));
    }

    public List<MilkProduction> getProductionsByCowId(String cowId) {
        // Verificar que la vaca existe
        cowService.getCowById(cowId);
        return productions.stream()
                .filter(prod -> prod.getCowId().equals(cowId))
                .collect(Collectors.toList());
    }

    public MilkProduction createProduction(MilkProduction production) {
        // Verificar que la vaca existe
        cowService.getCowById(production.getCowId());
        
        production.setId(UUID.randomUUID().toString());
        productions.add(production);
        return production;
    }

    public MilkProduction updateProduction(String id, MilkProduction productionDetails) {
        MilkProduction production = getProductionById(id);
        
        // Verificar que la vaca existe si se está cambiando
        if (!production.getCowId().equals(productionDetails.getCowId())) {
            cowService.getCowById(productionDetails.getCowId());
        }
        
        production.setCowId(productionDetails.getCowId());
        production.setFecha(productionDetails.getFecha());
        production.setCantidad(productionDetails.getCantidad());
        production.setTipoProduccion(productionDetails.getTipoProduccion());
        production.setObservaciones(productionDetails.getObservaciones());
        
        return production;
    }

    public void deleteProduction(String id) {
        MilkProduction production = getProductionById(id);
        productions.remove(production);
    }

    public double getTotalProduccionDiaria(String cowId, LocalDate fecha) {
        return productions.stream()
                .filter(prod -> prod.getCowId().equals(cowId) &&
                        prod.getFecha().toLocalDate().equals(fecha) &&
                        prod.getTipoProduccion() == ProductionType.DIARIA)
                .mapToDouble(MilkProduction::getCantidad)
                .sum();
    }

    public double getTotalProduccionSemanal(String cowId, LocalDate fechaInicio) {
        LocalDate fechaFin = fechaInicio.plusDays(7);
        return productions.stream()
                .filter(prod -> prod.getCowId().equals(cowId) &&
                        !prod.getFecha().toLocalDate().isBefore(fechaInicio) &&
                        prod.getFecha().toLocalDate().isBefore(fechaFin) &&
                        prod.getTipoProduccion() == ProductionType.SEMANAL)
                .mapToDouble(MilkProduction::getCantidad)
                .sum();
    }

    public double getTotalProduccionMensual(String cowId, LocalDate fechaInicio) {
        LocalDate fechaFin = fechaInicio.plusMonths(1);
        return productions.stream()
                .filter(prod -> prod.getCowId().equals(cowId) &&
                        !prod.getFecha().toLocalDate().isBefore(fechaInicio) &&
                        prod.getFecha().toLocalDate().isBefore(fechaFin) &&
                        prod.getTipoProduccion() == ProductionType.MENSUAL)
                .mapToDouble(MilkProduction::getCantidad)
                .sum();
    }

    public List<MilkProduction> getProductionsByDateRange(LocalDateTime inicio, LocalDateTime fin) {
        return productions.stream()
                .filter(prod -> !prod.getFecha().isBefore(inicio) && !prod.getFecha().isAfter(fin))
                .collect(Collectors.toList());
    }

    public double getPromedioProduccionDiaria(String cowId) {
        List<MilkProduction> produccionesDiarias = productions.stream()
                .filter(prod -> prod.getCowId().equals(cowId) &&
                        prod.getTipoProduccion() == ProductionType.DIARIA)
                .collect(Collectors.toList());

        if (produccionesDiarias.isEmpty()) {
            return 0.0;
        }

        double totalProduccion = produccionesDiarias.stream()
                .mapToDouble(MilkProduction::getCantidad)
                .sum();

        return totalProduccion / produccionesDiarias.size();
    }

    public void validateProduction(MilkProduction production) {
        if (production.getCowId() == null || production.getCowId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la vaca no puede estar vacío");
        }
        if (production.getFecha() == null) {
            throw new IllegalArgumentException("La fecha de producción no puede estar vacía");
        }
        if (production.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad de producción debe ser mayor que 0");
        }
        if (production.getTipoProduccion() == null) {
            throw new IllegalArgumentException("El tipo de producción no puede estar vacío");
        }
    }
}