package com.example.weatherforecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class DateConversion {

    private static final long DATE_TRANSITION = 1000L;

    private static final String TODAY = "Сегодня";
    private static final String TOMORROW = "Завтра";

    private static final int SET_TIME = 0;
    private static final int ADD_HOUR_OF_DAY = 1;
    private static final int ADD_DAY_OF_MONTH = 1;

    private DateConversion() {

    }

    public static String getDateInMilliseconds(long timeInMilliseconds, String dateFormat) {

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

    public static String getDateInIndex(int index, String dateFormat) {

        if (index == 0)
            return TODAY;
        if (index == 1)
            return TOMORROW;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, index);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        return simpleDateFormat.format(calendar.getTime());
    }

    public static int getTimeSpecificFormat(long date, String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date*DATE_TRANSITION);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String t = simpleDateFormat.format(calendar.getTime());
        int result = Integer.parseInt(t);
        return result;
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

    public static long getStartDay(int index) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, SET_TIME);
        calendar.set(Calendar.MINUTE, SET_TIME);
        calendar.set(Calendar.SECOND, SET_TIME);
        calendar.set(Calendar.MILLISECOND, SET_TIME);

        calendar.add(Calendar.DAY_OF_MONTH, index);
        calendar.add(Calendar.HOUR_OF_DAY, ADD_HOUR_OF_DAY);

        return calendar.getTimeInMillis() / DATE_TRANSITION;
    }

    public static long getEndDay(long startDay) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDay * DATE_TRANSITION);

        calendar.add(Calendar.DAY_OF_MONTH, ADD_DAY_OF_MONTH);
        calendar.add(Calendar.HOUR_OF_DAY, -3);

        return calendar.getTimeInMillis() / DATE_TRANSITION;
    }

}
