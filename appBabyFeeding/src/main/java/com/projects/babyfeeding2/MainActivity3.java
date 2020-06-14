package com.projects.babyfeeding2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;


import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends FragmentActivity {

	private static final int NUM_PAGES =500;
	private PagerTitleStrip strip;
	static final int LIST_BFEVENTS = 1;
	private  int testi=0;
	private Menu menu = null;
	int year, day, month;
	int refPosition=251;
	final Calendar refDate = Calendar.getInstance();
	String errorMessage=null;
	boolean ounce=false;
	/**
	 * The pager widget, which handles animation and allows swiping horizontally to access previous
	 * and next wizard steps.
	 */
	private ViewPager mPager;
	SharedPreferences sharedPreferences;
	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private BFEventsListPagerAdapter mPagerAdapter;
	public void setErrorMessage(String message)
	{
		errorMessage=message;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity2);
		 sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		   
	     final String vup= sharedPreferences.getString("Volumn_Unit", "");
	     if(vup.equalsIgnoreCase("1"))
			{	
	    	  ounce=false;
			}
	    	if(vup.equalsIgnoreCase("2"))
			{	
	    		ounce=true;
			}
	       
		String locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayName();
		//Log.i("locale", locale);
		// Instantiate a ViewPager and a PagerAdapter.
		TextView tv=(TextView)findViewById(com.projects.babyfeeding2.R.id.txtViewSummary);

		//setMenu(menu);
		
		mPager = (ViewPager) findViewById(R.id.pager);
		strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		
		strip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);


		mPagerAdapter = new BFEventsListPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPagerAdapter.setCurrentPosition(251);
		mPager.setCurrentItem(251);
		
		
		Calendar calendar = BFUtilities.getCurrentPageDate(mPager, refDate, refPosition);
		year=calendar.get(Calendar.YEAR);
		month=calendar.get(Calendar.MONTH);
		day=calendar.get(Calendar.DAY_OF_MONTH);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@SuppressLint({"ResourceAsColor" })
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they are dependent
				// on which page is currently active. An alternative approach is to have each
				// fragment expose actions itself (rather than the activity exposing actions),
				// but for simplicity, the activity provides the actions in this sample.
				//pagePosition=position;
				//Log.i("pageChange", "pageChange");
				mPagerAdapter.setCurrentPosition(position);
				strip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
				//if(position==251)
				//	strip.setTextColor(R.color.red);
				//((BFEventsListPagerAdapter)(mPager.getAdapter())
				invalidateOptionsMenu();
				refresh();

			}
		});



		Button btn1=(Button)findViewById(R.id.feedBtn);
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Calendar calendar = BFUtilities.getCurrentPageDate(mPager, refDate, refPosition);
					int year=calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH);
					int day=calendar.get(Calendar.DAY_OF_MONTH);

					Intent intent = new Intent(MainActivity3.this, BreastFeedingActivity.class);
					//Intent intent=new Intent();
					intent.setAction(Intent.ACTION_INSERT);
					intent.setData(FeedingEventsContract.Feeds.FEEDS_URI);
					intent.putExtra("position", mPager.getCurrentItem());
					//intent.putExtra("timeStamp", timeStampStr);
					intent.putExtra("year", year);
					intent.putExtra("month", month);
					intent.putExtra("day", day);
					startActivity(
							intent
							);
				}
				catch( ActivityNotFoundException e)
				{
					e.printStackTrace();
				}
			}

		}); 

		Button btn2=(Button)findViewById(R.id.breastPumpBtn);
		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Calendar calendar = BFUtilities.getCurrentPageDate(mPager, refDate, refPosition);
					int year=calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH);
					int day=calendar.get(Calendar.DAY_OF_MONTH);
					Intent intent = new Intent(MainActivity3.this, BreastPumpActivity.class);
					intent.setAction(Intent.ACTION_INSERT);
					intent.setData(FeedingEventsContract.BreastPumpEvents.CONTENT_URI);
					intent.putExtra("year", year);
					intent.putExtra("month", month);
					intent.putExtra("day", day);
					startActivity(
							intent
							);
				}
				catch( ActivityNotFoundException e)
				{
					e.printStackTrace();
				}
			}

		}); 

		Button btn3=(Button)findViewById(R.id.nappyBtn);
		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Calendar calendar = BFUtilities.getCurrentPageDate(mPager, refDate, refPosition);
					int year=calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH);
					int day=calendar.get(Calendar.DAY_OF_MONTH);
					Intent intent = new Intent(MainActivity3.this, BabyNappyActivity.class);
					intent.setAction(Intent.ACTION_INSERT);
					intent.setData(FeedingEventsContract.NapEvents.CONTENT_URI);
					intent.putExtra("year", year);
					intent.putExtra("month", month);
					intent.putExtra("day", day);

					startActivity(
							intent
							);
				}
				catch( ActivityNotFoundException e)
				{
					e.printStackTrace();
				}
			}

		}); 
		
		Button btn4=(Button)findViewById(R.id.CustomerEventBtn);
		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					Calendar calendar = BFUtilities.getCurrentPageDate(mPager, refDate, refPosition);
					int year=calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH);
					int day=calendar.get(Calendar.DAY_OF_MONTH);
					Intent intent = new Intent(MainActivity3.this, CustomerEventActivity.class);
					intent.setAction(Intent.ACTION_INSERT);
					
					
					intent.setData(FeedingEventsContract.CustomerEvents.CONTENT_URI);
					intent.putExtra("year", year);
					intent.putExtra("month", month);
					intent.putExtra("day", day);

					startActivity(
							intent
							);
				}
				catch( ActivityNotFoundException e)
				{
					e.printStackTrace();
				}
			}

		}); 


	}
	
	public void refresh()
	{
		Timestamp[] ts=BFUtilities.getDate(BFUtilities.getCurrentPageDate(mPager, refDate, refPosition));
		HashMap<String, String> summaryHM=BFUtilities.getPumpsandFeedsData(getApplicationContext(), ts,ounce);
		String summaryS=BFUtilities.getSummaryString(summaryHM, getApplicationContext(), ounce);
		
		TextView tv=(TextView)findViewById(R.id.txtViewSummary);
		tv.setText(summaryS);
		
		//tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);	
		tv.invalidate();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		   
	     final String vup= sharedPreferences.getString("Volumn_Unit", "");
	     if(vup.equalsIgnoreCase("1"))
			{	
	    	  ounce=false;
			}
	    	if(vup.equalsIgnoreCase("2"))
			{	
	    		ounce=true;
			}
			mPagerAdapter.notifyDataSetChanged();
			refresh();
	
	}

	protected void onPause()
	{
		super.onPause();
		

	}   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
		
        return super.onCreateOptionsMenu(menu);
	}
	
	

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.menu_item1ShowFeedQbyDay:
	        	Intent intent2 = new Intent(this, ShowFeedingQActivity.class);
	        	startActivity(intent2);
	        	return true;
	        case R.id.menu_item1ShowFeedQbyHour:
	        	Intent intent4 = new Intent(this, ShowFeedingqbyhourActivity.class);
	        	startActivity(intent4);
	        	return true;
	        case R.id.menu_item1ShowBreastU:
	        	Intent intent3 = new Intent(this, ShowBreastUsage.class);
	        	startActivity(intent3);
	        	return true;
	        
	        case R.id.menu_item1ExportXML:
	  
	        	boolean createdFile=false;
	        	try
	        	{
	        		
		        	BFDBOpenHelper db=new BFDBOpenHelper(this.getApplication().getApplicationContext());
		        	SQLiteDatabase dbr=db.getReadableDatabase();
		        	DatabaseExport2 de=new DatabaseExport2(dbr);
		        	//DatabaseExport de=new DatabaseExport(dbr);
		        	ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		        	de.dbToStream(byteArrayStream);
		        	//Log.i("XML Output", byteArrayStream.toString());
		        	
		        	Calendar calendar=Calendar.getInstance();
		        	Date date=calendar.getTime();
		        	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		        	String dts=dateFormat.format(date).toString();
		        	String fileNameBase=getApplicationContext().getResources().getString(R.string.fileName);
		        	String fileName="BreastfeedingJournal"+dts+".xml";
		        	
		        	String path = Environment.getExternalStorageDirectory().toString();
		        	OutputStream fOut = null;
		        	File file = new File(path,fileName);
		        	
		        	fOut = new FileOutputStream(file);
		        	byteArrayStream.writeTo(fOut);
		
		        	byteArrayStream.close();
		        	fOut.close();
		        	
		        	/*String path = Environment.getExternalStorageDirectory().toString();
		        	OutputStream fOut = null;
		        	File file = new File(path, "BFExternal.xml");*/
		        	
		        	boolean existed=BFUtilities.InstallApp(this);
		       
		        	
		        	Uri U = Uri.fromFile(file);
		        	Intent i = new Intent(Intent.ACTION_SEND);
		        	i.setType("application/xml");
		        
		        	i.putExtra(Intent.EXTRA_STREAM, U);
		        	String titleExportDatas=getApplicationContext().getResources().getString(R.string.title_ExportData);
		        	//startActivity(Intent.createChooser(i,titleExportDatas+fileName));
		        	startActivityForResult(Intent.createChooser(i,titleExportDatas+fileName), 2);
		        
		        	//file.delete();
		        /*	Context context = getApplicationContext();
		        	CharSequence text =getApplicationContext().getResources().getString(R.string.promptDataExport);
		        	int duration = Toast.LENGTH_SHORT;

		        	Toast toast = Toast.makeText(context, text, duration);
		        	toast.show();*/
		        
		        	/*Intent intent =new Intent(MainActivity3.this,DisplayXMLDataActivity.class);
		        	intent.putExtra("xmlText", byteArrayStream.toString());
		        	
		        	startActivity(intent);*/
		        	
	        	}
	        	catch(Exception e)
	        	{
	        		//Log.i("ExceptionMenu", e.toString());
	        	
	        	}
		       
	        	if(createdFile);
	        		//Log.i("createdFile", "true");
	        	else ;
	        		//Log.i("createdFile", "false");
	           return true;
	        case R.id.menu_item1ImportXML:
	        	
	        	showDialogWarning();
	        	return true;
	       /* case R.id.menu_item1ShowStatistics:
	        	Intent intent = new Intent(this, ShowStatisticsActivity.class);
	        	startActivity(intent);
	        	return true;*/
	       /* case R.id.menu_item1UserPreferences:
	        	Intent intent5 = new Intent(this, ShowUserPreferencesActivity.class);
	        	startActivity(intent5);
	        	return true;*/
	      case R.id.menu_item1UserPreferences:
	    	  Intent intentSetPref = new Intent(getApplicationContext(), UserPreferencesActivity.class);
	    	    startActivity(intentSetPref);
	    	  
	    	 /* getFragmentManager().beginTransaction()
              .replace(android.R.id.content, new UserPreferencesFragment())
              .commit();*/
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	
	public void showDialogWarning()
	{
		 // DialogFragment newFragment2 = DialogFragmentImportPrompt.newInstance();
		   // newFragment2.show(getSupportFragmentManager(), "dialog");
		    String title=getResources().getString(R.string.message_dataimport);
		    String yes=getResources().getString(R.string.title_buttonYes);
		    String no=getResources().getString(R.string.title_buttonNo);
		    String warning=getResources().getString(R.string.title_warning);
		//AlertDialogImportData ad=new AlertDialogImportData().newInstance(title);   
		//ad.show(getSupportFragmentManager(), "dialog");
		
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle(warning) ;
        adb.setIcon(R.drawable.pin128);
        adb.setMessage(title);
       
        adb.setNegativeButton(no, new Listener2());
        adb.setPositiveButton(yes, new Listener1());
        Dialog d= adb.create();
        d.show();
	}
	 
	class Listener1 implements DialogInterface.OnClickListener
	{
		public void onClick(DialogInterface dialog, int which)
		{
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        	i.setType("*/*");
        	i.addCategory(Intent.CATEGORY_OPENABLE);
        	String titleImportDatas= getResources().getString(R.string.title_ImportData);
        	startActivityForResult(Intent.createChooser(i,titleImportDatas), 1);
        	//dialog.dismiss();
		}
		
	}

	class Listener2 implements DialogInterface.OnClickListener
	{
		public void onClick(DialogInterface dialog, int which)
		{
			//dismiss();
		}
	}
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	     if(resultCode==RESULT_CANCELED)
	     {
	         // action cancelled
	    	/* if(requestCode==2)
	    	 {	 Context context = getApplicationContext();
	        	CharSequence text =getApplicationContext().getResources().getString(R.string.promptDataExport);
	        	int duration = Toast.LENGTH_SHORT;

	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();
	    	 }*/
	    		 
	     }
	     if(resultCode==RESULT_OK)
	     {
	    	 if(requestCode==2)
	    	 {
	    			Context context = getApplicationContext();
		        	CharSequence text =getApplicationContext().getResources().getString(R.string.promptDataExport);
		        	int duration = Toast.LENGTH_SHORT;

		        	Toast toast = Toast.makeText(context, text, duration);
		        	toast.show();
	    	 }
	    	 
	    	 if(requestCode==1)
	    	 {
	    		 Uri uri = data.getData();
	    	 
	        // Log.i("onActivityResult", uri.toString());
	         
	         InputStream isExternal2 = null;
	         InputStream isExternal = null;
				try {
					isExternal = getContentResolver().openInputStream(uri);
					isExternal2=getContentResolver().openInputStream(uri);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	        	 String dataExternal=BFUtilities.StreamToString(isExternal);
	        	// Intent intent =new Intent(MainActivity3.this,DisplayXMLDataActivity.class);
	 		     //  	intent.putExtra("xmlText", dataExternal);
	 		     //  	startActivity(intent);
	 	         XMLStreamToDB2 domParser=new XMLStreamToDB2();
	 	        	try {
	 					domParser.xmlToDB(getApplicationContext(), FeedingEventsContract.DB_URI, isExternal2);
	 					Context context = getApplicationContext();
		 	        	CharSequence text = getApplicationContext().getResources().getString(R.string.promptDataImport) ;
		 	        	int duration = Toast.LENGTH_SHORT;

		 	        	Toast toast = Toast.makeText(context, text, duration);
		 	        	toast.show();
		 			
	 				} catch (Exception e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 					errorMessage=e.toString();
	 					setErrorMessage(errorMessage);
	 					//showDialog(errorMessage);
	 					//Toast toast = Toast.makeText(getApplicationContext(), "Please choose BF.db", Toast.LENGTH_SHORT);
		 	        	//toast.show();
	 					//Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	 		        	//i.setType("*/*");
	 		        	//i.addCategory(Intent.CATEGORY_OPENABLE);
	 		        //	startActivityForResult(Intent.createChooser(i,"Wrong file, Please select File BF.db again"), 1);
	 				} /*catch (Exception e) {
	 					// TODO Auto-generated catch block
	 					e.printStackTrace();
	 					Toast toast = Toast.makeText(getApplicationContext(), "Please choose BF.xml", Toast.LENGTH_SHORT);
		 	        	toast.show();
	 				}*/
	    	 }
	 	 
	     }
	 }
	
		 
	 
	 public void showDialog(String error)
	 {
		 DialogFragment newFragment = DialogFragmentImportData.newInstance( error);
	//	  DialogFragmentImportData newFragment = new DialogFragmentImportData(error);
		  newFragment.show(getSupportFragmentManager(), "dialog");
	 	   
	 }
	 
	 @Override
	 protected void onResumeFragments() 
	 {
	   super.onResumeFragments();
	  // Log.i("onResumeFragments", "onResumeFragments");
	   
		if(errorMessage!=null)
		{
			String failImport= getResources().getString(R.string.message_failimport);
			showDialog(failImport + "\n"+errorMessage);
			
			
			/*DialogAlert da = new DialogAlert();
			da.newInstance("Error", errorMessage, this);*/
			errorMessage = null;
		}	   
	 }
	 
	 
	 
	 /**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
	 * sequence.
	 */
	private class BFEventsListPagerAdapter extends FragmentStatePagerAdapter {
		public BFEventsListPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			return  BFEventsListFragment2.create(position,sharedPreferences,  getApplicationContext().getResources().getConfiguration().locale );
		}

		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			
			Calendar calendar=(Calendar)refDate.clone();
			
			int increment=position-251;
		//	if(position-currentPosition==-1)
		//		return "previous";
		//	if(position-currentPosition==1)
		//		return "next";
			calendar.add(Calendar.DATE, increment);

			SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
			
			return date_format.format(calendar.getTime()).toString();

		}
		
		private int currentPosition=0;
		public int setCurrentPosition(int i)
		{
			currentPosition=i;
			return i;
		}
		

	}


}
