package com.example.fincalechera.controller;

import com.example.fincalechera.model.MilkProduction;
import com.example.fincalechera.service.MilkProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productions")
@CrossOrigin(origins = "*")
public class MilkProductionController {

    private final MilkProductionService productionService;

    @Autowired
    public MilkProductionController(MilkProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping
    public ResponseEntity<List<MilkProduction>> getAllProductions() {
        return ResponseEntity.ok(productionService.getAllProductions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilkProduction> getProductionById(@PathVariable String id) {
        return ResponseEntity.ok(productionService.getProductionById(id));
    }

    @GetMapping("/cow/{cowId}")
    public ResponseEntity<List<MilkProduction>> getProductionsByCowId(@PathVariable String cowId) {
        return ResponseEntity.ok(productionService.getProductionsByCowId(cowId));
    }

    @PostMapping
    public ResponseEntity<MilkProduction> createProduction(@RequestBody MilkProduction production) {
        productionService.validateProduction(production);
        return new ResponseEntity<>(productionService.createProduction(production), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MilkProduction> updateProduction(
            @PathVariable String id,
            @RequestBody MilkProduction productionDetails) {
        productionService.validateProduction(productionDetails);
        return ResponseEntity.ok(productionService.updateProduction(id, productionDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduction(@PathVariable String id) {
        productionService.deleteProduction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily/{cowId}")
    public ResponseEntity<Double> getTotalProduccionDiaria(
            @PathVariable String cowId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(productionService.getTotalProduccionDiaria(cowId, fecha));
    }

    @GetMapping("/weekly/{cowId}")
    public ResponseEntity<Double> getTotalProduccionSemanal(
            @PathVariable String cowId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {
        return ResponseEntity.ok(productionService.getTotalProduccionSemanal(cowId, fechaInicio));
    }

    @GetMapping("/monthly/{cowId}")
    public ResponseEntity<Double> getTotalProduccionMensual(
            @PathVariable String cowId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {
        return ResponseEntity.ok(productionService.getTotalProduccionMensual(cowId, fechaInicio));
    }

    @GetMapping("/range")
    public ResponseEntity<List<MilkProduction>> getProductionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(productionService.getProductionsByDateRange(inicio, fin));
    }

    @GetMapping("/average/{cowId}")
    public ResponseEntity<Double> getPromedioProduccionDiaria(@PathVariable String cowId) {
        return ResponseEntity.ok(productionService.getPromedioProduccionDiaria(cowId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
            .badRequest()
            .body(Map.of("error", e.getMessage()));
    }
}