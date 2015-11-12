package com.jay.commons.net.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;//in httpcore

import com.jay.commons.io.ProgressInputStream;
import com.jay.commons.io.ProgressListener;
import com.jay.commons.log.Logger;
import com.jay.commons.log.utils.TimeUtils;
import com.jay.commons.net.MsgType;
import com.jay.commons.net.NetResult;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpManager {
	
	public int MAXRETRY = 3;
	public int retry = 0;
	public String TAG = this.getClass().getSimpleName();
	public int SO_TIMEOUT = 20000;
	public int CONNECTION_TIMEOUT = 20000;

	@SuppressLint("NewApi")
	public String httpGet(String uri){
		int i = 0;
		Logger.i(TAG, "已经进入httpGet函数");
		if((null == uri) || (uri.isEmpty())){
			Logger.w(TAG, "URI is null");
			return null;
		}
		Logger.i(TAG, "http get uri: "+uri);
		
		StringBuffer buffer = new StringBuffer();
		for(i = 0; i < MAXRETRY; i++){
			buffer.setLength(0);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,SO_TIMEOUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
			HttpGet httpget = new HttpGet(uri);
			try {
				HttpResponse response = httpclient.execute(httpget);
				int status = response.getStatusLine().getStatusCode();
				Logger.d(TAG, "第"+i+"次尝试从服务器获取数据。");
				if(200 != status){
					//TODO 添加出错处理
					TimeUtils.sleep(3000);	
					continue;  
				}
				//
				HttpEntity httpEntity = response.getEntity();
				if(null != httpEntity){
					BufferedReader in = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);   
					  
					String line = "";  
                    while ((line=in.readLine())!=null) {
                        buffer.append(line);
                    }
				}
				Logger.d(TAG, buffer.toString());
				//此处处理接收到的数据
				break;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "客户端协议异常: "+e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "IO异常: "+e.toString());
				TimeUtils.sleep(1000);
			} finally {
				//关闭连接
				httpget.abort();
				httpclient.getConnectionManager().shutdown();
				
			}
		}
		if(i >= 3){
			Logger.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
			return null;
		}
		else{
			return buffer.toString();
		}
	}
	//
	@SuppressLint("NewApi")
	public NetResult httpPost(String uri, String data, List<NameValuePair> params, List<String> files){
		int i = 0;
		String result = "";
		if((null == uri) || (uri.isEmpty())){
			Logger.w(TAG, "URI is null");
			return null;
		}
		NetResult  nr = new NetResult();
		for(i = 0; i < MAXRETRY; i++){
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,SO_TIMEOUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
			HttpPost httppost = new HttpPost(uri);
			MultipartEntityBuilder mEntityBuilder = null;
			boolean isMulti = false;
			if(((data != null) && (params != null)) ||
			   ((data != null) && (files != null))  ||
			   ((params != null) && (files != null))||
			   ((files != null) && (files.size() > 0))){
				//多个文件
				mEntityBuilder = MultipartEntityBuilder.create();
				mEntityBuilder.setCharset(Charset.forName("UTF-8"));
				isMulti = true;
			}
			
			try {
				//如果第二个参数不为null或""，将第二个参数设置为entity
				if(null!=data&&data.trim().length()>0){
					//向服务器写json
					if(false == isMulti){
			            StringEntity se = new StringEntity(data+"\r\n\r\n", "UTF-8");
			            Logger.d(TAG, "JSON:"+data+"\r\n\r\n");
			            httppost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
			            httppost.setHeader("Accept", "*/*");
			            httppost.setEntity(se);
					}else{
						StringBody strBody = new StringBody(data+"\r\n", ContentType.create("application/json", "UTF-8"));
						mEntityBuilder.addPart("JSON", strBody);
					}
				}
				//如果第二个参数为空，判断第三个参数
				if((null == params) || (params.isEmpty())){
					Logger.w(TAG, "params is null");
				} else {//如果第三个参数不为空，将第三个参数设置为entity
					StringEntity se =  new UrlEncodedFormEntity(params,HTTP.UTF_8);
					
					Logger.d(TAG, "NameValue:"+EntityUtils.toString(se));
					if(false == isMulti){
						httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
						httppost.setEntity(se);
					}else{					
						for(int k = 0; k < params.size(); k++){
							Logger.d(TAG, params.get(k).getName()+":"+params.get(k).getValue());
							mEntityBuilder.addTextBody(params.get(k).getName(),params.get(k).getValue());
						}
						//mEntityBuilder.addTextBody("NameValue", EntityUtils.toString(se));
					}
				}
				//文件上传
				if((null != files) && (!files.isEmpty())){
					if(false == isMulti){
						Logger.d(TAG, "file"+": "+files.get(0));
						FileEntity entity = new FileEntity(new File(files.get(0)), ContentType.create("text/plain", "UTF-8").toString());
						httppost.setEntity(entity);
					}else{
						for(int j = 0; j < files.size(); j++){
							Logger.d(TAG, "file"+j+": "+files.get(j));
							File file = new File(files.get(j));
							if((null != file) && (file.isFile())){
								mEntityBuilder.addBinaryBody("file", file);
							}
						}
					}
				}
				if(true == isMulti){
					httppost.setEntity(mEntityBuilder.build());
				}
				
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "编码异常: "+e1.toString());
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "IO异常: "+e.toString());
				return null;
			} 
			try {
						
				HttpResponse response = httpclient.execute(httppost);
				int status = response.getStatusLine().getStatusCode();
				Logger.d(TAG, "第"+i+"次尝试发送数据到服务器。");
		
				
				HttpEntity httpEntity = response.getEntity(); 
                result = EntityUtils.toString(httpEntity);//取出应答字符串  
                nr.setStatus(status);
                nr.setResult(result);
				break;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "客户端协议异常: "+e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "IO异常: "+e.toString());
				TimeUtils.sleep(1000);
			}catch(Exception e){
				Logger.e(TAG, "Post异常: "+e.toString());
				TimeUtils.sleep(1000);
			}
			finally {
				//关闭连接
				httppost.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		if(i >= 3){
			Logger.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
		}
		return nr;
	}
	public boolean delete(File file) {  
        if (file.isFile()) {  
            return file.delete();   
        }  
  
        if(file.isDirectory()){  
            File[] childFiles = file.listFiles();  
            if (childFiles == null || childFiles.length == 0) {  
                return file.delete();    
            }  
      
            for (int i = 0; i < childFiles.length; i++) {  
                if(false == delete(childFiles[i])){
                	Logger.w(TAG, "删除文件"+childFiles[i].getName()+"失败！");
                }
            }  
            return file.delete();  
        }  
        return true;
    } 
	//
	@SuppressLint("NewApi")
	public boolean download(String uri, String filename, Handler handler) throws IOException{
		int i = 0;
		int BUFFER_LEN = 8192;
		if((null == uri) || (uri.isEmpty())){
			Logger.d(TAG, "URI is null");
			return false;
		}
		if((null == filename) || (filename.isEmpty())){
			Logger.d(TAG,  "filename is null");
			return false;
		}
		File file = new File(filename);
		delete(file);
		
		OutputStream  outputStream = null;
		int offset = 0;
		int filelen = 0;
		boolean isResumeBrokenTransfer = true;
		outputStream = new FileOutputStream(filename);
		for(i = 0; i < MAXRETRY; i++){
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,SO_TIMEOUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
			HttpGet httpget = new HttpGet(uri);
			try {
				if(true == isResumeBrokenTransfer){
					httpget.addHeader("Range", "bytes="+offset+"-");
				} else {
					if(httpget.containsHeader("Range")){
						httpget.removeHeaders("Range");
					}
					isResumeBrokenTransfer = false;
				}
				HttpResponse response = httpclient.execute(httpget);
				int status = response.getStatusLine().getStatusCode();
				
				Logger.d(TAG, "第"+i+"次尝试从服务器获取数据。");
				if((200 != status) && (206 != status)){
					Logger.w(TAG, "HttpClient返回错误码: "+status);
					TimeUtils.sleep(3000);
					continue;  
				}
				if(response.containsHeader("Content-Range")){
					String contentRange = response.getFirstHeader("Content-Range").getValue();
					if((null == contentRange) || (contentRange.isEmpty())){
						Logger.d(TAG,  "Content-Range内容为空");
					}else{
						Logger.d(TAG,  "Content-Range : "+contentRange);
						filelen = Integer.parseInt(contentRange.substring(contentRange.lastIndexOf("/")+1));
						Logger.d(TAG, "下载文件大小: "+filelen+" Bytes");
						if(filelen <= offset){
							Logger.e(TAG, "文件长度异常: "+filelen);
							break;
						}
					}
				}else{
					String contentLength = response.getFirstHeader("Content-Length").getValue();
					if((null == contentLength) || (contentLength.isEmpty())){
						Logger.d(TAG,  "Content-Length内容为空");
					}else{
						Logger.d(TAG,  "Content-Length : "+contentLength);
						filelen = Integer.parseInt(contentLength);
						Logger.d(TAG, "下载文件大小: "+filelen+" Bytes");
						if(filelen <= offset){
							Logger.e(TAG, "文件长度异常: "+filelen);
							break;
						}
					}
				}
				if(response.containsHeader("Accept-Ranges")){
					if(response.getFirstHeader("Accept-Ranges").getValue().equals("bytes")){
						isResumeBrokenTransfer = true;
					} else {
						isResumeBrokenTransfer = false;
					}
				}else{
					isResumeBrokenTransfer = false;
				}
				//此处处理接收到的数据
				HttpEntity httpEntity = response.getEntity();
				if(null != httpEntity){
			
					final Handler mHandler = handler;
					ProgressInputStream pis = new ProgressInputStream(
	                        httpEntity.getContent(), new ProgressListener() {

	                            @Override
	                            public void transferred(long transferedBytes) {
	                                Message msg = new Message();
	                                msg.what = MsgType.MSG_TYPE_HTTP_PROGRESS;
	                                Bundle data = new Bundle();
	                                data.putLong("progress", transferedBytes);
	                                data.putLong("maxvalue", transferedBytes);
	                                msg.setData(data);
	                                mHandler.sendMessage(msg);
	                            }
	                        });
										
					byte[] bytes = new byte[BUFFER_LEN];
					int size = 0;
                    while((size = pis.read(bytes)) != -1){
                    	outputStream.write(bytes,0,size);
                    	offset += size;
                    }
                    pis.close();
				}
				if(offset < filelen){
					continue;
				}
				
				break;
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "客户端协议异常: "+e.toString());
				TimeUtils.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "IO异常: "+e.toString());
				TimeUtils.sleep(1000);
			} finally {
				//关闭连接
				httpget.abort();
				httpclient.getConnectionManager().shutdown();
			}
	}
		if(null != outputStream){
			outputStream.flush();
			outputStream.close();
			outputStream = null;
		}
		if(i >= 3){
			Logger.w(TAG, "尝试3次从服务器获取数据失败，请检查网络。");
			return false;
		}
		else{
			return true;
		}
	}
	
	@SuppressLint("NewApi")
	public boolean upload(String uri, String filename){
		int i = 0;
		if((null == filename) || (filename.isEmpty())){
			Logger.w(TAG, "文件名不能为空！");
			return false;
		}
		File file = new File(filename);
		if(false == file.exists()){
			Logger.w(TAG,  "文件"+filename+"不存在！");
			return false;
		}
		FileEntity entity = new FileEntity(file, ContentType.create("text/plain", "UTF-8").toString());        
		
		for(i = 0; i < MAXRETRY; i++){
			
			HttpPost httppost = new HttpPost(uri);
			httppost.setEntity(entity);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,SO_TIMEOUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);
			try {
				Logger.d(TAG, "第"+i+"次尝试发送文件"+filename+"到服务器。");
				HttpResponse response = httpclient.execute(httppost);
				int status = response.getStatusLine().getStatusCode();				
				
				if(200 != status){
					//TODO 添加出错处理
					Logger.w(TAG, "上传文件"+filename+"失败错误码:"+status);
					TimeUtils.sleep(3000);
					continue;  
				}
				HttpEntity httpEntity = response.getEntity();  
                String result = EntityUtils.toString(httpEntity);
                Logger.d(TAG,  result);
				break;
				
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "上传文件: 客户端协议错误("+e1.toString()+")");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Logger.e(TAG, "上传文件: 客户端IO错误("+e1.toString()+")");
			} finally{
				httppost.abort();
				httpclient.getConnectionManager().shutdown();
			}
		}
		
		if(i >= 3){
			Logger.w(TAG, "尝试3次从服务器获取数据，均失败，请检查网络。");
			return false;
		}
		else{
			return true;
		}
	}
	//
}

