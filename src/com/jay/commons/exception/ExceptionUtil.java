package com.jay.commons.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Class Name : ExceptionUtil
 * @Description: 异常 工具类
 * @Author yuanlf
 * @Date 2014年5月28日 下午5:30:40
 */
public class ExceptionUtil {


	/**
	 * @Description      获得异常轨迹
	 * @param exception  要获取轨迹的异常
	 * @return
	 */
	public static String getExceptionStack(Exception exception) {
		if (exception == null) {
			return "异常为空！";
		}
		String exceptionStack = "异常信息为空";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			exception.printStackTrace(pw);
			exceptionStack = sw.toString();
		} finally {
			try {
				sw.close();
				pw.close();
			} catch (IOException ioe) {
				// ignore
			}
		}
		return exceptionStack;
	}
	
	/*public static String getExceptionStack(Exception exception) {
		if (exception == null) {
			return "异常为空！";
		}
		StringBuffer exceptionStr = new StringBuffer();
	       
	    String detailMessage = exception.getMessage();
	      if (StringUtil.isNotEmpty(detailMessage)) {
	      exceptionStr.append(detailMessage);  
	    }
	  
	    StackTraceElement[] elements = exception.getStackTrace();  
	    if (elements.length == 0) {
			return "异常信息为空";
		}
	    for (int i = 0; i < elements.length; i++) {  
	       exceptionStr.append(elements[i].toString() + "\n");  
	    }
		return exceptionStr.toString();
	}*/
	
}