package com.test.selenium.testscript;


import com.test.selenium.testscript.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.ge.selenium.testscript.ResponseParserUtil;
import com.test.selenium.utils.TestUtil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.Map.Entry;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static com.test.selenium.testscript.TestDriver.CONFIG;
import static com.test.selenium.testscript.TestDriver.UIMap;

public class RestAPIUtil {

      public static final String GET = "GET";
      public static final String PUT = "PUT";
      public static final String DELETE = "DELETE";
      public static final String POST = "POST";
      public static final boolean quiet = true; 
      public JSONObject taskObject= new JSONObject();
  	  public JSONObject taskMainObject= new JSONObject();
  	  public Connection con;
  	  public ResultSet rs1;
      
      public String testService(String object,String inputData) throws IOException{
    		
    	  HashMap<String, ArrayList<String>> expMap = new HashMap<String,ArrayList<String>>();
    	  //HashMap<String, ArrayList<String>> resp = new HashMap<String, ArrayList<String>>();
    	  String result="";
    	  String url="";
    	  String proto = "http";
    		try{
	    			//String url = "http://sjc1eserv01:9090/service/wsa/task";
	    			//String url = "https://services.qa.gewattstation.com/EVSEWebServices/services/EVSEWebServices/GetOwnerStations?username=pradeep&password=Test12345";
	    			//String url = "https://services.qa.gewattstation.com/EVSEWebServices/services-rest/GetOwnerStations?username=evse&password=Pa55word";
	    			//String url = "http://localhost:8080/rest-assured-example/service/persons/json";
    			if(object.contains("http")){
    				url = object.split("\\|")[0];
    			}else{
    				url = UIMap.getProperty(object.split("\\|")[0]);
    			}
    				
    				String subTask = object.split("\\|")[1];
    				if(url.contains("https")){
    					proto = "https";
    				}
	    			//taskObject.put("username","pradeep");
	    			//taskObject.put("password","Test12345");
	    			Properties headers = new Properties();
	    			//headers.put("Content-Type", "application/json");
	    			//headers.put("Content-Type", "application/soap+xml");
	    			//"Accept","application/soap+xml,application/dime,multipart/related,text/*"
	    		//	headers.put("accept", "application/json");
	    			//headers.put("Accept","application/soap+xml,application/dime,multipart/related,text/*");
	    			 String params="";
		    	        String expdata="";
	    			if(inputData.contains("::")){
	    	        	params = inputData.split("::")[0];
	    	        	expdata = inputData.split("::")[1];
	    	        	String[] paramsArray = params.split(",");
	    	        	url=url+"?";
	    	        	for(int pc=0;pc<paramsArray.length;pc++){
	    	        		url=url+paramsArray[pc]+"&";
	    	        	}
	    	        	url=url.substring(0, url.length()-1);
	    	        }else{
	    	        	expdata = inputData;
	    	        }
	    			String body = null;
	    			//body = taskMainObject.toString();
	    			//String output = request("GET",proto,url,body,headers);
	    			RestResponse  resp = sendRequest("GET",proto,url,body,headers);
	    			if(subTask.equalsIgnoreCase("verifyResponseCode")){
	    				if(resp.getStatusCode()== Integer.parseInt(expdata)){
	    					//System.out.println(Constants.KEYWORD_PASS);
	    					return com.test.selenium.testscript.Constants.KEYWORD_PASS;
	    				}else{
	    					//System.out.println(Constants.KEYWORD_FAIL+" Actual Response Code "+resp.getStatusCode()+" Did not match expected "+expdata);
	    					return com.test.selenium.testscript.Constants.KEYWORD_FAIL+" Actual Response Code "+resp.getStatusCode()+" Did not match expected "+expdata;
	    				}
	    			}else if(subTask.equalsIgnoreCase("verifyResponseSize")){
	    				int size = ResponseParserUtil.getResponseSize(resp.getResponse());
	    				if(size == Integer.parseInt(expdata)){
	    					//System.out.println(Constants.KEYWORD_PASS);
	    					return com.test.selenium.testscript.Constants.KEYWORD_PASS;
	    				}else{
	    					//System.out.println(Constants.KEYWORD_FAIL+" Actual Size "+size+" Did not match expected "+expdata);
	    					return com.test.selenium.testscript.Constants.KEYWORD_FAIL+" Actual Size "+size+" Did not match expected "+expdata;
	    				}
	    				
	    			}else{//new code 12/18/14
	    				String output = resp.getResponse();
	    				System.out.println(output);
	    				result = verifyResults(output,expdata);
	    				System.out.println("result === "+result);
	    			}
	    			System.out.println(url);
	    			System.out.println(body);
	    	      //  System.out.println(output);
	    	       
	    	        ///service/user/create?email=1&
	    	        
	    	        System.out.println("PARAMs = "+params);
	    	        System.out.println("input = "+expdata);
	    	        
	    	   //     result = verifyResults(output,expdata);
	    	        System.out.println("RES -"+result);
	    	    }catch (Exception e) {
    	        	   // Could not find the database driver
    	        	   System.out.println("Language Exception "+e.getMessage());
    			} 
    	           
    	           //return TestUtil.verifyResults(expMap,resp);
	    	    return result;
    	        
      }
      
