package com.projects.babyfeeding2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewPager;

public class BFUtilities {

	
	
	public static Calendar getCurrentPageDate(ViewPager mPager, Calendar refDate, int refPosition)
	{ 
		int position = mPager.getCurrentItem();
		Calendar calendar=(Calendar)refDate.clone();
		//calendar = Calendar.getInstance();
		int increment=position-refPosition;
		calendar.add(Calendar.DATE, increment);
		return calendar;
	}
	
	public static String getSummaryString(HashMap<String, String> hm, Context context, Boolean ounce)
	{
		
		String totalFeedTime=hm.get("totalFeedTime");
		String totalFeedVolumn=hm.get("totalFeedVolumn");
		String totalLDuration=hm.get("totalLDuration");
		String totalRDuration=hm.get("totalRDuration");
		String totalBottleFeed=hm.get("totalBottleFeed");
		String totalExpressFeed=hm.get("totalExpressFeed");
		String totalLBusage=hm.get("totalLBUsage");
		String totalRBusage=hm.get("totalRBUsage");
		String totalPLDuration=hm.get("totalPLDuration");
		String totalPRDuration=hm.get("totalPRDuration");
		String totalPLVolumn=hm.get("totalPLVolumn");
		String totalPRVolumn=hm.get("totalPRVolumn");
		
		String s1=context.getString(R.string.totalFeedS1);
		String s2=context.getString(R.string.minsLeftS2);
		String s3=context.getString(R.string.minsRightS3);
		String s4=context.getString(R.string.endBracketS4);
		String s5, s15, s18;
		if(ounce)
		{	
			s5=context.getString(R.string.mlsBottleS5Ounce);
			s15=context.getString(R.string.plmlsS15Ounce);
			s18=context.getString(R.string.prmlsS18Ounce);
		}
		else
		{	s5=context.getString(R.string.mlsBottleS5);
			s15=context.getString(R.string.plmlsS15);
			s18=context.getString(R.string.prmlsS18);
		}
		
		String s6=context.getString(R.string.expressS6);
		String s7=context.getString(R.string.endBracket2S7);
		String s8=context.getString(R.string.breastUsageS8);
		String s9=context.getString(R.string.leftS9);
		String s10=context.getString(R.string.minsS10);
		String s11=context.getString(R.string.rightS11);
		String s12=context.getString(R.string.mins2S12);
		String s13=context.getString(R.string.pumpLeftS13);
		String s14=context.getString(R.string.plminsS14);
		
		
		String s16=context.getString(R.string.prmlsS16);
		String s17=context.getString(R.string.prmlsS17);
		
		
		
		String text1=s1+totalFeedTime+s2+
				totalLDuration+ s3+
				totalRDuration+s4+
				totalFeedVolumn+s5 +
				totalBottleFeed+s6+
				totalExpressFeed + s7+
				s8+s9+ totalLBusage+s10+
				s11+totalRBusage+s12+
				s13+totalPLDuration+
				s14+totalPLVolumn+
				s15+
				s16+totalPRDuration+
				s17+totalPRVolumn+s18;
					
		return text1;
	}
	
