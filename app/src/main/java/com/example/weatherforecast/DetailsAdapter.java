package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.data.Weather;

import java.util.List;

public class DetailsAdapter extends ArrayAdapter<Weather> {

    private static final String URL_ICON_BEGIN = "http://openweathermap.org/img/wn/";
    private static final String URL_ICON_END = "@2x.png";

    private static final String DATE_FORMAT = "HH:mm";


    public DetailsAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.details_list_item, parent, false);
        }

        Weather currentWeather = getItem(position);

        TextView dateTimeTextView = listItemView.findViewById(R.id.details_date_time);
        dateTimeTextView.setText(DateConversion.getDateInMilliseconds(currentWeather.getDate(), DATE_FORMAT));

        TextView tempTextView = listItemView.findViewById(R.id.details_temp);
        tempTextView.setText(currentWeather.getTemp() + "°");

        TextView pressureTextView = listItemView.findViewById(R.id.details_pressure);
        pressureTextView.setText(currentWeather.getPressure() + " гПа");

        TextView cloudsTextView = listItemView.findViewById(R.id.details_clouds);
        cloudsTextView.setText(currentWeather.getClouds() + "%");

        TextView windTextView = listItemView.findViewById(R.id.details_wind);
        windTextView.setText(currentWeather.getWindSpeed() + " м/с");

        TextView humidityTextView = listItemView.findViewById(R.id.details_humidity);
        humidityTextView.setText(currentWeather.getHumidity() + "%");

        TextView descriptionTextView = listItemView.findViewById(R.id.details_description);
        descriptionTextView.setText(currentWeather.getDescription());

        ImageView iconImageView = listItemView.findViewById(R.id.details_icon);
        Glide
                .with(getContext())
                .load(URL_ICON_BEGIN + currentWeather.getIcon() + URL_ICON_END)
                .into(iconImageView);


        return listItemView;
    }

}
