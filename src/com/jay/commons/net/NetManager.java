package com.jay.commons.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;

import com.jay.commons.log.Logger;
import com.jay.commons.net.http.HttpManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetManager {
	
	
	private IHttpCallback httpCallback = null;
	private static String TAG = NetManager.class.getSimpleName();

	
	public void httpGet(String uri, IHttpCallback cb) {
		// httpGet
		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		HttpGetThread httpGet = new HttpGetThread();
		httpGet.uri = uri;
		httpGet.start();

	}

	class HttpGetThread extends Thread {
		public String uri = null;

		@Override
		public void run() {
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			String result = httpManager.httpGet(uri);
			if (null == result) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				Bundle data = new Bundle();
				data.putString("data", result);
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MsgType.MSG_TYPE_HTTP_START:
				httpCallback.onStart();
				break;
			case MsgType.MSG_TYPE_HTTP_SUCCESS:
				httpCallback.onSuccess(msg.getData().getString("data"));
				break;
			case MsgType.MSG_TYPE_HTTP_FAILURE:
				httpCallback.onFailure(msg.getData().getString("data"));
				break;
			case MsgType.MSG_TYPE_HTTP_PROGRESS:
				httpCallback.onProgress(msg.getData().getLong("progress"), msg
						.getData().getLong("maxvalue"));
				break;
			default:
				break;
			}

		}
	};

	// httpPost
	public void httpPost(String uri, String data, List<NameValuePair> params,
			List<String> files, IHttpCallback cb) {
		// httpGet
		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		HttpPostThread httpPost = new HttpPostThread();
		httpPost.uri = uri;
		httpPost.params = params;
		httpPost.data = data;
		httpPost.files = files;
		httpPost.start();

	}

	// httpPost
	public void httpPost(String uri, String data, List<NameValuePair> params,
			List<String> files, IHttpCallback cb, boolean flag) {
		// httpGet
		if (flag == true) {
			if (true == NetThreadsManager.hasURI(uri)) {
				return;
			}
			NetThreadsManager.addURI(uri);
		}
		httpCallback = cb;
		HttpPostThread httpPost = new HttpPostThread();
		httpPost.uri = uri;
		httpPost.params = params;
		httpPost.data = data;
		httpPost.files = files;
		httpPost.start();

	}

	class HttpPostThread extends Thread {
		public String uri = null;
		List<NameValuePair> params = null;
		List<String> files = null;
		public String data = null;

		@Override
		public void run() {
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			NetResult nr = httpManager.httpPost(uri, data, params, files);
			if (null == nr) {
				Logger.d(TAG, "NULL");
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				if (200 == nr.getStatus()) {
					msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				} else {
					msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
				}
				Logger.d(TAG,
						"status: " + nr.getStatus() + "Result:"
								+ nr.getResult());
				Bundle data = new Bundle();
				data.putString("data", nr.getResult());
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	// download
	public void download(String uri, String filename, IHttpCallback cb) {
		// download
		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		DownloadThread download = new DownloadThread();
		download.uri = uri;
		download.filename = filename;
		download.start();

	}

	class DownloadThread extends Thread {
		public String uri = null;
		public String filename = null;

		@Override
		public void run() {
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			boolean isSuccess = false;
			try {
				isSuccess = httpManager.download(uri, filename, mHandler);
			} catch (IOException e) {
				Logger.d(TAG, "下载文件IO错误: " + e.toString());
			}
			if (isSuccess == false) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				Bundle data = new Bundle();
				data.putString("data", "true");
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

	// upload
	public void upload(String uri, String filename, IHttpCallback cb) {

		if (true == NetThreadsManager.hasURI(uri)) {
			return;
		}
		NetThreadsManager.addURI(uri);
		httpCallback = cb;
		UploadThread upload = new UploadThread();
		upload.uri = uri;
		upload.filename = filename;
		upload.start();
	}

	class UploadThread extends Thread {
		public String uri = null;
		public String filename = null;

		@Override
		public void run() {
			super.run();

			Message msgStart = new Message();
			msgStart.what = MsgType.MSG_TYPE_HTTP_START;
			mHandler.sendMessage(msgStart);

			HttpManager httpManager = new HttpManager();
			Message msg = new Message();
			boolean isSuccess = httpManager.upload(uri, filename);
			if (isSuccess == false) {
				msg.what = MsgType.MSG_TYPE_HTTP_FAILURE;
			} else {
				msg.what = MsgType.MSG_TYPE_HTTP_SUCCESS;
				Bundle data = new Bundle();
				data.putString("data", "true");
				msg.setData(data);
			}
			NetThreadsManager.delURI(uri);
			mHandler.sendMessage(msg);
		}
	}

}