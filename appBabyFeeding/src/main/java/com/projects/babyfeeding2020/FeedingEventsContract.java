package com.projects.babyfeeding2020;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FeedingEventsContract {
	public static final String AUTHORITY = "com.projects.babyfeeding2020";
	private static final String SCHEME = "content://";
	public static final Uri DB_URI = Uri.parse(SCHEME+AUTHORITY);
	
	private FeedingEventsContract(){}
	
	public static abstract class Feeds implements BaseColumns
	{
		public static final String table_name="Feeds";
		public static final String column_timeStamp="timeStamp";
		public static final String column_leftDuration="leftDuration";
		public static final String column_rightDuration="rightDuration";
		public static final String column_bottleFeed="bottleQuantity";
		public static final String column_expressFeed="EFQuantity";
		public static final String column_first="First";
		public static final String column_second="Second";
		public static final String column_third="Third";
		public static final String column_fourth="fourth";
		
		public static final String _ID="_id";
		public static final String column_note="note";
		
		
		
		public static final String PATH_Feeds = "/Feeds";
		private static final String PATH_Feeds_ID = "/Feeds/";
		 public static final int FEEDS_ID_PATH_POSITION = 1;
		
		 public static final String DEFAULT_SORT_ORDER = "timeStamp DESC";
		 /**
         * The content:// style URL for this table
         */
        public static final Uri FEEDS_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_Feeds);
       
        /**
         * The content URI base for a single note. Callers must
         * append a numeric note id to this Uri to retrieve a note
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_Feeds_ID);

        /**
         * The content URI match pattern for a single note, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
            = Uri.parse(SCHEME + AUTHORITY + PATH_Feeds_ID + "/#");
		
	}
	
	
	
	public static abstract class BreastPumpEvents implements BaseColumns
	{
		public static final String table_name="BreastPumpEvents";
		public static final String column_timeStamp="timeStamp";
		public static final String column_leftDuration="leftDuration";
		public static final String column_rightDuration="rightDuration";
		public static final String column_leftVolumn="leftVolumn";
		public static final String column_rightVolumn="rightVolumn";

		public static final String _ID="_id";
		public static final String column_note="note";
		public static final int PUMPS_ID_PATH_POSITION = 1;
		private static final String SCHEME = "content://";
		public static final String PATH_BreastPumps = "/BreastPumpEvents";
		public static final String PATH_BreastPumps_ID = "/BreastPumpEvents/";
		public static final String DEFAULT_SORT_ORDER = "timeStamp DESC";
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_BreastPumps);
		public static final Uri CONTENT_ID_URI_BASE
          = Uri.parse(SCHEME + AUTHORITY + PATH_BreastPumps_ID);
		
	}
	
	
	public static abstract class NapEvents implements BaseColumns
	{
		public static final String table_name="NappyEvent";
		public static final String column_timeStamp="timeStamp";
		public static final String column_dirty="dirty";
		public static final String column_wet="wet";
		public static final String column_note="note";
		public static final String column_id="_id";
		public static final int NAPPYS_ID_PATH_POSITION = 1;
		private static final String SCHEME = "content://";
		public static final String PATH_Nappys = "/Nappys";
		public static final String PATH_Nappys_ID = "/Nappys/";
		public static final String DEFAULT_SORT_ORDER = "timeStamp DESC";
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_Nappys);
		public static final Uri CONTENT_ID_URI_BASE
          = Uri.parse(SCHEME + AUTHORITY + PATH_Nappys_ID);
	}
	
	public static abstract class CustomerEvents implements BaseColumns
	{
		public static final String table_name="CustomerEvents";
		public static final String column_timeStamp="timeStamp";
		public static final String column_title="title";
		public static final String column_description="description";
		public static final String column_id="_id";
		public static final int CUSTOMEREVENTS_ID_PATH_POSITION = 1;
		private static final String SCHEME = "content://";
		public static final String PATH_CustomerEvents = "/CustomerEvents";
		public static final String PATH_CustomerEvents_ID = "/CustomerEvents/";
		public static final String DEFAULT_SORT_ORDER = "timeStamp DESC";
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_CustomerEvents);
		public static final Uri CONTENT_ID_URI_BASE
          = Uri.parse(SCHEME + AUTHORITY + PATH_CustomerEvents_ID);
	}
	
	
	
	
	
	

}
