package com.dbutton.nce;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NceDatabaseProvider extends ContentProvider {


	private static int DATABASE_VERSION = 1;
	public static final String TABLE_NCE_TEXT = "nce_2_text";
	public static final String TABLE_USER = "nce_user";
	public static final String TABLE_USER_ACTION = "user_action";

	private static final int TEXT = 1;
	private static final int TEXT_ID = 2;
	
	private static final int ACTION = 3;
	private static final int ACTION_ID = 4;
	
	private static final int MULITABLE = 5;
	private static final int MULTI_TEXT_ID = 6;

	private static final UriMatcher sUriMatcher;
	private NceDatabaseHelper nceHelper;
	private static HashMap<String, String> sTextProjectionMap;
	

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "nce_2_text", TEXT);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "nce_2_text/#", TEXT_ID);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "nce_2_text/user_action/#", MULTI_TEXT_ID);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "user_action", ACTION);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "user_action/#", ACTION_ID);
		sUriMatcher.addURI(NceDatabase.AUTHORITY, "nce_2_text/user_action/", MULITABLE);

		sTextProjectionMap = new HashMap<String, String>();

		sTextProjectionMap
				.put(NceDatabase.NceText._ID, NceDatabase.NceText._ID);
		sTextProjectionMap.put(NceDatabase.NceText.TEXT_TITLE,
				NceDatabase.NceText.TEXT_TITLE);
		sTextProjectionMap.put(NceDatabase.NceText.TEXT_BODY,
				NceDatabase.NceText.TEXT_BODY);
		sTextProjectionMap.put(NceDatabase.NceText.CLICK_COUNT,
				NceDatabase.NceText.CLICK_COUNT);
		sTextProjectionMap.put(NceDatabase.NceText.USER_FAVORITE,
				NceDatabase.NceText.USER_FAVORITE);
		
		sTextProjectionMap.put(NceDatabase.UserAction._ID, 
				NceDatabase.UserAction._ID);
		sTextProjectionMap.put(NceDatabase.UserAction.LESSON_ID,
				NceDatabase.UserAction.LESSON_ID);
		sTextProjectionMap.put(NceDatabase.UserAction.START_TIME,
				NceDatabase.UserAction.START_TIME);
		sTextProjectionMap.put(NceDatabase.UserAction.END_TIME,
				NceDatabase.UserAction.END_TIME);
		sTextProjectionMap.put(NceDatabase.UserAction.DURATION,
				NceDatabase.UserAction.DURATION);

	}
	
	public class NceDatabaseHelper extends SQLiteOpenHelper {
/*		private static final String TEXT_TYPE = " TEXT";
		private static final String COMMA_SEP = ",";
		private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS"
				+ TABLE_NCE_TEXT + " ("
				+ NceDatabase.NceText._ID+ " INTEGER PRIMARY KEY ,"
				+ NceDatabase.NceText.TEXT_TITLE+ TEXT_TYPE + COMMA_SEP 
				+ NceDatabase.NceText.TEXT_BODY + " );";

		private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NCE_TEXT;*/

		
		NceDatabaseHelper(Context context) {
			super(context, NceDatabase.DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			/*db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);*/
		}
		
		    
	}

	@Override
	public boolean onCreate() {
		nceHelper = new NceDatabaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {

		/**
		 * Chooses the MIME type based on the incoming URI pattern
		 */
		switch (sUriMatcher.match(uri)) {

		// If the pattern is for text, returns the general
		// content type.
		case TEXT:

			// If the pattern is for text IDs, returns the text ID content type.
		case TEXT_ID:
			return NceDatabase.NceText.TEXT_ITEM_TYPE;
			
			// If the pattern is for user action, returns the general
			// content type.
		case ACTION:
			
			// If the pattern is for user action id, returns the action ID content type.
		case ACTION_ID:
			return NceDatabase.UserAction.ACTION_ITEM_TYPE;
			
		case MULTI_TEXT_ID:
			return NceDatabase.UserAction.MULTI_ITEM_TYPE;

			// If the URI pattern doesn't match any permitted patterns, throws
			// an exception.
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// A map to hold the new record's values.
        ContentValues values = null;
		switch (sUriMatcher.match(uri)) {
		case ACTION_ID:
			if (initialValues != null) {
	            values = new ContentValues(initialValues);
	        } else {
	            // Otherwise, create a new value map
	            values = new ContentValues();
	        }
			break;

		case MULTI_TEXT_ID:
			if (initialValues != null) {
	            values = new ContentValues(initialValues);
	        } else {
	            // Otherwise, create a new value map
	            values = new ContentValues();
	        }
		default:
			break;
		}

        /*// Validates the incoming URI. Only the full provider URI is allowed for inserts.
        if (sUriMatcher.match(uri) != ACTION_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // A map to hold the new record's values.
        ContentValues values;

        // If the incoming values map is not null, uses it for the new values.
        if (initialValues != null) {
            values = new ContentValues(initialValues);

        } else {
            // Otherwise, create a new value map
            values = new ContentValues();
        }
*/
        // Opens the database object in "write" mode.
        SQLiteDatabase db = nceHelper.getWritableDatabase();

        // Performs the insert and returns the ID of the new note.
        long rowId = db.insert(
            NceDatabase.UserAction.ACTION_TABLE_NAME,        // The table to insert into.
            NceDatabase.UserAction.LESSON_ID,  // A hack, SQLite sets this column value to null
                                             // if values is empty.
            values                           // A map of column names, and the values to insert
                                             // into the columns.
        );

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            // Creates a URI with the note ID pattern and the new row ID appended to it.
            Uri noteUri = ContentUris.withAppendedId(NceDatabase.UserAction.ACTION_ID_URI_BASE, rowId);
            System.out.println("rowId" + rowId);
            // Notifies observers registered against this provider that the data changed.
            getContext().getContentResolver().notifyChange(uri, null);
            return noteUri;
        }

        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
        throw new SQLException("Failed to insert row into " + uri);
    
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		String groupBy = null;
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		// If the incoming URI is for texts, chooses the texts projection
		case TEXT:
			qb.setTables(NceDatabase.NceText.TEXT_TABLE_NAME);
			qb.setProjectionMap(sTextProjectionMap);
			break;

		/*
		 * If the incoming URI is for a single text identified by its ID,
		 * chooses the text ID projection, and appends "_ID = <textID>" to the
		 * where clause, so that it selects that single text
		 */
		case TEXT_ID:
			qb.setTables(NceDatabase.NceText.TEXT_TABLE_NAME);
			qb.setProjectionMap(sTextProjectionMap);
			qb.appendWhere(NceDatabase.NceText._ID + // the name of the ID column
					"=" +							// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							NceDatabase.NceText.TEXT_ID_PATH_POSITION));
			break;
			
			// If the incoming URI is for texts, chooses the texts projection
		case ACTION:
			qb.setTables(NceDatabase.UserAction.ACTION_TABLE_NAME);
			qb.setProjectionMap(sTextProjectionMap);
			break;
			
			/*
			 * If the incoming URI is for a single text identified by its ID,
			 * chooses the text ID projection, and appends "_ID = <textID>" to the
			 * where clause, so that it selects that single text
			 */
		case ACTION_ID:
			qb.setTables(NceDatabase.UserAction.ACTION_TABLE_NAME);
			qb.setProjectionMap(sTextProjectionMap);
			qb.appendWhere(NceDatabase.UserAction._ID + // the name of the ID column
					"=" +							// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							NceDatabase.UserAction.TEXT_ID_PATH_POSITION));
			break;
		case MULITABLE:
			String table1 = uri.getPathSegments().get(0);
			String table2 = uri.getPathSegments().get(1);
			qb.setTables(table1 + " cross join " + table2);
			qb.setProjectionMap(sTextProjectionMap);
			groupBy = NceDatabase.UserAction.LESSON_ID;
			if (selection != null){  
                if (selection.length()>0){  
                    selection += "AND "+ NceDatabase.NceText._ID + " = "+ NceDatabase.UserAction.LESSON_ID;  
                }  
                else{  
                    selection = NceDatabase.NceText._ID +" = "+  NceDatabase.UserAction.LESSON_ID;
                }     
            }  
            else  
            {  
                selection = NceDatabase.NceText.TEXT_TABLE_NAME + "." + NceDatabase.NceText._ID +" = "
                			+ NceDatabase.UserAction.ACTION_TABLE_NAME + "." + NceDatabase.UserAction.LESSON_ID ;  
            }  
			break;
		case MULTI_TEXT_ID:
			qb.setTables(NceDatabase.NceText.TEXT_TABLE_NAME);
			qb.setProjectionMap(sTextProjectionMap);
			qb.appendWhere(NceDatabase.NceText._ID + // the name of the ID column
					"=" +							// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							NceDatabase.NceText.MULTI_TEXT_ID_PATH_POSITION));
			break;
		}
		SQLiteDatabase db = nceHelper.getReadableDatabase();
//		cursor = db.query(TABLE_NCE_TEXT, projection, selection, selectionArgs,
//				null, null, sortOrder);
				cursor = qb.query(db, projection, selection, selectionArgs, groupBy, null, null);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/**
	 * 更新数据库记录
	 */
	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {

		SQLiteDatabase db = nceHelper.getWritableDatabase();
		int count;
		String finalWhere;

		switch (sUriMatcher.match(uri)) {
		case TEXT_ID:
			String textId = uri.getPathSegments(). // the incoming note ID
					get(NceDatabase.NceText.TEXT_ID_PATH_POSITION);

			finalWhere = NceDatabase.NceText._ID + // The ID column name
					" = " + // test for equality
					textId; // the incoming note ID
			;
			count = db.update(TABLE_NCE_TEXT, values, finalWhere, whereArgs);
			break;
		case ACTION_ID:
			String actionId = uri.getPathSegments(). // the incoming note ID
			get(NceDatabase.NceText.TEXT_ID_PATH_POSITION);
			
			finalWhere = NceDatabase.UserAction._ID + // The ID column name
					" = " + // test for equality
					actionId; // the incoming note ID
			;
			count = db.update(TABLE_NCE_TEXT, values, finalWhere, whereArgs);
			break;
			
		case MULTI_TEXT_ID:
			String multiTextId = uri.getPathSegments(). // the incoming note ID
			get(NceDatabase.NceText.MULTI_TEXT_ID_PATH_POSITION);
			
			finalWhere = NceDatabase.UserAction._ID + // The ID column name
					" = " + // test for equality
					multiTextId; // the incoming note ID
			;
			count = db.update(TABLE_NCE_TEXT, values, finalWhere, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
