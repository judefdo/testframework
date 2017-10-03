package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.test.selenium.testscript.*;
import com.test.selenium.testscript.Constants;
import com.test.selenium.testscript.TestSteps;
import com.test.selenium.utils.Xls_Reader;

public class SuiteHelper {
	
	public List<com.test.selenium.testscript.TestSteps> tsList;
	
	public void start(){
		
		
		
	}
	public SuiteHelper(){
		
	}
	public SuiteHelper(Xls_Reader suiteXLS){
		this.tsList = getSteps(suiteXLS);
	}
	
	/*
	public HashMap<String,String> getSuite(Xls_Reader suiteXLS){
		String currentTestSuite;
		String runMode;
		HashMap<String,String> suiteMap = new HashMap<String,String>();
		for(int currentSuiteID=2;currentSuiteID<=suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET);currentSuiteID++){
			currentTestSuite=suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID, currentSuiteID);
			runMode=suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID);
			suiteMap.put(currentTestSuite, runMode);
		}
		
		return suiteMap;
	}
	*/
	
	public LinkedHashMap<String,String> getSuite(Xls_Reader suiteXLS){
		String currentTestSuite;
		String runMode;
		LinkedHashMap<String,String> suiteMap = new LinkedHashMap<String,String>();
		for(int currentSuiteID = 2; currentSuiteID<=suiteXLS.getRowCount(com.test.selenium.testscript.Constants.TEST_SUITE_SHEET); currentSuiteID++){
			currentTestSuite=suiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_SUITE_SHEET, com.test.selenium.testscript.Constants.Test_Suite_ID, currentSuiteID);
			runMode=suiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_SUITE_SHEET, com.test.selenium.testscript.Constants.RUNMODE, currentSuiteID);
			suiteMap.put(currentTestSuite, runMode);
		}
		
		return suiteMap;
	}
	/*
	public LinkedHashMap<String,String> getTC(Xls_Reader currentTestSuiteXLS){
		String currentTestCaseName;
		String runMode;
		LinkedHashMap<String,String> tcMap = new LinkedHashMap<String,String>();
		for(int currentTestCaseID=2;currentTestCaseID<=currentTestSuiteXLS.getRowCount("Test Cases");currentTestCaseID++){
			currentTestCaseName=currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestCaseID);;
			runMode=currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID);
			//System.out.println("runmode -- "+runMode);
			tcMap.put(currentTestCaseName, runMode);
		}
		
		return tcMap;
	}
	*/
	
	public List<com.test.selenium.testscript.TestCase> getTC(Xls_Reader currentTestSuiteXLS){
		String currentTestCaseName;
		String currentUS;
		String runMode;
		String currentTestCaseDesc;
		String dependency;
		String status;
		String priority;
		List<com.test.selenium.testscript.TestCase> tcList = new ArrayList<com.test.selenium.testscript.TestCase>();
		
		for(int currentTestCaseID=2;currentTestCaseID<=currentTestSuiteXLS.getRowCount("Test Cases");currentTestCaseID++){
			currentTestCaseName=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, com.test.selenium.testscript.Constants.TCID, currentTestCaseID).trim();
			currentUS=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, "USID", currentTestCaseID).trim();
			currentTestCaseDesc=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, "Description", currentTestCaseID).trim();
			runMode=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, com.test.selenium.testscript.Constants.RUNMODE, currentTestCaseID).trim();
			dependency=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, "Dependency", currentTestCaseID).trim();
			//1/24/14 change here
			status=currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, "Status", currentTestCaseID).trim();
			//3/21/14 -- added priority
			priority = currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_CASES_SHEET, "Priority", currentTestCaseID).trim();
			//if(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Priority", currentTestCaseID) == ""){
			if(priority == "" || priority == null){
				priority = "P1";
			}
			//TestCase tc = new TestCase(currentTestCaseName,currentUS,currentTestCaseDesc,runMode, dependency);
			//3/21/14 -- added priority
			//TestCase tc = new TestCase(currentTestCaseName,currentUS,currentTestCaseDesc,runMode, dependency, status);
			com.test.selenium.testscript.TestCase tc = new com.test.selenium.testscript.TestCase(currentTestCaseName,currentUS,currentTestCaseDesc,runMode, dependency, status,priority);
			tcList.add(tc);
		}
		
		return tcList;
	}
	
	public List<String> getDataSet(Xls_Reader currentTestSuiteXLS,String currentTestCaseName){
		String runMode;
		List<String> dataSet = new ArrayList<String>();
		for(int currentTestDataSetID=2;currentTestDataSetID<=currentTestSuiteXLS.getRowCount(currentTestCaseName);currentTestDataSetID++){
			runMode=currentTestSuiteXLS.getCellData(currentTestCaseName, com.test.selenium.testscript.Constants.RUNMODE, currentTestDataSetID).trim();
			dataSet.add(runMode);
		}
		
		return dataSet;
	}
	
	public ArrayList<ArrayList<String>> getTestSteps(Xls_Reader currentTestSuiteXLS,String currentTestCaseName){
		ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> keyword = new ArrayList<String>();
		ArrayList<String> object = new ArrayList<String>();
		for(int currentTestStepID = 2; currentTestStepID<=currentTestSuiteXLS.getRowCount(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET); currentTestStepID++){
			ArrayList<String> data = new ArrayList<String>();
			if(currentTestCaseName.equals(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.TCID, currentTestStepID))){
				
				/*
				 * 
				data.add(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,currentTestStepID));
				object.add(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,currentTestStepID));
				keyword.add(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, currentTestStepID));
				*/
				data.add(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.KEYWORD, currentTestStepID).trim());
				data.add(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.OBJECT,currentTestStepID).trim());
				data.add(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.DATA,currentTestStepID));
				
				
			}
			testSteps.add(data);
		}
		
		//testSteps.add(keyword);
		//testSteps.add(object);
		
		
		return testSteps;
	}
	
	
	public void readTestSteps(Xls_Reader currentTestSuiteXLS){
		this.tsList = getSteps(currentTestSuiteXLS);
	}
	public List<com.test.selenium.testscript.TestSteps> getSteps(Xls_Reader currentTestSuiteXLS){
		String tmp="";
		List<com.test.selenium.testscript.TestSteps> tsList = new ArrayList<com.test.selenium.testscript.TestSteps>();
		com.test.selenium.testscript.TestSteps ts = null;
		//TestCase tt = new TestCase();
		//String tc = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, 2);
		String tc="";
		for(int i = 2; i<=currentTestSuiteXLS.getRowCount(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET); i++){
			if(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.TCID, i).equalsIgnoreCase(tc)){
				ts.addTCDesc(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.DESC,i).trim());
				ts.addKeyWord(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.KEYWORD,i).trim());
				ts.addObject(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.OBJECT,i).trim());
				ts.addData(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.DATA,i));
				ts.addExecFlag(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, "Proceed_on_Fail",i).trim());
			}else{
				if(i!=2){
					tsList.add(ts);
				}
				
				tc = currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.TCID, i);
				ts = new com.test.selenium.testscript.TestSteps(tc);
				ts.addTCDesc(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.DESC,i).trim());
				ts.addKeyWord(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.KEYWORD,i).trim());
				ts.addObject(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.OBJECT,i).trim());
				ts.addData(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, com.test.selenium.testscript.Constants.DATA,i));
				ts.addExecFlag(currentTestSuiteXLS.getCellData(com.test.selenium.testscript.Constants.TEST_STEPS_SHEET, "Proceed_on_Fail",i).trim());
			}
		}
		tsList.add(ts);
		return tsList;
	}
	
	public com.test.selenium.testscript.TestSteps getTCSteps(String tcName){
		com.test.selenium.testscript.TestSteps ts = null;
		//List<TestSteps> tsList = getSteps(currentTestSuiteXLS);
		for(TestSteps t:tsList){
			if(t.getTCName().equals(tcName)){
				return t;
			}
		}
		return ts;
	}
	
	/* to read jMeter results file */
	public ArrayList<ArrayList<String>> getJMeterResults(Xls_Reader jmeterResults, String resSheet){
		ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();
		String result="";
		for(int currentTestStepID=2;currentTestStepID<=jmeterResults.getRowCount(resSheet);currentTestStepID++){
			ArrayList<String> data = new ArrayList<String>();
			data.add(jmeterResults.getCellData(resSheet, com.test.selenium.testscript.Constants.TESTCASE, currentTestStepID).trim());
			data.add(jmeterResults.getCellData(resSheet, com.test.selenium.testscript.Constants.RESPONSECODE,currentTestStepID).trim());
			data.add(jmeterResults.getCellData(resSheet, com.test.selenium.testscript.Constants.RESPONSEMSG,currentTestStepID).trim());
			data.add(jmeterResults.getCellData(resSheet, com.test.selenium.testscript.Constants.USERSTORY,currentTestStepID).trim().split(" ")[0]);
			if(jmeterResults.getCellData(resSheet, com.test.selenium.testscript.Constants.RESULTS,currentTestStepID).trim().equalsIgnoreCase("TRUE")){
				result = com.test.selenium.testscript.Constants.KEYWORD_PASS;
			}else{
				result = Constants.KEYWORD_FAIL;
			}
			data.add(result);
			testSteps.add(data);
		}
		
		return testSteps;
	}
	
	
