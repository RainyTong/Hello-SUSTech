package com.example.xuversion;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtil {

	private static SharedUtil mUtil;
	private static SharedPreferences mShared;

	/**
	 *  use the singleton pattern to get a SharedUtil
	 * @param ctx ctx
	 * @return shared utility
	 */
	public static SharedUtil getIntance(Context ctx) {
		if (mUtil == null) {
			mUtil = new SharedUtil();
		}
		mShared = ctx.getSharedPreferences("share", Context.MODE_PRIVATE);
		return mUtil;
	}

	/**
	 * write SharedUtil with key
	 * @param key the key
	 * @param value the value
	 */
	public void writeShared(String key, String value) {
		SharedPreferences.Editor editor = mShared.edit();
		editor.putString(key, value);
		editor.commit(); 
	}

	/**
	 * read SharedUtil value with key
	 * @param key key to find value
	 * @param defaultValue  default value
	 * @return the shared value
	 */
	public String readShared(String key, String defaultValue) {
		return mShared.getString(key, defaultValue);
	}

	/**
	 * remove value with key from SharedUtil
	 * @param key key to find value
	 */
	public void removeShared(String key) {
		SharedPreferences.Editor editor = mShared.edit();
		editor.remove(key);
		editor.commit();
	}
	
}
