package com.example.weatherforecast.worker;

import android.content.Context;
import android.content.Entity;

import com.example.weatherforecast.DetailActivity;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ChartWorker extends Worker {

    public ChartWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    // Заполнение информации для графика + его стилизация
    @NonNull
    @Override
    public Result doWork() {

        List<Entry> entities = new ArrayList<Entry>();

        for (int i = 0; i < DetailActivity.detailsWeathers.size(); i++) {
            int time = getTime(DetailActivity.detailsWeathers.get(i).getDate());
            int temp = DetailActivity.detailsWeathers.get(i).getTemp();

            entities.add(new Entry(time, temp));
        }


        LineDataSet dataSet = new LineDataSet(entities, "Температура");

        /** Форматирование температуры*/
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        };
        dataSet.setValueFormatter(formatter);

        // отключение легенды
        Legend legend = DetailActivity.chart.getLegend();
        legend.setEnabled(false);

        // Цвет графика и линий на графике
        dataSet.setColor(android.graphics.Color.rgb(93, 157, 191));

        // цвет текста значений (температуры)
        dataSet.setValueTextColor(android.graphics.Color.rgb(255, 255, 255));

        dataSet.setFormSize(16f);

        LineData lineData = new LineData(dataSet);
        DetailActivity.chart.setData(lineData);

        // Текст описания графика
        Description description = new Description();
        description.setText("График суточной температуры");
        description.setTextColor(android.graphics.Color.rgb(255, 255, 255));
        DetailActivity.chart.setDescription(description);

        // Текст, отображаемый, когда данных нет
        DetailActivity.chart.setNoDataText("Отображать нечего, данных нет :)");

        // Рамка графика (true/false)
        DetailActivity.chart.setDrawBorders(false);

        // Отключить сенсорное взаимодействие с графиком
        DetailActivity.chart.setTouchEnabled(false);

        // Масштабирование графика "щипком" и двоейным косанием
        DetailActivity.chart.setPinchZoom(false);
        DetailActivity.chart.setDoubleTapToZoomEnabled(false);


        /** Оси (по Y левая и правая) */
        YAxis leftAxisY = DetailActivity.chart.getAxisLeft();
        YAxis rightAxisY = DetailActivity.chart.getAxisRight();

        // Убираем отрисовку линий оси и меток
        leftAxisY.setDrawAxisLine(false);
        leftAxisY.setDrawLabels(false);
        rightAxisY.setDrawAxisLine(false);
        rightAxisY.setDrawLabels(false);
        leftAxisY.setDrawGridLines(false);
        rightAxisY.setDrawGridLines(false);

        /** Оси (по X) */
        XAxis axisX = DetailActivity.chart.getXAxis();
        axisX.setDrawAxisLine(false);
        axisX.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisX.setTextColor(android.graphics.Color.rgb(255, 255, 255));
        axisX.setDrawGridLines(false);
        // Установить количество часов на оси х
        axisX.setLabelCount(DetailActivity.detailsWeathers.size());
        // Размер
        axisX.setTextSize(12f);


        return Result.success();
    }

    private int getTime(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String t = simpleDateFormat.format(calendar.getTime());
        int result = Integer.parseInt(t);
        return result;
    }

}
