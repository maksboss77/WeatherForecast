package com.example.weatherforecast.data;

import com.example.weatherforecast.DateConversion;
import com.example.weatherforecast.fivedaysjsonschema.WeatherForFiveDaysJSON;

import java.util.ArrayList;

import retrofit2.Response;

public final class DataWeather {

    private DataWeather() {

    }

    public static ArrayList<Weather> getFiveDays(ArrayList<Weather> weatherArrayList, String dateFormat) {

        int averageTemp = 0, countTemp = 0, temp = 0, prevIndex = 0;

        long prevDateMilliseconds = 0;
        String prevDate = "", date = "", icon = "";

        ArrayList<Weather> fiveWeather = new ArrayList<>();
        Weather weather;

        for (int i = 0; i < weatherArrayList.size(); i++) {

            weather = weatherArrayList.get(i);

            date = DateConversion.getDateInMilliseconds(weather.getDate(), dateFormat);
            icon = weather.getIcon();
            temp = weather.getTemp();

            if (prevDate.isEmpty()) {
                prevDate = date;
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            } else if (!date.equals(prevDate)) {

                int t = (int) Math.round(averageTemp/(countTemp*1.0));

                int maxDifferent = 100;

                for (int j = prevIndex; j < i; j++) {
                    Weather searchIcon = weatherArrayList.get(j);

                    if (Math.abs(searchIcon.getTemp() - t) < maxDifferent) {
                        maxDifferent = Math.abs(searchIcon.getTemp() - t);
                        icon = searchIcon.getIcon();
                    }

                }

                Weather averageWeather = new Weather(prevDateMilliseconds, t, icon);

                fiveWeather.add(averageWeather);
                averageTemp = weather.getTemp();
                countTemp = 1;
                prevDate = date;
                prevIndex = i;
            } else {
                averageTemp += temp;
                countTemp++;
                prevDateMilliseconds = weather.getDate();
            }

        }

        return fiveWeather;

    }

    public static Weather getFiveDaysWeathers(Response<WeatherForFiveDaysJSON> response, int i) {
        long date = getFiveWeatherDate(response, i);
        int temp = getFiveWeatherTemp(response, i);
        int pressure = getFiveWeatherPressure(response, i);
        int clouds = getFiveWeatherClouds(response, i);
        int wind = getFiveWeatherWind(response, i);
        int humidity = getFiveWeatherHumidity(response, i);
        String description = getFiveWeatherDescription(response, i);
        String icon = getFiveWeatherIcon(response, i);

        return new Weather(date, temp, pressure, clouds, wind, humidity, description, icon);
    }

    private static String getFiveWeatherIcon(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getWeather().get(0).getIcon();
    }

    private static String getFiveWeatherDescription(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getWeather().get(0).getDescription();

    }

    private static int getFiveWeatherHumidity(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getMain().getHumidity();
    }

    private static int getFiveWeatherWind(Response<WeatherForFiveDaysJSON> response, int i) {
        return (int) Math.round(response.body().getList().get(i).getWind().getSpeed());
    }

    private static int getFiveWeatherClouds(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getClouds().getAll();
    }

    private static int getFiveWeatherPressure(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getMain().getPressure();
    }

    private static int getFiveWeatherTemp(Response<WeatherForFiveDaysJSON> response, int i) {
        return (int) Math.round(response.body().getList().get(i).getMain().getTemp());
    }

    private static long getFiveWeatherDate(Response<WeatherForFiveDaysJSON> response, int i) {
        return response.body().getList().get(i).getDt();
    }
}
