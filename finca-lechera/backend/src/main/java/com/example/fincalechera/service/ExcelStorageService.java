package com.example.fincalechera.service;

import com.example.fincalechera.model.Cow;
import com.example.fincalechera.model.MilkProduction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelStorageService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public byte[] generateExcel(List<Cow> cows, List<MilkProduction> productions) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            createCowsSheet(workbook, cows);
            createProductionsSheet(workbook, productions);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    private void createCowsSheet(Workbook workbook, List<Cow> cows) {
        Sheet sheet = workbook.createSheet("Vacas");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        String[] headers = {"ID", "Número", "Nombre", "Raza", "Edad", "Fecha Nacimiento", "Estado", "Observaciones"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
        
        // Create data rows
        int rowNum = 1;
        for (Cow cow : cows) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cow.getId());
            row.createCell(1).setCellValue(cow.getNumeroIdentificacion());
            row.createCell(2).setCellValue(cow.getNombre());
            row.createCell(3).setCellValue(cow.getRaza());
            row.createCell(4).setCellValue(cow.getEdad());
            row.createCell(5).setCellValue(cow.getFechaNacimiento());
            row.createCell(6).setCellValue(cow.getEstado());
            row.createCell(7).setCellValue(cow.getObservaciones());
        }
    }
    
    private void createProductionsSheet(Workbook workbook, List<MilkProduction> productions) {
        Sheet sheet = workbook.createSheet("Producción");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);
        
        String[] headers = {"ID", "ID Vaca", "Fecha", "Cantidad (L)", "Tipo", "Observaciones"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
        
        // Create data rows
        int rowNum = 1;
        for (MilkProduction production : productions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(production.getId());
            row.createCell(1).setCellValue(production.getCowId());
            row.createCell(2).setCellValue(production.getFecha().format(DATE_FORMATTER));
            row.createCell(3).setCellValue(production.getCantidad());
            row.createCell(4).setCellValue(production.getTipoProduccion().toString());
            row.createCell(5).setCellValue(production.getObservaciones());
        }
    }
    
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
}