      public String verifyResults(String actOutput,String expOutput) throws SAXException, IOException{
    	  String result="";
    	  HashMap<String, ArrayList<String>> expMap = new HashMap<String,ArrayList<String>>();
    	  HashMap<String, ArrayList<String>> resp = new HashMap<String, ArrayList<String>>();
    	  if(!expOutput.startsWith("XML")){
	        	//key:val1,val2,val3|key1:v1,v2,v3
	        	String[] input = expOutput.split(com.test.selenium.testscript.Constants.DATA_SPLIT);
	        	for(int i=0;i<input.length;i++){
	        		String key = input[i].split(":")[0];
	        		ArrayList<String> val = (ArrayList<String>) TestUtil.getList(input[i].split(":")[1], ",");
	        		expMap.put(key, val);
	        	}
	        	if(actOutput.startsWith("<")){
			      	resp = ResponseParserUtil.getRESTResponseData(actOutput);
			    }else{
			       	resp = ResponseParserUtil.getRESTJsonResponseData(actOutput);
			    }
	        	result = TestUtil.verifyResults(expMap,resp);
    	  }else{
    		  String expFile = expOutput.split(com.test.selenium.testscript.Constants.DATA_SPLIT)[1];
    		  String xmlPath = CONFIG.getProperty("xml_path");
    		  if(!xmlPath.endsWith("/")||xmlPath.endsWith("//"))
    			  xmlPath=xmlPath+"/";
    		  expFile = xmlPath+expFile;
    		  String actFile = xmlPath+"actual_response.xml";
    		  TestUtil.writeToFile(actFile, actOutput);
    		  result = TestUtil.diffFiles(expFile,actFile);
    	  }
    	  
    	  return result;
      }
      
