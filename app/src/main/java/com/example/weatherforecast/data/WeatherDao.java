package com.example.weatherforecast.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WeatherDao {

    /** Вывести всю таблицу */
    @Query("SELECT * FROM weather")
    List<Weather> getAll();

    /** Вывести информацию по конкретному дню */
    @Query("SELECT * FROM weather WHERE dt BETWEEN :startDate AND :endDate")
    List<Weather> oneDayQuery(long startDate, long endDate);

    /** Вставка информации в таблицу */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Если есть конфликт по ID, то просто заменит на новое значение
    void insert(List<Weather> weather);


    /** Удаление записей из таблицы */
    @Query("DELETE FROM weather WHERE dt < :startDate")
    void deleteOldRow(long startDate);
}
