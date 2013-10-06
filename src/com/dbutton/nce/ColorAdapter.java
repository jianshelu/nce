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
 * ����SimpleCursorAdapter������ʵ�����ݿ��ѯ�����ListView�Ĺ�����ʾ��ѯ���
 * ˽�г�Ա����clickCount��¼��ͬitem����õĵ������
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
		long mill = (long) Math.ceil(time /1000);//��ǰ

		long minute = (long) Math.ceil(time/60/1000.0f);// ����ǰ

		long hour = (long) Math.ceil(time/60/60/1000.0f);// Сʱ

		long day = (long) Math.ceil(time/24/60/60/1000.0f);// ��ǰ

		if (day - 1 > 0) {
			sb.append(day + "��");
		} else if (hour - 1 > 0) {
			if (hour >= 24) {
				sb.append("1��");
			} else {
				sb.append(hour + "Сʱ");
			}
		} else if (minute - 1 > 0) {
			if (minute == 60) {
				sb.append("1Сʱ");
			} else {
				sb.append(minute + "����");
			}
		} else if (mill - 1 > 0) {
			if (mill == 60) {
				sb.append("1����");
			} else {
				sb.append(mill + "��");
			}
		} else {
			sb.append("�ո�");
		}
		if (!sb.toString().equals("�ո�")) {
			sb.append("ǰ");
		}
		return sb.toString();
	}

}