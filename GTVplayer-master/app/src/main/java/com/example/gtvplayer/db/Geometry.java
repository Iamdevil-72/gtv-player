package com.example.gtvplayer.db;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Geometry{

	@SerializedName("coordinates")
	private List<Double> coordinates;

	@SerializedName("type")
	private String type;

	public List<Double> getCoordinates(){
		return coordinates;
	}

	public String getType(){
		return type;
	}
}