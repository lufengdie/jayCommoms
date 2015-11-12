package com.jay.commons.net;

public interface IHttpCallback {

	public void onStart();
	public void onSuccess(String msg);
	public void onFailure(String err);
	public void onProgress(long progress, long maxValue);
}
