package com.example.weatherforecast.data;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeatherDao {

    /** Вывести всю таблицу */
    @Query("SELECT * FROM weather")
    List<Weather> getAll();

    /** Вывести информацию по одному значению, например 15:00 21.08.2020 */
    @Query("SELECT * FROM weather WHERE dt IN (:mDateId)")
    List<Weather> loadAllByDates(int[] mDateId);

//    /** Вывести информацию по конкретному дню */
//    @Query("SELECT * FROM weather WHERE (dt >= :startDate AND dt < :endDate)")
//    List<Weather> findByDate(int startDate, int endDate);

    /** Вывести информацию по конкретному дню */
    @Query("SELECT * FROM weather WHERE dt BETWEEN :startDate AND :endDate")
    List<Weather> testQuery(long startDate, long endDate);

    /** Вставка информации в таблицу */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Если есть конфликт по ID, то просто заменит на новое значение
    void insert(List<Weather> weather);

    /** Обновление информации в таблице */
    @Update
    void update(Weather weather);

    /** Удаление записей из таблицы */
    @Delete
    void delete(Weather weather);
}