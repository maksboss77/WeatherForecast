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

public class WeatherAdapter extends ArrayAdapter<Weather> {

     private String urlIconBegin = "http://openweathermap.org/img/wn/";
     private String urlIconEnd = "@2x.png";

    public WeatherAdapter(@NonNull Context context, int resource, List<Weather> weathers) {
        super(context, resource, weathers);
    }

    // Отрисовка элементов списка
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
        assert currentWeather != null;
        dateView.setText(getDateString(currentWeather.getDate()));

//        // Установка иконки по URL
        ImageView iconImage = listItemView.findViewById(R.id.icon_image);
        Glide
                .with(getContext())
                .load(urlIconBegin + currentWeather.getIcon() + urlIconEnd)
                .into(iconImage);


        TextView tempView = (TextView) listItemView.findViewById(R.id.temp);
        tempView.setText(currentWeather.getTemp() +
                getContext().getResources().getString(R.string.degree));


        return listItemView;
    }

    // Преобразование даты в нужный формат (Сегодня завтра
    private String getDateString(long timeInMilliseconds) {

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds * 1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Сегодня";
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return "Завтра";

        return simpleDateFormat.format(calendar.getTime());
    }

}
