package com.dbutton.nce;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NceDatabase {
	
	public static final String AUTHORITY = "com.dbutton.nce.provider.nce_2";

	/**
	 * 私有构造方法,防止该类被实例化
	 */
	private NceDatabase() {}

	/**
	 *采用合约 类和伴随类的方式在程序中组织数据库的字段来封装数据库的实现
	 * @author jianshelu
	 *
	 */
	public static final class NceText implements BaseColumns {
		/**
		 * 禁止实例化该类
		 */
		private NceText(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/nce_2_text");
		

		/**
		 * _id自动生成的记录编号
		 */
		public static final String _ID = "_id";
		
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
	}
}