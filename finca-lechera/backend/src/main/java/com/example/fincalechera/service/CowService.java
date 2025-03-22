package com.example.fincalechera.service;

import com.example.fincalechera.exception.ResourceNotFoundException;
import com.example.fincalechera.model.Cow;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CowService {
    
    private final ExcelStorageService excelStorageService;
    private List<Cow> cows;

    @Autowired
    public CowService(ExcelStorageService excelStorageService) {
        this.excelStorageService = excelStorageService;
        this.cows = new ArrayList<>();
    }

    public List<Cow> getAllCows() {
        return cows;
    }

    public Cow getCowById(String id) {
        return cows.stream()
                .filter(cow -> cow.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Vaca no encontrada con ID: " + id));
    }

    public Cow createCow(Cow cow) {
        cow.setId(UUID.randomUUID().toString());
        cows.add(cow);
        return cow;
    }

    public Cow updateCow(String id, Cow cowDetails) {
        Cow cow = getCowById(id);
        
        cow.setNombre(cowDetails.getNombre());
        cow.setRaza(cowDetails.getRaza());
        cow.setEdad(cowDetails.getEdad());
        cow.setFechaNacimiento(cowDetails.getFechaNacimiento());
        cow.setNumeroIdentificacion(cowDetails.getNumeroIdentificacion());
        cow.setEstado(cowDetails.getEstado());
        cow.setObservaciones(cowDetails.getObservaciones());
        
        return cow;
    }

    public void deleteCow(String id) {
        Cow cow = getCowById(id);
        cows.remove(cow);
    }

    public List<Cow> searchCows(String query) {
        String searchQuery = query.toLowerCase();
        return cows.stream()
                .filter(cow -> 
                    cow.getNombre().toLowerCase().contains(searchQuery) ||
                    cow.getNumeroIdentificacion().toLowerCase().contains(searchQuery) ||
                    cow.getRaza().toLowerCase().contains(searchQuery))
                .toList();
    }

    public List<Cow> getCowsByEstado(String estado) {
        return cows.stream()
                .filter(cow -> cow.getEstado().equalsIgnoreCase(estado))
                .toList();
    }
}