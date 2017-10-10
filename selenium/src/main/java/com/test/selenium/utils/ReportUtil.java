package com.test.selenium.utils;



import com.test.selenium.testscript.TestResults;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.test.selenium.testscript.TestDriver.CONFIG;

public class ReportUtil {
	
	public static int scriptNumber=1;
	public static String indexResultFilename;
	public static String currentDir;
	public static String currentSuiteName;
	public static int tcid;
	//public static String currentSuitePath;
	
	public static double passNumber=0.0;
	public static double failNumber=0.0;
	public static double skipNumber=0.0;
	public static int passCount=0;
	public static int failCount=0;
	public static int skipCount=0;
	public static int totalCount=0;
	public static boolean newTest=true;
	public static ArrayList<String> description=new ArrayList<String>();;
	public static ArrayList<String> keyword=new ArrayList<String>();;
	public static ArrayList<String> teststatus=new ArrayList<String>();;
	public static ArrayList<String> screenShotPath=new ArrayList<String>();;
	public static ArrayList<String> dataSets=new ArrayList<String>();;
	public static List<TestResults> tcList = new ArrayList<TestResults>();
	public static ArrayList<String> usCoverage = new ArrayList<String>();
	public static String project = CONFIG.getProperty("project");
	
	public static void reportError(String filePath,String errMsg){
		String filename=filePath+"/index.html";
		indexResultFilename = filename;
		currentDir=filePath;
		FileWriter fstream =null;
		BufferedWriter out =null;
		try{
		    // Create file 
		   	  fstream = new FileWriter(filename);
		   	  out = new BufferedWriter(fstream);
		   	  String runDate = TestUtil.now("dd.MMMMM.yyyy").toString();
		   	  out.newLine();
			  out.write("<html>\n");
			  out.write("<HEAD>\n");
			  out.write(" <TITLE>Automation Test Results For "+project+"</TITLE>\n");
			  out.write("</HEAD>\n");
			  out.write("<body>\n");
			  out.write("<table cellspacing=0 cellpadding=0 width=100% ><tr width=100% bgcolor=COLORCODE ><td align=center><FONT COLOR=white FACE=Arial SIZE=2.5><h1 align='center'>"+errMsg+"</h1></td></tr></table>\n");
			  out.write("<FONT COLOR=white FACE=Arial SIZE=2.5><h1 align='center'>"+errMsg+"</h1>");
			  out.close();
	    }catch (Exception e){//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }finally{
	    	fstream=null;
		    out=null;
	    }
	}
	
