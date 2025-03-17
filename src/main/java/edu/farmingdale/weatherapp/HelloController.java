package edu.farmingdale.weatherapp;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller class for the weather application user interface.
 * Handles interactions with the UI elements such as labels, tables, and hyperlinks.
 * Responsible for setting weather data, displaying weather images, and handling temperature conversions.
 */
public class HelloController {
    @FXML
    public ImageView weatherPicture;
    @FXML
    private Label dayOfTheWeek,degreesLabel,humidutyLabel,precipitaitonLabel,dateLabel,hottestLabel,avgTempLabel,rainyLabel,weatherDateLabel, daySelected;
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
    @FXML
    private Hyperlink hyperlink;

    private boolean isCelsius = true;

    List<Weather> weatherList = new ArrayList<Weather>();
    private final ObservableList<Weather> weatherObservableList = FXCollections.observableArrayList();

    /**
     * Handles the click event for the "Random Date" button.
     * Displays random weather data when the button is clicked.
     */
    @FXML
    protected void onHelloButtonClick() {
        Random rd = new Random();
        int randomNum = rd.nextInt(weatherList.size());
        Weather weather = weatherList.get(randomNum);
        setWeatherImage(weather);
        degreesLabel.setText(String.valueOf((int)weather.temperature()));
        humidutyLabel.setText("Humidity: "+weather.humidity() + "%");
        precipitaitonLabel.setText("Precipitation: "+weather.precipitation());
        dateLabel.setText("Date: "+ weather.date());
    }
    /**
     * Initializes the controller by setting up the table and loading data from the CSV file.
     * It also displays current weather data, hottest weather data, and averages.
     */
    @FXML
    public void initialize() {
        setupTable();
        loadDataFromCSV();
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        Random rd = new Random();
        int randomNum = rd.nextInt(weatherList.size());
        Weather weather = weatherList.get(randomNum);
        setWeatherImage(weather);
        degreesLabel.setText(String.valueOf((int)weather.temperature()));
        humidutyLabel.setText("Humidity: "+weather.humidity() + "%");
        precipitaitonLabel.setText("Precipitation: "+weather.precipitation());
        dateLabel.setText("Date: "+ weather.date());

        Weather tempForLabels = getHottest();
        avgTempLabel.setText("Average Temperature from " + getMonth(tempForLabels)+ ": " + getAverageTemperature());
        rainyLabel.setText("Rainy days: "+ getRainy() );
        hottestLabel.setText("Hottest day: " + getHottest().date());
        dayOfTheWeek.setText(""+dayOfWeek);

    }
    /**
     * Displays information about the selected weather item from the table.
     * Updates labels with the weather data for the selected day.
     */
    @FXML
    public void columInfo(){
        Weather clikedWeather = tableView.getSelectionModel().getSelectedItem();
        degreesLabel.setText(String.valueOf((int) clikedWeather.temperature()));
        humidutyLabel.setText("Humidity: "+clikedWeather.humidity() + "%");
        precipitaitonLabel.setText("Precipitation: "+clikedWeather.precipitation());
        dateLabel.setText("Date: "+ clikedWeather.date());
        daySelected.setText("The day selected is consider: " + clikedWeather.temperatureCategory());
       setWeatherImage(clikedWeather);
    }
    /**
     * Converts temperature between Celsius and Fahrenheit.
     * Updates the displayed temperature label after conversion.
     */
    @FXML
    public void calculator(){
        String input = degreesLabel.getText();
        try {
            double temp = Double.parseDouble((input));
            if (isCelsius) {
                double fahrenheit = (temp * 9/5) + 32;
                degreesLabel.setText(String.valueOf(fahrenheit));
            } else {
                double celsius = (temp - 32) * 5/9;
                degreesLabel.setText(String.valueOf(celsius));
            }
            isCelsius = !isCelsius;
        } catch (NumberFormatException e) {
            degreesLabel.setText("Invalid Input");
        }


    }
    /**
     * Sets up the table view to display weather data.
     * Binds the table columns to the corresponding weather data properties.
     */
    private void setupTable() {

        DateView.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        temperatureView.setCellValueFactory(cellData -> cellData.getValue().temperatureProperty().asObject());
        humidityView.setCellValueFactory(cellData -> cellData.getValue().humidityProperty().asObject());
        precipitationView.setCellValueFactory(cellData -> cellData.getValue().precipitationProperty().asObject());
        tableView.getColumns().setAll(DateView, temperatureView, humidityView, precipitationView);
    }
    /**
     * Loads weather data from a CSV file into the weather list and updates the table.
     * Each weather entry is parsed and added to the observable list.
     */
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
                    weatherObservableList.add(data);
                    weatherList.add(data);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        tableView.setItems(weatherObservableList);
    }
    /**
     * Sets the appropriate weather image based on the given weather conditions.
     * The image is displayed in the weatherPicture ImageView.
     *
     * @param weather The weather data used to select the appropriate image.
     */
    public void setWeatherImage(Weather weather) {
        Image sun = new Image(getClass().getResource("/img/sun.png").toExternalForm());
        Image sunny = new Image(getClass().getResource("/img/sunny.png").toExternalForm());
        Image rainy = new Image(getClass().getResource("/img/rainy.png").toExternalForm());
        Image cloudy = new Image(getClass().getResource("/img/cloudy.png").toExternalForm());
        if (weather.humidity() > 60 && weather.precipitation() > 0.5) {
            weatherPicture.setImage(rainy);
        } else if (weather.temperature() > 31) {
            weatherPicture.setImage(sunny);
        } else if (weather.temperature() > 10 && weather.humidity() > 40) {
            weatherPicture.setImage(cloudy);
        }else {
            weatherPicture.setImage(sun);//just as a default, it only happens once on the list
        }

    }
    /**
     * Returns the name of the month from the weather date.
     *
     * @param weather The weather data containing the date.
     * @return The name of the month (e.g., "January", "February").
     */
    public String getMonth(Weather weather) {
        String date = weather.date();
        String[] parts = date.split("-");
        String month = switch (parts[1]) {
            case "01" -> "January";
            case "02" -> "February";
            case "03" -> "March";
            case "04" -> "April";
            case "05" -> "May";
            case "06" -> "June";
            case "07" -> "July";
            case "08" -> "August";
            case "09" -> "September";
            case "10" -> "October";
            case "11" -> "November";
            case "12" -> "December";
            default -> "Error getting the month";
        };
        return month;
    }
    /**
     * Finds and returns the hottest weather day from the weather list.
     *
     * @return The weather data for the hottest day.
     */
    public Weather getHottest(){
        Weather tempWeather = weatherList.get(0);
        for (int i = 0; i < weatherList.size(); i++) {
            if(weatherList.get(i).temperature() > tempWeather.temperature()){
                tempWeather = weatherList.get(i);
            }

        }
        return tempWeather;
    }
    /**
     * Returns the number of rainy days based on precipitation data.
     *
     * @return The number of rainy days (with precipitation > 0.3).
     */
    public int getRainy(){
        return (int) weatherList.stream().filter(w -> w.precipitation() > 0.3).count();
    }
    /**
     * Calculates and returns the average temperature for the month of the hottest day.
     *
     * @return The average temperature for the hottest day's month.
     */
    public double getAverageTemperature() {
        double sum = 0;
        int count = 0;
        Weather temp = getHottest();
        for (Weather weather : weatherList) {
            if (Objects.equals(getMonth(weather),getMonth(temp))){
                sum += weather.temperature();
                count++;
            }
        }
        double avgTemp = (count > 0) ? sum / count : 0;
        DecimalFormat df = new DecimalFormat("#.0"); // Format to 1 decimal place
        return Double.parseDouble(df.format(avgTemp));
    }

}

