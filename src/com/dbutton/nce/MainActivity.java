package com.dbutton.nce;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Guard;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnTouchListener, GestureDetector.OnGestureListener {
	private ColorAdapter adapter;
	private String TAG = "adapter";
	private Uri textUri;
	int count;
	private String[] dataColumns;
	private int[] viewIDs;
	private ListView listView;
	protected int b = 0;
	private Uri multiUri;
	private Uri actionUri;
	private Cursor multiCursor;
	private Cursor favoriteCursor;
	private GestureDetector mGestureDetector;
	
	private int verticalMinDistance = 40;  
	private int minVelocity         = 0;
	private boolean myFavoriteStatus;
	private String myFavoriteString;
	private String selection;
	private String[] multiProjections;  
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);    
		LinearLayout viewSnsLayout = (LinearLayout)findViewById(R.id.linearLayout);    

		myFavoriteStatus = false;
		myFavoriteString = "";
		
		
		if(myFavoriteStatus){
		}else{
		}
        viewSnsLayout.setOnTouchListener(this);    
        viewSnsLayout.setLongClickable(true);    
		listView = (ListView) findViewById(R.id.lv);
		initDatabase();

		textUri = NceDatabase.NceText.TEXT_URI;
		actionUri = NceDatabase.UserAction.ACTION_URI;
		multiUri = Uri.withAppendedPath(NceDatabase.NceText.TEXT_URI, NceDatabase.UserAction.ACTION_TABLE_NAME);
		multiProjections = new String[] { 
				NceDatabase.NceText.TEXT_TABLE_NAME + "." + NceDatabase.NceText._ID + " AS _id",
				NceDatabase.NceText.TEXT_TITLE,
				NceDatabase.NceText.CLICK_COUNT,
				NceDatabase.NceText.USER_FAVORITE,
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.DURATION };
		dataColumns = new String[] { NceDatabase.NceText._ID,
				NceDatabase.NceText.TEXT_TITLE,
				NceDatabase.NceText.CLICK_COUNT,
				NceDatabase.NceText.USER_FAVORITE, 
				NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.DURATION };
		viewIDs = new int[] { R.id.tv_id, R.id.tv_title, R.id.tv_count,R.id.cb_favorite,R.id.tv_start, R.id.tv_duration};
		selection = NceDatabase.NceText.TEXT_TABLE_NAME + "."+ NceDatabase.NceText._ID + " == "
						+ NceDatabase.UserAction.ACTION_TABLE_NAME + "." + NceDatabase.UserAction.LESSON_ID;
		multiCursor = this.managedQuery(multiUri, multiProjections, selection, null, null);
		CheckBox myFavorite = (CheckBox)findViewById(R.id.my_favorite); 
		myFavorite.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					String favoriteSelection = selection + " AND nce_2_text.user_favorite == 1";
					favoriteCursor = MainActivity.this.managedQuery(multiUri, multiProjections, favoriteSelection, null, null);
					adapter.swapCursor(favoriteCursor);
				}else{
					multiCursor = MainActivity.this.managedQuery(multiUri, multiProjections, selection, null, null);
					adapter.swapCursor(multiCursor);
				}
			}});
		
		multiCursor.setNotificationUri(getContentResolver(), textUri);
