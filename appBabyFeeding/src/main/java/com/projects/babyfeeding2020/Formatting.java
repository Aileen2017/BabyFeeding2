package com.projects.babyfeeding2020;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.XYSeries;

public class Formatting
{
	static NumberFormat getDeci2()
	{
		if(deci2 == null)
		{
			deci2 = NumberFormat.getInstance();
			if(deci2 instanceof DecimalFormat)
			{
				((DecimalFormat)deci2).applyPattern("##.##");
			}
		}
		
		return deci2;
	}
	
	static NumberFormat getDeci1()
	{
		if(deci1 == null)
		{
			deci1 = NumberFormat.getInstance();
			if(deci1 instanceof DecimalFormat)
			{
				((DecimalFormat)deci1).applyPattern("##.#");
			}
		}
		
		return deci1;
	}

	static NumberFormat getDeci0()
	{
		if(deci0 == null)
		{
			deci0 = NumberFormat.getInstance();
			if(deci0 instanceof DecimalFormat)
			{
				((DecimalFormat)deci0).applyPattern("##");
			}
		}
		
		return deci0;
	}
	
	static PointLabeler getPl1()
	{
		return pl1;
	}

	static private NumberFormat deci2;
	static private NumberFormat deci1;
	static private NumberFormat deci0;
	static private PointLabeler pl1 = new PointLabeler()
	{
		public String getLabel(XYSeries series, int index)
		{
			return getDeci1().format(series.getY(index));
		}
	};
}
