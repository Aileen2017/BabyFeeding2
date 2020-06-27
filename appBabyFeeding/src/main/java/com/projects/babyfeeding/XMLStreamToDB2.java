package com.projects.babyfeeding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class XMLStreamToDB2 {
	
	public XMLStreamToDB2()
	{
		
	}
	
	/*public List XMLToData(FileInputStream fin) throws XmlPullParserException, IOException
	{
		String s="<?xml version = '1.0' encoding = 'UTF-8'?> <database> "+
				
				"<Feeds>"+
				"<feed timeStamp=\"2013-06-30 22:42:07.124\" _id=\"6\""+
				"leftDuration=\"30\""+
				"rightDuration=\"0\"" +
				"bottleQuantity=\"0\""+
				"EFQuantity=\"0\""+
				"note=\"\""+
				"></feed>"+
				"</Feeds></database>";
		ByteArrayInputStream bis= new ByteArrayInputStream(s.getBytes());
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Document dom = builder.parse(bis);
            Document dom = builder.parse(fin);
            Element root = (Element) dom.getDocumentElement();
            NodeList feedItems = ((Element) root).getElementsByTagName("feed");
            NodeList pumpItems = ((Element) root).getElementsByTagName("pump");
            NodeList nappyItems = ((Element) root).getElementsByTagName("nappy");
            NodeList CEItems = ((Element) root).getElementsByTagName("CE");
            
            lfs=readFeeds(feedItems);
            lps=readPumps(pumpItems);
            lns=readNappys(nappyItems);
            
            lces=readNappys(CEItems);
        }      
        catch (Exception e) {
            throw new RuntimeException(e);
        } 
        List li =new ArrayList();
        li.addAll(lfs);
        li.addAll(lps);
        li.addAll(lns);
        
        return li;
    }*/

	public List<Event> XMLToData(InputStream in) throws XmlPullParserException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
       
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
           
            Document dom = builder.parse(in);
            Element root = (Element) dom.getDocumentElement();
            NodeList feedItems = ((Element) root).getElementsByTagName("feed");
            NodeList pumpItems = ((Element) root).getElementsByTagName("pump");
            NodeList nappyItems = ((Element) root).getElementsByTagName("nappy");
            
            NodeList CEItems = ((Element) root).getElementsByTagName("CE");
            
            List<Event> lfs=readFeeds(feedItems);
            List<Event> lps=readPumps(pumpItems);
            List<Event> lns=readNappys(nappyItems);
            List<Event> lces=readCEs(CEItems);
           
            List<Event> li =new ArrayList<Event>();
            li.addAll(lfs);
            li.addAll(lps);
            li.addAll(lns);
            li.addAll(lces);
            return li;
        }      
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        } 
    }
	
	public List<Event> readFeeds(NodeList feeds)
	{
		List<Event> lfs = new ArrayList<Event>();
		for (int i=0;i<feeds.getLength();i++){
            
            Node item = feeds.item(i);
            if(item.hasAttributes())
            {
            	Feed f = new Feed();
            	NamedNodeMap attrs = item.getAttributes();  
            	for(int j = 0 ; j<attrs.getLength() ; j++) 
            	{
            		Attr attribute = (Attr)attrs.item(j);     
            		
            		String name=attribute.getName();
            		String value=attribute.getValue();
            		if(name.equals("timeStamp"))
            			f.ts=Timestamp.valueOf(value);
            		if(name.equals("leftDuration"))
            			f.lDuration=Integer.parseInt(value);
            		if(name.equals("rightDuration"))
            			f.rDuration=Integer.parseInt(value);
            		if(name.equals("bottleQuantity"))
            			f.BFQuantity=Double.parseDouble(value);
            		if(name.equals("EFQuantity"))
            			f.EFQuantity=Double.parseDouble(value);
            		if(name.equals("note"))
            			f.note=value;
            	}
            	lfs.add(f);
            }
		}
		return lfs;
	}
	
	public List<Event> readPumps(NodeList pumps)
	{
		List<Event> lfs = new ArrayList<Event>();
		for (int i=0;i<pumps.getLength();i++){
            
            Node item = pumps.item(i);
            if(item.hasAttributes())
            {
            	Pump f = new Pump();
            	NamedNodeMap attrs = item.getAttributes();  
            	for(int j = 0 ; j<attrs.getLength() ; j++) 
            	{
            		Attr attribute = (Attr)attrs.item(j);     
            		
            		String name=attribute.getName();
            		String value=attribute.getValue();
            		if(name.equals("timeStamp"))
            			f.ts=Timestamp.valueOf(value);
            		if(name.equals("leftDuration"))
            			f.lDuration=Integer.parseInt(value);
            		if(name.equals("rightDuration"))
            			f.rDuration=Integer.parseInt(value);
            		if(name.equals("leftVolumn"))
            			f.LQuantity=Double.parseDouble(value);
            		if(name.equals("rightVolumn"))
            			f.RQuantity=Double.parseDouble(value);
            		if(name.equals("note"))
            			f.note=value;
            	}
            	lfs.add(f);
            }
		}
		return lfs;
	}
	
	public List<Event> readNappys(NodeList nappys)
	{
		List<Event> lfs = new ArrayList<Event>();
		for (int i=0;i<nappys.getLength();i++){
            
            Node item = nappys.item(i);
            if(item.hasAttributes())
            {
            	Nappy f = new Nappy();
            	NamedNodeMap attrs = item.getAttributes();  
            	for(int j = 0 ; j<attrs.getLength() ; j++) 
            	{
            		Attr attribute = (Attr)attrs.item(j);     
            		
            		String name=attribute.getName();
            		String value=attribute.getValue();
            		if(name.equals("timeStamp"))
            			f.ts=Timestamp.valueOf(value);
            		if(name.equals("wet"))
            		{	int valuei=Integer.parseInt(value);
            			if(valuei==1)
     		            	f.wet = true;
     		            else
     		            	f.wet=false;
            		}
            		if(name.equals("dirty"))
            		{
            			int valuei=Integer.parseInt(value); 
            			if(valuei==1)
      		            	f.dirty = true;
      		            else
      		            	f.dirty=false;
            		}
            		
            		if(name.equals("note"))
            			f.note=value;
            	}
            	lfs.add(f);
            }
		}
		return lfs;
	}
	
	public List<Event> readCEs(NodeList customerEvents)
	{
		List<Event> lces = new ArrayList<Event>();
		for (int i=0;i<customerEvents.getLength();i++){
            
            Node item = customerEvents.item(i);
            if(item.hasAttributes())
            {
            	CustomerEvent ce = new CustomerEvent();
            	NamedNodeMap attrs = item.getAttributes();  
            	for(int j = 0 ; j<attrs.getLength() ; j++) 
            	{
            		Attr attribute = (Attr)attrs.item(j);     
            		
            		String name=attribute.getName();
            		String value=attribute.getValue();
            		if(name.equals("timeStamp"))
            			ce.ts=Timestamp.valueOf(value);
            		if(name.equals("description"))
            			ce.description=value;
            		
            		if(name.equals("title"))
            			ce.title=value;
            	}
            	lces.add(ce);
            }
		}
		return lces;
	}
	
	
	public void listDataToDB(List<Event> li, Uri basePath, Context context) throws XmlPullParserException, IOException
	{
		String PATH_Feeds = "/Feeds";
		Uri feedPath =  Uri.parse(basePath+PATH_Feeds);
		String PATH_Pumps="/BreastPumpEvents";
		Uri pumpPath=Uri.parse(basePath+PATH_Pumps);
		
		String PATH_Nappys="/Nappys";
		Uri nappyPath=Uri.parse(basePath+PATH_Nappys);
		
		String PATH_CEs="/CustomerEvents";
		Uri CEPath=Uri.parse(basePath+PATH_CEs);
		
		context.getContentResolver().delete(basePath, null,null);    
	        int size= li.size();
	        for(int i=0;i<size;i++)
	        {	
	        	
	        	if(li.get(i).getClass().equals(Feed.class ))
	        	{
	        		Feed feed=(Feed) li.get(i);
	        		insertFeed(feed, feedPath, context);
	        	}
	        	if(li.get(i).getClass().equals(Pump.class ))
	        	{
	        		Pump pump=(Pump)li.get(i);
	        		insertPump(pump, pumpPath, context);
	        	}
	        	if(li.get(i).getClass().equals(Nappy.class ))
	        	{
	        		Nappy nappy = (Nappy)li.get(i);
	        		insertNappy(nappy, nappyPath, context);
	        	}
	        	if(li.get(i).getClass().equals(CustomerEvent.class ))
	        	{
	        		CustomerEvent ce = (CustomerEvent)li.get(i);
	        		insertCE(ce, CEPath, context);
	        	}
	        	
	        }

	}
	
	public void insertFeed(Feed feed, Uri feedPath, Context context)
	{
		
		ContentValues values = new ContentValues();
		
        values.put(FeedingEventsContract.Feeds.column_timeStamp, feed.ts.toString());
        values.put(FeedingEventsContract.Feeds.column_leftDuration, Integer.toString(feed.lDuration) );
        values.put(FeedingEventsContract.Feeds.column_rightDuration, Integer.toString(feed.rDuration));
        values.put(FeedingEventsContract.Feeds.column_bottleFeed, Double.toString(feed.BFQuantity));
        values.put(FeedingEventsContract.Feeds.column_expressFeed, Double.toString(feed.EFQuantity));
        values.put(FeedingEventsContract.Feeds.column_note, feed.note);
		context.getContentResolver().insert(feedPath, values);
	}
	
	public void insertPump(Pump pump, Uri pumpPath, Context context)
	{
	
		ContentValues values = new ContentValues();
		
	    values.put(FeedingEventsContract.BreastPumpEvents.column_timeStamp, pump.ts.toString());
	    values.put(FeedingEventsContract.BreastPumpEvents.column_leftDuration, Integer.toString(pump.lDuration) );
	    values.put(FeedingEventsContract.BreastPumpEvents.column_rightDuration, Integer.toString(pump.rDuration));
	    values.put(FeedingEventsContract.BreastPumpEvents.column_rightVolumn, Double.toString(pump.RQuantity));
	    values.put(FeedingEventsContract.BreastPumpEvents.column_leftVolumn, Double.toString(pump.LQuantity));
	    values.put(FeedingEventsContract.Feeds.column_note, pump.note);
	    
		context.getContentResolver().insert(pumpPath, values);
	}
	
	public void insertNappy(Nappy nappy, Uri nappyPath, Context context)
	{
	
			ContentValues values = new ContentValues();
    	
    		values.put(FeedingEventsContract.NapEvents.column_timeStamp, nappy.ts.toString());
	        values.put(FeedingEventsContract.NapEvents.column_wet, nappy.wet );
	        values.put(FeedingEventsContract.NapEvents.column_dirty,nappy.dirty);
	      
	        values.put(FeedingEventsContract.NapEvents.column_note, nappy.note);
    	
	        context.getContentResolver().insert(nappyPath, values);
	}
	
	public void insertCE(CustomerEvent CE, Uri CEPath, Context context)
	{
	
			ContentValues values = new ContentValues();
    	
    		values.put(FeedingEventsContract.CustomerEvents.column_timeStamp,CE.ts.toString());
	        values.put(FeedingEventsContract.CustomerEvents.column_title, CE.title );
	      
	        values.put(FeedingEventsContract.CustomerEvents.column_description, CE.description);
    	
	        context.getContentResolver().insert(CEPath, values);
	}
	
/*	
	public void xmlToDB(Context context, Uri basePath, FileInputStream fin) throws XmlPullParserException, IOException
	{
		 List li = XMLToData(fin);
		 listDataToDB(li, basePath,context);
		
	}*/
	
	public void xmlToDB(Context context, Uri basePath, InputStream in) throws XmlPullParserException, IOException
	{
		//String temp=BFUtilities.ISToString(in);
		String temp=BFUtilities.StreamToString2(in);
		//ByteArrayInputStream bis= new ByteArrayInputStream(temp.getBytes("UTF-8"));
		
		ByteArrayInputStream bis= new ByteArrayInputStream(temp.getBytes());
		List<Event> li = XMLToData(bis);
		//List li = XMLToData(in);
		listDataToDB(li, basePath,context);
		
	}

}
