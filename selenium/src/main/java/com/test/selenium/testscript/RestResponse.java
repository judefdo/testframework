package com.test.selenium.testscript;

import java.util.Hashtable;



public class RestResponse {
	private int statusCode = 0;
	private Hashtable <String,String> responseHeaders;
    private static String response;
                                        
    public int getStatusCode() {
			return statusCode;
	}
	
	public void setStatusCode( int statusCode) {
			 this.statusCode = statusCode;
	}
	public String getResponse() {
			return response;
	}
	
	public void setResponse( String response) {
			 this.response = response;
	}
	
	public Hashtable getResponseHeaders()  {
		return responseHeaders;
	}
	
	public void setResponseHeaders(Hashtable responseHeaders)  {
		 this.responseHeaders = responseHeaders;
	}
}
