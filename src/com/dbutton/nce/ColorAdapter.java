package com.dbutton.nce;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.R.integer;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/*
 * 创建SimpleCursorAdapter的子类实现数据库查询结果与ListView的关联显示查询结果
 * 私有成员数组clickCount记录不同item所获得的点击次数
 */
public class ColorAdapter extends SimpleCursorAdapter {
	
	private Context context;
	private Cursor c;
	
	@SuppressWarnings("deprecation")
	public ColorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
		this.c = c;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = super.getView(position, convertView, parent);
		Resources resource = context.getResources();
		TypedArray backColor = resource
				.obtainTypedArray(R.array.count_color);
		int colorNum = backColor.length();
		int clickIndex = c.getColumnIndex(NceDatabase.NceText.CLICK_COUNT);
		int click_count = c.getInt(clickIndex);
		convertView.setBackgroundDrawable(backColor
				.getDrawable((click_count % colorNum)));
		TextView startText = (TextView) convertView.findViewById(R.id.tv_start);
		int startTimeIndex = c.getColumnIndex(NceDatabase.UserAction.START_TIME);
		int startTime = c.getInt(startTimeIndex);
		if(startTime > 0){
			startText.setText(getStandardDate(String.valueOf(startTime)));
		}
		return convertView;
	}

	public static String getStandardDate(String timeStr) {

		StringBuffer sb = new StringBuffer();

		long t = Long.parseLong(timeStr);
		long time = System.currentTimeMillis() - (t*1000);
		long mill = (long) Math.ceil(time /1000);//秒前

		long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

		long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

		long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

		if (day - 1 > 0) {
			sb.append(day + "天");
		} else if (hour - 1 > 0) {
			if (hour >= 24) {
				sb.append("1天");
			} else {
				sb.append(hour + "小时");
			}
		} else if (minute - 1 > 0) {
			if (minute == 60) {
				sb.append("1小时");
			} else {
				sb.append(minute + "分钟");
			}
		} else if (mill - 1 > 0) {
			if (mill == 60) {
				sb.append("1分钟");
			} else {
				sb.append(mill + "秒");
			}
		} else {
			sb.append("刚刚");
		}
		if (!sb.toString().equals("刚刚")) {
			sb.append("前");
		}
		return sb.toString();
	}

}