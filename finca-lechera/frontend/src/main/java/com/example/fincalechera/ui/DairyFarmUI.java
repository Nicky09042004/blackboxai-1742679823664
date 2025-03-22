package com.example.fincalechera.ui;

import com.example.fincalechera.ui.controller.MainController;
import com.example.fincalechera.ui.service.ApiService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DairyFarmUI extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(DairyFarmUI.class);
    private static final String MAIN_FXML = "/fxml/main_layout.fxml";
    private static final String APP_TITLE = "Sistema de Gestión - Finca Lechera";
    private ApiService apiService;

    @Override
    public void init() {
        this.apiService = new ApiService();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_FXML));
            Parent root = loader.load();

            // Configure the main controller
            MainController mainController = loader.getController();
            mainController.setApiService(apiService);
            mainController.initialize();

            // Set up the scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/styles.css");

            // Configure the primary stage
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);

            // Add application icon if available
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/app_icon.png")));
            } catch (Exception e) {
                logger.warn("Could not load application icon", e);
            }

            // Show the application
            primaryStage.show();

            logger.info("Application started successfully");
        } catch (Exception e) {
            logger.error("Error starting application", e);
            throw new RuntimeException("Could not start application", e);
        }
    }

    @Override
    public void stop() {
        logger.info("Application shutting down");
    }

    public static void main(String[] args) {
        launch(args);
    }
}