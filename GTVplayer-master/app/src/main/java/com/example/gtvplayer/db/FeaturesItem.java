package com.example.gtvplayer.db;

import com.google.gson.annotations.SerializedName;

public class FeaturesItem{

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("type")
	private String type;

	@SerializedName("properties")
	private Properties properties;

	public Geometry getGeometry(){
		return geometry;
	}

	public String getType(){
		return type;
	}

	public Properties getProperties(){
		return properties;
	}
}