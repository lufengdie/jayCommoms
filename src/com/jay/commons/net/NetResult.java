package com.jay.commons.net;

public class NetResult {
	
	private int status;
	private String result;
	
	
	public void setStatus(int state){
		status = state;
	}
	public int getStatus(){
		return status;
	}
	
	public void setResult(String ret){
		result = ret;
	}
	public String getResult(){
		return result;
	}

}
