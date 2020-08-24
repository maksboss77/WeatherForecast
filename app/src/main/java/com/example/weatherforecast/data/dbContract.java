package com.example.weatherforecast.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class dbContract {

    /** Установим констанку для authorities */
    public static final String CONTENT_AUTHORITY = "com.example.weatherforecast";

    /** Объединяем константу CONTENT_AUTHORITY со схемой "content://" */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /** Эта константа хранит путь для каждой из таблиц, которые будут добавлены к URI базового содержимого */
    public static final String PATH_PLAN = "weather";

//    public static final String ORDER_BY = "time";

    public static final class WeatherEntry implements BaseColumns {
        /**
         * Тип MIME для погоды.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAN;

        /**
         * Тип MIME для конкретного погоды.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAN;

        /**
         * URI, который содержит схему и права  доступа к контенту
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLAN);

        /** URI, который содержит схему и права  доступа к контенту*/
        public final static String TABLE_NAME = "weather";

        /**
         * Создаем константы для столбцов таблицы
         */
        // ID, который также является временем
        public final static String _ID = BaseColumns._ID;
        // Температура
        public final static String COLUMN_TEMP = "temp";
        // Давление
        public final static String COLUMN_PRESSURE = "pressure";
        // Облака
        public final static String COLUMN_CLOUDS = "clouds";
        // Ветер
        public final static String COLUMN_WIND = "wind";
        // Влажность
        public final static String COLUMN_HUMIDITY = "humidity";
        // Описание
        public final static String COLUMN_DESCRIPTION = "description";
        // Иконка
        public final static String COLUMN_ICON = "icon";
    }


}
