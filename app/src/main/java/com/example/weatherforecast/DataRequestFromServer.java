package com.example.weatherforecast;

import android.util.Log;

import com.example.weatherforecast.data.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public final class DataRequestFromServer {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int NOT_USE = 0;

    private static final int WEATHER_OBJECT_INDEX = 0;

    private static final String WEATHER_ARRAY_LIST = "list";

    private static final String WEATHER_OBJECT_DATE = "dt";

    private static final String WEATHER_OBJECT_MAIN = "main";

    private static final String WEATHER_OBJECT_TEMP = "temp";

    private static final String WEATHER_OBJECT_PRESSURE = "pressure";

    private static final String WEATHER_OBJECT_HUMIDITY = "humidity";

    private static final String WEATHER_ARRAY = "weather";

    private static final String WEATHER_OBJECT_DESCRIPTION = "description";

    private static final String WEATHER_OBJECT_ICON = "icon";

    private static final String WEATHER_OBJECT_CLOUDS = "clouds";

    private static final String WEATHER_OBJECT_ALL = "all";

    private static final String WEATHER_OBJECT_WIND = "wind";

    private static final String WEATHER_OBJECT_SPEED = "speed";

    private static final String REQUEST_METHOD_GET = "GET";

    private static final int READ_TIMEOUT = 10000; /* milliseconds */

    private static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    private static final int RESPONSE_CODE = 200;

    private static final String FIVE_WEATHER_RESPONSE_URL =
            "http://api.openweathermap.org/data/2.5/forecast?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837";
    private static final String CURRENT_WEATHER_RESPONSE_URL =
            "http://api.openweathermap.org/data/2.5/weather?q=Novokuznetsk,ru&lang=ru&units=metric&appid=31b762ad9bd0b94b1c2a3cecee08e837";

    private DataRequestFromServer() {

    }

    // Преобразование JSON формата. Заносим данные в ArrayList<Weather> (5 дней)
    public static ArrayList<Weather> getFiveWeathersFromJSON() {

        ArrayList<Weather> weathers = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(getStringJSON(FIVE_WEATHER_RESPONSE_URL));
            JSONArray weatherArrayList = baseJsonResponse.getJSONArray(WEATHER_ARRAY_LIST);

            for (int i = 0; i < weatherArrayList.length(); i++) {
                JSONObject currentWeatherObject = weatherArrayList.getJSONObject(i);
                long date = currentWeatherObject.getLong(WEATHER_OBJECT_DATE);

                JSONObject mainObject = currentWeatherObject.getJSONObject(WEATHER_OBJECT_MAIN);
                int temp = mainObject.getInt(WEATHER_OBJECT_TEMP);
                int pressure = mainObject.getInt(WEATHER_OBJECT_PRESSURE);
                int humidity = mainObject.getInt(WEATHER_OBJECT_HUMIDITY);


                JSONArray weatherList = currentWeatherObject.getJSONArray(WEATHER_ARRAY);
                String description = "";
                String icon = "";
                for (int j = 0; j < weatherList.length(); j++) {

                    JSONObject currentWeatherListObject = weatherList.getJSONObject(j);
                    description = currentWeatherListObject.getString(WEATHER_OBJECT_DESCRIPTION);
                    icon = currentWeatherListObject.getString(WEATHER_OBJECT_ICON);

                }

                JSONObject cloudsObject = currentWeatherObject.getJSONObject(WEATHER_OBJECT_CLOUDS);
                int clouds = cloudsObject.getInt(WEATHER_OBJECT_ALL);

                JSONObject windObject = currentWeatherObject.getJSONObject(WEATHER_OBJECT_WIND);
                int wind = windObject.getInt(WEATHER_OBJECT_SPEED);

                Weather weather = new Weather(date, temp, pressure, clouds, wind, humidity, description, icon);
                weathers.add(weather);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the weather JSON results", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem URL connect", e);
        }

        return weathers;
    }

    // Преобразование JSON формата. Заносим данные в ArrayList<Weather> (текущая погода)
    public static Weather getCurrentWeatherFromJSON() {

        Weather weather;

        try {

            JSONObject baseJsonResponse = new JSONObject(getStringJSON(CURRENT_WEATHER_RESPONSE_URL));

            JSONArray weatherInfo = baseJsonResponse.getJSONArray(WEATHER_ARRAY);
            JSONObject weatherObject = weatherInfo.getJSONObject(WEATHER_OBJECT_INDEX);
            String description = weatherObject.getString(WEATHER_OBJECT_DESCRIPTION);
            String icon = weatherObject.getString(WEATHER_OBJECT_ICON);

            JSONObject mainObject = baseJsonResponse.getJSONObject(WEATHER_OBJECT_MAIN);
            int temp = mainObject.getInt(WEATHER_OBJECT_TEMP);

            weather = new Weather(NOT_USE, temp, NOT_USE, NOT_USE, NOT_USE, NOT_USE, description, icon);

            return weather;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the weather JSON results", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem URL connect", e);
        }


        return new Weather(NOT_USE, NOT_USE, NOT_USE, NOT_USE, NOT_USE, NOT_USE, "No data", "No data");
    }

    // Запрос на получение JSON (который преобразуется в строку)
    public static String getStringJSON(String urlString) throws IOException {

        String jsonResponse = "";

        if (urlString == null) {
            return jsonResponse;
        }

        URL url = null;

        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = null;
        InputStream inputStream = null;

        try {
            url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(REQUEST_METHOD_GET);
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.connect();

            stringBuilder = new StringBuilder();


            if (urlConnection.getResponseCode() == RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem URLConnection or inputStream connection", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return stringBuilder.toString();
    }


}
