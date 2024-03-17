package org.example.weatherapplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.*;
import com.google.gson.*;

@Setter@Getter@NoArgsConstructor
public class Service {


    private final static String appWeather="39a825072fbb3d248e36c3046334acc3";
    private final static String currencyKey="17534babb67212f330c9a3cc8584f341";
    private  String countryName;
    private  String cityName;
    private   String IScode;
    private Currency currency;

    public Service(String countryName) {
        this.countryName=countryName;
        this.currency=getCurrency(countryName);
    }

    private static String getIsoCode(String countryName) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)) {
                return locale.getISO3Country();
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
    public  Map<String, Object> getWeatherMap(){

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
    public  String getInfoAboutExchangeCursOfCurrency(String Currency){
        String urlCode="http://api.exchangerate.host/live" +
                "?access_key="+currencyKey+"&"+currency;
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
        String respond = "Curs of exchange " + getCurrency(countryName) + ": " + rates;

        return respond;
    }



    @SneakyThrows
    public LocalRateOfCurrency getNBPRate(String currency){
        if(!currency.equals("PLN")) {
            String urlCode = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/";
            URL url = new URL(urlCode);
            String s = "";
            @Cleanup
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null)
                s += line;

            Gson gson = new Gson();
            LocalRateOfCurrency localRateOfCurrency = gson.fromJson(s, LocalRateOfCurrency.class);
            System.out.println(localRateOfCurrency);
            return localRateOfCurrency;
        }
        LocalRateOfCurrency lrc=new LocalRateOfCurrency();
        lrc.setRates(List.of(new Rate()));
        return lrc;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
    @Setter
    class LocalRateOfCurrency {
        String table;
        String currency;
        String code;
        private List<Rate> rates;

        @Override
        public String toString() {
            return "Currency rate to pln " + rates.getFirst().mid;
        }
    }
    @Getter@ToString@Setter
    public static class Rate {
        public Rate() {
            this.mid = 1.0;
        }

        @SerializedName("no")
        private String number;

        @SerializedName("effectiveDate")
        private String effectiveDate;

        private double mid;

    }
    @ToString
    class City {
        private String name;
        private Map<String, String> local_names;
        private String state;
        private double lon;
        private double lat;
    }

    /*public Currency getCurrency() {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)) {
                Currency currency = Currency.getInstance(locale);
                return currency;
            }
        }
        return null;
    }*/

     /*@SneakyThrows
    public static LocalRateOfCurrency getNBPRate(String currency){

        String urlCode="http://api.nbp.pl/api/exchangerates/rates/a/"+currency+"/";
        URL url = new URL(urlCode);
        String s = "";
        @Cleanup
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String line;
        while((line = in.readLine()) != null)
            s += line;

        Gson gson = new Gson();
        LocalRateOfCurrency localRateOfCurrency = gson.fromJson(s,LocalRateOfCurrency.class);
        System.out.println(localRateOfCurrency);
        return localRateOfCurrency;
    }*/

    /* @SneakyThrows
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
    }*/

}
