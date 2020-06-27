package com.projects.babyfeeding;

import java.sql.Timestamp;
import java.util.Vector;

import android.database.Cursor;

public class BabyFeedingEventsFactory {

	static final String leftBreastEvent="leftBreastFeed";
	static final String rightBreastEvent="rightBreastFeed";
	static final String bottleFeedEvent="bottleFeed";
	static final String expressFeedEvent="expressFeed";
	
	static final String leftPumpEvent="leftPumpEvent";
	static final String rightPumpEvent="rightPumpEvent";
	static final String nappyEvent="nappyEvent";
	static final String customEvent="customEvent";
	
	
	public Vector<BabyFeedingEvent> generateBFEs(Cursor cursor)
	{
		Vector <BabyFeedingEvent> brfeVector = new Vector <BabyFeedingEvent>();
		if(cursor!=null)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				long id;
				String eventType;
				
				int tsColIndex=cursor.getColumnIndexOrThrow("timeStamp");
				String timeStampStr=cursor.getString(tsColIndex);
				Timestamp timeStamp = Timestamp.valueOf(timeStampStr);
			
				id=cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
				int ldColIndex = cursor.getColumnIndexOrThrow("leftDuration");
				int lduration=cursor.getInt(ldColIndex);
				if(lduration!=0)
				{	int duration=0, quantity=0;
					duration=lduration;
					eventType=leftBreastEvent;
					BabyFeedingEvent bfe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					brfeVector.add(bfe);
					
				}
				
				int rdColIndex = cursor.getColumnIndexOrThrow("rightDuration");
				int rduration=cursor.getInt(rdColIndex);
				if(rduration!=0)
				{	int duration=0, quantity=0;
					duration=rduration;
					eventType=rightBreastEvent;
					BabyFeedingEvent bfe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					brfeVector.add(bfe);
				}
				
				int rfColIndex=cursor.getColumnIndexOrThrow("bottleQuantity");
				int bq=cursor.getInt(rfColIndex);
				if(bq!=0)
				{
					int duration=0, quantity=0;
					quantity=bq;
					eventType=bottleFeedEvent;
					BabyFeedingEvent bfe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					brfeVector.add(bfe);
				}
				
				int lfColIndex=cursor.getColumnIndexOrThrow("EFQuantity");
				double eq=cursor.getDouble(lfColIndex);
				if(eq!=0)
				{
					int duration=0; double quantity=0;
					quantity=eq;
					eventType=expressFeedEvent;
					BabyFeedingEvent bfe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					brfeVector.add(bfe);
				}
		
				cursor.moveToNext();
				
			}
			return brfeVector;
		}
		else
			return null;
	}
	
	public Vector<BabyFeedingEvent> generateBPEs(Cursor cursor)
	{
		Vector <BabyFeedingEvent> bpeVector = new Vector <BabyFeedingEvent>();
		if(cursor!=null)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				long id;
				String eventType;
				
				int tsColIndex=cursor.getColumnIndexOrThrow("timeStamp");
				String timeStampStr=cursor.getString(tsColIndex);
				Timestamp timeStamp = Timestamp.valueOf(timeStampStr);
			
				id=cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
				int ldColIndex = cursor.getColumnIndexOrThrow("leftDuration");
				int lduration=cursor.getInt(ldColIndex);
				if(lduration!=0)
				{	int duration=0, quantity=0;
					duration=lduration;
					int lqColIndex = cursor.getColumnIndexOrThrow("leftVolumn");
					int lquantity=cursor.getInt(lqColIndex);
					quantity=lquantity;
					eventType=leftPumpEvent;
					BabyFeedingEvent bpe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					bpeVector.add(bpe);
					
				}
				else
				{
					int duration=0, quantity=0;
					duration=lduration;
					int lqColIndex = cursor.getColumnIndexOrThrow("leftVolumn");
					int lquantity=cursor.getInt(lqColIndex);
					if(lquantity!=0)
					{	quantity=lquantity;
						eventType=leftPumpEvent;
						BabyFeedingEvent bpe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
						bpeVector.add(bpe);
					}
					
				}
				int rdColIndex = cursor.getColumnIndexOrThrow("rightDuration");
				int rduration=cursor.getInt(rdColIndex);
				if(rduration!=0)
				{	int duration=0, quantity=0;
					duration=rduration;
					int rqColIndex = cursor.getColumnIndexOrThrow("rightVolumn");
					int rquantity=cursor.getInt(rqColIndex);
					quantity=rquantity;
					eventType=rightPumpEvent;
					BabyFeedingEvent bpe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
					bpeVector.add(bpe);
				}
				else
				{
					int duration=0, quantity=0;
					duration=rduration;
					int rqColIndex = cursor.getColumnIndexOrThrow("rightVolumn");
					int rquantity=cursor.getInt(rqColIndex);
					if(rquantity!=0)
					{	quantity=rquantity;
						eventType=rightPumpEvent;
						BabyFeedingEvent bpe=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
						bpeVector.add(bpe);
					}
					
				}
				cursor.moveToNext();
				
			}
			return bpeVector;
		}
		else
			return null;
	}
	
	public Vector<BabyFeedingEvent> generateBNs(Cursor cursor)
	{
		Vector <BabyFeedingEvent> bnVector = new Vector <BabyFeedingEvent>();
		if(cursor!=null)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				long id;
				String eventType=BabyFeedingEventsFactory.nappyEvent;
				int duration=0, quantity=0;
				
				int tsColIndex=cursor.getColumnIndexOrThrow("timeStamp");
				String timeStampStr=cursor.getString(tsColIndex);
				Timestamp timeStamp = Timestamp.valueOf(timeStampStr);
			
				id=cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
				int wColIndex = cursor.getColumnIndexOrThrow("wet");
				int weti =cursor.getInt(wColIndex);
				int dColIndex= cursor.getColumnIndexOrThrow("dirty");
				int di=cursor.getInt(dColIndex);
				
				if(weti==1)
					duration=1;
				if(di==1)
					duration=2;
			
				eventType=nappyEvent;
				BabyFeedingEvent bne=new BabyFeedingEvent(timeStamp,duration,quantity,eventType, id );
				bnVector.add(bne);
				
		
				cursor.moveToNext();
				
			}
			return bnVector;
		}
		else
			return null;
	}
	
	public Vector<BabyFeedingEvent> generateBCEs(Cursor cursor)
	{
		Vector <BabyFeedingEvent> bceVector = new Vector <BabyFeedingEvent>();
		if(cursor!=null)
		{
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				
				long id;
				String eventType=BabyFeedingEventsFactory.customEvent;
				
				
				int tsColIndex=cursor.getColumnIndexOrThrow("timeStamp");
				String timeStampStr=cursor.getString(tsColIndex);
				Timestamp timeStamp = Timestamp.valueOf(timeStampStr);
			
				id=cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
				int wColIndex = cursor.getColumnIndexOrThrow( FeedingEventsContract.CustomerEvents.column_title);
				String cet =cursor.getString(wColIndex);
				//int dColIndex= cursor.getColumnIndexOrThrow(FeedingEventsContract.CustomerEvents.column_description);
				//int cdi=cursor.getInt(dColIndex);
				
			
				eventType=customEvent;
				BabyFeedingEvent bne=new BabyFeedingEvent(timeStamp,0,0,eventType,cet, id );
				bceVector.add(bne);
				
		
				cursor.moveToNext();
				
			}
			return bceVector;
		}
		else
			return null;
	}

}
