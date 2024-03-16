package org.example.weatherapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        TextField countryField = new TextField();
        countryField.setPromptText("Wpisz nazwę kraju");

        TextField cityField = new TextField();
        cityField.setPromptText("Wpisz nazwę miasta");

        Button searchButton = new Button("Szukaj");

        TextArea responseArea = new TextArea();
        responseArea.setEditable(false);


        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        searchButton.setOnAction(e -> {
         /*   String country = countryField.getText();
            String city = cityField.getText();
            Service service = new Service(country);
            String weatherJson = Service.ge
            Double rate1 = service.getRateFor("USD");
            Double rate2 = service.getNBPRate();*/

           // responseArea.setText("Pogoda: " + weatherJson + "\nKurs wymiany: " + rate1 + "\nKurs NBP: " + rate2);
            webEngine.load("https://pl.wikipedia.org/wiki/Warszawa");
        });


        VBox root = new VBox(10, countryField, cityField, searchButton, responseArea, webView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Weather and Currency Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}