package com.example.gtvplayer.db;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GPSData{

	@SerializedName("features")
	private List<FeaturesItem> features;

	@SerializedName("type")
	private String type;

	public List<FeaturesItem> getFeatures(){
		return features;
	}

	public String getType(){
		return type;
	}
}