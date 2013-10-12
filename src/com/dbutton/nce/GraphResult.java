package com.dbutton.nce;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

public class GraphResult extends Activity {

	private int mState;
	private Uri actionIdUri;
	private final static int STATE_VIEW = 0;
	private long lesson_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RelativeLayout viewSnsLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
		ListView listView = (ListView) findViewById(R.id.lv);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if (Intent.ACTION_VIEW.equals(action)) {
			mState = STATE_VIEW;	
			actionIdUri = intent.getData();
			lesson_id = intent.getLongExtra("lesson_id", 0);
		}
			
		String[] multiProjections = new String[] { 
				NceDatabase.UserAction._ID,
				NceDatabase.UserAction.LESSON_ID,
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.END_TIME,
				NceDatabase.UserAction.DURATION };
		String[] dataColumns = new String[] { 
				NceDatabase.UserAction._ID,
				NceDatabase.UserAction.LESSON_ID,
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.END_TIME,
				NceDatabase.UserAction.DURATION };
		int[] viewIDs = new int[] { R.id.tv_id, R.id.tv_title, R.id.tv_start, R.id.tv_duration, R.id.tv_count};
		
		String selection = NceDatabase.UserAction.LESSON_ID + " =?) AND ("
				+ NceDatabase.UserAction.START_TIME + " NOT NULL) "
				+ " GROUP BY " + "(" + "strftime" + "(" + "'%Y%m%d%H%M',"
				+ NceDatabase.UserAction.START_TIME + ")";
		String[] selectionArgsString = {Long.toString(lesson_id + 1)};
		
		Cursor multiCursor = this.managedQuery(actionIdUri, multiProjections, selection, selectionArgsString, null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, multiCursor,dataColumns, viewIDs);
		listView.setAdapter(adapter);
		Log.i("info", "start GraphResult Activity");
	}

}
