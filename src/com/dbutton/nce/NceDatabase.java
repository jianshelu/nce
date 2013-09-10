package com.dbutton.nce;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NceDatabase {
	
	public static final String AUTHORITY = "com.dbutton.nce.provider.nce_2";

	/**
	 * ˽�й��췽��,��ֹ���౻ʵ����
	 */
	private NceDatabase() {}

	/**
	 *���ú�Լ ��Ͱ�����ķ�ʽ�ڳ�������֯���ݿ���ֶ�����װ���ݿ��ʵ��
	 * @author jianshelu
	 *
	 */
	public static final class NceText implements BaseColumns {
		/**
		 * ��ֹʵ��������
		 */
		private NceText(){}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/nce_2_text");
		

		/**
		 * _id�Զ����ɵļ�¼���
		 */
		public static final String _ID = "_id";
		
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
	}
}