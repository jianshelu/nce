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

	private static final String DURATION = NceDatabase.UserAction.DURATION;
	private static final String END_TIME = NceDatabase.UserAction.END_TIME;
	private static final String START_TIME = NceDatabase.UserAction.START_TIME;
	private static final String WEEKEND = "max(date(" + START_TIME + ", 'weekday 0', '-1 day')) as WeekEnd ";
	private static final String WEEKSTART = "max(date(" + START_TIME + ", 'weekday 0', '-7 day')) as WeekStart ";
	private static final String LESSON_ID = NceDatabase.UserAction.LESSON_ID;
	private static final String ID = NceDatabase.UserAction._ID;
	private static final String[][] TITLES = {{"学习时长"},{"学习次数"}};
	private int verticalMinDistance = 40;  
	private int minVelocity         = 0;
	private GestureDetector mGestureDetector;
	private ScrollView scrollLayout;
	private GraphicalView[] view;
	
	private int mState;
	private Uri actionIdUri;
	private final static int STATE_VIEW = 0;
	private long intentLessonId;
	private int cursorCount;
	private LinearLayout viewLayout1;
	private LinearLayout viewLayout2;
	private LinearLayout viewLayout3;
	private LinearLayout viewLayout4;
	private LinearLayout[] viewLayouts;
	private double[] starttimeArrays;
	private double[] durationArrays;
	private XYMultipleSeriesRenderer[] renderers;
	private List<double[]> x[];
	private List<double[]> values[];

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
			intentLessonId = intent.getLongExtra("lesson_id", 0);
		}
		/**
		 * SELECT _id, lesson_id, start_time, max(date(start_time, 'weekday 0', '-7 day')) as WeekStart, max(date(start_time, 'weekday 0', '-1 day')) as WeekEnd , 
		end_time, sum(duration)/60.0/1000.0, (strftime('%Y%m%d',start_time))
		FROM user_action WHERE (lesson_id =2) AND (start_time not null) GROUP BY (strftime('%Y%m%d%H',start_time))
		 */
		
		String[] graphOpreationStrings ={"SUM (" + DURATION + ") AS DUEDURATION","COUNT (" + DURATION + ") AS DUEDURATION"};
		
		String selection = LESSON_ID + " =?) AND ("
				+ START_TIME + " NOT NULL) "
				+ " GROUP BY " + "(" + "strftime" + "(" + "'%Y%m%d',"
				+ START_TIME + ")";
		String[] selectionArgsString = {Long.toString(intentLessonId)};
		
		renderers = new XYMultipleSeriesRenderer[graphOpreationStrings.length];
		x = new ArrayList[graphOpreationStrings.length];
		values = new ArrayList[graphOpreationStrings.length];
		double[] divFactor = {1000.0*60.0, 1.0};
		String[] DurationTitle = {"分钟","次数"};
		String[] xTitle = {"月-日","小时"};
		int valueColors[][] = {{Color.BLUE},{Color.RED}};
		
		for (int i = 0; i < graphOpreationStrings.length; i++) {
			String[] durationProjection = new String[] { ID,LESSON_ID,WEEKSTART,WEEKEND,START_TIME,END_TIME, graphOpreationStrings[i] };
			renderers[i] = new XYMultipleSeriesRenderer();
			x[i] = new ArrayList<double[]>();
			values[i] = new ArrayList<double[]>();
			@SuppressWarnings("deprecation")
			Cursor durationCursor = this.managedQuery(actionIdUri, durationProjection, selection, selectionArgsString, null);
			int durationIndex = durationCursor.getColumnIndex("DUEDURATION");
			int starttimeIndex = durationCursor.getColumnIndex(START_TIME);
			cursorCount = durationCursor.getCount();
			durationArrays = new double[cursorCount];
			starttimeArrays = new double[cursorCount];
			double maxDuration = 0.0;
			double minDuration = 0.0;
			durationCursor.moveToFirst();
			for(int j = 0;j<cursorCount;j++){
				durationArrays[j]=Math.ceil(durationCursor.getLong(durationIndex)/divFactor[i]);
				maxDuration = Math.max(durationArrays[j], maxDuration);
				minDuration = Math.min(durationArrays[j], minDuration);
				String starttimeString = durationCursor.getString(starttimeIndex);  
				try {
					starttimeArrays[j] = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(starttimeString).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("durationValues" + j + "---" + durationArrays[j]);
				System.out.println("starttime" + j + "---" + starttimeArrays[j]);
				durationCursor.moveToNext();
			}
			Arrays.sort(starttimeArrays);
	        x[i].add(starttimeArrays);
	        values[i].add(durationArrays);
	        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
	        renderers[i] = buildRenderer(valueColors[i], styles);
	        int length = renderers[i].getSeriesRendererCount();
	        for (int k = 0; k < length; k++) {
	            ((XYSeriesRenderer) renderers[i].getSeriesRendererAt(k)).setFillPoints(true);
	            ((XYSeriesRenderer) renderers[i].getSeriesRendererAt(k)).setLineWidth(3.0f);
	        }
		    renderers[i].setXLabels(12);
			renderers[i].setYLabels(10);
			renderers[i].setShowGrid(false);
			renderers[i].setShowLegend(false);
			renderers[i].setDisplayChartValues(true);
			renderers[i].setChartValuesTextSize(15);
			renderers[i].setXLabelsColor(Color.BLACK);
			renderers[i].setYLabelsColor(0, Color.BLACK);
			renderers[i].setXLabelsAlign(Align.CENTER);
			renderers[i].setYLabelsAlign(Align.RIGHT);
			renderers[i].setYLabelsPadding(5.0f);
			renderers[i].setXRoundedLabels(false);
			renderers[i].setZoomButtonsVisible(false);
			renderers[i].setInScroll(true);
			renderers[i].setClickEnabled(true);
			renderers[i].setApplyBackgroundColor(true);//是否可以自定义背景色
			renderers[i].setBackgroundColor(Color.WHITE); //chart内部的背景色
			renderers[i].setMarginsColor(Color.WHITE);//chart边缘部分的背景色
			
			setChartSettings(renderers[i], TITLES[i][0], xTitle[i], DurationTitle [i], starttimeArrays[i], starttimeArrays[cursorCount-1]+1000*3600*12, 0.0, 15.0, Color.BLACK, Color.BLACK);
		}
    }

	@Override
	protected void onResume() {
		super.onResume();
		viewLayout1 = (LinearLayout) findViewById(R.id.ll_textgraph1);
		viewLayout2 = (LinearLayout) findViewById(R.id.ll_textgraph2);
		viewLayout3 = (LinearLayout) findViewById(R.id.ll_textgraph3);
		viewLayout4 = (LinearLayout) findViewById(R.id.ll_textgraph4);
		viewLayouts = new LinearLayout[] {viewLayout1, viewLayout2, viewLayout3, viewLayout4 };
		String dateFormateString[] = {"MM-dd", "hh"};
		view = new GraphicalView[renderers.length];
		for (int i = 0; i < renderers.length; i++) {
			if (view[i] == null) {
				view[i] = ChartFactory.getTimeChartView(this,buildDataset(TITLES[i], x[i], values[i]), renderers[i], dateFormateString[i]);
				view[i].setBackgroundColor(Color.WHITE);
				viewLayouts[i].addView(view[i], new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			} else {
				view[i].repaint();
			}
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
