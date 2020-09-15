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

    private static final String REQUEST_METHOD_GET = "GET";

    private static final int READ_TIMEOUT = 10000; /* milliseconds */

    private static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    private static final int RESPONSE_CODE = 200;


    private DataRequestFromServer() {

    }

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
