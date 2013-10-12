package com.dbutton.nce;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
public class TextGraph extends Activity implements OnTouchListener, GestureDetector.OnGestureListener{

	private int verticalMinDistance = 40;  
	private int minVelocity         = 0;
	private GestureDetector mGestureDetector;
	private ScrollView scrollLayout;
	private GraphicalView view1;
	private GraphicalView view2;
	private GraphicalView view3;
	private GraphicalView view4;
	private String[] titles;
	private List<double[]> x;
	private List<double[]> values;
	private XYMultipleSeriesRenderer renderer;
	
	private int mState;
	private Uri actionIdUri;
	private final static int STATE_VIEW = 0;
	private long lesson_id;
	private int cursorCount;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_graph);
		mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this); 
		scrollLayout = (ScrollView)findViewById(R.id.sc_textgraph);    
        scrollLayout.setOnTouchListener(this);    
        scrollLayout.setLongClickable(true); 
        Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) {
			mState = STATE_VIEW;	
			actionIdUri = intent.getData();
			lesson_id = intent.getLongExtra("lesson_id", 0);
		}
			
		String[] durationProjections = new String[] { 
				NceDatabase.UserAction._ID,
				NceDatabase.UserAction.LESSON_ID,
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.END_TIME,
				"SUM (" + NceDatabase.UserAction.DURATION + ") AS SUMDURATION"};
		String[] dataColumns = new String[] { 
				NceDatabase.UserAction._ID,
				NceDatabase.UserAction.LESSON_ID,
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.END_TIME,
				NceDatabase.UserAction.DURATION };
		int[] viewIDs = new int[] { R.id.tv_id, R.id.tv_title, R.id.tv_start, R.id.tv_duration, R.id.tv_count};
		
		String selection = NceDatabase.UserAction.LESSON_ID + " =?) AND ("
				+ NceDatabase.UserAction.START_TIME + " NOT NULL) "
				+ " GROUP BY " + "(" + "strftime" + "(" + "'%Y%m%d',"
				+ NceDatabase.UserAction.START_TIME + ")";
		String[] selectionArgsString = {Long.toString(lesson_id)};
		String sumDuration = "SUMDURATION";
		@SuppressWarnings("deprecation")
		Cursor multiCursor = this.managedQuery(actionIdUri, durationProjections, selection, selectionArgsString, null);
		int durationIndex = multiCursor.getColumnIndex(sumDuration);
		int starttimeIndex = multiCursor.getColumnIndex(NceDatabase.UserAction.START_TIME);
		cursorCount = multiCursor.getCount();
		double[] durationArray = new double[cursorCount];
		double[] starttimeArray = new double[cursorCount];
		/*Date[] starttimeArray = new Date[cursorCount];
		starttimeArray[0] = new Date(System.currentTimeMillis());
		for (int i = 0; i < cursorCount; i++) {
			long tempTime = multiCursor.getLong(starttimeIndex);
			starttimeArray[i] = new Date(tempTime);  
		}
		
		Set<Date> tempSet = new TreeSet<Date>(Arrays.asList(starttimeArray));
		Date[] dinStartTimeArrayDates = tempSet.toArray(new Date[tempSet.size()]);
		double[] durationArray = new double[cursorCount];*/
		double maxDuration = 0.0;
		double minDuration = 0.0;
		multiCursor.moveToFirst();
		for(int i = 0;i<cursorCount;i++){
			durationArray[i]=multiCursor.getLong(durationIndex)/1000.0/60.0;
			maxDuration = Math.max(durationArray[i], maxDuration);
			minDuration = Math.min(durationArray[i], minDuration);
			String starttimeString = multiCursor.getString(starttimeIndex);  
			try {
				starttimeArray[i] = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(starttimeString).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("durationValues" + i + "---" + durationArray[i]);
			System.out.println("starttime" + i + "---" + starttimeArray[i]);
			multiCursor.moveToNext();
		}
		Arrays.sort(starttimeArray);
        titles = new String[] { NceDatabase.UserAction.DURATION };
        x = new ArrayList<double[]>();
        x.add(starttimeArray);
        values = new ArrayList<double[]>();
        values.add(durationArray);
        int orangeColor = R.color.c4;
        int[] colors = new int[] { Color.BLUE};
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
        renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setLineWidth(3.0f);
        }

        setChartSettings(renderer, "学习时长", "月-日", "分钟", starttimeArray[0], starttimeArray[cursorCount-1]+1000*3600*12, 0.0, 10.0, Color.BLACK, Color.BLACK);
        renderer.setXLabels(10);
        renderer.setYLabels(12);
        renderer.setShowGrid(false);
        renderer.setShowLegend(false);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setYLabelsPadding(5.0f);
        renderer.setXRoundedLabels(false);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanLimits(new double[] { -10, 20, -10, 20 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 20 });
        renderer.setClickEnabled(true);
        renderer.setApplyBackgroundColor(true);//是否可以自定义背景色
		renderer.setBackgroundColor(Color.WHITE); //chart内部的背景色
		renderer.setMarginsColor(Color.WHITE);//chart边缘部分的背景色
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		LinearLayout viewLayout1 = (LinearLayout) findViewById(R.id.ll_textgraph1);
		LinearLayout viewLayout2 = (LinearLayout) findViewById(R.id.ll_textgraph2);
		LinearLayout viewLayout3 = (LinearLayout) findViewById(R.id.ll_textgraph3);
		LinearLayout viewLayout4 = (LinearLayout) findViewById(R.id.ll_textgraph4);
		
		 if (view1 == null) {
			view1 = ChartFactory.getTimeChartView(this,buildDataset(titles, x, values), renderer, "MM-dd");
			view1.setBackgroundColor(Color.WHITE);
			    viewLayout1.addView(view1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			  } else {
			    view1.repaint();
			  }
		 if (view2 == null){
					view2 = ChartFactory.getTimeChartView(this,buildDataset(titles, x, values), renderer, "dd");
					view2.setBackgroundColor(Color.WHITE);
					    viewLayout2.addView(view2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			  }else {
			    view2.repaint();
			  }
		 if (view3 == null){
				view3 = ChartFactory.getTimeChartView(this,buildDataset(titles, x, values), renderer, "HH/MM");
				view3.setBackgroundColor(Color.WHITE);
				    viewLayout3.addView(view3, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		  }else {
		    view3.repaint();
		  }
		 if (view4 == null){
			 view4 = ChartFactory.getTimeChartView(this,buildDataset(titles, x, values), renderer, null);
			 view4.setBackgroundColor(Color.WHITE);
			 viewLayout4.addView(view4, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		 }else {
			 view4.repaint();
		 }
	}
	
    private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }
    
    private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    public static boolean isTheSameDay(Date d1,Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
				&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}

    
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.i("Fling", "Fling Happened!");  
		 if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
       	TextGraph.this.finish();
			//设置切换动画，从右边进入，左边退出
			overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right );
           return true;  
       } 
       return true; 
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {  
	    return mGestureDetector.onTouchEvent(event);  
	}  
}
