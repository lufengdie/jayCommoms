package com.jay.commons.log.utils;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.jay.commons.log.model.Parameter;



/**
 * @Class Name : StringUtil
 * @Description: Utility class for string-related processing.
 * @Author yuanlf
 * @Date 2014年5月29日 上午11:53:38
 */
public class StringUtil {

	
	/**
	 *  去除 指定字符串中 的分隔符，并返回字符数组
	 * 
	 * @param targetStr  String类型  目标字符串
	 * @param mark       String类型  要去除的分隔符
	 * @return 去掉分隔符后的字符数组
	 */
	public static String[] splitMark(String targetStr,String mark){
		String[] strArray = new String[]{};
		if ( !"".equals(targetStr)) {
			if (targetStr.indexOf(mark) != -1) {
				if ("|".equals(mark)) {
					strArray = targetStr.split("\\|");
				} else {
					strArray = targetStr.split(mark);
				}
			}else{
				strArray = new String[]{targetStr};
			}
		}
		return strArray;
	}
	
	
	/**
	 * The string is empty if it is null or contains only space (both
	 * single-byte and double-byte space counts).
	 * @param str The string to be tested
	 * @return true if the string is empty
	 */
	public static boolean isEmpty(String str) {
		if ((str == null) || trim(str).equals("") || str.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
	

	/**
	 * The string is not empty if it is not null or contains some char.
	 * @param str The string to be tested
	 * @return true if the string is empty
	 */
	public static boolean isNotEmpty(String str) {
		if ((str == null) || trim(str).equals("") || str.equalsIgnoreCase("null")) {
			return false;
		}
		return true;
	}

	
	/**
	 * The object is empty if it is null or contains only space (both
	 * single-byte and double-byte space counts).
	 * @param obj The Object to be tested
	 * @return true if the Object is empty
	 */
	public static boolean isEmpty(Object obj) {
		if ((null == obj) || obj.equals("")) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 是否为空  包括 null字符串
	 * @param input
	 * @return
	 */
	public static boolean isNullEmpty(String input) {
		if ((null == input) || input.equals("") || input.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 过滤显示的null字符串
	 */

	public static String StringFilteNull(String input) {
		if ((null == input) || input.equals("") || input.equalsIgnoreCase("null")) {
			return "";
		}
		return input;
	}
	
	
	/**
	 * To delete the space on beginning and end of the string. Both single-byte
	 * and double-byte space will be deleted.
	 * @param str String
	 * @return trimed string
	 */
	public static String trim(String str) {
		if ((str == null) || str.trim().equals("")) {
			return "";
		}

		String newStr = str.trim();
		boolean startFull = newStr.startsWith("　"); // 12288
		boolean endFull = newStr.endsWith("　"); // 12288
		boolean startHalf = newStr.startsWith(" "); // 97
		boolean endHalf = newStr.endsWith(" "); // 97

		while (startFull || endFull || startHalf || endHalf) {
			startFull = newStr.startsWith("　"); // 12288
			endFull = newStr.endsWith("　"); // 12288

			if (startFull) {
				if (newStr.length() == 1) {
					return "";
				}

				newStr = newStr.substring(1);
			}

			if (endFull) {
				if (newStr.length() == 1) {
					return "";
				}

				newStr = newStr.substring(0, newStr.length() - 1);
			}

			startHalf = newStr.startsWith(" "); // 97
			endHalf = newStr.endsWith(" "); // 97

			if (startHalf) {
				newStr = newStr.substring(1);
			}

			if (endHalf) {
				newStr = newStr.substring(0, newStr.length() - 1);
			}
		}

		return newStr;
	}

	/**
	 * 解析 param map 添加到 URL 后面
	 * @param uri
	 * @param parammap
	 * @return
	 */
	public static String addParametersForMap(String uri,Map<String, String> parammap){
		if(parammap==null||parammap.size()==0)
			return uri;
		else{
		    //先将参数放入List，再对参数进行URL编码  
		    List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			Set<Entry<String, String>> set=parammap.entrySet();
			for (Entry<String, String> entry : set) {
			    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
			}
			//对参数编码  
			String param = URLEncodedUtils.format(params, "UTF-8");
			return uri + "?" + param;
		}
	}

	/**
	 * 解析 params 添加到 URL 后面
	 *
	 * @param uri
	 * @param params
	 * @return
	 */
	public static String addParametersForParams(String uri,Parameter... params){
		if(params == null || params.length == 0)
			return uri;
		else{
			boolean isfirst=true;
			StringBuilder uribuffer=new StringBuilder(uri);
			for (Parameter param : params) {
				if(isfirst){
					uribuffer.append("?");
					isfirst=false;
				}else{
					uribuffer.append("&");
				}
				uribuffer.append(param.getName());
				uribuffer.append("=");
				uribuffer.append(param.getValue());
			}
			return uribuffer.toString();
		}
	}

}