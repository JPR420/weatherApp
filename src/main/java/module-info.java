module edu.farmingdale.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.farmingdale.weatherapp to javafx.fxml;
    exports edu.farmingdale.weatherapp;
}