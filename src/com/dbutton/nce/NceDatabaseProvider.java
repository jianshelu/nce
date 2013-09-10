package com.dbutton.nce;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class NceDatabaseProvider extends ContentProvider {

	private static final String DATABASE_NAME = "nce_2.db";
	private static int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "nce_2_text";

	private class NceDatabaseHelper extends SQLiteOpenHelper {
		public NceDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		private static final String TEXT_TYPE = " String";
		private static final String COMMA_SEP = ",";
		private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+ TABLE_NAME + " (" + NceDatabase.NceText._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ NceDatabase.NceText.TEXT_TITLE + TEXT_TYPE + COMMA_SEP
				+ NceDatabase.NceText.TEXT_BODY + " );";

		private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);
		}

	}

	private NceDatabaseHelper nceDatabase;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		nceDatabase = new NceDatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = nceDatabase.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
