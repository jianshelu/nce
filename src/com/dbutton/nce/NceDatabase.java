package com.dbutton.nce;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NceDatabase {
	
	public static final String AUTHORITY = "com.dbutton.nce.provider.nce_2";

	/**
	 * ˽�й��췽��,��ֹ���౻ʵ����
	 */
	private NceDatabase() {}

	public static final String DATABASE_NAME = "nce_2.db";
	/**
	 *���ú�Լ ��Ͱ�����ķ�ʽ�ڳ�������֯���ݿ���ֶ�����װ���ݿ��ʵ��
	 * @author jianshelu
	 *
	 */
	public static final class UserAction implements BaseColumns {
		/**
		 * ��ֹʵ��������
		 */
		private UserAction(){}
		
		private static final String SCHEME = "content://";
		public  static final String ACTION_TABLE_NAME = "user_action";
		private static final String PATH_ACTION = "/user_action";
		private static final String PATH_ACTION_ID = "/user_action/";
		public static final int ACTION_ID_PATH_POSITION = 1;
		
		public static final Uri ACTION_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ACTION);
		public static final Uri ACTION_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_ACTION_ID);
		public static final Uri ACTION_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_ACTION_ID + "/#");
		
		public static final String ACTION_ITEM_TYPE = "vnd.android.cursor.item/user_action";
		public static final String MULTI_ITEM_TYPE = "vnd.android.cursor.item/user_action";
		
		/**
		 * ����id
		 * ����:integer
		 */
		public static final String LESSON_ID = "lesson_id";
		
		/**
		 * ѧϰ��ʼʱ��
		 * ����:TIMESTAMP
		 */
		public static final String START_TIME = "start_time";
		
		/**
		 * ѧϰ����ʱ��
		 * ����:TIMESTAMP
		 */
		public static final String END_TIME = "end_time";
		
		/**
		 * ѧϰʱ��
		 * ����:TIME
		 */
		public static final String DURATION = "duration";
		
		/**
		 * �û�id
		 * ����:integer
		 */
		public static final String USER_ID = "user_id";
		
		/**
		 * ����
		 */
		public static final String _ID = "_id";
		public static final int TEXT_ID_PATH_POSITION = 1;
		
	}
	
	public static final class NceText implements BaseColumns {
		/**
		 * ��ֹʵ��������
		 */
		private NceText(){}
		
		public  static final String TEXT_TABLE_NAME = "nce_2_text";
		private static final String SCHEME = "content://";
		private static final String PATH_TEXT = "/nce_2_text";
		private static final String PATH_TEXT_ID = "/nce_2_text/";
		public static final int TEXT_ID_PATH_POSITION = 1;
		public static final int MULTI_TEXT_ID_PATH_POSITION = 2;
		
		public static final Uri TEXT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_TEXT);
		public static final Uri TEXT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_TEXT_ID);
		public static final Uri TEXT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_TEXT_ID + "/#");
		
		public static final String TEXT_ITEM_TYPE = "vnd.android.cursor.item/nce_2_text";
		
		/**
		 * ������Ŀ
		 * ����:text
		 */
		public static final String TEXT_TITLE = "text_title";
		
		/**
		 * ��������
		 * ����:text
		 */
		public static final String TEXT_BODY = "text_body";
		
		public static final String USER_NAME = "user_name";
		
		public static final String _ID = "_id";
		
		/**
		 * �û����ղ�
		 */
		public static final String USER_FAVORITE = "user_favorite";
		/**
		 * �û��������
		 * ��������:int
		 */
		public static final String CLICK_COUNT = "click_count";
		
	}
	
	public static final class NceUser implements BaseColumns{
		/**
		 *��ֹʵ�������� 
		 */
		private NceUser(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/nce_user");
		
		/**
		 * �û���
		 * ��������:text
		 */
		public static final String USER_NAME = "user_name";
		
		public static final String LESSON_ID = "lesson_id";
		/**
		 * �û��������
		 * ��������:int
		 */
		public static final String CLICK_COUNT = "click_count";
	}
}