package com.example.fincalechera.ui.service;

import com.example.fincalechera.ui.model.Cow;
import com.example.fincalechera.ui.model.MilkProduction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String API_BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }

    // Cow Operations
    public List<Cow> getAllCows() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/cows"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Cow>>() {});
        }
        
        logger.error("Error getting cows: {}", response.body());
        return Collections.emptyList();
    }

    public Cow saveCow(Cow cow) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(cow);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/cows"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), Cow.class);
        }
        
        throw new IOException("Error saving cow: " + response.body());
    }

    public void deleteCow(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/cows/" + id))
            .DELETE()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new IOException("Error deleting cow: " + response.body());
        }
    }

    // Production Operations
    public List<MilkProduction> getAllProductions() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/productions"))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<MilkProduction>>() {});
        }
        
        logger.error("Error getting productions: {}", response.body());
        return Collections.emptyList();
    }

    public MilkProduction saveProduction(MilkProduction production) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(production);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/productions"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), MilkProduction.class);
        }
        
        throw new IOException("Error saving production: " + response.body());
    }

    public void deleteProduction(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/productions/" + id))
            .DELETE()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 204) {
            throw new IOException("Error deleting production: " + response.body());
        }
    }

    // Statistics Operations
    public double getTotalProduccionDiaria(String cowId, LocalDate fecha) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("%s/productions/daily/%s?fecha=%s", API_BASE_URL, cowId, fecha)))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Double.class);
        }
        
        throw new IOException("Error getting daily production: " + response.body());
    }

    // Export Operations
    public byte[] exportExcel() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE_URL + "/export/excel"))
            .GET()
            .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        
        if (response.statusCode() == 200) {
            return response.body();
        }
        
        throw new IOException("Error exporting Excel: " + new String(response.body()));
    }
}