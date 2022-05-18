package com.example.gtvplayer.db;

import com.google.gson.annotations.SerializedName;

public class Properties{

	@SerializedName("altitude")
	private double altitude;

	@SerializedName("provider")
	private String provider;

	@SerializedName("time_long")
	private long timeLong;

	@SerializedName("accuracy")
	private double accuracy;

	@SerializedName("time")
	private String time;

	public double getAltitude(){
		return altitude;
	}

	public String getProvider(){
		return provider;
	}

	public long getTimeLong(){
		return timeLong;
	}

	public double getAccuracy(){
		return accuracy;
	}

	public String getTime(){
		return time;
	}
}