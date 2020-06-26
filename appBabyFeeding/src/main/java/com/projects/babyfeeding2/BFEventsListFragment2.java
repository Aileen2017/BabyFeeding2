package com.projects.babyfeeding2;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import com.projects.babyfeeding2.R;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class BFEventsListFragment2 extends Fragment {
	
	/*public static final String ARG_PAGE = "page";
	public static final String ARG_DAY = "day";
	public static final String ARG_MONTH = "month";
	public static final String ARG_YEAR = "year";*/
	
    private int mPageNumber, day, month, year;
    private long dateL;
	private Vector<BabyFeedingEvent> merged;
	static final int LIST_BFEVENTS = 1;
	boolean ounce, hours24; 
	String locLanguage;
	private static Calendar refDate;
	
		public static Fragment create(int pageNumber,SharedPreferences sp,Locale locale, Calendar refDate_in)
		{
	        BFEventsListFragment2 fragment = new BFEventsListFragment2();

	        Bundle args = new Bundle();
	        args.putInt("ARG_PAGE", pageNumber);
	        int increment = pageNumber-251;
			refDate = (Calendar)refDate_in.clone();
			Calendar calendar = (Calendar)refDate_in.clone();
    		calendar.add(Calendar.DATE, increment);
    		int year=calendar.get(Calendar.YEAR);
    		int month=calendar.get(Calendar.MONTH);
    		int day=calendar.get(Calendar.DAY_OF_MONTH);
	        args.putInt("ARG_DAY", day);
	        args.putInt("ARG_MONTH", month);
	        args.putInt("ARG_YEAR", year);
	        args.putBoolean("OUNCE",sp.getString("Volumn_Unit", "").equalsIgnoreCase("2"));
	        args.putBoolean("24Hours", sp.getString("Time_Type", "").equalsIgnoreCase("2"));
	        args.putString("LOCALE",locale.getLanguage());
	        fragment.setArguments(args);
	        return fragment;
	    }
	   
	    public BFEventsListFragment2() {
	    }

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mPageNumber = getArguments().getInt("ARG_PAGE");
	        day=getArguments().getInt("ARG_DAY");
	        month=getArguments().getInt("ARG_MONTH");
	        year=getArguments().getInt("ARG_YEAR");
	        ounce=getArguments().getBoolean("OUNCE", ounce);
	        hours24=getArguments().getBoolean("24Hours",hours24);
	        locLanguage=getArguments().getString("LOCALE");
	    }

	    @SuppressLint("ResourceAsColor")
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        // Inflate the layout containing a title and body text.
	        ViewGroup rootView = (ViewGroup) inflater
	                .inflate(R.layout.bfeventslist_fragment2, container, false);
	        ListView listView = (ListView) rootView.findViewById(R.id.list1);
	        Calendar today = Calendar.getInstance();
			today.set(Calendar.MILLISECOND,0);
			today.set(Calendar.SECOND,0);
			today.set(Calendar.MINUTE,0);
			today.set(Calendar.HOUR,0);
			Calendar pagerDay = Calendar.getInstance();

			pagerDay.set(Calendar.DAY_OF_MONTH, day);
			pagerDay.set(Calendar.MONTH, month);
			pagerDay.set(Calendar.YEAR, year);
			pagerDay.set(Calendar.MILLISECOND,0);
			pagerDay.set(Calendar.SECOND,0);
			pagerDay.set(Calendar.MINUTE,0);
			pagerDay.set(Calendar.HOUR,0);
			int increment = 251 - mPageNumber;
			pagerDay.add(Calendar.DATE, increment);
			long laggedNumber = TimeUnit.DAYS.convert((today.getTime().getTime() - pagerDay.getTime().getTime()),  TimeUnit.MILLISECONDS);

	        if(mPageNumber==(251+(int)laggedNumber))
	        {	listView.setBackgroundColor(R.color.red);
	        	listView.getBackground().setAlpha(51);
	        }
	        // Set the title view to show the page number.
	       
	        
	        listView.setOnItemClickListener(new OnItemClickListener(){
	        	
	        	public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id)
	        	{
	        		
	        		BabyFeedingEvent bfe= (BabyFeedingEvent) merged.get(position);
	        		
	        		
	        		if(isFeedingEvent(bfe))
	        		{	
	        			long idBF=bfe.id;
	        			Uri uri=ContentUris.withAppendedId(FeedingEventsContract.Feeds.CONTENT_ID_URI_BASE, idBF);
	        			// Gets the action from the incoming Intent
		        		String action = getActivity().getIntent().getAction();
		        		// Handles requests for note data
		        		if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) 
		        		{

		                // Sets the result to return to the component that called this Activity. The
		                // result contains the new URI
		        			getActivity().setResult(getActivity().RESULT_OK, new Intent().setData(uri));
		            	} 
		        		else {

		                // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
		                // Intent's data is the note ID URI. The effect is to call NoteEdit.
		        			try{
		            
			            		Intent intent = new Intent(getActivity(), BreastFeedingActivity.class);
			            		intent.setAction(Intent.ACTION_EDIT);
			            		intent.setData(uri);
			            		intent.putExtra("position", mPageNumber);
			            		intent.putExtra("year", year);
					    		intent.putExtra("month", month);
					    		intent.putExtra("day", day);
			            		
			            		startActivity(intent);
		            	
		        			}
			            	catch( ActivityNotFoundException e)
			            	{
			            		e.printStackTrace();
			            	}
		        		}
	        		}
	        			
	        		if(isPumpEvent(bfe))
	        		{
	        			long idBP=bfe.id;
	        			Uri uri=ContentUris.withAppendedId(FeedingEventsContract.BreastPumpEvents.CONTENT_ID_URI_BASE, idBP);
	        			// Gets the action from the incoming Intent
		        		String action = getActivity().getIntent().getAction();
		        		// Handles requests for note data
		        		if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) 
		        		{

		                // Sets the result to return to the component that called this Activity. The
		                // result contains the new URI
		        			getActivity().setResult(getActivity().RESULT_OK, new Intent().setData(uri));
		            	} 
		        		else {

		                // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
		                // Intent's data is the note ID URI. The effect is to call NoteEdit.
		        			try{
		            
			            		Intent intent = new Intent(getActivity(), BreastPumpActivity.class);
			            		intent.setAction(Intent.ACTION_EDIT);
			            		intent.setData(uri);
			            		intent.putExtra("position", mPageNumber);
			            		intent.putExtra("year", year);
					    		intent.putExtra("month", month);
					    		intent.putExtra("day", day);
			            		startActivity(intent);
		            	
		        			}
			            	catch( ActivityNotFoundException e)
			            	{
			            		e.printStackTrace();
			            	}
		        		}
	          
	            	}
	        		if(isNappyEvent(bfe))
	        		{
	        			long idBN=bfe.id;
	        			Uri uri=ContentUris.withAppendedId(FeedingEventsContract.NapEvents.CONTENT_ID_URI_BASE, idBN);
	        			// Gets the action from the incoming Intent
		        		String action = getActivity().getIntent().getAction();
		        		// Handles requests for note data
		        		if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) 
		        		{

		                // Sets the result to return to the component that called this Activity. The
		                // result contains the new URI
		        			getActivity().setResult(getActivity().RESULT_OK, new Intent().setData(uri));
		            	} 
		        		else {

		                // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
		                // Intent's data is the note ID URI. The effect is to call NoteEdit.
		        			try{
		            
			            		Intent intent = new Intent(getActivity(), BabyNappyActivity.class);
			            		intent.setAction(Intent.ACTION_EDIT);
			            		intent.setData(uri);
			            		intent.putExtra("position", mPageNumber);
			            		intent.putExtra("year", year);
					    		intent.putExtra("month", month);
					    		intent.putExtra("day", day);
			            		startActivity(intent);
		            	
		        			}
			            	catch( ActivityNotFoundException e)
			            	{
			            		e.printStackTrace();
			            	}
		        		}
	          
	            	}
	        		if(isCustomerEvent(bfe))
	        		{
	        			long idBN=bfe.id;
	        			Uri uri=ContentUris.withAppendedId(FeedingEventsContract.CustomerEvents.CONTENT_ID_URI_BASE, idBN);
	        			// Gets the action from the incoming Intent
		        		String action = getActivity().getIntent().getAction();
		        		// Handles requests for note data
		        		if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) 
		        		{

		                // Sets the result to return to the component that called this Activity. The
		                // result contains the new URI
		        			getActivity().setResult(getActivity().RESULT_OK, new Intent().setData(uri));
		            	} 
		        		else {

		                // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
		                // Intent's data is the note ID URI. The effect is to call NoteEdit.
		        			try{
		            
			            		Intent intent = new Intent(getActivity(),  CustomerEventActivity.class);
			            		intent.setAction(Intent.ACTION_EDIT);
			            		intent.setData(uri);
			            		intent.putExtra("position", mPageNumber);
			            		intent.putExtra("year", year);
					    		intent.putExtra("month", month);
					    		intent.putExtra("day", day);
			            		startActivity(intent);
		            	
		        			}
			            	catch( ActivityNotFoundException e)
			            	{
			            		e.printStackTrace();
			            	}
		        		}
	          
	            	}
	        		
	        	
	        	 }
	        	
	        });
	       
	        Intent intent= (Intent) getActivity().getIntent();
	        if (intent.getData() == null)
	        	{
	            	intent.setData(FeedingEventsContract.DB_URI);
	        	}
	      
	        Vector<BabyFeedingEvent> bfeV=getBFEV(intent,mPageNumber);
	        Vector<BabyFeedingEvent> bpeV=getBPEV(intent, mPageNumber);
	        Vector<BabyFeedingEvent> bnV=getNE(intent, mPageNumber);
	        Vector<BabyFeedingEvent> bceV=getBCEV(intent, mPageNumber);
	         merged=getMergedVector(bfeV,bpeV, bnV, bceV);
	       ArrayList<HashMap<String,String>> bfevList= getBFEVList(merged);  
	       SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), bfevList, R.layout.list_row2,
	               new String[] {"Time", "EventType", "Duration", "Quantity"}, new int[] {R.id.Time_CELL, R.id.EventType_CELL, R.id.Duration_CELL, R.id.Quantity_CELL});
	       listView.setAdapter(mSchedule);
	 
	        return rootView;
	    }
	    
	    protected boolean isFeedingEvent(BabyFeedingEvent bfe)
	    {
	    		boolean isFE=false;
	    		if(bfe.eventType.equals(BabyFeedingEventsFactory.leftBreastEvent))
	    			isFE=true;
	    		if(bfe.eventType.equals(BabyFeedingEventsFactory.rightBreastEvent))
	    			isFE=true;
	    		if(bfe.eventType.equals(BabyFeedingEventsFactory.bottleFeedEvent))
	    			isFE=true;
	    		if(bfe.eventType.equals(BabyFeedingEventsFactory.expressFeedEvent))
	    			isFE=true;
	    		return isFE;
	    }
	    
	    protected boolean isPumpEvent(BabyFeedingEvent bfe)
	    {
	    	boolean isPE=false;
	    	if(bfe.eventType.equals(BabyFeedingEventsFactory.leftPumpEvent))
    			isPE=true;
    		if(bfe.eventType.equals(BabyFeedingEventsFactory.rightPumpEvent))
    			isPE=true;
    		return isPE;
	    }
	    
	    protected boolean isNappyEvent(BabyFeedingEvent bfe)
	    {
	    	boolean isNappy=false;
	    	if(bfe.eventType.equals(BabyFeedingEventsFactory.nappyEvent))
    			isNappy=true;
 
    		return isNappy;
	    }
	    
	    protected boolean isCustomerEvent(BabyFeedingEvent bfe)
	    {
	    	boolean isCE=false;
	    	if(bfe.eventType.equals(BabyFeedingEventsFactory.customEvent))
    			isCE=true;
 
    		return isCE;
	    }
	    
	    public ArrayList<HashMap<String, String>> getBFEVList(Vector<BabyFeedingEvent> merged) 
	    {	
	    	ArrayList<HashMap<String, String>> mergedList=new ArrayList<HashMap<String, String>>();
	    	if(merged!=null)
	    	{	Iterator itr = merged.iterator();
		        while(itr.hasNext())
		        {
		        	HashMap<String, String> bfeHm=new HashMap<String,String>();
		        	BabyFeedingEvent bfe=(BabyFeedingEvent)itr.next();
		        	String eventType=bfe.eventType;
		        	int drawbleId=findDrawable(eventType);
		        	
		        	Timestamp ts=bfe.ts;
		        	Date dt=new Date(ts.getTime()); 
		        	SimpleDateFormat df=new SimpleDateFormat("HH:mm");
		        	SimpleDateFormat df12=new SimpleDateFormat("hh:mm a");
		        	String durationS="", quantityS="";
		        	
		        	
		        	
		        	if(eventType.equals(BabyFeedingEventsFactory.nappyEvent))
		        	{	if(bfe.duration==1)
		        			durationS=getString(R.string.Wet);
		        		if(bfe.duration==2)
		        			durationS=getString(R.string.Dirty);
		        	}
		        	else if(eventType.equals(BabyFeedingEventsFactory.customEvent)) //customerevent
		        	{
		        		durationS= bfe.ceTitle;
		        	}
		        	else
		        	{	
		        		if(bfe.duration>0)
		        		{
		        			if(locLanguage.contains("en"))
		        				durationS=Integer.toString(bfe.duration)+"min";
		        			else if(locLanguage.contains("zh"))
		        				durationS=Integer.toString(bfe.duration)+"分钟";
		        			else if(locLanguage.contains("fr"))
		        				durationS=Integer.toString(bfe.duration)+"min";
		        			else
		        				durationS=Integer.toString(bfe.duration)+"min";
		        		}
		        		if(bfe.quantity>0)
		        		{	if(ounce)
		        			{		
		        				Double tempq=bfe.quantity/30.00;
		        				String tempqs=Formatting.getDeci2().format(tempq);
		        				
		        				if(locLanguage.contains("en"))
		        					quantityS=tempqs+"oz";
		        				else if(locLanguage.contains("zh"))
		        					quantityS=tempqs+"盎司";
		        				else if(locLanguage.contains("fr"))
		        					quantityS=tempqs+"oz";
		        				else
		        					quantityS=tempqs+"oz";
		        			}
		        			else
		        			{
		        				String tempqs=Formatting.getDeci0().format(bfe.quantity);
		        				
		        				if(locLanguage.contains("en"))
		        					quantityS=tempqs+"ml";
		        				else if(locLanguage.contains("zh"))
		        					quantityS=tempqs+"毫升";
		        				else if(locLanguage.contains("fr"))
		        					quantityS=tempqs+"ml";
		        				else
		        					quantityS=tempqs+"ml";
		        				
		        				
		        			}
		        		}
		        	}
		        	String displayedTime;
		        	if(hours24)
		        		displayedTime=df.format(dt);
		        	else
		        		displayedTime=df12.format(dt);
		        	
		        	
		        	bfeHm.put("Time", displayedTime);
	
		        	bfeHm.put("Duration", durationS);
		        	bfeHm.put("Quantity", quantityS);
		        	bfeHm.put("EventType", Integer.toString(drawbleId));
		        	
		        	
		        	mergedList.add(bfeHm);
		        }	
	    	}
	    	else 
	    		mergedList=null;
	        return mergedList;
	    }

	    public int findDrawable(String eventType)
	    {
	    	if(eventType.equals(BabyFeedingEventsFactory.leftBreastEvent))
	    		return R.drawable.drop128_left;
	    	else if(eventType.equals(BabyFeedingEventsFactory.rightBreastEvent))
	    		return R.drawable.drop128_right;
	    	else if(eventType.equals(BabyFeedingEventsFactory.bottleFeedEvent))
	    		return R.drawable.bottle128;
	    	else if(eventType.equals(BabyFeedingEventsFactory.expressFeedEvent))
	    		return R.drawable.express128;
	    	else if(eventType.equals(BabyFeedingEventsFactory.leftPumpEvent))
	    		return R.drawable.express128_left;
	    	else if(eventType.equals(BabyFeedingEventsFactory.rightPumpEvent))
	    		return R.drawable.express128_right;
	    	else if(eventType.equals(BabyFeedingEventsFactory.nappyEvent))
	    		return R.drawable.pin128;
	    	else if(eventType.equals(BabyFeedingEventsFactory.customEvent))
	    		return R.drawable.notebook128;
	    	else
	    		return 0;
	    }
	    
	    
	    public Vector<BabyFeedingEvent> getMergedVector(Vector<BabyFeedingEvent> bfV, Vector<BabyFeedingEvent> bpV, Vector<BabyFeedingEvent> nV, Vector<BabyFeedingEvent> bceV)
	    {
	    	Vector<BabyFeedingEvent> merged=new Vector<BabyFeedingEvent>();
	    	if(bfV!=null)
	    		merged.addAll(bfV);
	    	if(bpV!=null)
	    		merged.addAll(bpV);
	    	if(nV!=null)
	    		merged.addAll(nV);
	    	if(bceV!=null)
	    		merged.addAll(bceV);
	    	Collections.sort(merged);
	    	//merged.addAll(nV);
	    	return merged;
	    }
	    
	    public Vector<BabyFeedingEvent> getNE(Intent intent, int position)
	    {
	    	 	String basePath=intent.getData().toString();
		        
		        Uri nappyPath =  Uri.parse(basePath+FeedingEventsContract.NapEvents.PATH_Nappys);   
		    	
		         
		    	final String[] PROJECTION =
		                new String[] {
		                    FeedingEventsContract.NapEvents.column_timeStamp,
		                    FeedingEventsContract.NapEvents.column_wet,
		                    FeedingEventsContract.NapEvents.column_dirty,
		                    FeedingEventsContract.NapEvents._ID,
		                    FeedingEventsContract.NapEvents.column_note
		            };
		       
		        	Cursor cursor = null;
		       	
		        	Timestamp[] ts=getDate(position);	
		        	cursor= getActivity().getContentResolver().query(nappyPath, PROJECTION, 
			        		FeedingEventsContract.NapEvents.column_timeStamp +  
			        		" between '"+ts[0] + "'  and '"+ts[1] + "'",
			        		null,null)	;
		    
		       BabyFeedingEventsFactory bfef= new BabyFeedingEventsFactory();
		       Vector<BabyFeedingEvent> napV= bfef.generateBNs(cursor);
		       if((napV!=null)&&(napV.size()>0))
		   	   
		    	 //  Log.i("Vector:", napV.get(0).toString());
		    	   return napV;
		       else
		    	   return null;
	    }
	    
	    
	    public Vector<BabyFeedingEvent> getBFEV(Intent intent, int position)
	    {
	       
	        String basePath=intent.getData().toString();
	        String PATH_Feeds = "/Feeds";
	       
	        Uri feedPath =  Uri.parse(basePath+PATH_Feeds);
	         
	    	final String[] PROJECTION =
	                new String[] {
	                    FeedingEventsContract.Feeds.column_timeStamp,
	                    FeedingEventsContract.Feeds.column_leftDuration,
	                    FeedingEventsContract.Feeds.column_rightDuration,
	                    FeedingEventsContract.Feeds.column_bottleFeed,
	                    FeedingEventsContract.Feeds.column_expressFeed,
	                    FeedingEventsContract.Feeds._ID
	            };
	       
	        	Cursor cursor = null;
	       	
	        	Timestamp[] ts=getDate(position);	
	        	cursor= getActivity().getContentResolver().query(feedPath, PROJECTION, 
		        		FeedingEventsContract.Feeds.column_timeStamp +  
		        		" between '"+ts[0] + "'  and '"+ts[1] + "'",
		        		null,null)	;
	    
	       BabyFeedingEventsFactory bfef= new BabyFeedingEventsFactory();
	       Vector<BabyFeedingEvent> brfeV= bfef.generateBFEs(cursor);
	       if((brfeV!=null)&&(brfeV.size()>0))
	   	   
	    	  // Log.i("Vector:", brfeV.get(0).toString());
	    	   return brfeV;
	       else
	    	   return null;
	      
	    }
	    
	    public Vector<BabyFeedingEvent> getBPEV(Intent intent, int position)
	    {
	    	String basePath=intent.getData().toString();
	       
	       
	        Uri pumpPath =  Uri.parse(basePath+FeedingEventsContract.BreastPumpEvents.PATH_BreastPumps);   
	    	
	    	final String[] PROJECTION =
		                new String[] {
		                    FeedingEventsContract.BreastPumpEvents.column_timeStamp,
		                    
		                    FeedingEventsContract.BreastPumpEvents.column_leftDuration,
		                    FeedingEventsContract.BreastPumpEvents.column_rightDuration,
		                    FeedingEventsContract.BreastPumpEvents.column_leftVolumn,
		                    FeedingEventsContract.BreastPumpEvents.column_rightVolumn,
		                    FeedingEventsContract.Feeds._ID
		            };
		       
		        	Cursor cursor = null;
		       	
		        	Timestamp[] ts=getDate(position);	
		        	cursor= getActivity().getContentResolver().query(pumpPath, PROJECTION, 
			        		FeedingEventsContract.BreastPumpEvents.column_timeStamp +  
			        		" between '"+ts[0] + "'  and '"+ts[1] + "'",
			        		null,null)	;
		    
		       BabyFeedingEventsFactory bfef= new BabyFeedingEventsFactory();
		       Vector<BabyFeedingEvent> bpV= bfef.generateBPEs(cursor);
		       if((bpV!=null)&&(bpV.size()>0))
		   	   
		    	   //Log.i("Vector:", bpV.get(0).toString());
		    	   return bpV;
		       else
		    	   return null;
	    }
	    
	    
	    public Vector<BabyFeedingEvent> getBCEV(Intent intent, int position)
	    {
	    	String basePath=intent.getData().toString();
	       
	       
	        Uri cePath =  Uri.parse(basePath+FeedingEventsContract.CustomerEvents.PATH_CustomerEvents);   
	    	
	    	final String[] PROJECTION =
		                new String[] {
		                    FeedingEventsContract.CustomerEvents.column_timeStamp,
		                    
		                    FeedingEventsContract.CustomerEvents.column_title,
		                    FeedingEventsContract.CustomerEvents.column_description,
		                    
		                    FeedingEventsContract.CustomerEvents._ID
		            };
		       
		        	Cursor cursor = null;
		       	
		        	Timestamp[] ts=getDate(position);	
		        	cursor= getActivity().getContentResolver().query(cePath, PROJECTION, 
			        		FeedingEventsContract.CustomerEvents.column_timeStamp +  
			        		" between '"+ts[0] + "'  and '"+ts[1] + "'",
			        		null,null)	;
		    
		       BabyFeedingEventsFactory bfef= new BabyFeedingEventsFactory();
		       Vector<BabyFeedingEvent> bceV= bfef.generateBCEs(cursor);
		       if((bceV!=null)&&(bceV.size()>0))
		   	   
		    	   //Log.i("Vector:", bpV.get(0).toString());
		    	   return bceV;
		       else
		    	   return null;
	    }
	    
	    
	    public Timestamp[] getDate(int position)
	    {
	    	Timestamp[] ts=new Timestamp[2];
	    	Calendar calendar = (Calendar)refDate.clone();
	    	if(position!=251)  
	    	{	

	    		int increment=position-251;
	    		calendar.add(Calendar.DATE, increment);
	    	}

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

}