	public static void startTesting(String filePath,String testStartTime,String environment,String release)
	  {
		//indexResultFilename = filename;
		String filename=filePath+"/index.html";
		indexResultFilename = filename;
		currentDir=filePath;
		//currentDir = indexResultFilename.substring(0,indexResultFilename.lastIndexOf("//"));
		
		FileWriter fstream =null;
		BufferedWriter out =null;
	      try{
	    // Create file 
	   
	    	  fstream = new FileWriter(filename);
	    	  out = new BufferedWriter(fstream);
	    	  String runDate = TestUtil.now("dd.MMMMM.yyyy").toString();
	    	  out.newLine();
			  out.write("<html>\n");
			  out.write("<HEAD>\n");
			  out.write(" <TITLE>Automation Test Results For "+project+"</TITLE>\n");
			  out.write("</HEAD>\n");
			  out.write("<body>\n");
			  out.write("<table cellspacing=0 cellpadding=0 width=100% ><tr width=100% bgcolor=COLORCODE ><td align=center><FONT COLOR=white FACE=Arial SIZE=2.5><h1 align='center'>Automation Test Results For "+project+"</h1></td></tr></table>\n");
			  //out.write("<h4 align=center bgcolor=green><FONT COLOR=660066 FACE=AriaL SIZE=6><b><u> Automation Test Results For "+project+"</u></b></h4>\n");
			  out.write("<table  border=1 cellspacing=1 cellpadding=1 >\n");
			  out.write("<tr>\n");
			  out.write("<br></br>");
			  out.write("<br></br>");
			  out.write("<p></p>");
			  out.write("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Details :</u></h4>\n");
	          out.write("<td width=250 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run Date</b></td>\n");
	          out.write("<td width=250 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+runDate+"</b></td>\n");
	          out.write("</tr>\n");
	          out.write("<tr>\n");
	          out.write("<td width=250 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Run StartTime</b></td>\n");
	          out.write("<td width=250 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+testStartTime+"</b></td>\n");
	          out.write("</tr>\n");
	          out.write("<tr>\n");
	          out.write("<td width=250 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Run EndTime</b></td>\n");
	          out.write("<td width=250 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>END_TIME</b></td>\n");
	          out.write("</tr>\n");
	          out.write("<tr>\n");
	          out.write("<td width=250 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Environment</b></td>\n");
	          out.write("<td width=250 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>"+environment+"</b></td>\n");
	          out.write("</tr>\n");
	          out.write("<tr>\n");
	          out.write("<td width=250 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Release</b></td>\n");
	          out.write("<td width=250 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>"+release+"</b></td>\n");
	          out.write("</tr>\n");
	          out.write("</table>\n");
	          out.write("<p></p>");
	      
	          out.write("<table cellspacing=1 cellpadding=1 >");
	          out.write("<tr>");
	          out.write("<td width=50%>");
	          out.write("<table cellspacing=1 cellpadding=1 ><tr><h4><FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Summary :</u></h4></tr><tr><td width=100 align=left>"
	        		  +"<FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Tests</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>TOTALTESTS</b></td>"
	        		  +"<td width='500' align='center'><div><span style='width:450px;float:left;background-color: white; width: 100%'></span>" 
	        		  +	"</div></td><td><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b></b></span></td></tr><tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75>"
	        		  +"<b>Total Pass</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>PASSCOUNT</b></td><td width='500'><div >"
	        		  +"<span style='width:450px;float:left;background-color: green; width: PASSNUMBER%'></span></div></td><td><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>PASSNUMBER%</b></span></td></tr>"
	        		  +"<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Pending</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>SKIPCOUNT</b></td>" +
      		  		  "<td width='500'><div><span style='width:450px;float:left;background-color: orange;width: SKIPNUMBER%'></span></div></td><td><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>SKIPNUMBER%</b></span></td></tr>"
	        		  +"<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Fail</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>FAILCOUNT</b></td>" +
	        		  		"<td width='500'><div><span style='width:450px;float:left;background-color: red;width: FAILNUMBER%'></span></div></td><td><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>FAILNUMBER%</b></span></td></tr></table>");
	          out.write("</td><td width=50%><table cellspacing=1 cellpadding=1 width=300 align='center'><tr><h4><FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Story Coverage :</u></h4></tr>COVERAGE</table></td></tr></table>\n");
	          //out.write("<div style='width:900px;'><span style='background-color: green; width: PASSNUMBER%'></span><span>PASSCNT</span></div><div><span style='background-color: red; width: FAILNUMBER%'><font color='white' FACE=Arial>FAILCOUNT</b></span></div>");
	          //Close the output stream
	          out.close();
	          fstream.close();
	    }catch (Exception e){//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }finally{
	    	fstream=null;
		    out=null;
	    }
	  }
	
    public static void startSuite(String suiteName,String runMode){

	    FileWriter fstream =null;
		BufferedWriter out =null;
		currentSuiteName = suiteName.replaceAll(" ", "_");
		tcid=1;
		scriptNumber=1;
	    try{
	    	// build the suite folder
	    //	currentSuitePath = currentDir; //+"//"+suiteName.replaceAll(" ","_");
	    //	currentSuiteDir = suiteName.replaceAll(" ","_");
	    //	File f = new File(currentSuitePath);
		//	f.mkdirs();
	   	    fstream = new FileWriter(indexResultFilename,true);
	   	    out = new BufferedWriter(fstream);
	   	    if(runMode.equals("Y")){
	   	    	out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>"+suiteName+" Report :</u></h4>\n");
	   	    	out.write("<table  border=1 cellspacing=1 cellpadding=1 width=100%>\n");
	   	    	out.write("<tr>\n");
	   	    	out.write("<td width=5%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Script#</b></td>\n");
	   	    	out.write("<td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case Name</b></td>\n");
	   	    	out.write("<td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Test Case Desc</b></td>\n");
	   	    	out.write("<td width=15% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Status</b></td>\n");
	   	    	out.write("<td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run Start Time</b></td>\n");
	   	    	out.write("<td width=20% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>Run End Time</b></td>\n");
	   	    	out.write("</tr>\n");
	   	    }else{
	   	    	//out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>"+suiteName+" Report :</u>         ****** SKIPPED ****** </h4>\n");
	   	    	out.write("<h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5> <u>"+suiteName+" Report :</u>         ------ SKIPPED ----- </h4>\n");
	   	    }
	   	    out.close();
	   	    fstream.close();
	    }catch(Exception e){
		      System.err.println("Error: " + e.getMessage());
	    }finally{
	    	fstream=null;
		    out=null;
	    }
	}
    
    public static void endSuite(){
    	FileWriter fstream =null;
 		BufferedWriter out =null;
 		
 	    try{
	 	    fstream = new FileWriter(indexResultFilename,true);
	 	  	out = new BufferedWriter(fstream);
	        out.write("</table>\n");
	        out.close();
	        fstream.close();
 	    }catch(Exception e){
 	    	System.err.println("Error: " + e.getMessage());
	    }finally{
	    	fstream=null;
		    out=null;
	    }
    }
	
	//public static void addTestCase(String testCaseName,String us,String testDesc, String testCaseStartTime,String testCaseEndTime,String status){
    //public static void addTestCase(String testCaseName,String us,String testDesc, String testCaseStartTime,String testCaseEndTime,String status,String isAuto){
    public static void addTestCase(String testCaseName,String us,String testDesc, String testCaseStartTime,String testCaseEndTime,String status,String isAuto, String priority){
		newTest=true;
		String tcName = testCaseName;
		FileWriter fstream=null;
		BufferedWriter out=null;
		String userStory = "";
		if(!tcName.equalsIgnoreCase("setup") && !tcName.equalsIgnoreCase("teardown")){
			if(!us.equals("")){
				userStory = us;
			}else{
				userStory = "unassigned";
			}
			addUS(userStory,status);
		}
		
		try {
			newTest=true;
			// build the keywords page
		   if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip")){
			   
		   }else{
				File f = new File(currentDir+"//"+currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html");
			   //File f = new File(currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html");
				f.createNewFile();
				fstream = new FileWriter(currentDir+"//"+currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html");
				//fstream = new FileWriter(currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html");
				out = new BufferedWriter(fstream);
				out.write("<html>");
				out.write("<head>");
				out.write("<title>");
				out.write(tcName + " Detailed Reports");
				out.write("</title>");
				out.write("</head>");
				out.write("<body>");
				out.write("<h4> <FONT COLOR=660000 FACE=Arial SIZE=4.5> Detailed Report for Test Case "+tcName+" :</h4>");
				out.write("<table  border=1 cellspacing=1    cellpadding=1 width=100%>");
				out.write("<tr> ");
		        out.write("<td align=center width=10%  align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Step/Row#</b></td>");
		        out.write("<td align=center width=50% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Description</b></td>");
		        out.write("<td align=center width=10% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Keyword</b></td>");
		        out.write("<td align=center width=15% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Result</b></td>");
		 		out.write("<td align=center width=15% align=center bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2><b>Screen Shot</b></td>");
		 		out.write("</tr>");
//		 		for(int i=0;i<keyword.size();i++){
//		 			System.out.println("path = "+screenShotPath.get(i));
//		 		}
		 		if(keyword!=null){
		 			//for(int i=0;i<description.size();i++){
		 			for(int i=0;i<keyword.size();i++){
		 				
		 				out.write("<tr> ");
		 				out.write("<td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>TS"+(i+1)+"</b></td>");
		 				out.write("<td align=center width=50%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+description.get(i)+"</b></td>");
		 				out.write("<td align=center width=10%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>"+keyword.get(i)+"</b></td>");
		 				if(teststatus.get(i).startsWith("Pass")){
		 					out.write("<td width=20% align= center  bgcolor=#BCE954><FONT COLOR=#153E7E FACE=Arial SIZE=2><b>"+teststatus.get(i)+"</b></td>\n");
		 				}				
		 				else if(teststatus.get(i).startsWith("Fail")){
		 					out.write("<td width=20% align= center  bgcolor=#F78181><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+teststatus.get(i)+"</b></td>\n");
		 				}
//		 				if(CONFIG.getProperty("screenshot_everystep").equalsIgnoreCase("y")){
//		 					out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b><a href='"+screenShotPath.get(i)+"' target=_blank>Screen Shot</a></b></td>");
//		 				}
		 				if(!teststatus.get(i).equalsIgnoreCase("pass") || CONFIG.getProperty("screenshot_everystep").equalsIgnoreCase("y")){
		 					if(!keyword.get(i).equals("openBrowser") && !keyword.get(i).equals("closeBrowser") && !keyword.get(i).equals("dataSetup") && !keyword.get(i).equals("testWebServices") && !keyword.get(i).equals("runJMeter")){
		 						//10/21
		 						//if(CONFIG.getProperty("screenshotPath").trim().contains(":")){
		 						if(CONFIG.getProperty("ResultsPath").trim().contains(":")){
		 							out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b><a href='"+screenShotPath.get(i)+"' target=_blank>Screen Shot</a></b></td>");
		 						}else{
		 							out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b><a href='../"+screenShotPath.get(i)+"' target=_blank>Screen Shot</a></b></td>");
		 						}
		 						
		 					}else{
		 						out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>&nbsp;</b></td>");
		 					}
		 				}else{
		 					out.write("<td align=center width=20%><FONT COLOR=#153E7E FACE=Arial SIZE=1><b>&nbsp;</b></td>");
		 				}
		 			  out.write("</tr>");
		 			}
		 		}
			 out.close();
		   }
			
			fstream = new FileWriter(indexResultFilename,true);
			out = new BufferedWriter(fstream);
			fstream = new FileWriter(indexResultFilename,true);
			out = new BufferedWriter(fstream);
			out.write("<tr>\n");
			
			out.write("<td width=5% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+scriptNumber+"</b></td>\n");
			
		    if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip")){
		    	out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+tcName+"</b></td>\n");
		    }else{
		    	//out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b><a href=file:///"+currentDir+"//"+currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html>"+tcName+"</a></b></td>\n");
		    	out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b><a href="+currentSuiteName+"_TC"+tcid+"_"+tcName.replaceAll(" ", "_")+".html>"+tcName+"</a></b></td>\n");
		    }
		    out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>&nbsp"+testDesc+"</b></td>\n");
		    tcid++;
		    
		    	if(status.equalsIgnoreCase("pass")){
			    	out.write("<td width=15% align= center  bgcolor=#BCE954><FONT COLOR=#153E7E FACE=Arial SIZE=2><b>"+status+"</b></td>\n");
			    	if(!testCaseName.equalsIgnoreCase("setup") && !testCaseName.equalsIgnoreCase("teardown")){
			    		passCount = passCount+1;
				    	//TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto);
			    		TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto, priority);
					    tcList.add(ts);
			    	}
			    	
			    }else if(status.startsWith("Fail")){
			    	out.write("<td width=15% align= center  bgcolor=#F78181><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+status+"</b></td>\n");
			    	if(!testCaseName.equalsIgnoreCase("setup") && !testCaseName.equalsIgnoreCase("teardown")){
			    		failCount = failCount+1;
				    	//TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto);
			    		TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto,priority);
					    tcList.add(ts);
			    	}
			    	
			    //}else if(status.equalsIgnoreCase("Skipped") || status.equalsIgnoreCase("Skip")){
			    }else {
			    	out.write("<td width=15% align= center  bgcolor=yellow><FONT COLOR=153E7E FACE=Arial SIZE=2><b>"+status+"</b></td>\n");
			    	if(!testCaseName.equalsIgnoreCase("setup") && !testCaseName.equalsIgnoreCase("teardown")){
			    		skipCount = skipCount+1;
				    	//TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto);
			    		TestResults ts = new TestResults(currentSuiteName,tcName,testDesc,userStory,testCaseStartTime,testCaseEndTime,status,isAuto,priority);
					    tcList.add(ts);
			    	}
			    }
		    	
		    
		    out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseStartTime+"</b></td>\n");
		    out.write("<td width=20% align= center ><FONT COLOR=#153E7E FACE= Arial  SIZE=2><b>"+testCaseEndTime+"</b></td>\n");
		    out.write("</tr>\n");
		    scriptNumber++;
		    
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}finally{
			try {
					out.close();
					fstream.close();
					out = null;
					fstream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		description= new ArrayList<String>();
		keyword= new ArrayList<String>();
		teststatus= new ArrayList<String>();
		screenShotPath = new ArrayList<String>();
		dataSets = new ArrayList<String>();
		newTest=false;
		
	}
	
	
	
	
	public static void addKeyword(String desc,String key,String stat,String path,String data){
		
		description.add(desc);
		keyword.add(key);
		teststatus.add(stat);
		screenShotPath.add(path);
		dataSets.add(data);
	}

	public static void updateEndTime(String endTime) {
		StringBuffer buf = new StringBuffer();
		StringBuffer content = new StringBuffer();
		String tableRow = "";
		String tableRow1 = "";
		String tmp="";
		String tmp1="";
		String color="";
		double passCov=0.0;
		double failCov=0.0;
		double tbdCov=0.0;
		//int totalCov=0;
		int tbdCount=0;
		totalCount = passCount+failCount+skipCount;
		if(totalCount!=0){
			passNumber = 100*passCount/totalCount;
			failNumber = 100*failCount/totalCount;
			skipNumber = 100*skipCount/totalCount;
		}
		String row = "<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>USNAME</b></td><td width=30 align=center><span><FONT COLOR=#4B8A08 FACE=Arial SIZE=2.75><b>COVERAGE%</b></span></td>" +
				     "<td width='500' align='center'><div ><span style='width:450px;float:left;background-color: green; width: COVERAGE%'></span><span style='width:450px;float:left;background-color: red; width: FAIL%'></span>" +
				     "<span style='width:450px;float:left;background-color: orange; width: TBD%'></span></div></td></tr>";
		String row1 = "<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>USNAME</b></td><td width=30 align=center><span><FONT COLOR=#4B8A08 FACE=Arial SIZE=2.75><b>COVERAGE%</b></span></td></tr>";
		if(failCount>0){
			color="red";
		}else{
			color="green";
		}
		String tc = "<table width=100% ><tr bgcolor="+color+" align='center'><FONT COLOR=white FACE=Arial SIZE=2.5><h3>Automation Test Results For "+project+"</h3></tr></table><table cellspacing=1 cellpadding=1  border=1 width=300><tr><tr><h4><FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Test Summary :</u></h4></tr><tr><td width=100 align=left>"
      		  +"<FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Tests</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(totalCount)+"</b></td>"
      		  +"<td><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b></b></span></td></tr><tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75>"
      		  +"<b>Total Pass</b></td><td  width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(passCount)+"</b></td>"
      		  +"<td bgcolor=green><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(passNumber)+"%</b></span></td></tr>"
      		+"<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Pending</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(skipCount)+"</b></td>" +
		  		"<td bgcolor=orange><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(skipNumber)+"%</b></span></td></tr>"
      		  +"<tr><td width=100 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>Total Fail</b></td><td width=50 align='center'><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(failCount)+"</b></td>" +
      		  		"<td bgcolor=red><span><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>"+String.valueOf(failNumber)+"%</b></span></td></tr></table>";
        
        
        
		for(int i=0;i<usCoverage.size();i=i+4){
			int totalCov=0;
			int pc = Integer.parseInt(usCoverage.get(i+1));
			int fc = Integer.parseInt(usCoverage.get(i+2));
			int sc = Integer.parseInt(usCoverage.get(i+3));
			//totalCov = Integer.parseInt(usCoverage.get(i+1))+Integer.parseInt(usCoverage.get(i+2)+Integer.parseInt(usCoverage.get(i+3)));
			totalCov = pc+fc+sc;
			if(totalCov<2){
				//if((Integer.parseInt(usCoverage.get(i+1))+Integer.parseInt(usCoverage.get(i+2))!=0)){
				if((pc+fc)!=0){
					tbdCount = 2-totalCov;
					totalCov = 2;
				}else{
					tbdCount = 2;
					totalCov = 2;
				}
			}else{
				//tbdCount = Integer.parseInt(usCoverage.get(i+3));
				tbdCount = sc;
			}
			passCov = 100*Integer.parseInt(usCoverage.get(i+1))/totalCov;
			failCov = 100*Integer.parseInt(usCoverage.get(i+2))/totalCov;
			tbdCov = 100*tbdCount/totalCov;
			tmp=row.replace("USNAME", usCoverage.get(i)).replaceAll("COVERAGE", String.valueOf(passCov)).replace("FAIL", String.valueOf(failCov)).replace("TBD", String.valueOf(tbdCov));
			tmp1=row1.replace("USNAME", usCoverage.get(i)).replaceAll("COVERAGE", String.valueOf(passCov));
			tableRow=tableRow+tmp;
			tableRow1=tableRow1+tmp1;
		}
		String uc="<table cellspacing=1 cellpadding=1 border=1 width=300 ><tr><h4><FONT COLOR=660000 FACE=Arial SIZE=4.5> <u>Story Coverage :</u></h4></tr>"+tableRow1+"</table></td></tr></table>\n";
		try{
			    // Open the file that is the first 
			    // command line parameter
			    FileInputStream fstream = new FileInputStream(indexResultFilename);
			    // Get the object of DataInputStream
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    String strLine;
			    
			    //Read File Line By Line
			    while ((strLine = br.readLine()) != null)   {
			    	if(strLine.indexOf("END_TIME") !=-1){
			    		strLine=strLine.replace("END_TIME", endTime);
			    	}
			    	if(strLine.indexOf("COLORCODE") !=-1){
			    		strLine=strLine.replace("COLORCODE", color);
			    	}
			    	if(strLine.indexOf("TOTALTESTS") !=-1){
			    		strLine=strLine.replace("TOTALTESTS", String.valueOf(totalCount));
			    		strLine=strLine.replace("PASSNUMBER", String.valueOf(passNumber));
			    		strLine=strLine.replace("PASSCOUNT", String.valueOf(passCount));
			    		strLine=strLine.replace("FAILNUMBER", String.valueOf(failNumber));
			    		strLine=strLine.replace("FAILCOUNT", String.valueOf(failCount));
			    		strLine=strLine.replace("SKIPNUMBER", String.valueOf(skipNumber));
			    		strLine=strLine.replace("SKIPCOUNT", String.valueOf(skipCount));
			    		//content.append(strLine);
			    	}
			    	if(strLine.indexOf("COVERAGE") !=-1){
			    		strLine=strLine.replace("COVERAGE", tableRow);
			    		content.append(strLine);
			    	}
			    	
			    	buf.append(strLine);
			    }
			    //Close the input stream
			    in.close();
			    br.close();
			    //System.out.println(buf);
			    FileOutputStream fos=new FileOutputStream(indexResultFilename);
				DataOutputStream   output = new DataOutputStream (fos);	 
		    	output.writeBytes(buf.toString());
		    	output.close();
		    	fos.close();
		    	fstream.close();
		    	
		    }catch (Exception e){//Catch exception if any
		    	System.err.println("Error: " + e.getMessage());
		    }
		    if(CONFIG.getProperty("persistResults")!= null ){
		    	if(CONFIG.getProperty("persistResults").equalsIgnoreCase("y")){
		    		insertResults();
		    	}
		    }	
		    String[] to;
		    if(CONFIG.getProperty("mailList")!=null){
		    	to =CONFIG.getProperty("mailList").split(",");
		    }else{
		    	to = new String[]{"hr@hr.com"};
		    }
		    
		    sendMail(to,tc+uc,"Automation test Reports For "+project);
		    //TestUtil.zip(CONFIG.getProperty("ResultsPath").trim());
	}
	
	public static void insertResults(){
		//String DB_CONNECTION = "jdbc:oracle:thin:@3.39.73.57:1521:DEEVSEDB";
		String DB_CONNECTION = "jdbc:oracle:thin:@3.39.73.57:1534:DEEVSEDB";
		int count=0;
		int runId=0;
		String envName="CI";
		String testType="FUNC";
		String query="";
		String tcQuery="";
		
		String automated="";
		String priority="";
		//String project=CONFIG.getProperty("project");
		int buildNo = 1;
        try {
        	Connection connection = null;
        	Statement statement = null;
        	Statement tcStatement = null;
        	try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(DB_CONNECTION,"sreddy","D1g1talEnergy");
                statement = connection.createStatement();
                tcStatement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select max(run_id) from TEST_RESULTS");
     			while (rs.next()) {
     				count = rs.getInt(1);
     			}
     			runId=count+1;
     			//to_timestamp('"+stts+"','yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp('"+endts+"','yyyy-mm-dd hh24:mi:ss.ff')
                for (TestResults tr: tcList) {
                	String tcName="";
//                	Date sttm = DateUtil.convertToDate(tr.getStartTime());
//                	sttm = DateUtil.utilDateToSqlTimestamp(sttm);
//                	Date endtm = DateUtil.convertToDate(tr.getEndTime());
//                	endtm = DateUtil.utilDateToSqlTimestamp(sttm);
                	//String query = "insert into TEST_RESULTS (run_id,env_name,build_number,test_type,test_suite,tc_name,tc_desc,us_number,tc_start_dtm,tc_end_dtm,tc_status,comments,project)"+ 
                    //"values("+runId+",'"+envName+"',"+buildNo+",'"+testType+"','"+tr.getSuite()+"','"+tr.getTCName()+"','"+tr.getTCDesc()+"','"+tr.getUS()+"',to_timestamp('"+sttm+"','yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp('"+endtm+"','yyyy-mm-dd hh24:mi:ss.ff'),'"+tr.getStatus()+"','"+tr.getStatus()+"','"+project+"')";
                	//String query = "insert into TEST_RESULTS (run_id,env_name,build_number,test_type,test_suite,tc_name,tc_desc,us_number,tc_start_dtm,tc_end_dtm,tc_status,comments,project)"+ 
                    //"values("+runId+",'"+envName+"',"+buildNo+",'"+testType+"','"+tr.getSuite()+"','"+tr.getTCName()+"','"+tr.getTCDesc()+"','"+tr.getUS()+"',to_timestamp('"+tr.getStartTime()+"','dd.MMMMM.yyyy hh.mm.ss aaa'), to_timestamp('"+tr.getEndTime()+"','dd.MMMMM.yyyy hh.mm.ss aaa'),'"+tr.getStatus()+"','"+tr.getStatus()+"','"+project+"')";
                	String[] us = tr.getUS().split(",");
                	for(String story : us){
                		//code here to insert test cases 03/19/14
                		//rs = tcStatement.executeQuery("select count(*) from test_cases where tc_name='"+tr.getTCName()+"' and us_number='"+story+"'");
                		rs = tcStatement.executeQuery("select tc_name,is_automated,priority from test_cases where tc_name='"+tr.getTCName()+"' and us_number='"+story+"'");
//             			while (rs.next()) {
//             				count = rs.getInt(1);
//             			}
             			while (rs.next()) {
             				tcName = rs.getString(1);
             				if(tcName == null)
             					tcName="";
             				automated = rs.getString(2);
             				if(automated == null )
             					automated = "";
             				priority = rs.getString(3);
             				if(priority == null )
             					priority = "";
             			}
             			//if(count == 0){
             			if(tcName.equals("")){
             				//to_timestamp('"+start+"','yyyy-mm-dd')
             				//String date = TestUtil.now("dd.MMM.yyyy").toString();
             				String date = TestUtil.now("yyyy-MM-dd").toString();
             				//TestUtil.now("dd.MMMMM.yyyy").toString()
             				System.out.println(" NOW " +date);
//             				tcQuery = "insert into TEST_CASES (US_NUMBER,tc_desc,is_automated,tc_name,project,priority)"+ 
//                            "values('"+story+"','"+tr.getTCDesc().replaceAll(","," ").replaceAll("'","")+"','"+tr.isAutomated()+"','"+tr.getTCName()+"','"+project+"','"+tr.getPriority()+"')";
             				tcQuery = "insert into TEST_CASES (US_NUMBER,tc_desc,is_automated,tc_name,project,priority,date_created)"+ 
                            "values('"+story+"','"+tr.getTCDesc().replaceAll(","," ").replaceAll("'","")+"','"+tr.isAutomated()+"','"+tr.getTCName()+"','"+project+"','"+tr.getPriority()+"',to_timestamp('"+date+"','yyyy-mm-dd'))";
             				statement.addBatch(tcQuery);
             				
             			}else if(!automated.equals(tr.isAutomated())){
             				tcQuery = "update test_cases set is_automated='"+tr.isAutomated()+"' where tc_name='"+tr.getTCName()+"' and us_number='"+story+"'";
             				statement.addBatch(tcQuery);
             			}else if(!priority.equals(tr.getPriority())){
             				tcQuery = "update test_cases set priority='"+tr.getPriority()+"' where tc_name='"+tr.getTCName()+"' and us_number='"+story+"'";
             				statement.addBatch(tcQuery);
             			}
                		query = "insert into TEST_RESULTS (run_id,env_name,build_number,test_type,test_suite,tc_name,tc_desc,us_number,tc_start_dtm,tc_end_dtm,tc_status,comments,project,isautomated)"+ 
                        "values("+runId+",'"+envName+"',"+buildNo+",'"+testType+"','"+tr.getSuite()+"','"+tr.getTCName()+"','"+tr.getTCDesc().replaceAll(","," ").replaceAll("'","")+"','"+story+"','"+tr.getStartTime()+"', '"+tr.getEndTime()+"','"+tr.getStatus()+"','"+tr.getStatus()+"','"+project+"','"+tr.isAutomated()+"')";
                		statement.addBatch(query);
                	}
                	//query = "insert into TEST_RESULTS (run_id,env_name,build_number,test_type,test_suite,tc_name,tc_desc,us_number,tc_start_dtm,tc_end_dtm,tc_status,comments,project,isautomated)"+ 
                    //"values("+runId+",'"+envName+"',"+buildNo+",'"+testType+"','"+tr.getSuite()+"','"+tr.getTCName()+"','"+tr.getTCDesc().replaceAll(","," ").replaceAll("'","")+"','"+tr.getUS()+"','"+tr.getStartTime()+"', '"+tr.getEndTime()+"','"+tr.getStatus()+"','"+tr.getStatus()+"','"+project+"','"+tr.isAutomated()+"')";
                    //statement.addBatch(query);
                }
                statement.executeBatch();
                //tcStatement.executeBatch();
            
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
            	if (null != statement)
            		statement.close();
            		tcStatement.close();
                if (null != connection)
                	connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static void addUS(String name,String status) {
		int fail = 0;
		int pass = 0;
		int tbd = 0;
		if(usCoverage.contains(name)){
			int index = usCoverage.indexOf(name);
			if(status.startsWith("Fail")){
				fail=Integer.parseInt(usCoverage.get(index+2))+1;
				usCoverage.set(index+2, String.valueOf(fail));
			}else if(status.startsWith("Pass")){
				pass=Integer.parseInt(usCoverage.get(index+1))+1;
				usCoverage.set(index+1, String.valueOf(pass));
			}else{
				tbd=Integer.parseInt(usCoverage.get(index+3))+1;
				usCoverage.set(index+3, String.valueOf(tbd));
			}
		}else{
			if(status.startsWith("Fail")){
				fail = fail+1;
			}else if(status.startsWith("Pass")){
				pass = pass+1;
			}else{
				tbd = tbd+1;
			}
			usCoverage.add(name);
			usCoverage.add(String.valueOf(pass));
			usCoverage.add(String.valueOf(fail));
			usCoverage.add(String.valueOf(tbd));
		}
	}
	
	public static void sendMail(String[] to,String content,String subject){
		Properties props = new Properties();
		props.put("mail.user", "pradeep.aerram@ge.com");
		//props.put("mail.host", "3.159.244.6");
		//props.put("mail.host", "3.159.19.190");
		//props.put("mail.host", "e2ksmtp01.e2k.ad.ge.com");
		props.put("mail.host", "mail.ad.ge.com");
		props.put("mail.port", "25");
		//props.put("mail.port", "587");
		//props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "3.159.244.6");
//		props.put("mail.smtp.port", "25");
//		props.put("mail.smtp.auth", "true");
		//props.put("mail.port", "587");
		//props.put("mail.smtp.starttls.enable", "true");
		try{
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage msg = new MimeMessage(session);
			msg.setText(content);
            //msg.setSubject("Automation test Reports For "+project);
			msg.setSubject(subject);
            msg.setFrom(new InternetAddress("pradeep.aerram@ge.com"));
            msg.setContent(content,"text/html" );
            for(int i=0;i<to.length;i++){
            	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            }
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(msg, msg.getAllRecipients());
            //Transport.connect();
            //transport.connect(host, userName, passWord);
            //Transport.send(msg, msg.getAllRecipients());
            //Transport.sendMessage(msg, msg.getAllRecipients());
            //Transport.send(msg);
            transport.close();
		}catch(MessagingException mex){
			mex.printStackTrace();
		}
	}
	public static void sendMail1(String[] to,String content,String subject){
		try {
            Session m_Session;
            Message m_simpleMessage;
            InternetAddress m_fromAddress;
            InternetAddress m_toAddress;
            Properties m_properties;

            m_properties     = new Properties();
            m_properties.put("mail.smtp.host", "AlphaUR.e2k.ad.hr.com");
            m_properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            m_properties.put("mail.smtp.starttls.enable", "true");
            m_properties.put("mail.smtp.auth", "false");
            m_properties.put("mail.smtp.port", "25");

            m_Session=Session.getDefaultInstance(m_properties,new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("204070237","Pandup0tt1@suvi"); // username and the password
                }
            });

            m_simpleMessage  =   new MimeMessage(m_Session);
            m_fromAddress    =   new InternetAddress("hr@hr.com");
            m_toAddress      =   new InternetAddress("hr@hr.com");

            m_simpleMessage.setFrom(m_fromAddress);
            m_simpleMessage.setRecipient(RecipientType.TO, m_toAddress);
            m_simpleMessage.setSubject(subject);

            m_simpleMessage.setContent(content, "text/html");

            //m_simpleMessage.setContent(m_body,"text/plain");

            Transport.send(m_simpleMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
	}
}
