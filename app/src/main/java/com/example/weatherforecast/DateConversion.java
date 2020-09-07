package com.example.weatherforecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class DateConversion {

    private static final long DATE_TRANSITION = 1000L;

    private static final String TODAY = "Сегодня";
    private static final String TOMORROW = "Завтра";

    private DateConversion() {

    }

    public static String getDateSpecificFormat(long timeInMilliseconds, String dateFormat) {

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds * DATE_TRANSITION);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return TODAY;
        else
            today.add(Calendar.DATE, +1);

        if (simpleDateFormat.format(calendar.getTime()).equals(simpleDateFormat.format(today.getTime())))
            return TOMORROW;

        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getDateSpecificIndex(int index, String dateFormat) {

        if (index == 0)
            return TODAY;
        if (index == 1)
            return TOMORROW;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, index);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        return simpleDateFormat.format(calendar.getTime());
    }

    // Получить время (для отображения информации и заголовка)
    // Если время больше 22 чаов, то отображать список на сегодняшний день не нужно, так как данных нет
    public static int getIndexAfterTenPM(String timeFormat) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        String t = simpleDateFormat.format(calendar.getTime());
        int result = Integer.parseInt(t);

        if (result >= 22)
            return  1;
        else
            return 0;

    }

}
