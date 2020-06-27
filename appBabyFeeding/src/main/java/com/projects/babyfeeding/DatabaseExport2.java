package com.projects.babyfeeding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.w3c.dom.Element;

public class DatabaseExport2 {

	private SQLiteDatabase db;
	
	public DatabaseExport2(SQLiteDatabase db)
	{
		this.db=db;
	}
	
	public Document createDocument() throws ParserConfigurationException
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		return documentBuilder.newDocument(); 
	}

	private Vector<String> getTables()
	{
		Vector<String> tableNames=new Vector<String>();
		String sql = "select * from sqlite_master where type='table'";
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) 
		{
	        while ( !c.isAfterLast() )
	        {
	            String tableName= c.getString( c.getColumnIndex("name")) ;
	            
	            //Log.i("AllTables",tableName );
	            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
	                     && !tableName.startsWith("uidx")) 
	            {
	              try{
	            	  tableNames.add(tableName);
	              }
	              catch(Exception e)
	              {
	            	 // Log.w("DatabaseExport", e.toString());
	              }
	          
	            }
		
	            c.moveToNext();
	        }
		
		}
		return tableNames;
	}
	
	
		private Element exportTable(Document document, final String tableName) throws IOException, ParserConfigurationException 
		{
				Element rootElement = document.createElement(tableName);
				String tableChild="child";
				if(tableName.equals("Feeds"))
					tableChild="feed";
				if(tableName.equals("BreastPumpEvents"))
					tableChild="pump";
				if(tableName.equals("NappyEvent"))
					tableChild="nappy";
				if(tableName.equals("CustomerEvents"))
					tableChild="CE";	
				
				String sql = "select * from " + tableName;
			     Cursor c = db.rawQuery(sql, null);
			     int count= c.getCount();
			      try{
				      if (c.moveToFirst()) {
				         int cols = c.getColumnCount();
				         do {
				        	 Element childElement = (Element)document.createElement(tableChild);
				        	 
				            for (int i = 0; i < cols; i++) 
				            {
				              String columnName=c.getColumnName(i);
				              String content=c.getString(c.getColumnIndex(columnName));
				              
				              childElement.setAttribute(columnName,content);
				             
				            }
				            ((Node) rootElement).appendChild((Node)childElement);
				         } while (c.moveToNext());
				      }
			      }
			      catch(Exception e)
			      {
			    	 // Log.i("TableExport", e.toString());
			      }
			      c.close();
	
			
			   return rootElement;
			      
		}
		
		
		
		
		public Document dbToXML() throws ParserConfigurationException, IOException 
		{
			Document document=createDocument();
			Element element=document.createElement("Database");
			document.appendChild(element);
			
			Vector<String> tableNames=getTables();
			int count = tableNames.size();
			for(int i=0;i<count;i++)
			{
					
				Element tableNode = exportTable(document, tableNames.get(i));
				element.appendChild(tableNode);
		
			}
			return document;
		}
		
		public void xmlToStream(Document document, OutputStream out) throws TransformerException 
		{
			
				TransformerFactory factory = TransformerFactory.newInstance();			
				Transformer transformer = factory.newTransformer();
				DOMSource domSource = new DOMSource(document.getDocumentElement());
			
				StreamResult result = new StreamResult(out);
				
				Properties outFormat = new Properties();
	            outFormat.setProperty(OutputKeys.INDENT, "yes");
	            outFormat.setProperty(OutputKeys.METHOD, "xml");
	            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	            outFormat.setProperty(OutputKeys.VERSION, "1.0");
	            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperties(outFormat);
	            transformer.transform(domSource, result);
	       
		}
		
		public void dbToStream(OutputStream out) throws IOException, ParserConfigurationException, TransformerException
		{
			
				Document document = dbToXML();
				xmlToStream(document, out);
			
		}
}