//		multiCursor.setNotificationUri(getContentResolver(), actionUri);
		adapter = new ColorAdapter(this, R.layout.list_item, multiCursor,dataColumns, viewIDs);

		// 以适配器的data为数组,返回的是所有查询记录的计数,不会因浏览下一页记录产生错误.
		adapter.setViewBinder(new ColorAdapter.ViewBinder (){
			  public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				  if(view instanceof CheckBox) {
		        	CheckBox checkbox = (CheckBox)view;
		        	int nCheckedIndex = cursor.getColumnIndexOrThrow(NceDatabase.NceText.USER_FAVORITE);
		        	boolean bChecked = (cursor.getInt(nCheckedIndex) != 0);
		        	checkbox.setChecked(bChecked);

					checkbox.setOnClickListener(new View.OnClickListener() {
						
							@Override
							public void onClick(View view) {
								int position = listView.getPositionForView(view);
								multiCursor.setNotificationUri(getContentResolver(), textUri);
								if (multiCursor.moveToPosition(position)) {
									int nCheckedIndex = multiCursor.getColumnIndexOrThrow(NceDatabase.NceText.USER_FAVORITE);
									int favValue = multiCursor.getInt(nCheckedIndex);
									boolean bChecked = (favValue != 0);
									Uri textIdUri = ContentUris.withAppendedId(multiUri, position);
									ContentValues values = new ContentValues();
									if (bChecked) {
										values.put(NceDatabase.NceText.USER_FAVORITE,0);
										((CheckBox)view).setChecked(false);
										System.out.println("position1: " + position);
									} else {
										values.put(NceDatabase.NceText.USER_FAVORITE,1);
										((CheckBox)view).setChecked(true);
										System.out.println("position0: " + position);
									}
									String selection = NceDatabase.NceText._ID + " =?";
									getContentResolver().update(textUri,values, selection, new String[]{(position+1) + ""});
								}
							}
					});
					return true;
				}
				return false;
			}
		});
			  
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new myItemClickListener());
	}
		
	
	protected class myItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//			System.out.println("parent:" + parent +"; view " + view + "; position" + position + "; id" + id);
//			int count = 0;
//			Uri multiIdUri = ContentUris.withAppendedId(multiUri, id);
//			String[] clickCount = { NceDatabase.NceText.CLICK_COUNT,
//					NceDatabase.NceText.USER_FAVORITE };
//			String selection = NceDatabase.NceText._ID + "=?";
//			String[] cid = { Long.toString(id)};
////			Cursor countCursor = getContentResolver().query(textIdUri, clickCount, selection, cid, null);
//			if(multiCursor.moveToPosition(position)){
//				count = multiCursor.getInt(multiCursor.getColumnIndex(NceDatabase.NceText.CLICK_COUNT));
//			}else{
//				return;
//			}
//
//			count++;
//			ContentValues values = new ContentValues();
//			values.put(NceDatabase.NceText.CLICK_COUNT, count);
//			getContentResolver().update(multiIdUri, values, null, null);
			//viewCursor.requery();
			//adapter.setClickCount(count, position);
			Log.i(TAG,"Postion"+ position+ "--"+ count);
			Intent intent = new Intent();
			intent.putExtra("lesson_id", id);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(textUri);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);     
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
        	Intent intent = new Intent();
			intent.setClass(MainActivity.this, UserAction.class);
			startActivity(intent);
			//设置切换动画，从右边进入，左边退出
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right); 
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
	
	
	private void initDatabase() throws Error {
		boolean dbExist = checkDataBase();
		if(dbExist){
			
		}else{//不存在就把raw里的数据库写入手机
			try{
				copyDataBase();
			}catch(IOException e){
				throw new Error("Error copying database");
			}
		}
	}
	
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String databaseFilename = getDatabasePath(NceDatabase.DATABASE_NAME)
					.toString();
			checkDB = SQLiteDatabase.openDatabase(databaseFilename, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}
	
	/**
	 * 复制数据库到手机指定文件夹下
	 * 
	 * @throws IOException
	 */
	public void copyDataBase() throws IOException {
		File dataFile  = getDatabasePath(NceDatabase.DATABASE_NAME);
		if (!dataFile.exists())// 判断文件夹是否存在，不存在就新建一个
			if(dataFile.getParentFile().mkdir()) 
				System.out.println("hello");
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(dataFile);// 得到数据库文件的写入流
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStream is = MainActivity.this.getResources().openRawResource(
				R.raw.nce_2);// 得到数据库文件的数据流
		byte[] buffer = new byte[8192];
		int count = 0;
		try {
			while ((count = is.read(buffer)) > 0) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (IOException e) {
			dataFile.delete();
		}
		try {
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
