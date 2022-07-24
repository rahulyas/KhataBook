package com.example.application.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import com.example.application.Model.Items;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDataAccessObject {
    @Insert(onConflict = REPLACE)
    void insert(Items notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Items> getAll();

    @Query("UPDATE notes SET title = :title , notes = :notes,  rate = :rate WHERE ID = :id")
    void update(int id, String title, String notes, String rate);

    @Delete
    void delete(Items notes);

    @Query("UPDATE notes Set pinned = :pin WHERE ID =:id")
    void pin(int id, boolean pin);


}
