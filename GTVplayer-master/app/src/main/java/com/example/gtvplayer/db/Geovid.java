package com.example.gtvplayer.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Geovid {

    @PrimaryKey
    @NonNull
    public String docid;

    @ColumnInfo(name="video_link")
    public String videoLink;

    @ColumnInfo(name="gps_link")
    public String gpsLink;

    @ColumnInfo(name="lVideo_path")
    public String lvideoppath;

    @ColumnInfo(name="lGps_path")
    public String lgpspath;
}
