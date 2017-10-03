package com.test.selenium.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.xml.sax.SAXException;

import com.ge.selenium.testscript.Constants;



public class TestUtil{

	final static String encoding = "UTF-8";
	// returns current date and time
	public static String now(String dateFormat) {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    return sdf.format(cal.getTime());

	}
	
	// store screenshots
	/*
	public static void takeScreenShot(String filePath) {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    try {
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	   
	    
	}
	*/
	// make zip of reports
	public static void zip(String filepath){
		String ts = TestUtil.now("dd.MMM.yyyy_hh_mm_ss");
		ZipOutputStream out = null;
		File inFolder = null;
		File outFolder = null;
		String[] files = null;
		BufferedInputStream in = null;
	 	try
	 	{
	 		inFolder=new File(filepath);
	 		outFolder=new File("Reports"+ts+".zip");
	 		out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
	 		
	 		byte[] data  = new byte[1000];
	 		files = inFolder.list();
	 		System.out.println("zip size "+files.length);
	 		
	 		for (int i=0; i<files.length; i++)
	 		{
	 			in = new BufferedInputStream(new FileInputStream
	 			(inFolder.getPath() + "/" + files[i]), 1000);  
	 			out.putNextEntry(new ZipEntry(files[i])); 
	 			int count;
	 			while((count = in.read(data,0,1000)) != -1)
	 			{
	 				out.write(data, 0, count);
	 			}
	 			in.close();
	 		}
	 		out.closeEntry();
	 		out.flush();
	 		out.close();
	 		
	 	}
	 	catch(Exception e)
	 	{
	 		e.printStackTrace();
	 	}finally{
			try {
				out.flush();
		 		out.close();
		 		inFolder = null;
		 		in.close();
		 		in = null;
		 	} catch (IOException e) {
				e.printStackTrace();
			}
	}
	 	
	 	
	}
	
	public static void deleteFiles(String filepath){
		String[] files = null;
		File inFolder = null;
		try
	 	{
	 		inFolder=new File(filepath);
	 		files = inFolder.list();
	 		for (int i=0; i<files.length; i++)
	 		{
	 			File myFile = new File(inFolder, files[i]);   
	            myFile.delete();
	            System.out.println("zip size ");
	 		}
	 	}catch(Exception e){
		 		e.printStackTrace();
		}
	}
	
	public static void checkDir(String filepath) throws IOException{
		File theDir = new File(filepath);
		  // if the directory does not exist, create it
		  if (!theDir.exists())
		  {
		    try{
		    	boolean result = theDir.mkdir();
		    	theDir.setWritable(true);
			    if(!result){    
			       System.out.println("Faile to Create DIR");
			     }
		    }catch(Exception e){
		    	System.out.println("ERR "+e.getMessage());
		    }
		  }
	 }
	public static String[] getArray(String data, String sparator) {
	    if (data.equals(""))
	        return null;
		return data.split(sparator);
	}

	public static String[] getArray(String data) {
	    return getArray(data, ",");
	}


	public static List<String> getList(String data, String sparator) {
	    if (data.equals(""))
	        return new ArrayList<String>();
		String[] str = data.split(sparator);
		List<String> lst = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			lst.add(str[i].trim());
		}
		return lst;
	}

	public static List<String> getList(String data) {
		return getList(data, ",");
	}

	public static List<String> getList(String[] data) {
		List<String> lst = new ArrayList<String>();
		//for (int i = 0; i < data.length; i++) {
		for (int i = 0; i < 3; i++) {
			lst.add(data[i].trim());
		}
		return lst;
	}
	
	public static String getListAsString(List<String> list){
		String str="";
		for(String s:list){
			str=str+","+s;
		}
		if(str.length()>0)
			str = str.substring(1, str.length());
		return str;
	}
	
	public static String verifyResults(HashMap<String, ArrayList<String>> exp, HashMap<String, ArrayList<String>> act){
  	  for(String k : exp.keySet()){
	        	ArrayList<String> expData = exp.get(k);
	        	ArrayList<String> actData = act.get(k);
//	        	for(String s:expData){
//	        		if(!actData.contains(s)){
//	        			return Constants.KEYWORD_FAIL+" Data Did not match for "+s;
//	        		}
//		        }
	        	if(!new HashSet(expData).equals(new HashSet(actData))){
	        		String tmp="";
	        		String tmp1="";
	        		for(String s: expData){
	        			tmp = tmp+s+",";
	        		}
	        		for(String s: actData){
	        			tmp1 = tmp1+s+",";
	        		}
	        		return Constants.KEYWORD_FAIL+" Exp Data "+tmp+" Did not match with Act Data " +tmp1;
	        	}
//	        	for(int i=0;i<expData.size();i++){
//	        		if(!expData.get(i).equals(actData.get(i))){
//	        			return Constants.KEYWORD_FAIL+" Exp Data "+expData.get(i)+" Did not match with Act Data " +actData.get(i);
//	        		}
//	        	}
	        }
  	  return Constants.KEYWORD_PASS;
    }
	public static void writeToFile(String fileName,String data){
		try{
			File responseFile = new File(fileName);    
		    responseFile.createNewFile();    
		    OutputStreamWriter oSW = new OutputStreamWriter
		    (new FileOutputStream(responseFile),encoding); 
		    oSW.write(data);
		    oSW.flush();    
		    oSW.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String diffFiles(String expFile,String actFile){
		Diff xmlDiff = null;
		Reader expectedXMLReader = null;
		Reader actualXMLReader = null;
		try{
			expectedXMLReader = new InputStreamReader(new FileInputStream(expFile), encoding);    
		    actualXMLReader = new InputStreamReader(new FileInputStream(actFile), encoding);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Ignore the white space while comparing    
	    XMLUnit.setIgnoreWhitespace(true);
	    try {
			xmlDiff = new Diff(expectedXMLReader, actualXMLReader);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DetailedDiff myDiff = new DetailedDiff(xmlDiff);
	    List allDifferences = myDiff.getAllDifferences();
	    for(int i=0;i<allDifferences.size();i++){
	    	System.out.println("diff "+allDifferences.get(i).toString());
	    }
	    if(xmlDiff.identical()){
	    	return Constants.KEYWORD_PASS;
	    }else{
	    	return Constants.KEYWORD_FAIL+" Actual and Expected Files are not same";
	    }
	    //XMLAssert.assertXMLEqual(xmlDiff, true);
		
	}
	
}
