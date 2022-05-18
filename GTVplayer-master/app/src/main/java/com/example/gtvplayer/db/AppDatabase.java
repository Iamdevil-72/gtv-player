package com.example.gtvplayer.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Geovid.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GeovidDAO geovidDAO();
    private static AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "VideoUpload")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
