package com.example.gtvplayer.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GeovidDAO {

    @Query("SELECT * FROM geovid")
    List<Geovid> getAllGeovids();

    @Insert
    void insertGeovid(Geovid... geovids);

}
