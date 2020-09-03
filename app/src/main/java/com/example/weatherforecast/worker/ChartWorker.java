package com.example.weatherforecast.worker;

import android.content.Context;
import android.content.Entity;
import android.graphics.Color;

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

    private static final String DATE_FORMAT = "HH";
    private static final long DATE_TRANSITION = 1000L;

    private static final String LABEL_TEXT = "Температура";
    private static final String DESCRIPTION_TEXT = "График суточной температуры";
    private static final String NO_DATA_TEXT = "Отображать нечего, данных нет :)";

    private static final int COLOR_CHART_R = 93;
    private static final int COLOR_CHART_G = 157;
    private static final int COLOR_CHART_B = 191;

    private static final float FORM_SIZE = 16f;
    private static final float TEXT_SIZE = 12f;


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


        LineDataSet dataSet = new LineDataSet(entities, LABEL_TEXT);

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
        dataSet.setColor(android.graphics.Color.rgb(COLOR_CHART_R, COLOR_CHART_G, COLOR_CHART_B));

        // цвет текста значений (температуры)
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setFormSize(FORM_SIZE);

        LineData lineData = new LineData(dataSet);
        DetailActivity.chart.setData(lineData);

        // Текст описания графика
        Description description = new Description();
        description.setText(DESCRIPTION_TEXT);
        description.setTextColor(Color.WHITE);
        DetailActivity.chart.setDescription(description);

        // Текст, отображаемый, когда данных нет
        DetailActivity.chart.setNoDataText(NO_DATA_TEXT);

        // Рамка графика (true/false)
        DetailActivity.chart.setDrawBorders(false);

        // Сенсорное взаимодействие (true/false)
        DetailActivity.chart.setTouchEnabled(false);

        // Масштабирование графика "щипком" и двойным косанием (true/false)
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
        axisX.setTextColor(Color.WHITE);
        axisX.setDrawGridLines(false);
        // Установить количество часов на оси х
        axisX.setLabelCount(DetailActivity.detailsWeathers.size());
        // Размер
        axisX.setTextSize(TEXT_SIZE);


        return Result.success();
    }

    private int getTime(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date*DATE_TRANSITION);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String t = simpleDateFormat.format(calendar.getTime());
        int result = Integer.parseInt(t);
        return result;
    }

}
