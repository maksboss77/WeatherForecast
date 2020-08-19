package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private int averageTemp;
    private int countTemp;
    private int index = 0;

    private String prevDate = "";

    public WeatherAdapter(@NonNull Context context, int resource, List<Weather> weathers) {
        super(context, resource, weathers);
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        Weather currentWeather = getItem(index);
//
//        String date = getDateString(currentWeather.getDate());
//        String icon = currentWeather.getIcon();
//        int temp = currentWeather.getTemp();
//
//        if (prevDate.isEmpty()) {
//            prevDate = date;
//        }
//
//
//        while (date.equals(prevDate)) {
//            averageTemp += temp;
//            countTemp++;
//            index++;
//
//            if (index == getCount()) {
//                index--;
//                break;
//            }
//            currentWeather = getItem(index);
//            date = getDateString(currentWeather.getDate());
//            icon = currentWeather.getIcon();
//            temp = currentWeather.getTemp();
//        }
//
//        // Get average temp for the day
//        averageTemp /= countTemp;
//
//        View listItemView = convertView;
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.weather_list_item, parent, false);
//        }
//
//        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
//        dateView.setText(prevDate);
//
//        TextView iconView = (TextView) listItemView.findViewById(R.id.icon);
//        iconView.setText(icon);
//
//        TextView tempView = (TextView) listItemView.findViewById(R.id.temp);
//        tempView.setText(String.valueOf(averageTemp));
//
//        // Assign a new date to the previous date
//        prevDate = date;
//        averageTemp = 0;
//        countTemp = 0;
//
//        return listItemView;
//    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.weather_list_item, parent, false);
        }

        Weather currentWeather = getItem(position);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(getDateString(currentWeather.getDate()));

        TextView iconView = (TextView) listItemView.findViewById(R.id.icon);
        iconView.setText(currentWeather.getIcon());

        TextView tempView = (TextView) listItemView.findViewById(R.id.temp);
        tempView.setText(String.valueOf(currentWeather.getTemp()));


        return listItemView;
    }

    private String getDateString(long timeInMilliseconds) {

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds * 1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Сегодня";
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Завтра";

        return simpleDateFormat.format(calendar.getTime());
    }

}
