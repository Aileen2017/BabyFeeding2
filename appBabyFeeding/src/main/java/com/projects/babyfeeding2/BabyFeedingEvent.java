package com.projects.babyfeeding2;

import java.sql.Timestamp;

public class BabyFeedingEvent  implements Comparable<BabyFeedingEvent>{
	
	
	public int duration;
	public double quantity;
	public Timestamp ts;
	public String eventType;
	public String ceTitle;
	
	//public Uri uri;
	public long id;
	
	public BabyFeedingEvent(Timestamp ts, int duration, double quantity, String eventType, String cet, long id)
	{
		this.duration=duration;
		this.quantity=quantity;
		this.eventType=eventType;
		this.ts=ts;
		this.id=id;
		this.ceTitle=cet;
	}
	
	public BabyFeedingEvent(Timestamp ts, int duration, double quantity, String eventType,  long id)
	{
		this.duration=duration;
		this.quantity=quantity;
		this.eventType=eventType;
		this.ts=ts;
		this.id=id;
		this.ceTitle="";
		
	}
	
	
	@Override
	public int compareTo(BabyFeedingEvent another)
	{
		return ts.compareTo(another.ts);
	}

}