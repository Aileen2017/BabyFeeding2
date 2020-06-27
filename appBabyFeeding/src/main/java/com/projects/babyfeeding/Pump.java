package com.projects.babyfeeding;

import java.sql.Timestamp;

public class Pump extends Event implements Comparable<Pump> {
	
	public int lDuration;
	public int rDuration;
	public Double LQuantity;
	public Double RQuantity;
	public Timestamp ts;

	public String note;
	public long id;
	
	public Pump()
	{
		
	}
	
	public Pump(Timestamp ts, int lDuration, int rDuration, Double LQuantity, Double RQuantity,String note)
	{
		this.lDuration=lDuration;
		this.rDuration=rDuration;
		this.LQuantity=LQuantity;
		this.RQuantity=RQuantity;
		
		this.note=note;
		this.ts=ts;
		
	}
	
	@Override
	public int compareTo(Pump another)
	{
		return ts.compareTo(another.ts);
	}

}
