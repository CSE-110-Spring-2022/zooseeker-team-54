package com.example.zooseeker_team54;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocItemDao {
    @Insert
    long insert(LocItem locItem);

    @Insert
    List<Long> insertAll(List<LocItem> locItems);

    @Query("SELECT * FROM loc_items WHERE `id`=:id")
    LocItem get(long id);

    @Query("SELECT * FROM loc_items ORDER BY `id`")
    List<LocItem> getAll();

    @Update
    int update(LocItem locItem);

    @Delete
    int delete(LocItem locItem);

    @Query("SELECT * FROM loc_items ORDER BY `id`")
    LiveData<List<LocItem>> getAllLive();

    @Query("SELECT * FROM loc_items WHERE `planned` = 1 ORDER BY `id`")
    LiveData<List<LocItem>> getAllPlannedLive();
}
