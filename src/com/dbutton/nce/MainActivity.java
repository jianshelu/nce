package com.dbutton.nce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		 * NceDatabaseProvider helper = new NceDatabaseProvider(
		 * this.getApplicationContext(), null); SQLiteDatabase db_nce2 =
		 * helper.getReadableDatabase(); String[] projection = {
		 * NceDatabase.NceText._ID, NceDatabase.NceText.TEXT_TITLE }; String
		 * selection = null; String[] selectionArgs = null; String sortOrder =
		 * null; Cursor cursor = db_nce2.query(NceDatabase.NceText.TABLE_NAME,
		 * projection, selection, selectionArgs, null, null, sortOrder);
		 */

		// @SuppressWarnings("deprecation")
		// SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
		// R.layout.list_item, cursor, new String[] {
		// NceDatabase.NceText._ID,
		// NceDatabase.NceText.TEXT_TITLE }, new int[] {
		// R.id.tv_id, R.id.tv_title });

		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(NceDatabase.NceText.CONTENT_URI);
		}
		String[] projection = { NceDatabase.NceText._ID,
				NceDatabase.NceText.TEXT_TITLE };

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(getIntent().getData(), projection, null,
				null, null);
		/*LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.list_item, null);
		
		linearLayout.setBackgroundColor(Color.BLUE);*/

		@SuppressWarnings("deprecation")
		ColorSimpleCursorAdapter adapter = new ColorSimpleCursorAdapter(this,
				R.layout.list_item, cursor,
				new String[] { NceDatabase.NceText._ID,
						NceDatabase.NceText.TEXT_TITLE }, new int[] {
						R.id.tv_id, R.id.tv_title });

		ListView listView = (ListView) findViewById(R.id.lv);
		listView.setAdapter(adapter);
		// db_nce2.close();
	}
	
	private class ColorSimpleCursorAdapter extends SimpleCursorAdapter{

		public ColorSimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			super.bindView(view, context, cursor);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				LayoutInflater mInflator = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				convertView = mInflator.inflate(R.layout.list_item,null);
			}
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			tv_title.setBackgroundResource(R.color.yellow);
			return convertView;
		}
		
	}

}
