package com.projects.babyfeeding;

import java.sql.Timestamp;

public class Nappy  extends Event implements Comparable<Nappy>
{


	public Timestamp ts;
	public boolean wet, dirty;
	public String note;
	public long id;
	
	public Nappy()
	{
		
	}
	
	public Nappy(Timestamp ts, boolean wet, boolean dirty, String note)
	{
		this.wet=wet;
		this.dirty=dirty;
		
		this.note=note;
		this.ts=ts;
		
	}
	
	@Override
	public int compareTo(Nappy another)
	{
		return ts.compareTo(another.ts);
	}
}
