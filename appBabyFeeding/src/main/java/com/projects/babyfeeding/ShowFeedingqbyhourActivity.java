package com.projects.babyfeeding;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.androidplot.ui.Anchor;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;

public class ShowFeedingqbyhourActivity extends Activity implements OnTouchListener, OnLayoutChangeListener
{

	private XYPlot plot;

	long minX;
	long maxX;
	long wminX;
	long wmaxX;
	int maxRangeValue = 180;
	Calendar maxDomainValue ;
	Number[] seriesdNumbers={0,0};
	Number[] seriesbNumbers={0,0};
	Number[] serieseNumbers={0,0};
	private MyBarFormatter formatter1;
	private MyBarFormatter formatter2;
	private MyBarFormatter formatter3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_feedingqbyhour);
		getFeedsVolumebyHour();
		plot=(XYPlot)findViewById(R.id.plot5);

		localiseRangeLabel();
		configureDomainAndRangeTitle();
		configureGraph();
		configureLegend();
		configureRangeDomainStepValues();
		createAndApplySeries1();
		createAndApplySeries2();
		createAndApplySeries3();

		MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));
		renderer.setBarOrientation(MyBarRenderer.BarOrientation.SIDE_BY_SIDE);

		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, 60);
	//	plot.setOnTouchListener(this);
		PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.STRETCH_HORIZONTAL);

		configureDomainRangeMaxMin();

		//plot.addOnLayoutChangeListener(this);
	}




	void createAndApplySeries1(){
		String brf=getResources().getString(R.string.legendBreastfeed);
		XYSeries seriesd = new SimpleXYSeries(
				Arrays.asList(seriesdNumbers),          // SimpleXYSeries takes a List so turn our array into a List
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED , // Y_VALS_ONLY means use the element index as the x value
				brf);
		formatter1 = new MyBarFormatter(getResources().getColor(R.color.feedHBreast), getResources().getColor(R.color.blue));
		plot.addSeries(seriesd, formatter1);

	}

	void createAndApplySeries2(){
		String bof=getResources().getString(R.string.legendBottlefeed);
		XYSeries seriesb = new SimpleXYSeries(
				Arrays.asList(seriesbNumbers),          // SimpleXYSeries takes a List so turn our array into a List
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED , // Y_VALS_ONLY means use the element index as the x value
				bof);
		formatter2 = new MyBarFormatter(getResources().getColor(R.color.feedHBottle), getResources().getColor(R.color.blue));
		plot.addSeries(seriesb, formatter2);

	}

	void createAndApplySeries3(){
		String pf=getResources().getString(R.string.legendPumpfeed);
		XYSeries seriese = new SimpleXYSeries(
				Arrays.asList(serieseNumbers),          // SimpleXYSeries takes a List so turn our array into a List
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED , // Y_VALS_ONLY means use the element index as the x value
				pf);
		formatter3 = new MyBarFormatter(getResources().getColor(R.color.feedHExpress), getResources().getColor(R.color.blue));
		plot.addSeries(seriese, formatter3);
	}

	void configureRangeDomainStepValues(){
		//	plot.setRangeLowerBoundary(0, BoundaryMode.GROW);
		//	plot.setDomainLowerBoundary(0, BoundaryMode.GROW);
		plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 3600*1000);
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10);

		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new SimpleDateFormat("H:00 E"));
		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setTextSize(30);
		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(Formatting.getDeci0());
		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).getPaint().setTextSize(30);


		plot.setLinesPerRangeLabel(5);
		plot.setLinesPerDomainLabel(2);

		//plot.setRangeUpperBoundary(0, BoundaryMode.FIXED);
		//plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);

	}



	void configureDomainRangeMaxMin(){
		plot.calculateMinMaxVals();
		//Calendar lowerBoundary = Calendar.getInstance();
		//lowerBoundary.add(Calendar.DAY_OF_MONTH, -1);
		if(maxDomainValue == null)
			maxDomainValue = Calendar.getInstance();
		Calendar lowerDomainBoudary = (Calendar)maxDomainValue.clone();
		lowerDomainBoudary.add(Calendar.HOUR, -12);
		plot.setDomainBoundaries(lowerDomainBoudary.getTime().getTime(), maxDomainValue.getTime().getTime(), BoundaryMode.FIXED);
		plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		plot.setRangeUpperBoundary(maxRangeValue, BoundaryMode.FIXED);

	}



	void configureLegend() {
		//plot.getGraph().getRangeCursorPaint().setTextSize(3);
		//plot.getGraph().getRangeOriginLinePaint().setTextSize(12);
		//plot.getGraph().getDomainOriginLinePaint().setTextSize(8);
		//	plot.getGraph().getDomainCursorPaint().setTextSize(3);

		plot.getLegend().getTextPaint().setTextSize(30);
		plot.getLegend().setPadding(0,0,0,0);
		plot.getLegend().position(30, HorizontalPositioning.ABSOLUTE_FROM_RIGHT, 30, VerticalPositioning.ABSOLUTE_FROM_BOTTOM);
	/*	plot.getLegend().getIconSize().getHeight().setValue(15);
		plot.getLegend().getIconSize().getWidth().setValue(15);
		plot.getLegend().getHeightMetric().setValue(25);
		plot.getLegend().getWidthMetric().setValue(0.7f);
		plot.getLegend().getPositionMetrics().getXPositionMetric().setValue(4);*/
		plot.getLegend().getPositionMetrics().setAnchor(Anchor.RIGHT_BOTTOM);
		plot.getGraph().getDomainGridLinePaint().setColor(0xffb6e9b5);
		plot.getGraph().getRangeGridLinePaint().setColor(0xffb6e9b5);
		plot.getGraph().getGridBackgroundPaint().setColor(0xFFFFFF);

	}

	void configureGraph(){
		plot.getGraph().setMarginTop(0);
		plot.getGraph().setMarginBottom(100);
		plot.getGraph().setMarginLeft(100);
		plot.getGraph().setMarginRight(50);

	}

	void configureDomainAndRangeTitle(){

		//plot.getDomainTitle().setAnchor(Anchor.LEFT_TOP);
		plot.getDomainTitle().getLabelPaint().setTextSize(30);
		//plot.getDomainTitle().setPaddingBottom(15);
		plot.getDomainTitle().setAutoPackEnabled(true);
		plot.getDomainTitle().position(30, HorizontalPositioning.ABSOLUTE_FROM_LEFT, 50, VerticalPositioning.ABSOLUTE_FROM_BOTTOM);


		//plot.getRangeTitle().setAnchor(Anchor.CENTER);
		//plot.getRangeTitle().setMarginTop(0);
		plot.getRangeTitle().getLabelPaint().setTextSize(30);
		//plot.getRangeTitle().setPaddingLeft(15);
		plot.getRangeTitle().setAutoPackEnabled(true);
		plot.getRangeTitle().position(30, HorizontalPositioning.ABSOLUTE_FROM_LEFT, 30, VerticalPositioning.ABSOLUTE_FROM_TOP);

	}


	void localiseRangeLabel(){

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String locale = getApplicationContext().getResources().getConfiguration().locale.getLanguage();
		final String vup= sharedPreferences.getString("Volumn_Unit", "");


		if(vup.equalsIgnoreCase("2"))
		{
			if(locale.equalsIgnoreCase("en"))
				plot.setRangeLabel("mins/ozs");
				// else if(locale.equalsIgnoreCase("zh_cn")||locale.equalsIgnoreCase("zh")||locale.equalsIgnoreCase("zh_TW"))
			else if(locale.contains("zh"))
				plot.setRangeLabel("分钟／盎司");
			else if(locale.contains("fr"))
				plot.setRangeLabel("mins/ozs");
			else
				plot.setRangeLabel("mins/ozs");
		}


	}





	public void getFeedsVolumebyHour()
	{
		int maxDuration = 0;
		double maxBf = 0, maxEf = 0;
		Vector<XYData> dLine=new Vector<XYData>();
		Vector<XYDataDouble> vELine=new Vector<XYDataDouble>();
		Vector<XYDataDouble> vBLine=new Vector<XYDataDouble>();
		int totalRDuration=0, totalLDuration=0;

		BFDBOpenHelper db=new BFDBOpenHelper(getApplicationContext());
		SQLiteDatabase dbr=db.getReadableDatabase();

		String sqlIfData= "SELECT COUNT(*) FROM "+ FeedingEventsContract.Feeds.table_name;
		Cursor cur = dbr.rawQuery(sqlIfData, null);
		if (cur != null) {
			cur.moveToFirst();                       // Always one row returned.
			if (cur.getInt (0) == 0)
			{

			}
			else
			{
				String sqlFindMinDay="select Min("+ FeedingEventsContract.Feeds.column_timeStamp+ ") " +
						"as minDay from "+FeedingEventsContract.Feeds.table_name;
				Cursor c=dbr.rawQuery(sqlFindMinDay, null);
				c.moveToFirst();

				//String mindays = c.getString(c.getColumnIndex("minDay"));

				//Log.i("MinDay", c.getString(c.getColumnIndex("minDay")));

				String sqlFindMaxDay="select Max("+ FeedingEventsContract.Feeds.column_timeStamp+ ") as maxDay from "+FeedingEventsContract.Feeds.table_name;
				Cursor c2=dbr.rawQuery(sqlFindMaxDay, null);
				int temp=c2.getCount();
				c2.moveToFirst();
				String maxdays = c2.getString(c2.getColumnIndex("maxDay"));
				//Log.i("MaxDay", c2.getString(c2.getColumnIndex("maxDay")));

				Calendar cal=Calendar.getInstance();

				Timestamp tsmaxDays=Timestamp.valueOf(maxdays);
				Calendar calEnd=Calendar.getInstance();
				calEnd.setTimeInMillis(tsmaxDays.getTime());
				calEnd.set(Calendar.HOUR_OF_DAY, calEnd.get(Calendar.HOUR_OF_DAY)+2);
				calEnd.set(Calendar.MINUTE, 0);
				calEnd.set(Calendar.SECOND, 0);
				//Timestamp[] ts2= BFUtilities.getDate(calEnd);
				//calEnd.setTimeInMillis(ts2[1].getTime());

				Calendar calStart=(Calendar)calEnd.clone();
				calStart.add(Calendar.DATE, -6);


				Calendar current=(Calendar)calStart.clone();
				int com=current.compareTo(calEnd);

				while(current.compareTo(calEnd)<=0)

				{
					Timestamp[] ts= BFUtilities.getDate(current);
					Calendar calt=Calendar.getInstance();
					calt.setTimeInMillis(ts[0].getTime());
					Calendar cale=Calendar.getInstance();
					cale.setTimeInMillis(ts[1].getTime());

					while(calt.compareTo(cale)<=0)
					{
						int h=calt.get(Calendar.HOUR_OF_DAY);
						Timestamp[] tsh=BFUtilities.getHour(calt);

						String sqlFeedsByHour= "select sum("+ FeedingEventsContract.Feeds.column_leftDuration +") as totalLeftD, sum("
								+ FeedingEventsContract.Feeds.column_rightDuration
								+ ") as totalRightD, sum("+
								FeedingEventsContract.Feeds.column_bottleFeed +
								") as totalBottle, sum(" + FeedingEventsContract.Feeds.column_expressFeed+") as totalExpressed from "+
								FeedingEventsContract.Feeds.table_name + " "+"where "+
								FeedingEventsContract.Feeds.column_timeStamp +
								" between '"+tsh[0] + "'  and '"+tsh[1] + "'";

						Cursor c3=dbr.rawQuery(sqlFeedsByHour, null);

						int x=c3.getCount();
						int i=0;
						if(c3.moveToFirst())
						{

							while(!c3.isAfterLast())
							{


								int tldc=c3.getColumnIndex("totalLeftD");

								int tlDuration=c3.getInt(tldc);
								int trdc=c3.getColumnIndex("totalRightD");

								int trDuration=c3.getInt(trdc);

								int tDuration=tlDuration+trDuration;
								String es=FeedingEventsContract.Feeds.column_expressFeed;
								String bs= FeedingEventsContract.Feeds.column_bottleFeed;
								int bqc=c3.getColumnIndex("totalBottle");
								Double totalBQ=c3.getDouble(bqc);
								int eqc=c3.getColumnIndex("totalExpressed");
								Double totalEQ=c3.getDouble(eqc);


								SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

								final String vup= sharedPreferences.getString("Volumn_Unit", "");
								if(vup.equalsIgnoreCase("2"))
								{	totalBQ=totalBQ/30;
									totalEQ=totalEQ/30;
								}
								XYData tempDataD=new XYData(tsh[0],tDuration);
								XYDataDouble tempDataE=new XYDataDouble(tsh[0],totalEQ);
								XYDataDouble tempDataB=new XYDataDouble(tsh[0], totalBQ);
								dLine.add(tempDataD);
								vELine.add(tempDataE);
								vBLine.add(tempDataB);
								c3.moveToNext();
							}


						}
						calt.add(Calendar.HOUR_OF_DAY,1);
					}
					current.add(Calendar.DATE,1);
					//com=current.compareTo(calEnd);

				}

				maxDomainValue = calEnd;
				int lc=dLine.size();

				int bc=vBLine.size();

				int ec=vELine.size();

				maxDuration = (Collections.max(dLine, new Comparator<ShowFeedingqbyhourActivity.XYData>() {


					@Override
					public int compare(ShowFeedingqbyhourActivity.XYData o1, ShowFeedingqbyhourActivity.XYData o2) {
						if(o1.quanY > o2.quanY)
							return 1;
						else if(o1.quanY < o2.quanY)
							return -1;
						else
							return 0;
					}
				})).quanY;

				maxBf = (Collections.max(vBLine, new Comparator<ShowFeedingqbyhourActivity.XYDataDouble>() {


					@Override
					public int compare(ShowFeedingqbyhourActivity.XYDataDouble o1, ShowFeedingqbyhourActivity.XYDataDouble o2) {
						if(o1.quanYD > o2.quanYD)
							return 1;
						else if(o1.quanYD < o2.quanYD)
							return -1;
						else
							return 0;
					}
				})).quanYD;

				maxEf = (Collections.max(vELine, new Comparator<ShowFeedingqbyhourActivity.XYDataDouble>() {


					@Override
					public int compare(ShowFeedingqbyhourActivity.XYDataDouble o1, ShowFeedingqbyhourActivity.XYDataDouble o2) {
						if(o1.quanYD > o2.quanYD)
							return 1;
						else if(o1.quanYD < o2.quanYD)
							return -1;
						else
							return 0;
					}
				})).quanYD;

				Integer[] maxValues = {maxDuration, (int)maxBf, (int)maxEf};
				List<Integer> list = Arrays.asList(maxValues);
				maxRangeValue = Collections.max(Arrays.asList(maxValues));

				seriesdNumbers=toNumbers(dLine);
				seriesbNumbers=toNumbersDouble(vBLine);
				serieseNumbers=toNumbersDouble(vELine);
			}
		}
	}


	public Number[] toNumbers(Vector<XYData> vdata)
	{
		int size=vdata.size();
		Number[] numbers= new Number[size*2];
		for(int i=0;i<size;i++)
		{
			int count=i*2;
			numbers[count]=vdata.get(i).tsX.getTime();
			numbers[count+1]=vdata.get(i).quanY;
		}
		return numbers;

	}


	public Number[] toNumbersDouble(Vector<XYDataDouble> vdata)
	{
		int size=vdata.size();
		Number[] numbers= new Number[size*2];
		for(int i=0;i<size;i++)
		{
			int count=i*2;
			numbers[count]=vdata.get(i).tsX.getTime();
			numbers[count+1]=vdata.get(i).quanYD;

		}
		return numbers;

	}

	public class XYData
	{
		public Timestamp tsX;
		public int quanY;
		XYData(Timestamp tsx, int quany )
		{
			tsX=tsx;
			quanY=quany;
		}
	}

	public class XYDataDouble
	{
		public Timestamp tsX;
		public Double quanYD;
		XYDataDouble(Timestamp tsx, Double quany )
		{
			tsX=tsx;
			quanYD=quany;
		}
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//	getMenuInflater().inflate(R.menu.show_feeding_q, menu);
		return true;
	}

	static final int NONE = 0;
	static final int ONE_FINGER_DRAG = 1;
	static final int TWO_FINGERS_DRAG = 2;
	int mode = NONE;

	PointF firstFinger;
	float distBetweenFingers;
	boolean stopThread = false;

	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: // Start gesture
				firstFinger = new PointF(event.getX(), event.getY());
				mode = ONE_FINGER_DRAG;
				stopThread = true;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: // second finger
				distBetweenFingers = spacing(event);
				// the distance check is done to avoid false alarms
				if (distBetweenFingers > 5f) {
					mode = TWO_FINGERS_DRAG;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == ONE_FINGER_DRAG) {
					PointF oldFirstFinger = firstFinger;
					firstFinger = new PointF(event.getX(), event.getY());
					scroll(oldFirstFinger.x - firstFinger.x);
					plot.setDomainBoundaries(wminX, wmaxX,
							BoundaryMode.FIXED);
					plot.redraw();

				}
	               /* else if (mode == TWO_FINGERS_DRAG) {
	                    float oldDist = distBetweenFingers;
	                    distBetweenFingers = spacing(event);
	                    zoom(oldDist / distBetweenFingers);
	                    plot.setDomainBoundaries(wminX, wmaxX,
	                            BoundaryMode.FIXED);
	                    plot.redraw();
	                }*/
				break;
		}
		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private void scroll(double pan) {
		long domainSpan = wmaxX - wminX;
		double step = domainSpan / plot.getWidth();
		long offset = (long) (pan * step);
		wminX += offset;
		wmaxX += offset;

		clampToDomainBounds(domainSpan);
	}

	private void zoom(double scale)
	{
		long domainSpan = wmaxX - wminX;
		long domainMidPoint = wmaxX - domainSpan / 2;
		long offset = (long) (domainSpan * scale / 2.0f);

		// Don't zoom more than a day
		if(offset > 24*3600*1000)
		{
			wminX = domainMidPoint - offset;
			wmaxX = domainMidPoint + offset;

			wminX = Math.max(wminX, minX);
			wmaxX = Math.min(wmaxX, maxX);
		}

		MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));
		long nbHours=(wmaxX-wminX)/(3600L*1000L);
		float width = (((float)plot.getWidth()) / nbHours ) / 3;
		// renderer.setBarWidth(width);
		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, width);
	}

	private void clampToDomainBounds(long domainSpan)
	{
		// enforce left scroll boundary:
		if (wminX < minX)
		{
			wminX = minX;
			//wminX = minX+1800L*1000L;
			wmaxX = wminX + domainSpan;
		}
		if (wmaxX > maxX)
		{
			wmaxX = maxX;
			// wmaxX = maxX+1800L*1000L;
			wminX =wmaxX - domainSpan;
		}
	}




	@Override
	public void onLayoutChange(View v, int left, int top, int right,
							   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
	{

		MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));
		long nbHours=(wmaxX-wminX)/(3600L*1000L);
		float gridWidth = plot.getWidth();
		float width = gridWidth / nbHours / 1.5f;
		//renderer.setBarWidth(width);
		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH,width);

	}


	class MyBarFormatter extends BarFormatter {
		public MyBarFormatter(int fillColor, int borderColor) {
			super(fillColor, borderColor);
		}

		@Override
		public Class<? extends SeriesRenderer> getRendererClass() {
			return MyBarRenderer.class;
		}

		@Override
		public SeriesRenderer getRendererInstance(XYPlot plot) {
			return new MyBarRenderer(plot);
		}
	}

	class MyBarRenderer extends BarRenderer<MyBarFormatter>
	{

		public MyBarRenderer(XYPlot plot) {
			super(plot);
		}


		//@Override
		// TODO: figure out why using @Override screws up the Maven builds
		//protected MyBarFormatter getFormatter(int index, XYSeries series) {
/*	            if(selection != null &&
	                    selection.second == series && 
	                    selection.first == index) {
	                return selectionFormatter;
	            } else {*/
		//return getFormatter(series);
		//}

		//return formatter1;
		//}

	}

}
