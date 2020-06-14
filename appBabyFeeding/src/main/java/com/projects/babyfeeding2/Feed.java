package com.projects.babyfeeding2;

import java.sql.Timestamp;

public class Feed  extends Event implements Comparable<Feed>{
	
	public int lDuration;
	public int rDuration;
	public Double BFQuantity;
	public Double EFQuantity;
	public Timestamp ts;
	public String first;
	public String second;
	public String third;
	public String fourth;
	public String note;
	public long id;
	
	public Feed()
	{
		
	}
	public Feed(Timestamp ts, int lDuration, int rDuration,Double BFQuantity, Double EFQuantity,String note)
	{
		this.lDuration=lDuration;
		this.rDuration=rDuration;
		this.BFQuantity=BFQuantity;
		this.EFQuantity=EFQuantity;
		
		this.note=note;
		this.ts=ts;
		
	}
	

	@Override
	public int compareTo(Feed another)
	{
		return ts.compareTo(another.ts);
	}
}
