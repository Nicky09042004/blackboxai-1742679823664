package com.example.fincalechera.controller;

import com.example.fincalechera.model.Cow;
import com.example.fincalechera.service.CowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cows")
@CrossOrigin(origins = "*")
public class CowController {

    private final CowService cowService;

    @Autowired
    public CowController(CowService cowService) {
        this.cowService = cowService;
    }

    @GetMapping
    public ResponseEntity<List<Cow>> getAllCows() {
        return ResponseEntity.ok(cowService.getAllCows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cow> getCowById(@PathVariable String id) {
        return ResponseEntity.ok(cowService.getCowById(id));
    }

    @PostMapping
    public ResponseEntity<Cow> createCow(@RequestBody Cow cow) {
        return new ResponseEntity<>(cowService.createCow(cow), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cow> updateCow(@PathVariable String id, @RequestBody Cow cowDetails) {
        return ResponseEntity.ok(cowService.updateCow(id, cowDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCow(@PathVariable String id) {
        cowService.deleteCow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Cow>> searchCows(@RequestParam String query) {
        return ResponseEntity.ok(cowService.searchCows(query));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cow>> getCowsByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(cowService.getCowsByEstado(estado));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
            .badRequest()
            .body(Map.of("error", e.getMessage()));
    }
}