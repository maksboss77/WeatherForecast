package com.example.weatherforecast;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.weatherforecast.data.Weather;
import com.example.weatherforecast.worker.ChartWorker;
import com.example.weatherforecast.worker.ReadDetailsWorker;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {

    public ListView detailListView;

    public static DetailsAdapter adapter;

    public static ArrayList<Weather> detailsWeathers;

    public static LineChart chart;

    public static int indexSelectedElement;

    private static final String KEY = "index";

    private static final String TIME_FORMAT = "HH";
    private static final String DATE_FORMAT = "dd MMMM";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        indexSelectedElement = getIndex();

        setContentView(R.layout.activity_detail);

        /** Установка кнопки "Назад", если делать через манифест, то страница перезагружается*/
        ImageButton button = findViewById(R.id.button_prev);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setDateInHeader();

        chart = findViewById(R.id.chart);
        detailListView = findViewById(R.id.list);

        startWorker();

    }

    private int getIndex() {
        int fistValueIndex = getIntent().getExtras().getInt(KEY);
        int secondValueIndex = DateConversion.getIndexAfterTenPM(TIME_FORMAT);

        return fistValueIndex + secondValueIndex;
    }

    private void setDateInHeader() {
        TextView title = findViewById(R.id.text_title);
        title.setText(DateConversion.getDateInIndex(indexSelectedElement, DATE_FORMAT));
    }

    private void startWorker() {

        OneTimeWorkRequest readDetails = new OneTimeWorkRequest.Builder(ReadDetailsWorker.class).build();
        OneTimeWorkRequest viewChart = new OneTimeWorkRequest.Builder(ChartWorker.class).build();

        WorkManager.getInstance(this)
                .beginWith(readDetails)
                .then(viewChart)
                .enqueue();

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(readDetails.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            detailListView.setAdapter(adapter);
                        }
                    }
                });

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(viewChart.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo.getState().isFinished()) {
                            chart.invalidate();
                        }
                    }
                });
    }

}
