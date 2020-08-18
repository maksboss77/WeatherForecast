package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    public WeatherAdapter(@NonNull Context context, int resource, List<Weather> weathers) {
        super(context, resource, weathers);
    }

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
        dateView.setText(String.valueOf(currentWeather.getDate()));

        TextView iconView = (TextView) listItemView.findViewById(R.id.icon);
        iconView.setText(currentWeather.getIcon());

        TextView tempView = (TextView) listItemView.findViewById(R.id.temp);
        tempView.setText(String.valueOf(currentWeather.getTemp()));


        return listItemView;
    }
}
