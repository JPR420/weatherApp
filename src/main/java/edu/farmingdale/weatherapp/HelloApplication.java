package edu.farmingdale.weatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
/**
 * Main entry point for the Weather View application.
 * This class launches the JavaFX application window and loads the main FXML layout.
 */
public class HelloApplication extends Application {
    /**
     * Start the JavaFX application by loading the FXML layout and applying CSS styling.
     * This method is called when the application starts.
     * @param stage The primary stage for this application.
     * @throws IOException If an I/O error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Styling/style.css").toExternalForm());
        stage.setTitle("Weather View");
        stage.setScene(scene);
        stage.show();

    }
    /**
     * The main method that launches the JavaFX application.
     * @param args Command-line arguments (not used).
     * @throws FileNotFoundException If the required files are not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        launch();
    }
}