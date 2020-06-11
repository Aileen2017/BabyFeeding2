package com.projects.babyfeeding2020;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class BFDBOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME="BFdb";
    private static final String BFdb_TABLEBFE_NAME = FeedingEventsContract.Feeds.table_name;
    
    private static final String BFdb_TABLEBP_NAME = FeedingEventsContract.BreastPumpEvents.table_name;
    private static final String BFdb_TABLENapE_NAME = FeedingEventsContract.NapEvents.table_name;
    private static final String BFdb_TABLECE_NAME = FeedingEventsContract.CustomerEvents.table_name;
    
    
    private static final String BFdb_TABLEBF_CREATE =
                "CREATE TABLE " + BFdb_TABLEBFE_NAME + " (" +
                FeedingEventsContract.Feeds.column_leftDuration + " integer not null, " +
                FeedingEventsContract.Feeds.column_rightDuration + " integer not null, " +
                FeedingEventsContract.Feeds.column_timeStamp+" timestamp not null, "+
                FeedingEventsContract.Feeds.column_bottleFeed+" integer not null, " +
                FeedingEventsContract.Feeds.column_expressFeed+" integer not null, "+
               FeedingEventsContract.Feeds._ID+" integer primary key autoincrement, "+
                FeedingEventsContract.Feeds.column_note+"  text not null "+
                ");";

    private static final String BFdb_TABLEBP_CREATE=
    		"CREATE TABLE " + BFdb_TABLEBP_NAME + " (" +
                   
                    
                    FeedingEventsContract.BreastPumpEvents.column_timeStamp+" timestamp not null, "+
                    FeedingEventsContract.BreastPumpEvents._ID+" integer primary key autoincrement, "+
                    FeedingEventsContract.BreastPumpEvents.column_leftDuration+" integer not null, " +
                    
                    FeedingEventsContract.BreastPumpEvents.column_rightDuration+" integer not null, "+
                    FeedingEventsContract.BreastPumpEvents.column_leftVolumn+" integer not null, "+
                    FeedingEventsContract.BreastPumpEvents.column_rightVolumn+" integer not null, "+
                    FeedingEventsContract.BreastPumpEvents.column_note+"  text not null "+
                    ");";
    
    private static final String BFdb_TABLENap_CREATE=
    		"CREATE TABLE " + BFdb_TABLENapE_NAME + " (" +
                   
                    
                    FeedingEventsContract.NapEvents.column_timeStamp+" timestamp not null, "+
                    FeedingEventsContract.NapEvents.column_dirty+" boolean not null, " +
                    FeedingEventsContract.NapEvents.column_wet+" boolean not null, " +
                    FeedingEventsContract.NapEvents.column_id+" integer primary key autoincrement, "+
                    FeedingEventsContract.NapEvents.column_note+"  text not null "+
                    ");";
    
    private static final String BFdb_TABLECE_CREATE=
    		"CREATE TABLE " + BFdb_TABLECE_NAME + " (" +
                   
                    
                    FeedingEventsContract.CustomerEvents.column_timeStamp+" timestamp not null, "+
                    FeedingEventsContract.CustomerEvents.column_title+" text not null, " +
                   
                    FeedingEventsContract.CustomerEvents.column_id+" integer primary key autoincrement, "+
                    FeedingEventsContract.CustomerEvents.column_description+"  text not null "+
                    ");";
    
    
  private static final String BFdb_TABLEBFE_CREATEINDEX= "create index breastfeed_idx on breastfeed(ts)";
    
    private static final String BFdb_TABLECE_DELETE = "DROP TABLE IF EXISTS " + BFdb_TABLECE_NAME;
    
    BFDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	//Log.i("BFdatabaseOpenHelper","onCreate");
       Log.i("BFHelper","BFdbTableCreate=" + BFdb_TABLENap_CREATE);
        
    	onUpgrade(db, 0, DATABASE_VERSION);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
    	Log.i("info:", "onUpgrade");
    	// Upgrade to version 1
    	if(oldVersion < 1 && newVersion >= 1)
    	{
        	Log.i("info:", "upgrade v1");
        	db.execSQL(BFdb_TABLEBF_CREATE);
        	db.execSQL(BFdb_TABLEBP_CREATE);
        	db.execSQL(BFdb_TABLENap_CREATE);        	
        }        	
    	
    	// Upgrade to version 4
    	if(oldVersion < 4 && newVersion >= 4)
    	{
    		Log.i("info:", "upgrade v4");
    		db.execSQL(BFdb_TABLECE_CREATE);
    	} 
    }
   
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	// Downgrade from version 4
    	Log.i("info:", "onDowngrade ");
    	if(oldVersion >= 4 && newVersion < 4)
    	{
    		Log.i("info:", "downgrade v4");
    		db.execSQL(BFdb_TABLECE_DELETE);
    	}
    }

    
}