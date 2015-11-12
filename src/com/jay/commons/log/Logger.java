package com.jay.commons.log;


import com.jay.commons.exception.ExceptionUtil;
import com.jay.commons.log.utils.ConfigFaced;
import com.jay.commons.log.utils.EnvironmentShare;

import android.util.Log;

/**
 * <b>日志打印类</b>
 * @author YuanLiangFeng 袁良锋
 * <p> at 2012-04-24
 * 有两类开关，日志开关(LOG_ON)和分类信息开关(如：LOG_ERROR，LOG_DEBUG等)，
 * 需要打开或关闭所有日志时，关闭日志开关；需要对某个分类信息开关时，只需要修改对应打印开关状态即可。
 */
public class Logger {
	

	/** 应用程序标签 **/
	private final static String TAG = "Smart";

	
	/**
	 * 发送一个ERROR 日志信息,Send an ERROR log message.
	 * @param msg 消息内容
	 */
	public static void e(String msg){
		e(TAG, msg);
	}
	
	/**
	 * 发送一个ERROR 日志信息,Send an ERROR log message.
	 * @param msg 消息内容
	 */
	public static void e(String tag,String msg){
		if (ConfigFaced.LOG_ERROR) {
			Log.e(tag, msg);
			printToSdCard(msg);
		}
	}
	
	/**
     * @Description   打印带有异常信息的日志
     * @param str  普通日志
     * @param e       异常信息
     */
	public static void e(String str, Exception e) {
		if (e != null) {
			String msg = str + "\n" + ExceptionUtil.getExceptionStack(e);
			e(msg);
		}
	}
	
	/**
	 * 打印带有异常信息的日志
	 * @param tag     标签
	 * @param string  普通日志
	 * @param e       异常信息
	 */
	public static void e(String tag, String str, Exception e) {
		if (e != null) {
			String msg = str + "\n" + ExceptionUtil.getExceptionStack(e);
			e(tag, msg);
		}
	}
	
	
	/**
	 * 发送一个DEBUG 日志信息,Send an DEBUG log message.
	 * @param msg 消息内容
	 */
	public static void d(String msg){
		d(TAG, msg);
	}
	
	/**
	 * 发送一个DEBUG 日志信息,Send an DEBUG log message.
	 * @param tag 
	 * @param msg
	 */
	public static void d(String tag, String msg){
		if (ConfigFaced.LOG_DEBUG) {
			Log.d(TAG, msg);
			printToSdCard(msg);
		}
	}
	
	/**
     * @Description   打印带有异常信息的日志
     * @param string  普通日志
     * @param e       异常信息
     */
	public static void d(String string, Exception e) {
		if (e != null) {
			String msg = string + "\n" + ExceptionUtil.getExceptionStack(e);
			d(msg);
		}
	}
	
	/**
	 * 发送一个WARN 日志信息,Send an WARN log message.
	 * @param msg 消息内容
	 */
	public static void w(String msg){
		w(TAG, msg);
	}
	
	/**
	 * 发送一个WARN 日志信息,Send an WARN log message.
	 * @param tag 标签名称
	 * @param msg 消息内容
	 */
	public static void w(String tag, String msg){
		if (ConfigFaced.LOG_WARN) {
			Log.w(TAG, msg);
			printToSdCard(msg);
		}
	}
	
	/**
     * @Description   打印带有异常信息的Warning日志
     * @param string  普通日志
     * @param e       异常信息
     */
	public static void w(String string, Exception e) {
		if (e != null) {
			String msg = string + "\n" + ExceptionUtil.getExceptionStack(e);
			w(msg);
		}
	}
	
	/**
	 * 发送一个INFO 日志信息,Send an INFO log message.
	 * @param msg 消息内容
	 */
	public static void i(String msg){
		i(TAG, msg);
	}
	
	/**
	 * 发送一个INFO 日志信息,Send an INFO log message.
	 * @param tag
	 * @param msg 消息内容
	 */
	public static void i(String tag, String msg){
		if (ConfigFaced.LOG_INFO) {
			Log.i(tag, msg);
			printToSdCard(msg);
		}
	}
	
	/**
	 * 打印带有异常信息的info日志
	 * @param string
	 * @param e       异常信息
	 */
	public static void i(String string, Exception e) {
		if (e != null) {
			String msg = string + "\n" + ExceptionUtil.getExceptionStack(e);
			i(msg);
		}
	}

	
	/**
	 * @Description  将打印日志到SD卡上
	 * @param string
	 */
	private static void printToSdCard(String string){
		if (ConfigFaced.LOG_WRITE_SD_CARD) {
			EnvironmentShare.print(string);
		}
	}
	
}