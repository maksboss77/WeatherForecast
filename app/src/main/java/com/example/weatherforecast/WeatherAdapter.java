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

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private static final String DATE_FORMAT = "dd MMMM";

    private static final String URL_ICON_BEGIN = "http://openweathermap.org/img/wn/";
    private static final String URL_ICON_END = "@2x.png";

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

        TextView dateView = listItemView.findViewById(R.id.date);
        assert currentWeather != null;
        dateView.setText(DateConversion.getDateInMilliseconds(currentWeather.getDate(), DATE_FORMAT));


        ImageView iconImage = listItemView.findViewById(R.id.icon_image);
        Glide
                .with(getContext())
                .load(URL_ICON_BEGIN + currentWeather.getIcon() + URL_ICON_END)
                .into(iconImage);


        TextView tempView = listItemView.findViewById(R.id.temp);
        tempView.setText(currentWeather.getTemp() +
                getContext().getResources().getString(R.string.degree));
        return listItemView;
    }

}