/**
 * Represents weather data for a specific day, including date, temperature, humidity, and precipitation.
 * Provides methods for weather categorization, property access, and binding.
 */
record Weather(String date,double temperature,int humidity,double precipitation){


    /**
     * Returns a string representing the temperature category based on temperature.
     * Categories include "Hot", "Warm", or "Cold".
     *
     * @return The temperature category based on the temperature.
     */
    public String temperatureCategory() {
        return switch ((int) temperature) {
            case int t when t > 32 -> "Hot";
            case int t when t > 15 -> "Warm";
            default -> "Cold";
        };
    }

    /**
     * Returns a StringProperty representing the date.
     *
     * @return A StringProperty for the date.
     */
    public StringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }
    /**
     * Returns a DoubleProperty representing the temperature.
     *
     * @return A DoubleProperty for the temperature.
     */
    public DoubleProperty temperatureProperty() {
        return new SimpleDoubleProperty(temperature);
    }
    /**
     * Returns an IntegerProperty representing the humidity.
     *
     * @return An IntegerProperty for the humidity.
     */
    public IntegerProperty humidityProperty() {
        return new SimpleIntegerProperty(humidity);
    }
    /**
     * Returns a DoubleProperty representing the precipitation.
     *
     * @return A DoubleProperty for the precipitation.
     */
    public DoubleProperty precipitationProperty() {
        return new SimpleDoubleProperty(precipitation);
    }

}