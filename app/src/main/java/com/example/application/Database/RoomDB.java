package com.example.application.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.application.Model.Items;

@Database(entities = Items.class, version = 2, exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    private  static RoomDB database;
    private static  String DATABASE_NAME = "Application";

    public synchronized  static RoomDB getDatabase(Context context){
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDataAccessObject mainDataAccessObject();

}
