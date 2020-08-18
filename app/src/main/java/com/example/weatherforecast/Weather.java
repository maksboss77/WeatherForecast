package com.example.weatherforecast;

public class Weather {

    // Дата (Primary Key)
    private int mDate;

    // Температура
    private int mTemp;

    // Давление
    private int mPressure;

    // Облака
    private int mClouds;

    // Скорость верта
    private int mWindSpeed;

    // Влажность
    private int mHumidity;

    // Описание
    private String mDescription;

    // Иконка
    private String mIcon;


    public Weather(int date, int temp, int pressure, int clouds, int windSpeed,
                   int humidity, String description, String icon) {
        mDate = date;
        mTemp = temp;
        mPressure = pressure;
        mHumidity = humidity;
        mWindSpeed = windSpeed;
        mClouds = clouds;
        mDescription = description;
        mIcon = icon;

    }

    public int getDate() { return mDate; }

    public int getTemp() { return mTemp; }

    public int getPressure() { return mPressure; }

    public int getHumidity() { return  mHumidity; }

    public int getWindSpeed() { return mWindSpeed; }

    public int getClouds() { return mClouds; }

    public String getDescription() { return mDescription; }

    public String getIcon() { return mIcon; }
}
