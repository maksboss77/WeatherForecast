package com.example.weatherforecast;

import android.content.Context;
import android.os.Build;
import android.text.Html;
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

import java.util.ArrayList;
import java.util.List;

public class WeatherJsonAdapter extends ArrayAdapter<Weather> {

    private ArrayList<Weather> weathers;

    private static final String DATE_FORMAT = "dd MMMM";

    private static final String URL_ICON_BEGIN = "http://openweathermap.org/img/wn/";
    private static final String URL_ICON_END = "@2x.png";

    public WeatherJsonAdapter(@NonNull Context context, int resource, @NonNull List<Weather> weathers) {
        super(context, resource, weathers);
        this.weathers = (ArrayList<Weather>) weathers;
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

        TextView tempView = (TextView) listItemView.findViewById(R.id.temp);
        ImageView iconImage = listItemView.findViewById(R.id.icon_image);
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempView.setText(Html.fromHtml(String.valueOf(currentWeather.getTemp()), Html.FROM_HTML_MODE_LEGACY) +
                    getContext().getResources().getString(R.string.degree));
        } else {
            tempView.setText(Html.fromHtml(String.valueOf(currentWeather.getTemp())) +
                    getContext().getResources().getString(R.string.degree));
        }

        Glide
                .with(getContext())
                .load(URL_ICON_BEGIN + currentWeather.getIcon() + URL_ICON_END)
                .into(iconImage);

        dateView.setText(DateConversion.getDateInMilliseconds(currentWeather.getDate(), DATE_FORMAT));


        return listItemView;
    }
}
