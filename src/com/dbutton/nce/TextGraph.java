package com.dbutton.nce;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
public class TextGraph extends Activity implements OnTouchListener, GestureDetector.OnGestureListener{

	private int verticalMinDistance = 40;  
	private int minVelocity         = 0;
	private GestureDetector mGestureDetector;
	private ScrollView scrollLayout;
	private GraphicalView view1;
	private GraphicalView view2;
	private GraphicalView view3;
	private GraphicalView view4;
//	private RelativeLayout viewLayout1;
//	private RelativeLayout viewLayout2;
	private String[] titles;
	private List<double[]> x;
	private List<double[]> values;
	private XYMultipleSeriesRenderer renderer;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textgraph);
		mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this); 
		scrollLayout = (ScrollView)findViewById(R.id.sc_textgraph);    
//		viewLayout1 = (RelativeLayout)findViewById(R.id.ll_textgraph1);    
//		viewLayout2 = (RelativeLayout)findViewById(R.id.ll_textgraph2);    
        scrollLayout.setOnTouchListener(this);    
        scrollLayout.setLongClickable(true); 
        
        titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };
        x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
        }
        values = new ArrayList<double[]>();
        values.add(new double[] { 12.3, 12.5, 12.8, 12.8, 12.4, 12.4, 12.4, 12.1, 12.6, 12.3, 12.2, 12.9 });
        values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
        values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
        values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });
        int[] colors = new int[] { Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE };
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE };
        renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, -10, 40, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(12);
        renderer.setShowGrid(false);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.RIGHT);

        renderer.setZoomButtonsVisible(false);
        renderer.setPanLimits(new double[] { -10, 20, -10, 20 });
        renderer.setZoomLimits(new double[] { -10, 20, -10, 20 });

    /*    View view1 = ChartFactory.getLineChartView(this, buildDataset(titles, x, values), renderer);
        view1.setBackgroundColor(Color.GRAY);
        
        View view2 = ChartFactory.getLineChartView(this, buildDataset(titles, x, values), renderer);
        view2.setBackgroundColor(Color.BLACK);
        viewLayout.removeAllViews();
        for (int i = 0; i < 50; i++) {
			TextView text = new TextView(this);
			text.setText("第" + i + "个 TextView");
			Log.i("------", text.toString());
			viewLayout.addView(text);
		}
        viewLayout1.addView(view1,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewLayout2.addView(view2,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        viewScroll.removeAllViews();
        viewLayout.removeAllViews();
        viewLayout.addView(viewLayout1);
        viewLayout.addView(viewLayout2);
        viewScroll.addView(viewLayout);
        setContentView(viewScroll);*/
        
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		LinearLayout viewLayout1 = (LinearLayout) findViewById(R.id.ll_textgraph1);
		LinearLayout viewLayout2 = (LinearLayout) findViewById(R.id.ll_textgraph2);
		LinearLayout viewLayout3 = (LinearLayout) findViewById(R.id.ll_textgraph3);
		LinearLayout viewLayout4 = (LinearLayout) findViewById(R.id.ll_textgraph4);
		 if (view1 == null) {
			view1 = ChartFactory.getLineChartView(this,buildDataset(titles, x, values), renderer);
			view1.setBackgroundColor(Color.GRAY);
			    viewLayout1.addView(view1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			  } else {
			    view1.repaint();
			  }
		 if (view2 == null){
					view2 = ChartFactory.getLineChartView(this,buildDataset(titles, x, values), renderer);
					view2.setBackgroundColor(Color.BLACK);
					    viewLayout2.addView(view2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			  }else {
			    view2.repaint();
			  }
		 if (view3 == null){
				view3 = ChartFactory.getLineChartView(this,buildDataset(titles, x, values), renderer);
				view3.setBackgroundColor(Color.BLACK);
				    viewLayout3.addView(view3, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		  }else {
		    view3.repaint();
		  }
		 if (view4 == null){
			 view4 = ChartFactory.getLineChartView(this,buildDataset(titles, x, values), renderer);
			 view4.setBackgroundColor(Color.BLACK);
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
       	this.finish();
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
