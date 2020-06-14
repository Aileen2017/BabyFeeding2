package com.projects.babyfeeding2;

import java.sql.Timestamp;

public class CustomerEvent extends Event implements Comparable<CustomerEvent>
{
	
	public Timestamp ts;

	public String title, description;
	public long id;

	
	public CustomerEvent()
	{
		
	}
	
	public CustomerEvent(Timestamp ts, String title, String description)
	{
		this.title=title;
		this.description=description;
		
		this.ts=ts;
		
	}

	
	@Override
	public int compareTo(CustomerEvent another)
	{
		return ts.compareTo(another.ts);
	}
	

}
