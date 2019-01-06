package models;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Order {
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@JSONField (format="yyyy-MM-dd HH:mm:ss")
	private Date stopTime;
	private double[] startLocation;
	private double[] stopLocation;
	public static final double gps2m = 88513.16296916528;
	
	public Order() {
		
	}
	
	public Order(Date startTime, Date stopTime, double[] startLocation, double[] stopLocation) 
	{
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.startTime = startTime;
		this.stopTime = stopTime;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getStopTime() {
		return stopTime;
	}

	public double[] getStartLocation() {
		return startLocation;
	}
	
	public double[] getStopLocation() {
		return stopLocation;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public void setStartLocation(double[] startLocation) {
		this.startLocation = startLocation;
	}
	
	public void setStopLocation(double[] stopLocation) {
		this.stopLocation = stopLocation;
	}
	
	public long getDuration()
	{
		return stopTime.getTime() - startTime.getTime();
	}
	
	public double getDistance() {
		double x = startLocation[0]-stopLocation[0];
		double y = startLocation[1]-stopLocation[1];
		return Math.sqrt(x*x+y*y)*gps2m;
	}
	
	
	
	
	
	
}
