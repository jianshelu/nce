package com.dbutton.nce;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class NceText extends Activity implements OnTouchListener, GestureDetector.OnGestureListener {

	private static final String START_TIME = NceDatabase.UserAction.START_TIME;
	private static final Uri actionUri = NceDatabase.UserAction.ACTION_ID_URI_BASE;
	private final static int STATE_VIEW = 0;
	private static final String ORIGINAL_CONTENT = "origContent";
	private int mState;
	private Uri multiIdUri;
	private Uri actionIdUri;
	private Uri textUri;
	private String mOriginalContent;
	private TextView mTitle;
	private TextView mBody;
	private SeekBar mSeekBar;
	protected int iCount = 90;
	private int cycle = 10;
	private int time = 2 * cycle;
	private long intentLessonId;
	private ContentValues values;
	private Long createTime;
	private Long start;
	private GestureDetector mGestureDetector;
	private Intent intent;
	private String action;
	private long duration;
	private boolean inText = false;
	private String createStarttimeString;
	private long lastStarttime;
	
	private int verticalMinDistance = 40;  
	private int minVelocity         = 0;
	private Uri textIdUri;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_view);
		mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);    
        LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.ll_ncetext);    
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true); 
		mTitle = (TextView) findViewById(R.id.tv_title);
		mBody = (TextView) findViewById(R.id.tv_body);
		mSeekBar =(SeekBar) findViewById(R.id.sb_timer);
		
		intent = getIntent();
//		action = intent.getAction();
//		if (Intent.ACTION_VIEW.equals(action)) {
			mState = STATE_VIEW;	
			textUri= intent.getData();
			intentLessonId = intent.getLongExtra("lesson_id", 0);
			textIdUri=ContentUris.withAppendedId(NceDatabase.NceText.TEXT_URI,intentLessonId);
			intent.setAction(Intent.ACTION_INSERT);
//		}
		String[] textProjection = new String[] {
				NceDatabase.NceText.TEXT_TABLE_NAME + "." + NceDatabase.NceText._ID + " AS ID", 
				NceDatabase.NceText.TEXT_TITLE,NceDatabase.NceText.TEXT_BODY};
		String[] actionProjection = new String[] {
				NceDatabase.UserAction.ACTION_TABLE_NAME + "." + NceDatabase.UserAction.LESSON_ID + " AS ID", 
				NceDatabase.UserAction.LESSON_ID,"MAX(" + NceDatabase.UserAction.START_TIME +") AS MAXSTARTTIME"};
		String selection = "ID =?";
/*		String selection = "ID =?) AND ("
				+ START_TIME + " NOT NULL";
*/				
		String[] selectionArgsString = {Long.toString(intentLessonId)};
		long createStarttime = Long.valueOf(System.currentTimeMillis());
		createStarttimeString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(createStarttime));
		start = createStarttime;
		mThread.start();
		Cursor textCursor = managedQuery(textUri, textProjection, selection, selectionArgsString, null);
		Cursor actionCursor = managedQuery(actionUri, actionProjection, selection, selectionArgsString, null);
		if (actionCursor != null && textCursor != null) {
			textCursor.moveToFirst();
			actionCursor.moveToFirst();
			int titleIndex = textCursor
					.getColumnIndex(NceDatabase.NceText.TEXT_TITLE);
			int bodyIndex = textCursor
					.getColumnIndex(NceDatabase.NceText.TEXT_BODY);
			String title = textCursor.getString(titleIndex);
			String body = textCursor.getString(bodyIndex);
			int starttimeIndex = actionCursor
					.getColumnIndex("MAXSTARTTIME");
			String lastStarttimeString = actionCursor.getString(starttimeIndex);
			if(lastStarttimeString != null){
				try {
					lastStarttime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(lastStarttimeString).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mTitle.setText(title);
			mBody.setText(body);
			Toast.makeText(this, "学习计时 "+Math.round(time*5.0/60)+" 分钟开始", Toast.LENGTH_SHORT).show();
		}
		System.out.println("activity start");
		if (savedInstanceState != null) {
			mOriginalContent = savedInstanceState.getString(ORIGINAL_CONTENT);
		}
	}

	// 定义一个Handler
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mSeekBar.setProgress(iCount);
//				mTitle.setText(msg.arg1+"");
				break;
			case 1:
				if (!Thread.currentThread().isInterrupted()) {
					mSeekBar.setProgress(iCount);
//					mTitle.setText(msg.arg1+"");
				}
				break;
			}
		}
	};
	

	private Thread mThread = new Thread(new Runnable() {
		public void run() {
			
			for (int i = time + 1 ; i > 0; i--) {
				
				try {
					Message msg = new Message();
					msg.arg1 = i-1;
					
					Thread.sleep(5000);
					if (i == 1) {
						msg.what = 0;
						iCount-=cycle;
						mHandler.sendMessage(msg);
						break;
					} else {
						msg.what = 1;
						mHandler.sendMessage(msg);
//						iCount = iCount>0?iCount-cycle:100 ;
						iCount = ((msg.arg1-1) % 10)*10;
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});
	

	 @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        // Save away the original text, so we still have it if the activity
	        // needs to be killed while paused.
	        outState.putString(ORIGINAL_CONTENT, mOriginalContent);
	    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		long end = Long.valueOf(System.currentTimeMillis());
		lastStarttime = lastStarttime==0?end:lastStarttime;
		long interval = end - lastStarttime;
		String endTimeString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(end));
//		long inter = end - start;
//		System.out.println("duration destory before" + duration + "---" + inter);
//		duration += inter;
//		System.out.println("duration destory after" + duration + "---" + inter);
		values = new ContentValues();
		values.put(NceDatabase.UserAction.LESSON_ID, intentLessonId);
		values.put(NceDatabase.UserAction.START_TIME, createStarttimeString);
		values.put(NceDatabase.UserAction.END_TIME, endTimeString);
		values.put(NceDatabase.UserAction.DURATION, duration);
		values.put(NceDatabase.UserAction.INTERVAL, interval);
		getContentResolver().insert(actionUri, values);
		System.out.println("activity destory");
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("activity resume");
		iCount = 80;
		start = System.currentTimeMillis();
		System.out.println("duration resume after" + duration + "---" + start);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("activity pasue");
		long end = Long.valueOf(System.currentTimeMillis());
		long inter = end -start;
		duration += inter;
		System.out.println("duration pasue" + duration + "---" + inter);
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
		 if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
//			startActivity(intent);
			Intent intent = new Intent();
			intent.putExtra("lesson_id", intentLessonId);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(NceDatabase.UserAction.ACTION_ID_URI_BASE);
			startActivity(intent);
			//设置切换动画，从右边进入，左边退出
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        // scroll.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
