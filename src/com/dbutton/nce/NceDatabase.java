package com.dbutton.nce;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NceDatabase {
	
	public static final String AUTHORITY = "com.dbutton.nce.provider.nce_2";

	/**
	 * 私有构造方法,防止该类被实例化
	 */
	private NceDatabase() {}

	public static final String DATABASE_NAME = "nce_2.db";
	/**
	 *采用合约 类和伴随类的方式在程序中组织数据库的字段来封装数据库的实现
	 * @author jianshelu
	 *
	 */
	public static final class UserAction implements BaseColumns {
		/**
		 * 禁止实例化该类
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
		 * 文章id
		 * 类型:integer
		 */
		public static final String LESSON_ID = "lesson_id";
		
		/**
		 * 学习开始时间
		 * 类型:TIMESTAMP
		 */
		public static final String START_TIME = "start_time";
		
		/**
		 * 学习结束时间
		 * 类型:TIMESTAMP
		 */
		public static final String END_TIME = "end_time";
		
		/**
		 * 学习时长
		 * 类型:TIME
		 */
		public static final String DURATION = "duration";
		
		/**
		 * 用户id
		 * 类型:integer
		 */
		public static final String USER_ID = "user_id";
		
		/**
		 * 活动编号
		 */
		public static final String _ID = "_id";
		public static final int TEXT_ID_PATH_POSITION = 1;
		
	}
	
	public static final class NceText implements BaseColumns {
		/**
		 * 禁止实例化该类
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
		 * 文章题目
		 * 类型:text
		 */
		public static final String TEXT_TITLE = "text_title";
		
		/**
		 * 文章内容
		 * 类型:text
		 */
		public static final String TEXT_BODY = "text_body";
		
		public static final String USER_NAME = "user_name";
		
		public static final String _ID = "_id";
		
		/**
		 * 用户的收藏
		 */
		public static final String USER_FAVORITE = "user_favorite";
		/**
		 * 用户点击次数
		 * 数据类型:int
		 */
		public static final String CLICK_COUNT = "click_count";
		
	}
	
	public static final class NceUser implements BaseColumns{
		/**
		 *禁止实例化该类 
		 */
		private NceUser(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/nce_user");
		
		/**
		 * 用户名
		 * 数据类型:text
		 */
		public static final String USER_NAME = "user_name";
		
		public static final String LESSON_ID = "lesson_id";
		/**
		 * 用户点击次数
		 * 数据类型:int
		 */
		public static final String CLICK_COUNT = "click_count";
	}
}