	public static Timestamp[] getDate(Calendar calendar)
	{
		Timestamp[] ts=new Timestamp[2];

		Calendar beginningOfDay = Calendar.getInstance();
		Calendar endOfDay = Calendar.getInstance();

		beginningOfDay.setTime(calendar.getTime());
		beginningOfDay.set(Calendar.HOUR_OF_DAY, 0);
		beginningOfDay.set(Calendar.MINUTE, 0);
		beginningOfDay.set(Calendar.SECOND, 0);
		beginningOfDay.set(Calendar.MILLISECOND, 0);

		endOfDay.setTime(calendar.getTime());
		endOfDay.set(Calendar.HOUR_OF_DAY, 23);
		endOfDay.set(Calendar.MINUTE, 59);
		endOfDay.set(Calendar.SECOND, 59);
		endOfDay.set(Calendar.MILLISECOND, 999);

		Timestamp tsBeginningofDay=new Timestamp(beginningOfDay.getTimeInMillis());
		Timestamp tsEndofDay=new Timestamp(endOfDay.getTimeInMillis());
		ts[0]=tsBeginningofDay;
		ts[1]=tsEndofDay;


		return ts;
	}
	
	
	public static Timestamp[] getHour(Calendar calendar)
	{
		Timestamp[] ts=new Timestamp[2];

		Calendar beginningOfHour = Calendar.getInstance();
		Calendar endOfHour = Calendar.getInstance();

		beginningOfHour.setTime(calendar.getTime());
		beginningOfHour.set(Calendar.MINUTE, 0);
		
		beginningOfHour.set(Calendar.SECOND, 0);
		beginningOfHour.set(Calendar.MILLISECOND, 0);

		endOfHour.setTime(calendar.getTime());
		
		endOfHour.set(Calendar.MINUTE, 59);
		endOfHour.set(Calendar.SECOND, 59);
		endOfHour.set(Calendar.MILLISECOND, 999);

		Timestamp tsBeginningofDay=new Timestamp(beginningOfHour.getTimeInMillis());
		Timestamp tsEndofDay=new Timestamp(endOfHour.getTimeInMillis());
		ts[0]=tsBeginningofDay;
		ts[1]=tsEndofDay;


		return ts;
	}
	
	
	public static Timestamp[] getPreDaysDates(Calendar current, int increment)
	{
		
    	Calendar currentDate= (Calendar)current.clone();
    	Calendar preDaysDate=Calendar.getInstance();
    	preDaysDate.add(currentDate.DATE, increment);
    	preDaysDate.set(Calendar.HOUR_OF_DAY, 0);
		preDaysDate.set(Calendar.MINUTE, 0);
		preDaysDate.set(Calendar.SECOND, 0);
		preDaysDate.set(Calendar.MILLISECOND, 0);

		currentDate.add(currentDate.DATE, -1);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.MILLISECOND, 999);
    	
		Timestamp ts[] = new Timestamp[2];
		Timestamp tsLastDay=new Timestamp(currentDate.getTimeInMillis());
		Timestamp tsFirstDay=new Timestamp(preDaysDate.getTimeInMillis());
		ts[0]=tsFirstDay;
		ts[1]=tsLastDay;