      public String testService(String object,String input,String output){
    	  
    	  if(input.startsWith("File")){
    		  
    	  }else{
    		  
    	  }
    	  
    	  return com.test.selenium.testscript.Constants.KEYWORD_PASS;
      }
      
      
      public String request(String method, String secure, String targetURL, String body, Properties param)
                throws IOException, MalformedURLException{
    	  		RestResponse  resp = new RestResponse();
				Hashtable<String,String> result =new Hashtable<String,String>();
				
            	//System.out.println("Starting with Request  \n");
                    URL url;
                        HttpsURLConnection connection1 = null;  
                        HttpURLConnection connection = null;
                        url = new URL(targetURL);
                        try {
                            //Create connection
							if (secure.equalsIgnoreCase("https"))
                            {
                                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                                    //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.research.ge.com", 80));
                                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-src.research.ge.com", 8080));
                                    
                                	connection1 = (HttpsURLConnection)url.openConnection(proxy);
                                    //connection1 = (HttpsURLConnection)url.openConnection();
                                    connection1.setHostnameVerifier(hostnameVerifier);
                                    connection = connection1;
                            }
                            if (secure.equalsIgnoreCase("http"))
                            {
                            	/*
                            	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.research.ge.com", 80));
                            	connection = (HttpURLConnection)url.openConnection(proxy);
                            	*/
                                connection = (HttpURLConnection)url.openConnection();
                            }

							try{
							Enumeration em = param.keys();
							while(em.hasMoreElements()){
									String str = (String)em.nextElement();
									System.out.println(str + ": " + param.get(str));
									connection.setRequestProperty(str,param.get(str).toString());
							}
							}catch(Exception e){
									System.out.println(e.getMessage());
							}
							
							connection.setDoInput(true);
							connection.setDoOutput(true);
							connection.setRequestMethod(method);
                      //Send request
                            if ((method.equalsIgnoreCase("POST"))|| (method.equalsIgnoreCase("PUT")))
							{
                            //System.out.println(body.toString());
                            DataOutputStream wr = new DataOutputStream (
                            connection.getOutputStream ());
							wr.writeBytes (body);
							wr.flush ();
							wr.close ();
                            }
                                  
                      //Get Response
                            resp.setStatusCode(connection.getResponseCode());      
                            System.out.println("Response Code "+connection.getResponseCode());
                      InputStream is = connection.getInputStream();
                      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                      String line;
                      StringBuffer response = new StringBuffer(); 
                      while((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                      }
                      rd.close();
                      return response.toString();

                    } catch (Exception e) {

                      e.printStackTrace();
                      return null;

                    } finally {

                      if(connection != null) {
                        connection.disconnect(); 
                      }
                    }
       }
      public RestResponse sendRequest(String method, String secure, String targetURL, String body, Properties param)
      			throws IOException, MalformedURLException  
      	{
			RestResponse  geResponse = new RestResponse();
			Hashtable<String,String> result =new Hashtable<String,String>();

			//System.out.println("Starting with Request  \n");
			URL url;
			HttpsURLConnection connection1 = null;  
			HttpURLConnection connection = null;
			url = new URL(targetURL);
			try {
			      //Create connection
				if (secure.equalsIgnoreCase("https")){
					HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                    //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.research.ge.com", 80));
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-src.research.ge.com", 8080));
                    connection1 = (HttpsURLConnection)url.openConnection(proxy);
                    connection1.setHostnameVerifier(hostnameVerifier);
                    connection = connection1;
				}else if(secure.equalsIgnoreCase("http")){
					connection = (HttpURLConnection)url.openConnection();
				}
                try{
                	Enumeration em = param.keys();
					while(em.hasMoreElements()){
							String str = (String)em.nextElement();
							System.out.println(str + ": " + param.get(str));
							connection.setRequestProperty(str,param.get(str).toString());
					}
                }catch(Exception e){
					System.out.println(e.getMessage());
				}
                connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod(method);
				//Send request
                if((method.equalsIgnoreCase("POST"))|| (method.equalsIgnoreCase("PUT"))){
                	 DataOutputStream wr = new DataOutputStream (
                     connection.getOutputStream ());
                	 wr.writeBytes (body);
                	 wr.flush ();
                	 wr.close ();
                }
                
                        
                //Get Response         
	            InputStream is = connection.getInputStream();
	            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	            String line;
	            StringBuffer response = new StringBuffer(); 
	            while((line = rd.readLine()) != null) {
	              response.append(line);
	              response.append('\r');
	            }
	            rd.close();
            
	            geResponse.setStatusCode(connection.getResponseCode());
	            
	             
	            for(int i=0;i< connection.getHeaderFields().size();i++){
		          	  Map<String, List<String>> header = connection.getHeaderFields();
		      		  Iterator keys = header.keySet().iterator();
		      		  StringBuilder builder = new StringBuilder();
		          	  while(keys.hasNext()){
		          		  Object key = keys.next();
		          		  if(key != null){
		          			  builder.append( key.toString()+ "=");
	                		  if(key != null){
	                			  List<String> values = header.get(key.toString());
	                			  for(String value: values){
	                				  builder.append(value + "\n");
	                				  result.put(key.toString(),value);
	                			  }
	                		  }
		          		  }
		          	  }
	          	 // System.out.println(builder.toString());
	            }
	            geResponse.setResponseHeaders(result);
	            geResponse.setResponse(response.toString());
	            
	            return geResponse;
          }catch (Exception e) {
            e.printStackTrace();
            return null;
          }finally{
            if(connection != null) {
              connection.disconnect(); 
            }
          }
        }
  }