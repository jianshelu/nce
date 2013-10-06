package com.dbutton.nce;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class NceText extends Activity {

	private static final String[] PROJECTION = new String[] {
			NceDatabase.NceText._ID, NceDatabase.NceText.TEXT_TITLE,
			NceDatabase.NceText.TEXT_BODY };
	private final static int STATE_VIEW = 0;
	private static final String ORIGINAL_CONTENT = "origContent";
	private int mState;
	private Uri multiIdUri;
	private Uri actionIdUri;
	private Cursor mCursor;
	private Object mOriginalContent;
	private TextView mTitle;
	private TextView mBody;
	private SeekBar mSeekBar;
	protected int iCount = 90;
	private int cycle = 10;
	private int time = 2 * cycle;
	private long lesson_id;
	private ContentValues values;
	private Long start;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_view);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mBody = (TextView) findViewById(R.id.tv_body);
		mSeekBar =(SeekBar) findViewById(R.id.sb_timer);
		
		final Intent intent = getIntent();
		final String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) {
			mState = STATE_VIEW;
			multiIdUri = intent.getData();
			lesson_id = intent.getLongExtra("lesson_id", 0);
			actionIdUri=ContentUris.withAppendedId(NceDatabase.UserAction.ACTION_URI,lesson_id);
			intent.setAction(Intent.ACTION_INSERT);
		}
		start = Long.valueOf(System.currentTimeMillis());
		
		mCursor = managedQuery(multiIdUri, PROJECTION, null, null, null);
		if (mCursor != null) {
			mCursor.requery();
			mCursor.moveToFirst();
			int colTitleIndex = mCursor
					.getColumnIndex(NceDatabase.NceText.TEXT_TITLE);
			int colBodyIndex = mCursor
					.getColumnIndex(NceDatabase.NceText.TEXT_BODY);
			String title = mCursor.getString(colTitleIndex);
			String body = mCursor.getString(colBodyIndex);
			mTitle.setText(title);
			mBody.setText(body);
			Toast.makeText(this, "学习计时 "+Math.round(time*5.0/60)+" 分钟开始", Toast.LENGTH_SHORT).show();
		}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		long end = Long.valueOf(System.currentTimeMillis());
		long duration = end - start;
		values = new ContentValues();
		values.put(NceDatabase.UserAction.LESSON_ID, lesson_id);
		values.put(NceDatabase.UserAction.START_TIME, start);
		values.put(NceDatabase.UserAction.END_TIME, end);
		values.put(NceDatabase.UserAction.DURATION, duration);
		getContentResolver().insert(multiIdUri, values);
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("activity resume");
//		iCount = 80;
		mThread.start();
	}
}