		return ts;
	}
	
	
	
	public static HashMap<String,String> getPumpsandFeedsData(Context context, Timestamp[] ts,boolean ounce)
	{
		int totalFeedTime=0,  totalLBUsage=0, totalRBUsage=0; 
		double totalFeedVolumn=0; double totalBottleFeed=0, totalExpressFeed=0;
		int totalLDuration=0, totalRDuration=0;
		int totalPLDuration=0; int totalPRDuration=0;
		int totalPLVolumn=0; int totalPRVolumn=0;
		String basePath=FeedingEventsContract.DB_URI.toString();
		HashMap<String, String> hm=new HashMap<String, String>();
		String PATH_Feeds = "/Feeds";
		String PATH_Pumps="/BreastPumpEvents";
		Uri feedPath =  Uri.parse(basePath+PATH_Feeds);
		Uri pumpPath=Uri.parse(basePath+PATH_Pumps);
		String[] projection =
				new String[] {
				"sum(" + FeedingEventsContract.Feeds.column_leftDuration + ") as leftDuration",
				"sum(" + FeedingEventsContract.Feeds.column_rightDuration + ") as rightDuration",
				"sum(" + FeedingEventsContract.Feeds.column_bottleFeed + ") as bottleFeed",
				"sum(" + FeedingEventsContract.Feeds.column_expressFeed + ") as expressFeed"
		};
		String[] projection2 =
				new String[] {
				"sum(" + FeedingEventsContract.BreastPumpEvents.column_leftDuration + ") as leftDuration",
				"sum(" + FeedingEventsContract.BreastPumpEvents.column_rightDuration + ") as rightDuration",
				"sum(" + FeedingEventsContract.BreastPumpEvents.column_leftVolumn+") as leftVolumn",
				"sum(" + FeedingEventsContract.BreastPumpEvents.column_rightVolumn+") as rightVolumn"
				
		};
		
		
		String selection=FeedingEventsContract.BreastPumpEvents.column_timeStamp +  
				" between '"+ts[0] + "'  and '"+ts[1] + "'";

		Cursor cursor=context.getContentResolver().query(feedPath, projection, selection, null, null);
		
		if(cursor!=null)
		{
			int totalFLDuration=0; int totalFRDuration=0;
			double totalBotQuan=0;
			double totalExpQuan=0;
			cursor.moveToFirst();
			if(!cursor.isAfterLast())
			{ 
				int ldColIndex = cursor.getColumnIndexOrThrow("leftDuration");
				int lduration=cursor.getInt(ldColIndex);
				totalFLDuration+=lduration;
				String s[]=cursor.getColumnNames();

				int rdColIndex = cursor.getColumnIndexOrThrow("rightDuration");
				int rduration=cursor.getInt(rdColIndex);
				totalFRDuration+=rduration;

				int rfColIndex=cursor.getColumnIndexOrThrow("bottleFeed");
				double bq=cursor.getDouble(rfColIndex);
				totalBotQuan+=bq;

				int lfColIndex=cursor.getColumnIndexOrThrow("expressFeed");
				double eq=cursor.getDouble(lfColIndex);
				totalExpQuan+=eq;

			}
			totalFeedVolumn=totalBotQuan+totalExpQuan;
			totalFeedTime=totalFLDuration+totalFRDuration;
			totalLBUsage+=totalFLDuration;
			totalRBUsage+=totalFRDuration;
			totalBottleFeed=totalBotQuan;
			totalExpressFeed=totalExpQuan;
			totalLDuration=totalFLDuration;
			totalRDuration=totalFRDuration;
			
		}
		Cursor cursor2=context.getContentResolver().query(pumpPath, projection2, selection, null, null);
		
		if(cursor2!=null)
		{
			int totalPLD=0;  int totalPRD=0; int totalPLV=0; int totalPRV=0;
			cursor2.moveToFirst();
			if(!cursor2.isAfterLast())
			{ 
				int ldColIndex = cursor2.getColumnIndexOrThrow("leftDuration");
				int lduration=cursor2.getInt(ldColIndex);
				totalPLD+=lduration;


				int rdColIndex = cursor2.getColumnIndexOrThrow("rightDuration");
				int rduration=cursor2.getInt(rdColIndex);
				totalPRD+=rduration;
				
				int rvColIndex = cursor2.getColumnIndexOrThrow("rightVolumn");
				int rvVolumn=cursor2.getInt(rvColIndex);
				totalPRV+=rvVolumn;
				
				int lvColIndex = cursor2.getColumnIndexOrThrow("leftVolumn");
				int lvVolumn=cursor2.getInt(lvColIndex);
				totalPLV+=lvVolumn;

			}
			
			totalPLDuration=totalPLD;
			totalPRDuration=totalPRD;
			
			totalLBUsage+=totalPLDuration;
			totalRBUsage+=totalPRDuration;
			
			totalPLVolumn=totalPLV;
			totalPRVolumn=totalPRV;
		}
		
		double tempTotalFeedVolumn;
		double tempTotalBottleFeed;
		double tempTotalExpressFeed;
		double tempTotalPLVolumn;
		double tempTotalPRVolumn;
		
		if(ounce)
		{
			tempTotalFeedVolumn=totalFeedVolumn/30.0;
			tempTotalBottleFeed=totalBottleFeed/30.0;
			tempTotalExpressFeed=totalExpressFeed/30.0;
			tempTotalPLVolumn=totalPLVolumn/30.0;
			tempTotalPRVolumn=totalPRVolumn/30.0;
			
		}
		else
		{
			tempTotalFeedVolumn=totalFeedVolumn;
			tempTotalBottleFeed=totalBottleFeed;
			tempTotalExpressFeed=totalExpressFeed;
			tempTotalPRVolumn=totalPRVolumn;
			tempTotalPLVolumn=totalPLVolumn;
		}

		hm.put("totalFeedVolumn",Formatting.getDeci2().format(tempTotalFeedVolumn));
		hm.put("totalFeedTime", Integer.toString(totalFeedTime));
		hm.put("totalPLDuration", Integer.toString(totalPLDuration));
		hm.put("totalPRDuration", Integer.toString(totalPRDuration));
		hm.put("totalLBUsage", Integer.toString(totalLBUsage));
		hm.put("totalRBUsage", Integer.toString(totalRBUsage));
		hm.put("totalBottleFeed", Formatting.getDeci2().format(tempTotalBottleFeed));
		hm.put("totalExpressFeed", Formatting.getDeci2().format(tempTotalExpressFeed));
		hm.put("totalLDuration", Integer.toString(totalLDuration));
		hm.put("totalRDuration", Integer.toString(totalRDuration));
		hm.put("totalPLVolumn", Formatting.getDeci2().format(tempTotalPLVolumn));
		hm.put("totalPRVolumn",Formatting.getDeci2().format(tempTotalPRVolumn));
		return hm;
	}
	
	
	
	
	
	
	
	public static String StreamToString(InputStream in)
	{
		StringBuffer strBuffer=null;
		
		int ch;
		try {
			strBuffer = new StringBuffer(in.available());
			boolean good = true;
			while(good)
			{
				
				ch=in.read();
				if(ch == -1)
				{
					good = false;
				}
				else
				{
					strBuffer.append((char)ch);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strBuffer.toString();
	}
	
	
	public static String StreamToString2(InputStream in)
	{
		 StringBuilder inputStringBuilder = new StringBuilder();
		
		int ch;
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(in, "UTF-8"));
			
			
			String line = br.readLine();
	        while(line != null){
	            inputStringBuilder.append(line);
	            inputStringBuilder.append('\n');
	            line = br.readLine();
	        }
     

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStringBuilder.toString();
	}
	
	
	
	public static String StreamToString(FileInputStream fin)
	{
		StringBuffer strBuffer=null;
		
		int ch;
		try {
			strBuffer = new StringBuffer(fin.available());
			boolean good = true;
			while(good)
			{
				ch=fin.read();
				if(ch == -1)
				{
					good = false;
				}
				else
				{
					strBuffer.append((char)ch);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strBuffer.toString();
	}
	
	public static boolean InstallApp(Context context)
	{
		PackageManager pm=context.getPackageManager();
    	String targetPackage= context.getString(R.string.targetPackage_name);
    	boolean found=false;
    	try{
    		PackageInfo info=pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
    		found=true;
    		
		} catch (NameNotFoundException e) 
		{
			
    	}  
    	
    	if(!found)
    	{ 	
    		Intent goToMarket = new Intent(Intent.ACTION_VIEW)
    	    .setData(Uri.parse(context.getString(R.string.market_name)));
    		context.startActivity(goToMarket);
    		return false;
    	}
    	else
    	{
    		
    	}
    	return true;
	}
	
	
	public static String ISToString(InputStream is)
	{
		StringBuffer strBuffer=null;
		byte[] buffer = new byte[10];
		int bytesRead;
		boolean finished=false;
		try {
			is.reset();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(!finished)
		{	try 
			{
				bytesRead = is.read(buffer);
				if(bytesRead==-1)
				{
					finished=true;
				}
				else
				{	
					String page = new String(buffer, 0, bytesRead, "UTF-8");
					strBuffer.append(page);
				}
			} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return strBuffer.toString();
	}
	
}
