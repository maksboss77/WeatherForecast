package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.data.Weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsAdapter extends ArrayAdapter<Weather> {

    private String urlIconBegin = "http://openweathermap.org/img/wn/";
    private String urlIconEnd = "@2x.png";

    public DetailsAdapter(@NonNull Context context, int resource, @NonNull List<Weather> objects) {
        super(context, resource, objects);
    }

    /** Отрисовка списка (экран Детали)*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.details_list_item, parent, false);
        }

        Weather currentWeather = getItem(position);

        TextView dateTimeTextView = (TextView) listItemView.findViewById(R.id.details_date_time);
        dateTimeTextView.setText(getDateString(currentWeather.getDate()));

        TextView tempTextView = (TextView) listItemView.findViewById(R.id.details_temp);
        tempTextView.setText(currentWeather.getTemp() + "°");

        TextView pressureTextView = (TextView) listItemView.findViewById(R.id.details_pressure);
        pressureTextView.setText(currentWeather.getPressure() + " гПа");

        TextView cloudsTextView = (TextView) listItemView.findViewById(R.id.details_clouds);
        cloudsTextView.setText(currentWeather.getClouds() + "%");

        TextView windTextView = (TextView) listItemView.findViewById(R.id.details_wind);
        windTextView.setText(currentWeather.getWindSpeed() + " м/с");

        TextView humidityTextView = (TextView) listItemView.findViewById(R.id.details_humidity);
        humidityTextView.setText(currentWeather.getHumidity() + "%");

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.details_description);
        descriptionTextView.setText(currentWeather.getDescription());

        ImageView iconImageView = listItemView.findViewById(R.id.details_icon);
        Glide
                .with(getContext())
                .load(urlIconBegin + currentWeather.getIcon() + urlIconEnd)
                .into(iconImageView);


        return listItemView;
    }


    private String getDateString(long timeInMilliseconds) {

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds * 1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Сегодня";
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Завтра";

        return simpleDateFormat.format(calendar.getTime());
    }
}
