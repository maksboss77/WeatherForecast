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


    private static final long DATE_TRANSITION = 1000L;
    private static final String DATE_FORMAT = "dd MMMM";

    private static final String TODAY = "Сегодня";
    private static final String TOMORROW = "Завтра";


    private static final String URL_ICON_BEGIN = "http://openweathermap.org/img/wn/";
    private static final String URL_ICON_END = "@2x.png";

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
                .load(URL_ICON_BEGIN + currentWeather.getIcon() + URL_ICON_END)
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
        calendar.setTimeInMillis(timeInMilliseconds * DATE_TRANSITION);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return TODAY;
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return TOMORROW;

        return simpleDateFormat.format(calendar.getTime());
    }

}