/*	
	public static void main(String[] args){
		Xls_Reader suiteXLS;
		Xls_Reader currentTestSuiteXLS;
		ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();
		String currentTestSuite;
		SuiteHelper sm = new SuiteHelper();
		suiteXLS = new Xls_Reader(System.getProperty("user.dir")+"//src//xls//Suite.xlsx");
		HashMap<String, String> map = sm.getSuite(suiteXLS);
		for(String key: map.keySet()){
			System.out.println("Key: "+key + ", Value: "+map.get(key));
			if(map.get(key).equals("Y")){
				currentTestSuite = key;
				System.out.println("CurrentSuite: "+currentTestSuite);
				currentTestSuiteXLS=new Xls_Reader(System.getProperty("user.dir")+"//src//xls//"+currentTestSuite+".xlsx");
				HashMap<String, String> tcMap = sm.getTC(currentTestSuiteXLS);
				for(String tc: tcMap.keySet()){
					System.out.println("Key: "+tc + ", Runmode: "+tcMap.get(tc));
					if(tcMap.get(tc).equals("Y")){
						List<String> ds = sm.getDataSet(currentTestSuiteXLS,tc);
						for(String s: ds){
							System.out.println("dataset "+s);
							if(s.equals("Y")){
								testSteps = sm.getTestSteps(currentTestSuiteXLS, tc);
								System.out.println(testSteps.size());
								
							}
						}
					}
					
				}
			}
		}
	}
*/
}
