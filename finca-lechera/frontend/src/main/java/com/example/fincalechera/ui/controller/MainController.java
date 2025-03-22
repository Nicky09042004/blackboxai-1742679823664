package com.example.fincalechera.ui.controller;

import com.example.fincalechera.ui.model.Cow;
import com.example.fincalechera.ui.model.MilkProduction;
import com.example.fincalechera.ui.model.ProductionType;
import com.example.fincalechera.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private TableView<Cow> cowTable;
    @FXML private TableView<MilkProduction> productionTable;
    @FXML private TextField searchCowField;
    @FXML private ComboBox<Cow> filterCowComboBox;
    @FXML private ComboBox<ProductionType> filterTypeComboBox;
    @FXML private DatePicker filterDatePicker;
    @FXML private Label totalDiarioLabel;
    @FXML private Label totalSemanalLabel;
    @FXML private Label totalMensualLabel;
    @FXML private Label statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private ApiService apiService;
    private ObservableList<Cow> cows;
    private ObservableList<MilkProduction> productions;

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    @FXML
    public void initialize() {
        setupTables();
        setupFilters();
        loadData();
    }

    private void setupTables() {
        // Configure Cow Table
        cowTable.getColumns().forEach(column -> {
            if (column.getId() != null) {
                switch (column.getId()) {
                    case "cowIdColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("id"));
                        break;
                    case "cowNumeroIdentificacionColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
                        break;
                    case "cowNombreColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("nombre"));
                        break;
                    case "cowRazaColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("raza"));
                        break;
                    case "cowEdadColumn":
                        ((TableColumn<Cow, Integer>) column).setCellValueFactory(new PropertyValueFactory<>("edad"));
                        break;
                    case "cowFechaNacimientoColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
                        break;
                    case "cowEstadoColumn":
                        ((TableColumn<Cow, String>) column).setCellValueFactory(new PropertyValueFactory<>("estado"));
                        break;
                }
            }
        });

        // Configure Production Table
        productionTable.getColumns().forEach(column -> {
            if (column.getId() != null) {
                switch (column.getId()) {
                    case "productionIdColumn":
                        ((TableColumn<MilkProduction, String>) column).setCellValueFactory(new PropertyValueFactory<>("id"));
                        break;
                    case "productionCowInfoColumn":
                        ((TableColumn<MilkProduction, String>) column).setCellValueFactory(new PropertyValueFactory<>("cowInfo"));
                        break;
                    case "productionFechaColumn":
                        ((TableColumn<MilkProduction, LocalDateTime>) column).setCellValueFactory(new PropertyValueFactory<>("fecha"));
                        break;
                    case "productionCantidadColumn":
                        ((TableColumn<MilkProduction, Double>) column).setCellValueFactory(new PropertyValueFactory<>("cantidad"));
                        break;
                    case "productionTipoColumn":
                        ((TableColumn<MilkProduction, ProductionType>) column).setCellValueFactory(new PropertyValueFactory<>("tipoProduccion"));
                        break;
                }
            }
        });
    }

    private void setupFilters() {
        filterTypeComboBox.setItems(FXCollections.observableArrayList(ProductionType.values()));
        filterDatePicker.setValue(LocalDate.now());

        searchCowField.textProperty().addListener((observable, oldValue, newValue) -> filterCows());
        filterCowComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterProductions());
        filterTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterProductions());
        filterDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterProductions());
    }

    private void loadData() {
        try {
            showProgress("Cargando datos...");
            
            cows = FXCollections.observableArrayList(apiService.getAllCows());
            cowTable.setItems(cows);
            filterCowComboBox.setItems(cows);

            productions = FXCollections.observableArrayList(apiService.getAllProductions());
            productionTable.setItems(productions);

            updateStatistics();
            hideProgress();
        } catch (Exception e) {
            logger.error("Error loading data", e);
            showError("Error al cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    private void handleNewCow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cow_form.fxml"));
            Parent root = loader.load();

            CowFormController controller = loader.getController();
            controller.setApiService(apiService);
            controller.setCows(cows);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nueva Vaca");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatistics();
        } catch (IOException e) {
            logger.error("Error opening cow form", e);
            showError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditCow() {
        Cow selectedCow = cowTable.getSelectionModel().getSelectedItem();
        if (selectedCow == null) {
            showError("Por favor, seleccione una vaca para editar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cow_form.fxml"));
            Parent root = loader.load();

            CowFormController controller = loader.getController();
            controller.setApiService(apiService);
            controller.setCows(cows);
            controller.setCow(selectedCow);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Vaca");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatistics();
        } catch (IOException e) {
            logger.error("Error opening cow form", e);
            showError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCow() {
        Cow selectedCow = cowTable.getSelectionModel().getSelectedItem();
        if (selectedCow == null) {
            showError("Por favor, seleccione una vaca para eliminar");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro que desea eliminar esta vaca?",
                ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                apiService.deleteCow(selectedCow.getId());
                cows.remove(selectedCow);
                showSuccess("Vaca eliminada exitosamente");
                updateStatistics();
            } catch (Exception e) {
                logger.error("Error deleting cow", e);
                showError("Error al eliminar la vaca: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleNewProduction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/production_form.fxml"));
            Parent root = loader.load();

            ProductionFormController controller = loader.getController();
            controller.setApiService(apiService);
            controller.setProductions(productions);
            controller.setCows(cows);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuevo Registro de Producción");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatistics();
        } catch (IOException e) {
            logger.error("Error opening production form", e);
            showError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditProduction() {
        MilkProduction selectedProduction = productionTable.getSelectionModel().getSelectedItem();
        if (selectedProduction == null) {
            showError("Por favor, seleccione un registro para editar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/production_form.fxml"));
            Parent root = loader.load();

            ProductionFormController controller = loader.getController();
            controller.setApiService(apiService);
            controller.setProductions(productions);
            controller.setCows(cows);
            controller.setProduction(selectedProduction);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Registro de Producción");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateStatistics();
        } catch (IOException e) {
            logger.error("Error opening production form", e);
            showError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProduction() {
        MilkProduction selectedProduction = productionTable.getSelectionModel().getSelectedItem();
        if (selectedProduction == null) {
            showError("Por favor, seleccione un registro para eliminar");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro que desea eliminar este registro?",
                ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                apiService.deleteProduction(selectedProduction.getId());
                productions.remove(selectedProduction);
                showSuccess("Registro eliminado exitosamente");
                updateStatistics();
            } catch (Exception e) {
                logger.error("Error deleting production", e);
                showError("Error al eliminar el registro: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Excel");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("reporte_finca_lechera.xlsx");

        File file = fileChooser.showSaveDialog(cowTable.getScene().getWindow());
        if (file != null) {
            try {
                showProgress("Generando reporte Excel...");
                byte[] excelContent = apiService.exportExcel();
                
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(excelContent);
                }
                
                showSuccess("Reporte Excel generado exitosamente");
            } catch (Exception e) {
                logger.error("Error exporting Excel", e);
                showError("Error al generar el reporte Excel: " + e.getMessage());
            } finally {
                hideProgress();
            }
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) cowTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Sistema de Gestión - Finca Lechera");
        alert.setContentText("Versión 1.0\n\nDesarrollado por [Tu Nombre/Empresa]");
        alert.showAndWait();
    }

    private void filterCows() {
        String searchText = searchCowField.getText().toLowerCase();
        ObservableList<Cow> filteredCows = FXCollections.observableArrayList(
            cows.filtered(cow ->
                cow.getNombre().toLowerCase().contains(searchText) ||
                cow.getNumeroIdentificacion().toLowerCase().contains(searchText) ||
                cow.getRaza().toLowerCase().contains(searchText)
            )
        );
        cowTable.setItems(filteredCows);
    }

    private void filterProductions() {
        ObservableList<MilkProduction> filteredProductions = FXCollections.observableArrayList(productions);

        if (filterCowComboBox.getValue() != null) {
            filteredProductions = filteredProductions.filtered(
                prod -> prod.getCowId().equals(filterCowComboBox.getValue().getId())
            );
        }

        if (filterTypeComboBox.getValue() != null) {
            filteredProductions = filteredProductions.filtered(
                prod -> prod.getTipoProduccion() == filterTypeComboBox.getValue()
            );
        }

        if (filterDatePicker.getValue() != null) {
            LocalDate filterDate = filterDatePicker.getValue();
            filteredProductions = filteredProductions.filtered(
                prod -> prod.getFecha().toLocalDate().equals(filterDate)
            );
        }

        productionTable.setItems(filteredProductions);
        updateStatistics();
    }

    private void updateStatistics() {
        // Implementation depends on specific business requirements
        // This is a placeholder implementation
        double totalDiario = 0.0;
        double totalSemanal = 0.0;
        double totalMensual = 0.0;

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.minusDays(30);

        for (MilkProduction prod : productions) {
            LocalDate prodDate = prod.getFecha().toLocalDate();
            if (prodDate.equals(today)) {
                totalDiario += prod.getCantidad();
            }
            if (!prodDate.isBefore(weekStart)) {
                totalSemanal += prod.getCantidad();
            }
            if (!prodDate.isBefore(monthStart)) {
                totalMensual += prod.getCantidad();
            }
        }

        totalDiarioLabel.setText(String.format("%.2f L", totalDiario));
        totalSemanalLabel.setText(String.format("%.2f L", totalSemanal));
        totalMensualLabel.setText(String.format("%.2f L", totalMensual));
    }

    private void showProgress(String message) {
        statusLabel.setText(message);
        progressIndicator.setVisible(true);
    }

    private void hideProgress() {
        statusLabel.setText("Listo");
        progressIndicator.setVisible(false);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }
}