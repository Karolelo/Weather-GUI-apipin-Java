package org.example.weatherapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import lombok.*;
import com.google.gson.*;
@Setter@Getter
public class Service {

    public static void main(String[] args) {
        var tmp = getIsoCode("Poland");
        var test=getInfoAboutCity("Warsaw",tmp);
        getWeatherMap();
    }


    private final static String app_key="39a825072fbb3d248e36c3046334acc3";
    private static String countryName;
    private static String cityName;
    private  static String IScode;

    public Service() {
    }

    private static String getIsoCode(String countryName) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)) {
                return locale.getISO3Country();
            }
        }
        return null;
    }
    @SneakyThrows
    public static  Map<String, Object> getWeatherMap(){

        City city=getInfoAboutCity(cityName,countryName);
        String urlCode="https://api.openweathermap.org/data/2.5/weather?lat="+city.lat+"&lon="+ city.lon +"&appid="+app_key;
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
