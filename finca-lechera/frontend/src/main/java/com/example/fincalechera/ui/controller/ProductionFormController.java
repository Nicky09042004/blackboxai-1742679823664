package com.example.fincalechera.ui.controller;

import com.example.fincalechera.ui.model.Cow;
import com.example.fincalechera.ui.model.MilkProduction;
import com.example.fincalechera.ui.model.ProductionType;
import com.example.fincalechera.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.IntStream;

public class ProductionFormController {
    @FXML private TextField idField;
    @FXML private ComboBox<Cow> cowComboBox;
    @FXML private DatePicker fechaDatePicker;
    @FXML private ComboBox<String> horaComboBox;
    @FXML private ComboBox<String> minutoComboBox;
    @FXML private ComboBox<ProductionType> tipoProduccionComboBox;
    @FXML private Spinner<Double> cantidadSpinner;
    @FXML private TextArea observacionesArea;
    @FXML private Label errorLabel;
    @FXML private Label ultimaProduccionLabel;
    @FXML private Label promedioDiarioLabel;
    @FXML private Label totalDiaLabel;

    private ApiService apiService;
    private ObservableList<MilkProduction> productions;
    private ObservableList<Cow> cows;
    private MilkProduction production;

    @FXML
    public void initialize() {
        setupControls();
        setupValidation();
    }

    private void setupControls() {
        // Configurar ComboBox de horas (00-23)
        ObservableList<String> horas = FXCollections.observableArrayList();
        IntStream.rangeClosed(0, 23)
                .mapToObj(i -> String.format("%02d", i))
                .forEach(horas::add);
        horaComboBox.setItems(horas);

        // Configurar ComboBox de minutos (00-59)
        ObservableList<String> minutos = FXCollections.observableArrayList();
        IntStream.rangeClosed(0, 59)
                .mapToObj(i -> String.format("%02d", i))
                .forEach(minutos::add);
        minutoComboBox.setItems(minutos);

        // Configurar ComboBox de tipos de producción
        tipoProduccionComboBox.setItems(FXCollections.observableArrayList(ProductionType.values()));

        // Configurar Spinner de cantidad
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 0.0, 0.5);
        cantidadSpinner.setValueFactory(valueFactory);
        cantidadSpinner.setEditable(true);

        // Establecer valores por defecto
        fechaDatePicker.setValue(LocalDate.now());
        horaComboBox.setValue(String.format("%02d", LocalTime.now().getHour()));
        minutoComboBox.setValue(String.format("%02d", LocalTime.now().getMinute()));
    }

    private void setupValidation() {
        cowComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                hideError();
                updateStatistics(newVal);
            }
        });

        cantidadSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal > 0) {
                hideError();
            }
        });
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setProductions(ObservableList<MilkProduction> productions) {
        this.productions = productions;
    }

    public void setCows(ObservableList<Cow> cows) {
        this.cows = cows;
        cowComboBox.setItems(cows);
    }

    public void setProduction(MilkProduction production) {
        this.production = production;
        populateFields();
    }

    private void populateFields() {
        if (production != null) {
            idField.setText(production.getId());
            
            cows.stream()
                .filter(c -> c.getId().equals(production.getCowId()))
                .findFirst()
                .ifPresent(cow -> {
                    cowComboBox.setValue(cow);
                    updateStatistics(cow);
                });

            LocalDateTime fecha = production.getFecha();
            fechaDatePicker.setValue(fecha.toLocalDate());
            horaComboBox.setValue(String.format("%02d", fecha.getHour()));
            minutoComboBox.setValue(String.format("%02d", fecha.getMinute()));
            
            tipoProduccionComboBox.setValue(production.getTipoProduccion());
            cantidadSpinner.getValueFactory().setValue(production.getCantidad());
            observacionesArea.setText(production.getObservaciones());
        }
    }

    private void updateStatistics(Cow cow) {
        double ultimaProduccion = 0.0;
        double totalDia = 0.0;
        int contadorDia = 0;
        LocalDate hoy = LocalDate.now();

        for (MilkProduction p : productions) {
            if (p.getCowId().equals(cow.getId())) {
                // Última producción
                if (ultimaProduccion == 0.0 || p.getFecha().isAfter(production != null ? production.getFecha() : LocalDateTime.MIN)) {
                    ultimaProduccion = p.getCantidad();
                }

                // Total y promedio del día
                if (p.getFecha().toLocalDate().equals(hoy)) {
                    totalDia += p.getCantidad();
                    contadorDia++;
                }
            }
        }

        ultimaProduccionLabel.setText(String.format("%.2f L", ultimaProduccion));
        totalDiaLabel.setText(String.format("%.2f L", totalDia));
        promedioDiarioLabel.setText(String.format("%.2f L", contadorDia > 0 ? totalDia / contadorDia : 0.0));
    }

    @FXML
    private void handleSave() {
        if (validateForm()) {
            try {
                MilkProduction productionToSave = production != null ? production : new MilkProduction();
                updateProductionFromFields(productionToSave);
                
                MilkProduction savedProduction = apiService.saveProduction(productionToSave);
                
                if (production == null) {
                    productions.add(savedProduction);
                } else {
                    int index = productions.indexOf(production);
                    productions.set(index, savedProduction);
                }
                
                closeWindow();
            } catch (IOException e) {
                showError("Error al guardar el registro: " + e.getMessage());
            }
        }
    }

    private void updateProductionFromFields(MilkProduction productionToUpdate) {
        productionToUpdate.setCowId(cowComboBox.getValue().getId());
        
        LocalDateTime fecha = LocalDateTime.of(
            fechaDatePicker.getValue(),
            LocalTime.of(
                Integer.parseInt(horaComboBox.getValue()),
                Integer.parseInt(minutoComboBox.getValue())
            )
        );
        productionToUpdate.setFecha(fecha);
        
        productionToUpdate.setTipoProduccion(tipoProduccionComboBox.getValue());
        productionToUpdate.setCantidad(cantidadSpinner.getValue());
        productionToUpdate.setObservaciones(observacionesArea.getText());
        
        // Actualizar la información de la vaca para mostrar
        productionToUpdate.setCowInfo(cowComboBox.getValue().toString());
    }

    private boolean validateForm() {
        if (cowComboBox.getValue() == null) {
            showError("Debe seleccionar una vaca");
            cowComboBox.requestFocus();
            return false;
        }

        if (fechaDatePicker.getValue() == null) {
            showError("Debe seleccionar una fecha");
            fechaDatePicker.requestFocus();
            return false;
        }

        if (horaComboBox.getValue() == null || minutoComboBox.getValue() == null) {
            showError("Debe especificar la hora completa");
            horaComboBox.requestFocus();
            return false;
        }

        if (tipoProduccionComboBox.getValue() == null) {
            showError("Debe seleccionar el tipo de producción");
            tipoProduccionComboBox.requestFocus();
            return false;
        }

        if (cantidadSpinner.getValue() == null || cantidadSpinner.getValue() <= 0) {
            showError("La cantidad debe ser mayor que 0");
            cantidadSpinner.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cowComboBox.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}