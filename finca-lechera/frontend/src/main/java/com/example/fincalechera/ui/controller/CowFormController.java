package com.example.fincalechera.ui.controller;

import com.example.fincalechera.ui.model.Cow;
import com.example.fincalechera.ui.service.ApiService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CowFormController {
    private static final Logger logger = LoggerFactory.getLogger(CowFormController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private TextField idField;
    @FXML private TextField numeroIdentificacionField;
    @FXML private TextField nombreField;
    @FXML private TextField razaField;
    @FXML private Spinner<Integer> edadSpinner;
    @FXML private DatePicker fechaNacimientoDatePicker;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private TextArea observacionesArea;
    @FXML private Label errorLabel;

    private ApiService apiService;
    private ObservableList<Cow> cows;
    private Cow cow;

    @FXML
    public void initialize() {
        setupControls();
        setupValidation();
    }

    private void setupControls() {
        // Configure edad spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0);
        edadSpinner.setValueFactory(valueFactory);
        edadSpinner.setEditable(true);

        // Configure estado combo box
        estadoComboBox.getItems().addAll("Activa", "Inactiva", "En Tratamiento", "Vendida");

        // Set default values
        fechaNacimientoDatePicker.setValue(LocalDate.now());
        estadoComboBox.setValue("Activa");

        // Hide error label initially
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setupValidation() {
        numeroIdentificacionField.textProperty().addListener((obs, oldVal, newVal) -> hideError());
        nombreField.textProperty().addListener((obs, oldVal, newVal) -> hideError());
        razaField.textProperty().addListener((obs, oldVal, newVal) -> hideError());
        edadSpinner.valueProperty().addListener((obs, oldVal, newVal) -> hideError());
        fechaNacimientoDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> hideError());
        estadoComboBox.valueProperty().addListener((obs, oldVal, newVal) -> hideError());
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setCows(ObservableList<Cow> cows) {
        this.cows = cows;
    }

    public void setCow(Cow cow) {
        this.cow = cow;
        populateFields();
    }

    private void populateFields() {
        if (cow != null) {
            idField.setText(cow.getId());
            numeroIdentificacionField.setText(cow.getNumeroIdentificacion());
            nombreField.setText(cow.getNombre());
            razaField.setText(cow.getRaza());
            edadSpinner.getValueFactory().setValue(cow.getEdad());
            
            if (cow.getFechaNacimiento() != null) {
                try {
                    LocalDate fecha = LocalDate.parse(cow.getFechaNacimiento(), DATE_FORMATTER);
                    fechaNacimientoDatePicker.setValue(fecha);
                } catch (Exception e) {
                    logger.warn("Could not parse fecha nacimiento: {}", cow.getFechaNacimiento());
                }
            }
            
            estadoComboBox.setValue(cow.getEstado());
            observacionesArea.setText(cow.getObservaciones());
        }
    }

    @FXML
    private void handleSave() {
        if (validateForm()) {
            try {
                Cow cowToSave = cow != null ? cow : new Cow();
                updateCowFromFields(cowToSave);
                
                Cow savedCow = apiService.saveCow(cowToSave);
                
                if (cow == null) {
                    cows.add(savedCow);
                } else {
                    int index = cows.indexOf(cow);
                    cows.set(index, savedCow);
                }
                
                closeWindow();
            } catch (Exception e) {
                logger.error("Error saving cow", e);
                showError("Error al guardar la vaca: " + e.getMessage());
            }
        }
    }

    private void updateCowFromFields(Cow cowToUpdate) {
        cowToUpdate.setNumeroIdentificacion(numeroIdentificacionField.getText());
        cowToUpdate.setNombre(nombreField.getText());
        cowToUpdate.setRaza(razaField.getText());
        cowToUpdate.setEdad(edadSpinner.getValue());
        cowToUpdate.setFechaNacimiento(fechaNacimientoDatePicker.getValue().format(DATE_FORMATTER));
        cowToUpdate.setEstado(estadoComboBox.getValue());
        cowToUpdate.setObservaciones(observacionesArea.getText());
    }

    private boolean validateForm() {
        if (numeroIdentificacionField.getText().trim().isEmpty()) {
            showError("El número de identificación es requerido");
            numeroIdentificacionField.requestFocus();
            return false;
        }

        if (nombreField.getText().trim().isEmpty()) {
            showError("El nombre es requerido");
            nombreField.requestFocus();
            return false;
        }

        if (razaField.getText().trim().isEmpty()) {
            showError("La raza es requerida");
            razaField.requestFocus();
            return false;
        }

        if (edadSpinner.getValue() == null || edadSpinner.getValue() < 0) {
            showError("La edad debe ser un número válido mayor o igual a 0");
            edadSpinner.requestFocus();
            return false;
        }

        if (fechaNacimientoDatePicker.getValue() == null) {
            showError("La fecha de nacimiento es requerida");
            fechaNacimientoDatePicker.requestFocus();
            return false;
        }

        if (fechaNacimientoDatePicker.getValue().isAfter(LocalDate.now())) {
            showError("La fecha de nacimiento no puede ser futura");
            fechaNacimientoDatePicker.requestFocus();
            return false;
        }

        if (estadoComboBox.getValue() == null) {
            showError("El estado es requerido");
            estadoComboBox.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
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