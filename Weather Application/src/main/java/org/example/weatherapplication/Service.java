package org.example.weatherapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import lombok.*;
import com.google.gson.*;
import org.controlsfx.control.WorldMapView;

@Setter@Getter@NoArgsConstructor
public class Service {

    public static void main(String[] args) {

    }


    private final static String appWeather="39a825072fbb3d248e36c3046334acc3";
    private final static String currencyKey="17534babb67212f330c9a3cc8584f341";
    private static String countryName;
    private static String cityName;
    private  static String IScode;


    private static String getIsoCode(String countryName) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)) {
                return locale.getISO3Country();
            }
        }
        return null;
    }
    public static Currency getCurrency() {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)) {
                Currency currency = Currency.getInstance(locale);
                return currency;
            }
        }
        return null;
    }
    public static Currency getCurrency(String country) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(country)) {
                Currency currency = Currency.getInstance(locale);
                return currency;
            }
        }
        return null;
    }
    @SneakyThrows
    public static  Map<String, Object> getWeatherMap(){

        City city=getInfoAboutCity(cityName,countryName);
        String urlCode="https://api.openweathermap.org/data/2.5/weather?lat="+city.lat+"&lon="+ city.lon +"&appid="+appWeather;
        URL url = new URL(urlCode);
        String s="";
        @Cleanup
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String line;
        while((line = in.readLine()) != null)
            s += line;
        Type listType = new TypeToken<HashMap<String, Object>>(){}.getType();
        Map<String, Object> map = new Gson().fromJson(s, listType);
        System.out.println(map);
        return map;
    }
    @SneakyThrows
    private static City getInfoAboutCity(String cityName,String IS3code) {
        String urlCode="http://api.openweathermap.org/geo/1.0/direct?q="+cityName+","+IS3code+"limit=1&appid=39a825072fbb3d248e36c3046334acc3";
        URL url = new URL(urlCode);
        String s = "";
        @Cleanup
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while((line = in.readLine()) != null)
                s += line;
        Gson gson = new Gson();
        ArrayList<City> city = gson.fromJson(s,new TypeToken<ArrayList<City>>(){}.getType());

        return city.getFirst();
    }
    @SneakyThrows
    private static String getInfoAboutExchangeCursOfCurrency(String Country){
        String urlCode="http://api.exchangerate.host/live" +
                "?access_key="+currencyKey+"&"+getCurrency(Country);
        URL url = new URL(urlCode);
        String s = "";
        @Cleanup
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String line;
        while((line = in.readLine()) != null)
            s += line;


        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, Object>>(){}.getType();
        Map<String, Object> map = new Gson().fromJson(s, listType);
        Map<String, Double> rates = (Map<String, Double>) map.get("quotes");
        String respond = "Kursy wymiany dla " + getCurrency(Country) + ": " + rates;
        System.out.println(respond);
        return respond;
    }

    @SneakyThrows
    private static String getInfoAboutExchangeCursOfCurrency(){
        String urlCode="http://api.exchangerate.host/live" +
                "?access_key="+currencyKey+"&"+getCurrency(countryName);
        URL url = new URL(urlCode);
        String s = "";
        @Cleanup
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String line;
        while((line = in.readLine()) != null)
            s += line;

        Gson gson = new Gson();
        Type listType = new TypeToken<HashMap<String, Object>>(){}.getType();
        Map<String, Object> map = new Gson().fromJson(s, listType);
        Map<String, Double> rates = (Map<String, Double>) map.get("quotes");
        String respond = "Kursy wymiany dla " + getCurrency(countryName) + ": " + rates;
        System.out.println(respond);
        return respond;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
    @ToString
    class City {
        private String name;
        private Map<String, String> local_names;
        private String state; // Zakładam, że te dane są dostępne
        private double lon;
        private double lat;
    }

}
