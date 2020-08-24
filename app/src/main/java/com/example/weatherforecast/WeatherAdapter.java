package com.example.weatherforecast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherAdapter extends ArrayAdapter<Weather> {

     private String urlIconBegin = "http://openweathermap.org/img/wn/";
     private String urlIconEnd = "@2x.png";

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
        dateView.setText(getDateString(currentWeather.getDate()));

        TextView iconView = (TextView) listItemView.findViewById(R.id.icon);
        iconView.setText(currentWeather.getIcon());

//        // Установка иконки по URL
//        ImageView iconImage = listItemView.findViewById(R.id.icon_image);
//        FutureTarget<Bitmap> futureTarget = Glide.with(getContext()).asBitmap()
//                .load(urlIconBegin + currentWeather.getIcon() + urlIconEnd).submit(10, 10);
//        Bitmap bitmap = null;
//        try {
//            bitmap = futureTarget.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        iconImage.setImageBitmap(bitmap);
//        Glide.with(getContext()).clear(futureTarget);


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
