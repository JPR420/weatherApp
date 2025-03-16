package edu.farmingdale.weatherapp;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TableView<Weather> tableView;
    @FXML
    private TableColumn<Weather, String> DateView;
    @FXML
    private TableColumn<Weather, Double> temperatureView ;
    @FXML
    private TableColumn<Weather, Integer> humidityView;
    @FXML
    private TableColumn<Weather, Double> precipitationView;

    List<Weather> weatherList = new ArrayList<Weather>();

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @FXML
    public void initialize() {
        loadDataFromCSV();
        setupTable();
    }

    private void setupTable() {
        // Set the cell value factories for each column
        DateView.setCellValueFactory(new PropertyValueFactory<Weather, String>("date"));
        temperatureView.setCellValueFactory(new PropertyValueFactory<Weather, Double>("temperature"));
        humidityView.setCellValueFactory(new PropertyValueFactory<Weather, Integer>("humidity"));
        precipitationView.setCellValueFactory(new PropertyValueFactory<Weather, Double>("precipitation"));


        ObservableList<Weather> weatherObservableList = FXCollections.observableArrayList(weatherList);

        tableView.setItems(weatherObservableList);
    }
    private void loadDataFromCSV() {
        try (Scanner sc = new Scanner(new FileReader("weatherdata.csv"))) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String date = parts[0];
                    double temperature = Double.parseDouble(parts[1]);
                    int humidity = Integer.parseInt(parts[2]);
                    double precipitation = Double.parseDouble(parts[3]);

                    Weather data = new Weather(date, temperature, humidity, precipitation);
                    weatherList.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        for (Weather data : weatherList) {
            System.out.println(data);
        }
    }
}


record Weather(String date,double temperature,int humidity,double precipitation){

    public String temperatureCategory() {
        return switch ((int) temperature) {
            case int t when t > 32 -> "Hot";
            case int t when t > 15 -> "Warm";
            default -> "Cold";
        };
    }
    public StringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }

    public DoubleProperty temperatureProperty() {
        return new SimpleDoubleProperty(temperature);
    }

    public IntegerProperty humidityProperty() {
        return new SimpleIntegerProperty(humidity);
    }

    public DoubleProperty precipitationProperty() {
        return new SimpleDoubleProperty(precipitation);
    }

}