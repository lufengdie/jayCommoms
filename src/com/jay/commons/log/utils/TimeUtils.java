package com.jay.commons.log.utils;

import com.jay.commons.log.Logger;

public class TimeUtils {

	public static String TAG = TimeUtils.class.getSimpleName();
	
	public static void sleep(long ms){
		//TODO 添加出错处理
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Logger.e(TAG, "Sleep failed: "+e.toString());
		}		
	}

}