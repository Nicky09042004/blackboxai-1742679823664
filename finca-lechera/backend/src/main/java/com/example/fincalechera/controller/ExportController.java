package com.example.fincalechera.controller;

import com.example.fincalechera.service.CowService;
import com.example.fincalechera.service.ExcelStorageService;
import com.example.fincalechera.service.MilkProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExcelStorageService excelStorageService;
    private final CowService cowService;
    private final MilkProductionService productionService;

    @Autowired
    public ExportController(ExcelStorageService excelStorageService,
                          CowService cowService,
                          MilkProductionService productionService) {
        this.excelStorageService = excelStorageService;
        this.cowService = cowService;
        this.productionService = productionService;
    }

    @GetMapping("/excel")
    public ResponseEntity<ByteArrayResource> exportToExcel() throws IOException {
        byte[] excelContent = excelStorageService.generateExcel(
            cowService.getAllCows(),
            productionService.getAllProductions()
        );

        String filename = "finca_lechera_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
            ".xlsx";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .contentLength(excelContent.length)
            .body(new ByteArrayResource(excelContent));
    }
}