package com.projects.babyfeeding2;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;

public class ShowBreastUsage extends Activity implements OnTouchListener, OnLayoutChangeListener{

	private XYSeries series1;
	private XYSeries series2;
	private MyBarFormatter formatter1;
	private MyBarFormatter formatter2;
	Number[] series1Numbers={0,0} ;
	Number[] series2Numbers={0,0};
	int maxRangeValue;
	long minX;
	long maxX;
	long wminX;
	long wmaxX;

	private XYPlot plot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_breast_usage);

		Calendar c=Calendar.getInstance();
		long ts1=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts2=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts3=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts4=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts5=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts6=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts7=c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1);
		long ts8=c.getTimeInMillis();

		getBUbyDay();
		plot = (XYPlot) findViewById(R.id.BUplot);


		plot.setOnTouchListener(this);
		plot.addOnLayoutChangeListener(this);
		localiseRangeLabel();
		configureDomainAndRangeTitle();
		configureGraph();
		configureLegend();
		configureRangeDomainStepValues();
		createAndApplySeries1();
		createAndApplySeries2();
		configureDomainRangeMaxMin();
		ShowBreastUsage.MyBarRenderer renderer = ((ShowBreastUsage.MyBarRenderer)plot.getRenderer(ShowBreastUsage.MyBarRenderer.class));
		renderer.setBarOrientation(ShowFeedingqbyhourActivity.MyBarRenderer.BarOrientation.SIDE_BY_SIDE);

		renderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, 60);

		PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.STRETCH_HORIZONTAL);

	}

	void createAndApplySeries1(){
		String lb=getResources().getString(R.string.legendBBL);
		series1 = new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, lb);
		formatter1 = new MyBarFormatter(getResources().getColor(R.color.lblue), getResources().getColor(R.color.blue));
		formatter1.getLinePaint().setStrokeWidth(50.0f);
		plot.addSeries(series1, formatter1);
	}

	void createAndApplySeries2(){
		String rb=getResources().getString(R.string.legendBBR);
		formatter2 = new MyBarFormatter(getResources().getColor(R.color.lred), getResources().getColor(R.color.blue));
		series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, rb);
		plot.addSeries(series2, formatter2);
	}



	void configureRangeDomainStepValues(){
		//	plot.setRangeLowerBoundary(0, BoundaryMode.GROW);
		//	plot.setDomainLowerBoundary(0, BoundaryMode.GROW);

		plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 24*3600*1000);
		plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 10);

		Locale current = getResources().getConfiguration().locale;
		if(current.equals(Locale.SIMPLIFIED_CHINESE)) {
			plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new SimpleDateFormat("M月d日EE,"));
		}
		else if(current.equals(Locale.TRADITIONAL_CHINESE)) {
			plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new SimpleDateFormat("M月d日EE,"));
		}
		else {
			plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new SimpleDateFormat("EEE d MMM,"));
		}

		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).getPaint().setTextSize(30);
		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(Formatting.getDeci0());
		plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).getPaint().setTextSize(30);


		plot.setLinesPerRangeLabel(5);
		plot.setLinesPerDomainLabel(3);

		plot.setRangeUpperBoundary(0, BoundaryMode.FIXED);
		plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);

		//plot.setRangeBottomMin(0);
		//plot.setRangeBottomMax(0);


		//plot.getDomainLabelWidget().setMarginBottom(0);

		//plot.setRangeTopMin(plot.getCalculatedMaxY());
		//plot.setRangeTopMax(plot.getCalculatedMaxY());

	}


	void configureDomainRangeMaxMin(){
		plot.calculateMinMaxVals();
		Calendar lowerBoundary = Calendar.getInstance();
		lowerBoundary.add(Calendar.DAY_OF_MONTH, -7);
		plot.setDomainBoundaries(lowerBoundary.getTime().getTime(), Calendar.getInstance().getTime().getTime(), BoundaryMode.FIXED);
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

		MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));

		//renderer.setBarGap(30);



		//renderer.setBarRenderStyle(BarRenderer.BarRenderStyle.SIDE_BY_SIDE);
		//renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.VARIABLE_WIDTH);
		//renderer.setBarWidthStyle(BarRenderer.BarWidthStyle.FIXED_WIDTH);
		//renderer.setBarWidth(15);


		//plot.getLegendWidget().setMarginBottom(30);
	}

	void configureGraph(){
		plot.getGraph().setMarginTop(0);
		plot.getGraph().setMarginBottom(100);
		plot.getGraph().setMarginLeft(100);
		plot.getGraph().setMarginRight(50);
		//plot.getGraph().setPadding(30, 10, 30, 0);
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


		//plot.getTitleWidget().setMarginTop(5);
		//plot.getTitleWidget().getLabelPaint().setTypeface(Typeface.SANS_SERIF);

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




	public void getBUbyDay()
	{

		Vector<XYData> vLeft=new Vector<XYData>();
		Vector<XYData>  vRight=new Vector<XYData>();


		BFDBOpenHelper db=new BFDBOpenHelper(getApplicationContext());
		SQLiteDatabase dbr=db.getReadableDatabase();

		boolean feedData=false;
		boolean pumpData=false;
		Timestamp minfdaysTS=null, maxfdaysTS=null;
		Timestamp minpdaysTS=null, maxpdaysTS=null;
		Timestamp mindaysTS=null, maxdaysTS=null;

		String sqlIfData= "SELECT COUNT(*) FROM "+ FeedingEventsContract.Feeds.table_name;
		Cursor curf = dbr.rawQuery(sqlIfData, null);
		if (curf != null) {
			curf.moveToFirst();                       // Always one row returned.
			if (curf.getInt (0) == 0)
			{
				feedData=false;
			}
			else
			{
				feedData=true;
				String sqlFindMinDayf="select Min("+ FeedingEventsContract.Feeds.column_timeStamp+ ") as minDay from "+FeedingEventsContract.Feeds.table_name;
				Cursor c=dbr.rawQuery(sqlFindMinDayf, null);
				c.moveToFirst();
				String minfdays = c.getString(c.getColumnIndex("minDay"));
				minfdaysTS=Timestamp.valueOf(minfdays);
				//Log.i("MinDay", c.getString(c.getColumnIndex("minDay")));

				String sqlFindMaxDayf="select Max("+ FeedingEventsContract.Feeds.column_timeStamp+ ") as maxDay from "+FeedingEventsContract.Feeds.table_name;
				Cursor c2=dbr.rawQuery(sqlFindMaxDayf, null);
				c2.moveToFirst();
				String maxfdays = c2.getString(c2.getColumnIndex("maxDay"));
				maxfdaysTS=Timestamp.valueOf(maxfdays);
				//Log.i("MaxDay", c2.getString(c2.getColumnIndex("maxDay")));
			}

		}

		String sqlIfpData= "SELECT COUNT(*) FROM "+ FeedingEventsContract.BreastPumpEvents.table_name;
		Cursor curp = dbr.rawQuery(sqlIfpData, null);
		if (curp != null) {
			curp.moveToFirst();                       // Always one row returned.
			if (curp.getInt (0) == 0)
			{
				pumpData=false;
			}
			else
			{
				pumpData=true;
				String sqlFindMinDayp="select Min("+ FeedingEventsContract.BreastPumpEvents.column_timeStamp+ ") as minDay from "+FeedingEventsContract.BreastPumpEvents.table_name;
				Cursor cp=dbr.rawQuery(sqlFindMinDayp, null);
				cp.moveToFirst();
				String minpdays = cp.getString(cp.getColumnIndex("minDay"));
				minpdaysTS=Timestamp.valueOf(minpdays);
				//Log.i("MinDay", cp.getString(cp.getColumnIndex("minDay")));

				String sqlFindMaxDayp="select Max("+ FeedingEventsContract.BreastPumpEvents.column_timeStamp+ ") as maxDay from "+FeedingEventsContract.BreastPumpEvents.table_name;
				Cursor cp2=dbr.rawQuery(sqlFindMaxDayp, null);
				cp2.moveToFirst();
				String maxpdays = cp2.getString(cp2.getColumnIndex("maxDay"));
				maxpdaysTS=Timestamp.valueOf(maxpdays);
				//Log.i("MaxDay", cp2.getString(cp2.getColumnIndex("maxDay")));
			}

		}

		if(!feedData && pumpData)
		{
			mindaysTS=minpdaysTS;
			maxdaysTS=maxpdaysTS;
		}
		else if(feedData&&pumpData)
		{
			if(minfdaysTS.compareTo(minpdaysTS)<0)
				mindaysTS=(Timestamp)minfdaysTS.clone();
			else
				mindaysTS=minpdaysTS;

			if(maxfdaysTS.compareTo(maxpdaysTS)<0)
				maxdaysTS=maxpdaysTS;
			else
				maxdaysTS=maxfdaysTS;

		}
		else if(feedData&&!pumpData)
		{
			mindaysTS=minfdaysTS;
			maxdaysTS=maxfdaysTS;
		}
		else
		{
			mindaysTS=new Timestamp(0l);
			maxdaysTS=new Timestamp(0l);
		}


		if(pumpData||feedData)
		{

			Calendar cal=Calendar.getInstance();

			Calendar calStart=Calendar.getInstance();
			calStart.setTimeInMillis(mindaysTS.getTime());
			Timestamp[] ts3= BFUtilities.getDate(calStart);
			calStart.setTimeInMillis(ts3[0].getTime());

			Calendar calEnd=Calendar.getInstance();
			calEnd.setTimeInMillis(maxdaysTS.getTime());
			Timestamp[] ts2= BFUtilities.getDate(calEnd);
			calEnd.setTimeInMillis(ts2[1].getTime());

			Calendar current=Calendar.getInstance();
			current=(Calendar)calStart.clone();
			int com=current.compareTo(calEnd);



			while(current.compareTo(calEnd)<=0)
			{
				Timestamp[] ts= BFUtilities.getDate(current);

				String sqlSumFeedsByDay= "select sum(" + FeedingEventsContract.Feeds.column_leftDuration + ") as leftDuration, "+
						"sum(" + FeedingEventsContract.Feeds.column_rightDuration + ") as rightDuration from "+
						FeedingEventsContract.Feeds.table_name + " "+"where "+
						FeedingEventsContract.Feeds.column_timeStamp +
						" between '"+ts[0] + "'  and '"+ts[1] + "'";

				String sqlSumPumpsByDay= "select sum(" + FeedingEventsContract.BreastPumpEvents.column_leftDuration
						+ ") as leftDuration, "
						+ "sum("+FeedingEventsContract.BreastPumpEvents.column_rightDuration + ") as rightDuration from "
						+FeedingEventsContract.BreastPumpEvents.table_name+" "+"where "+
						FeedingEventsContract.BreastPumpEvents.column_timeStamp +
						" between '"+ts[0] + "'  and '"+ts[1] + "'";

				int totalLDuration=0, totalRDuration=0;
				Cursor c3=dbr.rawQuery(sqlSumFeedsByDay, null);
				Cursor c4=dbr.rawQuery(sqlSumPumpsByDay,  null);
				int x=c3.getCount();
				int i=0;

				if(c3.moveToFirst())
				{

					int ldc=c3.getColumnIndex("leftDuration");
					int lDuration=c3.getInt(ldc);
					int rdc=c3.getColumnIndex("rightDuration");
					int rDuration=c3.getInt(rdc);
					totalLDuration+=lDuration;
					totalRDuration+=rDuration;
					//Log.i("lDuration", Integer.toString(lDuration));
				}
				if(c4.moveToFirst())
				{
					int ldc=c4.getColumnIndex("leftDuration");
					int lDuration=c4.getInt(ldc);
					int rdc=c4.getColumnIndex("rightDuration");
					int rDuration=c4.getInt(rdc);
					totalLDuration+=lDuration;
					totalRDuration+=rDuration;
				}
				XYData tempDataD=new XYData(ts[0],totalLDuration);
				XYData tempDataE=new XYData(ts[0],totalRDuration);

				vLeft.add(tempDataD);
				vRight.add(tempDataE);
				current.add(Calendar.DATE,1);

			}

			int lc=vLeft.size();

			int rc=vRight.size();

			int maxLd = 0, maxRd = 0;
			maxLd = (Collections.max(vLeft, new Comparator<ShowBreastUsage.XYData>() {


				@Override
				public int compare(ShowBreastUsage.XYData o1, ShowBreastUsage.XYData o2) {
					if(o1.quanY > o2.quanY)
						return 1;
					else if(o1.quanY < o2.quanY)
						return -1;
					else
						return 0;
				}
			})).quanY;

			maxRd = (Collections.max(vRight, new Comparator<ShowBreastUsage.XYData>() {


				@Override
				public int compare(ShowBreastUsage.XYData o1, ShowBreastUsage.XYData o2) {
					if(o1.quanY > o2.quanY)
						return 1;
					else if(o1.quanY < o2.quanY)
						return -1;
					else
						return 0;
				}
			})).quanY;

			Integer[] maxValues = {maxLd, maxRd};
			List<Integer> list = Arrays.asList(maxValues);
			maxRangeValue = Collections.max(Arrays.asList(maxValues));

			series1Numbers=toNumbers(vLeft);
			series2Numbers=toNumbers(vRight);
		}
		else
		{
			series2Numbers[0]=0;
			series2Numbers[1]=0;
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






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.show_breast_usage, menu);
		return true;
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


	class MyBarRenderer extends BarRenderer<MyBarFormatter> {

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
				else if (mode == TWO_FINGERS_DRAG) {
					float oldDist = distBetweenFingers;
					distBetweenFingers = spacing(event);
					zoom(oldDist / distBetweenFingers);
					plot.setDomainBoundaries(wminX, wmaxX,
							BoundaryMode.FIXED);
					plot.redraw();
				}
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
		long nbDays=(wmaxX-wminX)/(3600L*24L*1000L);
		float width = (((float)plot.getWidth()) / nbDays) / 2;
		// renderer.setBarWidth(width);
	}

	private void clampToDomainBounds(long domainSpan)
	{
		// enforce left scroll boundary:
		if (wminX < minX)
		{
			wminX = minX;
			wmaxX = minX + domainSpan;
		}
		else if (wmaxX > maxX)
		{
			wmaxX = maxX;
			wminX = maxX - domainSpan;
		}
	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right,
							   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

		MyBarRenderer renderer = ((MyBarRenderer)plot.getRenderer(MyBarRenderer.class));
		long nbDays=(wmaxX-wminX)/(3600L*24L*1000L);
		float width = (((float)plot.getWidth()) / nbDays) / 2;
		//renderer.setBarWidth(width);
	}

}
