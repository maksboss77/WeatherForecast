package com.example.weatherforecast.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.weatherforecast.data.dbContract.WeatherEntry;

import com.example.weatherforecast.Weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class dbProvider extends ContentProvider {

    /**
     * Тэг для сообщений журнала
     */
    public static final String LOG_TAG = dbProvider.class.getSimpleName();

    /**
     * Код соответствия URI для содержимого URI для таблицы Plan
     */
    private static final int WEATHER = 100;

    /**
     * Код соответствия URI для содержимого URI для одного мероприятия в таблице Plan
     */
    private static final int ITEM = 101;

    /**
     * Объект UriMatcher для сопоставления URI содержимого с соответствующим кодом.
     * Входные данные, переданные в конструктор, представляют собой код, возвращаемый для корневого URI.
     * Обычно для этого случая используется NO_MATCH.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /** Статический инициализатор. Это запускается при первом вызове чего-либо из этого класса. */
    static {
        /** Создаем собственный код "100" и "101" для каждого доступа
         *
         * Здесь выполняются вызовы addURI () для всех шаблонов URI контента, которые поставщик
         * должен распознать. Все пути, добавленные в UriMatcher, имеют соответствующий код для возврата
         * когда совпадение найдено.
         *
         */

        // Вызовы addURI () идут здесь, для всех шаблонов URI содержимого, которые поставщик
        // должны признать. Все пути, добавленные в UriMatcher, имеют соответствующий код для возврата
        // когда найдено совпадение.

        // Содержание URI формы "content://com.example.weatherforecast/weather" будет отображаться на карте
        // целочисленный код {@link #WEATHER}. Этот URI используется для предоставления доступа к нескольким строкам
        // из таблицы расписания
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.PATH_PLAN, WEATHER);

        // Содержание URI формы "content://com.example.latestudent/plan/#" будет отображаться на
        // целочисленный код {@link #ITEM}. Этот URI используется для предоставления доступа к одной строке
        // из таблицы погоды
        //
        // В этом случае используется подстановочный знак"#", где " # " может быть заменено целым числом.
        // Например, "content://com.example.weatherforecast/weather/3" матчи, но
        // "content://com.example.weatherforecast/weather" (без номера в конце) не соответствует.
        sUriMatcher.addURI(dbContract.CONTENT_AUTHORITY, dbContract.PATH_PLAN + "/#", ITEM);
    }

    /**
     * Вспомогательный объект Базы Данных
     */
    private dbHelper mDbHelper;

    /**
     * Инициализируйте поставщика и вспомогательный объект базы данных.
     */
    @Override
    public boolean onCreate() {
        // Создали и инициализировать объект dbHelper, чтобы получить доступ к базе расписания
        mDbHelper = new dbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        /** Получаем доступ к БД с помощью mDbHelper, которую мы инициализировали в методе OnCreate */
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        /** Этот курсор будет содержать результат запроса */
        Cursor cursor;

        /** Выясняем, какой тип входного URI был передан нам сюда
         * Выяснить, может ли сопоставитель URI сопоставить URI с конкретным кодом
         */
        int match = sUriMatcher.match(uri);

        switch (match) {
            /** Если код 100, выполняем запрос по всей таблице расписания
             * Для кода PLAN, запросить таблицу домашних животных непосредственно с заданным
             * проекция, выбор, аргументы выбора и порядок сортировки. Курсор
             * может содержать несколько таблицы
             */
            case WEATHER:
                cursor = database.query(WeatherEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            /** Если код 101, то выполняем запрос на одного питомца
             * Для кода EVENT извлекаем идентификатор из URI.
             * Для примера URI, такого как «content: //com.example.latestudent/plan/3»,
             * выбор будет "_id =?" и аргумент выбора будет
             * Строковый массив, содержащий действительный идентификатор 3 в этом случае.
             *
             * Для каждого "?" в выборе, мы должны иметь элемент в выборе
             * аргументы, которые будут заполнять «?». Так как у нас есть 1 знак вопроса в
             * selection, у нас есть 1 String в массиве String аргументов выбора.
             */
            case ITEM:
                selection = WeatherEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the PLAN table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(WeatherEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Устанавливает уведомление в курсоре
        // для каждого содержимого URI был создан курсор
        // и если какие-либо данные в этом URI меняются, мы знаем, что нужно обновить курсор
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                return WeatherEntry.CONTENT_LIST_TYPE;
            case ITEM:
                return WeatherEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                return insertWeather(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertWeather(Uri uri, ContentValues values) {
        // Как только мы узнаем идентификатор новой строки в таблице,
        // возвращает новый URI с идентификатором, добавленным к его концу

        // Получить доступную для записи БД
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Вставить новое домашнее животное с заданными значениями
        long id = database.insert(WeatherEntry.TABLE_NAME, null, values);
        // Если идентификатор равен -1, то вставка не удалась. Отчет об ошибке и возвращать null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Данный метод уведомляет всех Listener, что данные изменились за каким-то URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Получить доступную для записи базу данных
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Отслеживание количества строк, которые были удалены
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                // Удалить все строки, соответствующие элементам selection и selectionargs
                rowsDeleted = database.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM:
                // Удалить одну строку, заданную идентификатором в URL-адресе
                selection = WeatherEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // Если 1 или более строк были удалены, то уведомить всех слушателей о том, что данные на
        // данный URI изменился
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Возвращает количество удаленных строк
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                return updateWeather(uri, values, selection, selectionArgs);
            case ITEM:
                // Для кода EVENT извлеките идентификатор из URI,
                // таким образом, мы знаем, какую строку обновить. Выбор будет "_id=?"и выбор
                // аргументы будут строковым массивом, содержащим фактический идентификатор.
                selection = WeatherEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateWeather(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Обновление погоды в базе данных с заданными значениями содержимого. Примените изменения к строкам
     * указывается в аргументах выбора и выбора.
     * Возвращает количество строк, которые были успешно обновлены.
     */
    private int updateWeather(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // В противном случае get writable database обновит данные
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Данный метод уведомляет всех Listener, что данные изменились за каким-то URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Выполните обновление базы данных и получите количество затронутых строк
        int rowsUpdated = database.update(WeatherEntry.TABLE_NAME, values, selection, selectionArgs);

        // Если 1 или более строк были обновлены, то уведомить всех слушателей о том, что данные на
        // данный URI изменился
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Возвращает количество обновленных строк
        return rowsUpdated;
    }
}
