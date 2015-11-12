package com.jay.commons.log.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {
	/* 判断是否是IP地址 */
	public static boolean isIP(String addr){
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(addr);
		return matcher.matches();
	}
}
