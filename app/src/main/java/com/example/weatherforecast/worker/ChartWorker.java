package com.example.weatherforecast.worker;

import android.content.Context;
import android.content.Entity;
import android.graphics.Color;

import com.example.weatherforecast.DateConversion;
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

    private static final String LABEL_TEXT = "Температура";
    private static final String DESCRIPTION_TEXT = "График суточной температуры";
    private static final String NO_DATA_TEXT = "Отображать нечего, данных нет :)";

    private static final int COLOR_CHART_R = 93;
    private static final int COLOR_CHART_G = 157;
    private static final int COLOR_CHART_B = 191;

    private static final float FORM_SIZE = 16f;
    private static final float TEXT_SIZE = 12f;

    private List<Entry> entities;

    private LineDataSet dataSet;
    private Description description;
    private LineData lineData;

    private int sizeList;

    public ChartWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        fillEntities();

        setPropertiesChart(entities);
        setPropertiesAxis();
        return Result.success();
    }

    private void fillEntities() {

        sizeList = DetailActivity.detailsWeathers.size();
        entities = new ArrayList<Entry>();

        for (int i = 0; i < sizeList; i++) {
            int time = DateConversion.getTimeInMilliseconds(DetailActivity.detailsWeathers.get(i).getDate(), DATE_FORMAT);
            int temp = DetailActivity.detailsWeathers.get(i).getTemp();

            entities.add(new Entry(time, temp));
        }
    }


    private void setPropertiesChart(List<Entry> entities) {

        initializingDataSet(entities);
        setNumberFormat();
        disableLegend();
        setGraphColor();
        setValueTextColor(Color.WHITE);
        setFormSize(FORM_SIZE);
        initializingLineData();
        setChartData();
        setTextDescriptionGraphics(DESCRIPTION_TEXT);
        setColorDescriptionGraphics(Color.WHITE);
        setChartDescription();
        setTextNoData(NO_DATA_TEXT);
        setChartDrawBorders();
        setTouchInteraction();

    }

    private void initializingDataSet(List<Entry> en) {
        dataSet = new LineDataSet(en, LABEL_TEXT);
    }

    private void setNumberFormat() {
        /** Форматирование температуры*/
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        };
        dataSet.setValueFormatter(formatter);
    }

    private void disableLegend() {
        Legend legend = DetailActivity.chart.getLegend();
        legend.setEnabled(false);
    }

    private void setGraphColor() {
        dataSet.setColor(android.graphics.Color.rgb(COLOR_CHART_R, COLOR_CHART_G, COLOR_CHART_B));
    }

    private void setValueTextColor(int color) {
        dataSet.setValueTextColor(color);
    }

    private void setFormSize(float formSize) {
        dataSet.setFormSize(formSize);
    }

    private void initializingLineData() {
        lineData = new LineData(dataSet);
    }

    private void setChartData() {
        DetailActivity.chart.setData(lineData);
    }

    private void setTextDescriptionGraphics(String descriptionText) {
        description = new Description();
        description.setText(descriptionText);
    }

    private void setColorDescriptionGraphics(int color) {
        description.setTextColor(color);
    }

    private void setChartDescription() {
        DetailActivity.chart.setDescription(description);
    }

    private void setTextNoData(String noDataText) {
        DetailActivity.chart.setNoDataText(noDataText);
    }

    private void setChartDrawBorders() {
        DetailActivity.chart.setDrawBorders(false);
    }

    private void setTouchInteraction() {
        DetailActivity.chart.setTouchEnabled(false);
        DetailActivity.chart.setPinchZoom(false);
        DetailActivity.chart.setDoubleTapToZoomEnabled(false);
    }

    private YAxis leftAxisY;
    private YAxis rightAxisY;

    private XAxis axisX;

    private void setPropertiesAxis() {

        initializingChartYAxis();

        disableDrawAxisYLeft();
        disableDrawAxisYRight();

        initializingChartXAxis();
        disableDrawXAxis();
        setPositionTextXAxis(XAxis.XAxisPosition.BOTTOM);
        setTextColorXAxis(Color.WHITE);
        setLabelCountXAxis(sizeList);
        setTextSizeXAxis(TEXT_SIZE);
    }

    private void setTextSizeXAxis(float textSize) {
        axisX.setTextSize(textSize);
    }


    private void initializingChartYAxis() {
        leftAxisY = DetailActivity.chart.getAxisLeft();
        rightAxisY = DetailActivity.chart.getAxisRight();
    }

    private void disableDrawAxisYLeft() {
        leftAxisY.setDrawAxisLine(false);
        leftAxisY.setDrawLabels(false);
        leftAxisY.setDrawGridLines(false);
    }

    private void disableDrawAxisYRight() {
        rightAxisY.setDrawAxisLine(false);
        rightAxisY.setDrawLabels(false);
        rightAxisY.setDrawGridLines(false);
    }

    private void initializingChartXAxis() {
        axisX = DetailActivity.chart.getXAxis();
    }

    private void disableDrawXAxis() {
        axisX.setDrawAxisLine(false);
        axisX.setDrawGridLines(false);
    }

    private void setPositionTextXAxis(XAxis.XAxisPosition position) {
        axisX.setPosition(position);
    }

    private void setTextColorXAxis(int color) {
        axisX.setTextColor(color);

    }

    private void setLabelCountXAxis(int size) {
        axisX.setLabelCount(size);
    }


}
