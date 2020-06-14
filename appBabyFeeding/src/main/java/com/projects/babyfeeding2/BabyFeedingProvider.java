package com.projects.babyfeeding2;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BabyFeedingProvider extends ContentProvider
{
	// Used for debugging and logging
	private static final String TAG = "BabyFeedingProvider";

	/**
	 * The database that the provider uses as its underlying data store
	 */
	private static final String DATABASE_NAME = "baby_feeding.db";

	/**
	 * A projection map used to select columns from the database
	 */
	private static HashMap<String, String> sBFEProjectionMap;
	//private static HashMap<String, String> sBFEProjectionMap;
	/**
	 * A projection map used to select columns from the database
	 */
	private static HashMap<String, String> sLiveFolderProjectionMap;

	/*    *//**
	 * Standard projection for the interesting columns of a normal note.
	 *//*
    private static final String[] READ_FEEDS_PROJECTION = new String[] {
            FeedingEventsContract.Feeds.column_timeStamp,               // Projection position 0, the note's id
            FeedingEventsContract.Feeds.column_leftDuration,  // Projection position 1, the note's content
            FeedingEventsContract.Feeds.column_rightDuration,
            FeedingEventsContract.Feeds.column_bottleFeed,
            FeedingEventsContract.Feeds.column_expressFeed,
            FeedingEventsContract.Feeds.column_note
    };*/
	private static final int READ_NOTE_NOTE_INDEX = 1;
	private static final int READ_NOTE_TITLE_INDEX = 2;

	/*
	 * Constants used by the Uri matcher to choose an action based on the pattern
	 * of the incoming URI
	 */
	// The incoming URI matches the Notes URI pattern
	private static final int BFES = 1;
	private static final int PUMPS=3;
	private static final int NAPPYS=5;
	private static final int CUSTOMEREVENTS=9;
	// The incoming URI matches the Note ID URI pattern
	private static final int BFE_ID = 2;
	private static final int PUMP_ID=4;
	private static final int NAPPY_ID=6;
	private static final int DATABASE=8;
	private static final int CUSTOMEREVENT_ID=10;
	// The incoming URI matches the Live Folder URI pattern
	private static final int LIVE_FOLDER_NOTES = 7;

	/**
	 * A UriMatcher instance
	 */
	private static final UriMatcher sUriMatcher;

	// Handle to a new DatabaseHelper.
	private BFdatabaseOpenHelper bfOpenHelper;


	/**
	 * A block that instantiates and sets static objects
	 */
	static {

		/*
		 * Creates and initializes the URI matcher
		 */
		// Create a new instance
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		// Add a pattern that routes URIs terminated with "notes" to a NOTES operation
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, null, DATABASE);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "Feeds", BFES);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "BreastPumpEvents", PUMPS);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "Nappys",NAPPYS);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "CustomerEvents",CUSTOMEREVENTS);
		// Add a pattern that routes URIs terminated with "notes" plus an integer
		// to a note ID operation
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "Feeds/#", BFE_ID);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "BreastPumpEvents/#",PUMP_ID);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "Nappys/#",NAPPY_ID);
		sUriMatcher.addURI(FeedingEventsContract.AUTHORITY, "CustomerEvents/#",CUSTOMEREVENT_ID);
		// Add a pattern that routes URIs terminated with live_folders/notes to a
		// live folder operation
		// sUriMatcher.addURI(NotePad.AUTHORITY, "live_folders/notes", LIVE_FOLDER_NOTES);

		/*
		 * Creates and initializes a projection map that returns all columns
		 */

		// Creates a new projection map instance. The map returns a column name
		// given a string. The two are usually equal.
		sBFEProjectionMap = new HashMap<String, String>();

		// Maps the string "_ID" to the column name "_ID"
		sBFEProjectionMap.put(FeedingEventsContract.Feeds.column_timeStamp, FeedingEventsContract.Feeds.column_timeStamp);

		// Maps "title" to "title"
		sBFEProjectionMap.put(FeedingEventsContract.Feeds.column_leftDuration, FeedingEventsContract.Feeds.column_leftDuration);

		// Maps "note" to "note"
		sBFEProjectionMap.put(FeedingEventsContract.Feeds.column_rightDuration, FeedingEventsContract.Feeds.column_rightDuration);


	}

	static class BFdatabaseOpenHelper extends SQLiteOpenHelper {

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

		BFdatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Log.i("BFdatabaseOpenHelper","onCreate");
			//Log.i("BFHelper","BFdbTableCreate=" + BFdb_TABLENap_CREATE);

			onUpgrade(db, 0, DATABASE_VERSION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			//Log.i("info:", "onUpgrade");
			// Upgrade to version 1
			if(oldVersion < 1 && newVersion >= 1)
			{
				//Log.i("info:", "upgrade v1");
				db.execSQL(BFdb_TABLEBF_CREATE);
				db.execSQL(BFdb_TABLEBP_CREATE);
				db.execSQL(BFdb_TABLENap_CREATE);        	
			}        	

			// Upgrade to version 4
			if(oldVersion < 4 && newVersion >= 4)
			{
				//Log.i("info:", "upgrade v4");
				db.execSQL(BFdb_TABLECE_CREATE);
			} 
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// Downgrade from version 4
			//Log.i("info:", "onDowngrade ");
			if(oldVersion >= 4 && newVersion < 4)
			{
				//Log.i("info:", "downgrade v4");
				db.execSQL(BFdb_TABLECE_DELETE);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		int count=0;
		String finalWhere=null, tableName=null;
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();
		//Log.i("ProviderDelete-URI:",uri.toString());


		if(sUriMatcher.match(uri)==BFE_ID)
		{
			tableName=FeedingEventsContract.Feeds.table_name;
			String ll= uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION);
			finalWhere =
					FeedingEventsContract.Feeds._ID +                              // The ID column name
					" = " +                                          // test for equality
					uri.getPathSegments().                           // the incoming note ID
					get(FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION)   ;

			if (whereArgs !=null) {
				finalWhere = finalWhere + " AND " + whereArgs;
			}

			try{  

				count = db.delete(tableName, finalWhere, whereArgs);
			}catch(Exception e)
			{
				e.printStackTrace();
			}

		} 
		else if(sUriMatcher.match(uri)==PUMP_ID)
		{
			tableName=FeedingEventsContract.BreastPumpEvents.table_name;
			String ll= uri.getPathSegments().get( FeedingEventsContract.BreastPumpEvents.PUMPS_ID_PATH_POSITION);
			finalWhere =
					FeedingEventsContract.BreastPumpEvents._ID +                              // The ID column name
					" = " +                                          // test for equality
					uri.getPathSegments().                           // the incoming note ID
					get(FeedingEventsContract.BreastPumpEvents.PUMPS_ID_PATH_POSITION)   ;

			if (whereArgs !=null) {
				finalWhere = finalWhere + " AND " + whereArgs;
			}

			try{  

				count = db.delete(tableName, finalWhere, whereArgs);
			}catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		else if(sUriMatcher.match(uri)==NAPPY_ID)
		{
			tableName=FeedingEventsContract.NapEvents.table_name;
			String ll= uri.getPathSegments().get( FeedingEventsContract.NapEvents.NAPPYS_ID_PATH_POSITION);
			finalWhere =
					FeedingEventsContract.NapEvents._ID +                              // The ID column name
					" = " +                                          // test for equality
					uri.getPathSegments().                           // the incoming note ID
					get(FeedingEventsContract.NapEvents.NAPPYS_ID_PATH_POSITION)   ;

			if (whereArgs !=null) {
				finalWhere = finalWhere + " AND " + whereArgs;
			}

			try{  

				count = db.delete(tableName, finalWhere, whereArgs);
			}catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		else if(sUriMatcher.match(uri)==CUSTOMEREVENT_ID)
		{
			tableName=FeedingEventsContract.CustomerEvents.table_name;
			String ll= uri.getPathSegments().get( FeedingEventsContract.CustomerEvents.CUSTOMEREVENTS_ID_PATH_POSITION);
			finalWhere =
					FeedingEventsContract.CustomerEvents._ID +                              // The ID column name
					" = " +                                          // test for equality
					uri.getPathSegments().get( FeedingEventsContract.CustomerEvents.CUSTOMEREVENTS_ID_PATH_POSITION);


			if (whereArgs !=null) {
				finalWhere = finalWhere + " AND " + whereArgs;
			}

			try{  

				count = db.delete(tableName, finalWhere, whereArgs);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		else if(sUriMatcher.match(uri)==DATABASE)
		{
			File file=new File("/data/data/com.projects.babyfeeding2/databases/BFdb.db");
			//db.deleteDatabase(file);
			deleteAll(uri);
		}
		/*else
		{
			tableName=null;
			finalWhere=null;
		}*/

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	public void deleteAll(Uri uri)
	{	SQLiteDatabase db = bfOpenHelper.getWritableDatabase();
	String feedsTable=null; 
	String pumpsTable=null;
	String nappysTable=null;
	String customerEventsTable=null;
	if(sUriMatcher.match(uri)==DATABASE)
	{	feedsTable = FeedingEventsContract.Feeds.table_name;
	pumpsTable = FeedingEventsContract.BreastPumpEvents.table_name;
	nappysTable = FeedingEventsContract.NapEvents.table_name;
	customerEventsTable=FeedingEventsContract.CustomerEvents.table_name;
	}
	else
	{
		feedsTable=null;
		pumpsTable=null;
		nappysTable=null;
		customerEventsTable=null;
	}
	db.delete(feedsTable,null,null);
	db.delete(pumpsTable, null,null);
	db.delete(nappysTable,null, null);
	db.delete(customerEventsTable,null,null);
	getContext().getContentResolver().notifyChange(uri, null);
	}



	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		// Validates the incoming URI. Only the full provider URI is allowed for inserts.


		int match=sUriMatcher.match(uri);
		Uri BFuri=null;

		switch(match)
		{	case BFES:

			BFuri=insertFeed(initialValues, bfOpenHelper, uri);
			break;
		case PUMPS:
			BFuri=insertPump(initialValues, bfOpenHelper, uri);
			break;
		case NAPPYS:
			BFuri=insertNappy(initialValues,bfOpenHelper,uri);
			break;
		case CUSTOMEREVENTS:
			BFuri=insertCE(initialValues,bfOpenHelper,uri);
			break;
		default:
			return null;

		}
		return BFuri;
	}


	private Uri insertNappy(ContentValues v, BFdatabaseOpenHelper bfOpenHelper, Uri uri)
	{
		ContentValues values;
		Uri bfeUri=null;

		// If the incoming values map is not null, uses it for the new values.
		if (v != null) 
		{
			values = new ContentValues(v);
		}
		else 
		{
			// Otherwise, create a new value map
			values = new ContentValues();
		}
		if (values.containsKey(FeedingEventsContract.NapEvents.column_timeStamp) == false) {
			long now = Long.valueOf(System.currentTimeMillis());
			Timestamp ts=new Timestamp(now);
			values.put(FeedingEventsContract.NapEvents.column_timeStamp, ts.toString());
		}

		// If the values map doesn't contain the modification date, sets the value to the current
		// time.
		if (values.containsKey(FeedingEventsContract.NapEvents.column_wet) == false) {

			values.put(FeedingEventsContract.NapEvents.column_wet, false);
		}
		if (values.containsKey(FeedingEventsContract.NapEvents.column_dirty) == false) {
			values.put(FeedingEventsContract.NapEvents.column_dirty, false);
		}

		if (values.containsKey(FeedingEventsContract.NapEvents.column_note) == false) {
			values.put(FeedingEventsContract.Feeds.column_note, "");
		}

		// Opens the database object in "write" mode.
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		long rowId = db.insert(
				FeedingEventsContract.NapEvents.table_name,        // The table to insert into.
				null,  // A hack, SQLite sets this column value to null
				// if values is empty.
				values                           // A map of column names, and the values to insert
				// into the columns.
				);

		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID appended to it.
			bfeUri = ContentUris.withAppendedId(FeedingEventsContract.NapEvents.CONTENT_ID_URI_BASE, rowId);

			// Notifies observers registered against this provider that the data changed.
			getContext().getContentResolver().notifyChange(bfeUri, null);
			return bfeUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.

		throw new SQLException("Failed to insert row into " + uri);

	}



	private Uri insertPump(ContentValues v, BFdatabaseOpenHelper bfOpenHelper, Uri uri)
	{
		ContentValues values;
		Uri bfeUri=null;

		// If the incoming values map is not null, uses it for the new values.
		if (v != null) 
		{
			values = new ContentValues(v);
		}
		else 
		{
			// Otherwise, create a new value map
			values = new ContentValues();
		}
		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_timeStamp) == false) {
			long now = Long.valueOf(System.currentTimeMillis());
			Timestamp ts=new Timestamp(now);
			values.put(FeedingEventsContract.BreastPumpEvents.column_timeStamp, ts.toString());
		}

		// If the values map doesn't contain the modification date, sets the value to the current
		// time.
		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_leftDuration) == false) {

			values.put(FeedingEventsContract.BreastPumpEvents.column_leftDuration, 0);
		}
		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_rightDuration) == false) {
			values.put(FeedingEventsContract.BreastPumpEvents.column_rightDuration, 0);
		}

		// If the values map doesn't contain a title, sets the value to the default title.
		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_leftVolumn) == false) {

			values.put(FeedingEventsContract.BreastPumpEvents.column_leftVolumn, 0);
		}

		// If the values map doesn't contain note text, sets the value to an empty string.
		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_rightVolumn) == false) {
			values.put(FeedingEventsContract.BreastPumpEvents.column_rightVolumn, 0);
		}

		if (values.containsKey(FeedingEventsContract.BreastPumpEvents.column_note) == false) {
			values.put(FeedingEventsContract.Feeds.column_note, "");
		}

		// Opens the database object in "write" mode.
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		long rowId = db.insert(
				FeedingEventsContract.BreastPumpEvents.table_name,        // The table to insert into.
				null,  // A hack, SQLite sets this column value to null
				// if values is empty.
				values                           // A map of column names, and the values to insert
				// into the columns.
				);

		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID appended to it.
			bfeUri = ContentUris.withAppendedId(FeedingEventsContract.BreastPumpEvents.CONTENT_ID_URI_BASE, rowId);

			// Notifies observers registered against this provider that the data changed.
			getContext().getContentResolver().notifyChange(bfeUri, null);
			return bfeUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.

		throw new SQLException("Failed to insert row into " + uri);

	}





	private Uri insertFeed(ContentValues v, BFdatabaseOpenHelper bfOpenHelper, Uri uri)
	{
		ContentValues values;
		Uri bfeUri=null;

		// If the incoming values map is not null, uses it for the new values.
		if (v != null) 
		{
			values = new ContentValues(v);
		}
		else 
		{
			// Otherwise, create a new value map
			values = new ContentValues();
		}
		if (values.containsKey(FeedingEventsContract.Feeds.column_timeStamp) == false) {
			long now = Long.valueOf(System.currentTimeMillis());
			Timestamp ts=new Timestamp(now);
			values.put(FeedingEventsContract.Feeds.column_timeStamp, ts.toString());
		}

		// If the values map doesn't contain the modification date, sets the value to the current
		// time.
		if (values.containsKey(FeedingEventsContract.Feeds.column_leftDuration) == false) {

			values.put(FeedingEventsContract.Feeds.column_leftDuration, 0);
		}
		if (values.containsKey(FeedingEventsContract.Feeds.column_rightDuration) == false) {
			values.put(FeedingEventsContract.Feeds.column_rightDuration, 0);
		}

		// If the values map doesn't contain a title, sets the value to the default title.
		if (values.containsKey(FeedingEventsContract.Feeds.column_bottleFeed) == false) {
			Resources r = Resources.getSystem();
			values.put(FeedingEventsContract.Feeds.column_bottleFeed, 0);
		}

		// If the values map doesn't contain note text, sets the value to an empty string.
		if (values.containsKey(FeedingEventsContract.Feeds.column_expressFeed) == false) {
			values.put(FeedingEventsContract.Feeds.column_expressFeed, 0);
		}

		if (values.containsKey(FeedingEventsContract.Feeds.column_note) == false) {
			values.put(FeedingEventsContract.Feeds.column_note, "");
		}


		// Opens the database object in "write" mode.
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		long rowId = db.insert(
				FeedingEventsContract.Feeds.table_name,        // The table to insert into.
				null,  // A hack, SQLite sets this column value to null
				// if values is empty.
				values                           // A map of column names, and the values to insert
				// into the columns.
				);

		if (rowId > 0) {
			// Creates a URI with the note ID pattern and the new row ID appended to it.
			bfeUri = ContentUris.withAppendedId(FeedingEventsContract.Feeds.CONTENT_ID_URI_BASE, rowId);

			// Notifies observers registered against this provider that the data changed.
			getContext().getContentResolver().notifyChange(bfeUri, null);
			return bfeUri;
		}

		// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.

		throw new SQLException("Failed to insert row into " + uri);

	}

	private Uri insertCE(ContentValues v, BFdatabaseOpenHelper bfOpenHelper, Uri uri)
	{
		ContentValues values;
		Uri bfeUri=null;

		// If the incoming values map is not null, uses it for the new values.
		if (v != null) 
		{
			values = new ContentValues(v);
		}
		else 
		{
			// Otherwise, create a new value map
			values = new ContentValues();
		}
		if (values.containsKey(FeedingEventsContract.CustomerEvents.column_timeStamp) == false) {
			long now = Long.valueOf(System.currentTimeMillis());
			Timestamp ts=new Timestamp(now);
			values.put(FeedingEventsContract.CustomerEvents.column_timeStamp, ts.toString());
		}

		// If the values map doesn't contain the modification date, sets the value to the current
		// time.
		if (values.containsKey(FeedingEventsContract.CustomerEvents.column_title) == false) {

			values.put(FeedingEventsContract.CustomerEvents.column_title, "");
		}
		if (values.containsKey(FeedingEventsContract.CustomerEvents.column_description) == false) {
			values.put(FeedingEventsContract.CustomerEvents.column_description, "");
		}


		// Opens the database object in "write" mode.
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();

		// Performs the insert and returns the ID of the new note.
		try{
			long rowId = db.insert(
					FeedingEventsContract.CustomerEvents.table_name,        // The table to insert into.
					null,  // A hack, SQLite sets this column value to null
					// if values is empty.
					values                           // A map of column names, and the values to insert
					// into the columns.
					);
			if (rowId > 0) {
				// Creates a URI with the note ID pattern and the new row ID appended to it.
				bfeUri = ContentUris.withAppendedId(FeedingEventsContract.CustomerEvents.CONTENT_ID_URI_BASE, rowId);

				// Notifies observers registered against this provider that the data changed.
				getContext().getContentResolver().notifyChange(bfeUri, null);
				return bfeUri;
			}
		}
		catch(Exception e)
		{
			//Log.i("CE", e.toString());
		}



		// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.

		throw new SQLException("Failed to insert row into " + uri);

	}



	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		bfOpenHelper = new BFdatabaseOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		// Constructs a new query builder and sets its table name

		// Opens the database object in "read" mode, since no writes need to be done.
		SQLiteDatabase db = bfOpenHelper.getReadableDatabase();
		Cursor c=null;
		String orderBy;
		SQLiteQueryBuilder qbb;
		/**
		 * Choose the projection and adjust the "where" clause based on URI pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case BFES:

			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.Feeds.table_name);
			/*qbb.appendWhere(
	            		   FeedingEventsContract.Feeds._ID +    // the name of the ID column
	                   "=" +
	                   // the position of the note ID itself in the incoming URI
	                   uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));*/


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;
		case LIVE_FOLDER_NOTES:
			// If the incoming URI is from a live folder, chooses the live folder projection.
			// qb.setProjectionMap(sLiveFolderProjectionMap);
			break;
			/* If the incoming URI is for a single note identified by its ID, chooses the
			 * note ID projection, and appends "_ID = <noteID>" to the where clause, so that
			 * it selects that single note
			 */
		case PUMPS:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.BreastPumpEvents.table_name);
			/*qbb.appendWhere(
	            		   FeedingEventsContract.Feeds._ID +    // the name of the ID column
	                   "=" +
	                   // the position of the note ID itself in the incoming URI
	                   uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));*/


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;

		case NAPPYS:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.NapEvents.table_name);
			/*qbb.appendWhere(
	            		   FeedingEventsContract.Feeds._ID +    // the name of the ID column
	                   "=" +
	                   // the position of the note ID itself in the incoming URI
	                   uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));*/


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;

		case CUSTOMEREVENTS:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.CustomerEvents.table_name);
			/*qbb.appendWhere(
	            		   FeedingEventsContract.Feeds._ID +    // the name of the ID column
	                   "=" +
	                   // the position of the note ID itself in the incoming URI
	                   uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));*/


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.CustomerEvents.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;

		case PUMP_ID:
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(FeedingEventsContract.BreastPumpEvents.table_name);
			qb.appendWhere(
					FeedingEventsContract.BreastPumpEvents._ID +    // the name of the ID column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get( FeedingEventsContract.BreastPumpEvents.PUMPS_ID_PATH_POSITION));

			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;
		case BFE_ID:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.Feeds.table_name);
			qbb.appendWhere(
					FeedingEventsContract.Feeds._ID +    // the name of the ID column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get( FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION));


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);


			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;
		case NAPPY_ID:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.NapEvents.table_name);
			qbb.appendWhere(
					FeedingEventsContract.NapEvents._ID +    // the name of the ID column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get( FeedingEventsContract.NapEvents.NAPPYS_ID_PATH_POSITION));


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.Feeds.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;
		case CUSTOMEREVENT_ID:
			qbb = new SQLiteQueryBuilder();
			qbb.setTables(FeedingEventsContract.CustomerEvents.table_name);
			qbb.appendWhere(
					FeedingEventsContract.CustomerEvents._ID +    // the name of the ID column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get( FeedingEventsContract.CustomerEvents.CUSTOMEREVENTS_ID_PATH_POSITION));


			// If no sort order is specified, uses the default
			if (TextUtils.isEmpty(sortOrder)) {
				orderBy = FeedingEventsContract.CustomerEvents.DEFAULT_SORT_ORDER;
			} else {
				// otherwise, uses the incoming sort order
				orderBy = sortOrder;
			}
			/*
			 * Performs the query. If no problems occur trying to read the database, then a Cursor
			 * object is returned; otherwise, the cursor variable contains null. If no records were
			 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
			 */
			c = qbb.query(
					db,            // The database to query
					projection,    // The columns to return from the query
					selection,     // The columns for the where clause
					selectionArgs, // The values for the where clause
					null,          // don't group the rows
					null,          // don't filter by row groups
					orderBy        // The sort order
					);

			// Tells the Cursor what URI to watch, so it knows when its source data changes
			c.setNotificationUri(getContext().getContentResolver(), uri);

			break;

		default:
			// If the URI doesn't match any of the known patterns, throw an exception.
			// throw new IllegalArgumentException("Unknown URI " + uri);
		}

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		// Opens the database object in "write" mode.
		SQLiteDatabase db = bfOpenHelper.getWritableDatabase();
		int count;
		String finalWhere;

		// Does the update based on the incoming URI pattern
		switch (sUriMatcher.match(uri)) {


		// If the incoming URI matches a single note ID, does the update based on the incoming
		// data, but modifies the where clause to restrict it to the particular note ID.
		case BFE_ID:
			// From the incoming URI, get the note ID
			String noteId = uri.getPathSegments().get(FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION);

			/*
			 * Starts creating the final WHERE clause by restricting it to the incoming
			 * note ID.
			 */
			finalWhere =
					FeedingEventsContract.Feeds._ID +                              // The ID column name
					" = " +                                          // test for equality
					uri.getPathSegments().                           // the incoming note ID
					get(FeedingEventsContract.Feeds.FEEDS_ID_PATH_POSITION)
					;

			// If there were additional selection criteria, append them to the final WHERE
			// clause
			if (where !=null) {
				finalWhere = finalWhere + " AND " + where;
			}


			// Does the update and returns the number of rows updated.
			count = db.update(
					FeedingEventsContract.Feeds.table_name, // The database table name.
					values,                   // A map of column names and new values to use.
					finalWhere,               // The final WHERE clause to use
					// placeholders for whereArgs
					whereArgs                 // The where clause column values to select on, or
					// null if the values are in the where argument.
					);
			break;
		case PUMP_ID:
			// From the incoming URI, get the note ID
			String pumpId = uri.getPathSegments().get(FeedingEventsContract.BreastPumpEvents.PUMPS_ID_PATH_POSITION);

			finalWhere =
					FeedingEventsContract.BreastPumpEvents._ID +                              // The ID column name
					" = " + pumpId     ;      

			if (where !=null) {
				finalWhere = finalWhere + " AND " + where;
			}

			count = db.update(
					FeedingEventsContract.BreastPumpEvents.table_name, // The database table name.
					values,                   // A map of column names and new values to use.
					finalWhere,               // The final WHERE clause to use
					// placeholders for whereArgs
					whereArgs                 // The where clause column values to select on, or
					// null if the values are in the where argument.
					);
			break;

		case NAPPY_ID:
			// From the incoming URI, get the note ID
			String nappyId = uri.getPathSegments().get(FeedingEventsContract.NapEvents.NAPPYS_ID_PATH_POSITION);

			finalWhere =
					FeedingEventsContract.NapEvents.column_id +                              // The ID column name
					" = " + nappyId     ;      

			if (where !=null) {
				finalWhere = finalWhere + " AND " + where;
			}

			count = db.update(
					FeedingEventsContract.NapEvents.table_name, // The database table name.
					values,                   // A map of column names and new values to use.
					finalWhere,               // The final WHERE clause to use
					// placeholders for whereArgs
					whereArgs                 // The where clause column values to select on, or
					// null if the values are in the where argument.
					);
			break;
			// If the incoming pattern is invalid, throws an exception.
		case CUSTOMEREVENT_ID:
			// From the incoming URI, get the note ID
			String ceId = uri.getPathSegments().get(FeedingEventsContract.CustomerEvents.CUSTOMEREVENTS_ID_PATH_POSITION);

			finalWhere =
					FeedingEventsContract.CustomerEvents.column_id+                              // The ID column name
					" = " + ceId   ;      

			if (where !=null) {
				finalWhere = finalWhere + " AND " + where;
			}

			count = db.update(
					FeedingEventsContract.CustomerEvents.table_name, // The database table name.
					values,                   // A map of column names and new values to use.
					finalWhere,               // The final WHERE clause to use
					// placeholders for whereArgs
					whereArgs                 // The where clause column values to select on, or
					// null if the values are in the where argument.
					);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		/*Gets a handle to the content resolver object for the current context, and notifies it
		 * that the incoming URI changed. The object passes this along to the resolver framework,
		 * and observers that have registered themselves for the provider are notified.
		 */
		getContext().getContentResolver().notifyChange(uri, null);

		// Returns the number of rows updated.
		return count;
	}




}
