package com.example.weatherforecast.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Weather {

    // Дата
    @PrimaryKey
    @ColumnInfo(name = "dt")
    private long mDate;

    // Температура
    @ColumnInfo(name = "temp")
    private int mTemp;

    // Давление
    @ColumnInfo(name = "pressure")
    private int mPressure;

    // Облака
    @ColumnInfo(name = "clouds")
    private int mClouds;

    // Скорость верта
    @ColumnInfo(name = "wind")
    private int mWindSpeed;

    // Влажность
    @ColumnInfo(name = "humidity")
    private int mHumidity;

    // Описание
    @ColumnInfo(name = "description")
    private String mDescription;

    // Иконка
    @ColumnInfo(name = "icon")
    private String mIcon;

    @Ignore
    public Weather(long date, int temp, String icon) {
        mDate = date;
        mTemp = temp;
        mIcon = icon;
    }

    @Ignore
    public Weather (int temp, String description, String icon) {
        mTemp = temp;
        mDescription = description;
        mIcon = icon;
    }

    public Weather(long date, int temp, int pressure, int clouds, int windSpeed,
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

    public long getDate() {
        return mDate;
    }

    public int getTemp() {
        return mTemp;
    }

    public int getPressure() {
        return mPressure;
    }

    public int getClouds() {
        return mClouds;
    }

    public int getWindSpeed() {
        return mWindSpeed;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getIcon() {
        return mIcon;
    }


    @Override
    public String toString() {
        return "Weather{" +
                "mDate=" + mDate +
                ", mTemp=" + mTemp +
                ", mPressure=" + mPressure +
                ", mClouds=" + mClouds +
                ", mWindSpeed=" + mWindSpeed +
                ", mHumidity=" + mHumidity +
                ", mDescription=" + mDescription +
                ", mIcon=" + mIcon +
                '}';
    }
}
