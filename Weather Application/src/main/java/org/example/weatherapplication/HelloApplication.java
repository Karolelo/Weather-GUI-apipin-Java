package org.example.weatherapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.Map;

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
        responseArea.setPrefHeight(300);
        VBox.setVgrow(responseArea, Priority.ALWAYS);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "GBP");
        currencyComboBox.setPromptText("Choose currency");

        searchButton.setOnAction(e -> {
            String country = countryField.getText();
            String city = cityField.getText();
            Service service = new Service(country);

            Map<String,Object> weatherJson = service.getWeatherMap();
            if(!currencyComboBox.getItems().contains(service.getCurrency().toString()))
            currencyComboBox.getItems().add(service.getCurrency().toString());

            Service.LocalRateOfCurrency rateToPLN = service.getNBPRate(service.getCurrency().getCurrencyCode());

            responseArea.setText("Tempreture "+weatherJson.get("main").toString()+"\n"+rateToPLN);
            webEngine.load("https://en.wikipedia.org/wiki/"+service.getCityName());
        });
        currencyComboBox.setOnAction(event -> {
            String selectedCurrency = currencyComboBox.getValue();

        });


        VBox root = new VBox(10, countryField, cityField, searchButton,currencyComboBox, responseArea, webView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Weather and Currency Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}