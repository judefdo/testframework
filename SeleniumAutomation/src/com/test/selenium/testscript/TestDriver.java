package com.test.selenium.testscript;



import static com.ge.selenium.testscript.TestDriver.CONFIG;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.test.selenium.utils.CSVLoader;
import com.test.selenium.utils.DBUtil;
import com.test.selenium.utils.ReportUtil;
import com.test.selenium.utils.TestUtil;

import com.test.selenium.utils.Xls_Reader;

public class TestDriver {

	public static Logger APP_LOGS;
	//suite.xlsx
	public Xls_Reader suiteXLS;
	public int currentSuiteID;
	public String currentTestSuite;
	
	// current test suite
	public static Xls_Reader currentTestSuiteXLS;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestStepID;
	public static String currentKeyword;
	public static String stepDescription;
	public static int currentTestDataSetID=2;
	public static Method method[];
	public static Method capturescreenShot_method;
	

	public static Keywords keywords;
	public static String keyword_execution_result;
	public static ArrayList<String> resultSet;
	public static String data;
	public static String object;
	
	// properties
	public static Properties CONFIG;
	public static Properties UIMap;
	public static ResultsContainer<Object> testResults;
	public DBUtil dbutil;

	/* my change 06/19
	public TestDriver() throws NoSuchMethodException, SecurityException{
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
		capturescreenShot_method =keywords.getClass().getMethod("captureScreenshot",String.class,String.class);
		dbutil = new DBUtil(CONFIG.getProperty("DB_CONNECTION"),CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
	}
	*/
	public TestDriver(Keywords sk) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException{
		//keywords = new Keywords();
		keywords = sk;
		method = sk.getClass().getMethods();
		capturescreenShot_method =sk.getClass().getMethod("captureScreenshot",String.class,String.class);
		//dbutil = new DBUtil(CONFIG.getProperty("DB_CONNECTION"),CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
		//setUp();
		//dbutil = new DBUtil();
		//setUp();
	}
	
	public TestDriver(Keywords sk, String configFile) throws NoSuchMethodException, SecurityException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException{
		//keywords = new Keywords();
		keywords = sk;
		method = sk.getClass().getMethods();
		capturescreenShot_method =sk.getClass().getMethod("captureScreenshot",String.class,String.class);
		FileInputStream fs = new FileInputStream(configFile);
		CONFIG= new Properties();
		CONFIG.load(fs);
		//dbutil = new DBUtil(CONFIG.getProperty("DB_CONNECTION"),CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
		//setUp();
		//dbutil = new DBUtil();
		//setUp();
	}
	
	//public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, NoSuchMethodException, SecurityException {
	public  void setUp() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, NoSuchMethodException, SecurityException {
		
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		APP_LOGS.debug("Hello");
		APP_LOGS.debug("Properties loaded. Starting testing");
		
		FileInputStream fs;
		/*
			if(args.length == 0){
				fs = new FileInputStream(System.getProperty("user.dir")+"/src/com/ge/selenium/config/config.properties");
			}else{
				fs = new FileInputStream(args[0]+"/config.properties");
			}
		*/	
		//fs = new FileInputStream(System.getProperty("user.dir")+"/src/com/ge/selenium/config/config.properties");
		if(CONFIG == null){
			//fs = new FileInputStream(System.getProperty("user.dir")+"/src/com/ge/selenium/config/config.properties");
			fs = new FileInputStream(System.getProperty("user.dir")+"/config/selenium.config.properties");
		    //FileInputStream fs = new FileInputStream(args[0]+"/config.properties");
			CONFIG= new Properties();
			CONFIG.load(fs);
		}
		String UIMapPath = "";
		String ResultsPath = "";
		
		if(CONFIG.getProperty("UIMapPath") == null || CONFIG.getProperty("TestSuitePath") == null || CONFIG.getProperty("ResultsPath") == null){
		//if(UIMapPath == null || CONFIG.getProperty("TestSuitePath") == null || ResultsPath == null){
			APP_LOGS.debug("config properties like UIMapPath, TestSuitePath, ResultsPath are not defined .. please check your config.properties");
			ReportUtil.reportError(CONFIG.getProperty("ResultsPath"),"config properties like UIMapPath, TestSuitePath, ResultsPath are not defined .. please check your config.properties");
			System.exit(1);
		}else{
			UIMapPath = CONFIG.getProperty("UIMapPath").trim();
			ResultsPath = CONFIG.getProperty("ResultsPath").trim();
		}
					
		if(!UIMapPath.endsWith("/") || !UIMapPath.endsWith("//")){
			UIMapPath=UIMapPath+"/";
		}
		fs = new FileInputStream(UIMapPath+"UIMap.properties");
		UIMap= new Properties();
		UIMap.load(fs);
		testResults = new ResultsContainer();
		TestUtil.checkDir(ResultsPath);
		ReportUtil.startTesting(ResultsPath,TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),"Stage","1.1.3");
		//TestDriver test = new TestDriver();
		//test.start();
		//start();
		fs.close();
		//ReportUtil.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
	}
	
	
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException{
		// initialize the app logs
		setUp();
		String testStatus="";
		String startTime="";
		String automation="";
		// 1) check the runmode of test Suite
		// 2) Runmode of the test case in test suite
	    // 3) Execute keywords of the test case serially
		// 4) Execute Keywords as many times as
		// number of data sets - set to Y
		APP_LOGS.debug("Intialize Suite xlsx");
		SuiteHelper sm = new SuiteHelper();
		String suitePath=CONFIG.getProperty("TestSuitePath").trim();
		if(!suitePath.endsWith("/") || !suitePath.endsWith("//")){
			suitePath=suitePath+"/";
		}
		suiteXLS = new Xls_Reader(suitePath+"Suite.xlsx");
		if(!suiteXLS.verifyMainSuiteFormat()){
			APP_LOGS.debug("Failed to parse Suite.xlsx... please check the column headings or sheet names");
			ReportUtil.reportError(CONFIG.getProperty("ResultsPath"),"Failed to parse Suite.xlsx... please check the column headings or sheet names");
			return;
		}
		
		//suiteXLS = new Xls_Reader(suitePath+"Suite.csv");
		HashMap<String, String> map = sm.getSuite(suiteXLS);
		for(String key: map.keySet()){
			currentTestSuite = key;
			ReportUtil.startSuite(currentTestSuite,map.get(key));
			if(map.get(key).equalsIgnoreCase("Y")){
				currentTestSuiteXLS=new Xls_Reader(suitePath+currentTestSuite+".xlsx");
				if(!currentTestSuiteXLS.verifyTestSuiteFormat()){
					APP_LOGS.debug("Failed to parse "+currentTestSuite+".xlsx... please check the column headings or sheet names");
					ReportUtil.reportError(CONFIG.getProperty("ResultsPath"),"Failed to parse "+currentTestSuite+".... please check the column headings or sheet names");
					return;
				}
				//currentTestSuiteXLS=new Xls_Reader(suitePath+currentTestSuite+".csv");
				//HashMap<String, String> tcMap = sm.getTC(currentTestSuiteXLS);
				List<TestCase> tcList = sm.getTC(currentTestSuiteXLS);
				//sm.readTestSteps(currentTestSuiteXLS);
				//for(String tc: tcMap.keySet()){
				for(TestCase tc: tcList){
					//currentTestCaseName = tc;
					currentTestCaseName = tc.getTCName();
					//if(tcMap.get(currentTestCaseName).equals("Y")){
					if(tc.getRunMode().equalsIgnoreCase("Y")){
						sm.readTestSteps(currentTestSuiteXLS);
						automation="Y";
						if(!testStatus.equals(Constants.KEYWORD_PASS) && tc.getDependency().equalsIgnoreCase("y")){
							ReportUtil.addTestCase(currentTestCaseName,tc.getUSName(),tc.getTCDesc(), 
									startTime, 
									TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
									"Fail[Dep. Test Failed/Skipped]",automation, tc.getPriority() );
							continue;
						}
						APP_LOGS.debug("Executing the test case -> "+currentTestCaseName);
						//new code here 05/07/13
						TestSteps ts = sm.getTCSteps(currentTestCaseName);
						if(ts == null){
							ReportUtil.addTestCase(currentTestCaseName,tc.getUSName(),tc.getTCDesc(), 
													startTime, 
													TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
													"Fail No Test Steps Defined",automation,tc.getPriority() );
							continue;
						}
						if(currentTestSuiteXLS.isSheetExist(currentTestCaseName)){
								List<String> ds = sm.getDataSet(currentTestSuiteXLS,currentTestCaseName);
								currentTestDataSetID=2;
								String tcName = "";
								for(String s: ds){
									startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
									resultSet = new ArrayList<String>();
									APP_LOGS.debug("Iteration number "+(currentTestDataSetID-1));
									tcName = currentTestCaseName+"_"+(currentTestDataSetID-1);
									if(s.equals("Y")){
										//testStatus=executeKeywords(); // multiple sets of data
										testStatus=executeKeywords(ts);
										ReportUtil.addTestCase(tcName,tc.getUSName(), tc.getTCDesc(),
																startTime, 
																TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
																testStatus,automation,tc.getPriority() );
									}else{
										ReportUtil.addTestCase(tcName,tc.getUSName(), tc.getTCDesc(),
												startTime, 
												TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
												"Skip",automation,tc.getPriority() );
										APP_LOGS.debug("Data set Runmode is set to N .. skipping");
									}
									currentTestDataSetID=currentTestDataSetID+1;
								}	
						}else{
								// iterating through all keywords
								startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
								currentTestDataSetID=2;
								resultSet= new ArrayList<String>();
								//testStatus=executeKeywords();// no data with the test
								testStatus=executeKeywords(ts);// no data with the test
								
								//jmeter changes here 02/25/14
								
								if(ts.getKeywords().contains("runJMeter")){
									CSVLoader loader = new CSVLoader();
							    	ArrayList<ArrayList<String>> jMeterRes = new ArrayList<ArrayList<String>>();
									try {
										//jMeterRes = loader.getJMeterResults("C:\\reports\\test.csv");
										jMeterRes = loader.getJMeterResults(CONFIG.getProperty("ResultsPath").trim()+"\\"+ts.getData().get(0)+".csv");
										if(jMeterRes.size() == 0){
											ReportUtil.addTestCase(currentTestCaseName, tc.getUSName(),tc.getTCDesc(),
													startTime, 
													TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
													testStatus,automation, tc.getPriority() );
										}else{
											for(int i=0;i<jMeterRes.size();i++){
												ReportUtil.addTestCase(jMeterRes.get(i).get(0), jMeterRes.get(i).get(3),jMeterRes.get(i).get(0),
														startTime, 
														TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
														jMeterRes.get(i).get(4),"Y", tc.getPriority() );
											}
										}
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}else{
									ReportUtil.addTestCase(currentTestCaseName, tc.getUSName(),tc.getTCDesc(),
											startTime, 
											TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
											testStatus,automation, tc.getPriority() );
								}
								
						}
					}else{
							APP_LOGS.debug("Skipping the test "+ currentTestCaseName);
							testStatus = tc.getStatus();
							if(testStatus.equals("") || testStatus.equals(null)){
								testStatus="Skip";
								automation="Y";
							}else{
								automation="N";
							}
							// report skipped
							APP_LOGS.debug("***********************************"+currentTestCaseName+" --- " +testStatus);
							ReportUtil.addTestCase(currentTestCaseName,tc.getUSName(), tc.getTCDesc(),
													TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), 
													TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
													testStatus,automation, tc.getPriority() );
					}
				}
			}
			ReportUtil.endSuite();
		}
		ReportUtil.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
		//TestUtil.zip(CONFIG.getProperty("ResultsPath").trim());
		//TestUtil.deleteFiles(CONFIG.getProperty("ResultsPath").trim());
	}
	
	private String executeKeywords(TestSteps ts) {
		// iterating through all keywords	
		int rc = 0;
		String exeOnFailureFlag="";
		String results="Fail";
		String fileName;
		for(int j=0;j<ts.getKeywords().size();j++){
			keyword_execution_result="Fail --could be some exceptions please check logs";
			data = ts.getData().get(j);
			exeOnFailureFlag=ts.getExecFlag().get(j);
			if(data.startsWith(Constants.DATA_START_COL)){
				// read actual data value from the corresponding column				
				data=currentTestSuiteXLS.getCellData(currentTestCaseName, data.split(Constants.DATA_SPLIT)[1] ,currentTestDataSetID );
			}else if(data.startsWith(Constants.CONFIG)){
				//read actual data value from config.properties		
				data=CONFIG.getProperty(data.split(Constants.DATA_SPLIT)[1]).trim();
			}else if(data.startsWith(Constants.UIMAP)){
				data=UIMap.getProperty(data.split(Constants.DATA_SPLIT)[1]).trim();
			//}else if(data.contains("select") && data.contains("from")){
			}else if(data.startsWith("select") || data.startsWith("SELECT")){
				try {
					//data=dbutil.getDBData(data);
					data=DBUtil.getDBData(data);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			object = ts.getObjects().get(j);
			currentKeyword = ts.getKeywords().get(j);
			//stepDescription="testing";
			stepDescription=ts.getTCDesc().get(j);
			APP_LOGS.debug(currentKeyword);
			System.out.println("Executing Step");
			System.out.println(ts.getTCName()+"--"+ts.getKeywords().get(j)+"--"+ts.getObjects().get(j)+"--"+ts.getData().get(j));
			//for(int i=0;i<method.length;i++){
				//keyword_execution_result="Fail";
			//	if(method[i].getName().equals(currentKeyword)){
				//if(method[i].getName().toLowerCase().equals(currentKeyword.toLowerCase())){
					try {
						keyword_execution_result=(String) keywords.getClass().getMethod(currentKeyword,String.class, String.class).invoke(keywords,object,data);
						//keyword_execution_result=(String)method[i].invoke(keywords,object,data);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						keyword_execution_result="Fail -- Failed to execute keyword";
						APP_LOGS.error("Fail -- Failed to execute keyword "+e);
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						keyword_execution_result="Fail -- keyword not found, please check typos";
						APP_LOGS.error("Fail -- keyword not found, please check typos \n"+e);
						//e.printStackTrace();
					}
					APP_LOGS.debug(keyword_execution_result);
					resultSet.add(keyword_execution_result);
					results = keyword_execution_result;
					System.out.println(results);
					String tcName=currentTestCaseName;
					if(tcName.indexOf("|") != -1){
					//if(currentTestCaseName.contains("\\|")){
						tcName=currentTestCaseName.split("\\|")[1];
					}
					//String fileName=currentTestSuite+"_"+tcName+"_TS"+currentTestStepID+"_"+(currentTestDataSetID-1);
					fileName=currentTestSuite+"_"+tcName+"_TS"+j+"_"+(currentTestDataSetID-1);
					//if(!keyword_execution_result.equals(Constants.KEYWORD_PASS)){
					if(CONFIG.getProperty("screenshot_everystep").trim().equalsIgnoreCase("y") || !keyword_execution_result.equals(Constants.KEYWORD_PASS)){
						if(!ts.getKeywords().get(j).equals("openBrowser") && !ts.getKeywords().get(j).equals("closeBrowser") && !ts.getKeywords().get(j).equals("dataSetup") && !ts.getKeywords().get(j).equals("runJMeter")){
							try {
								//capturescreenShot_method.invoke(keywords,
								//		currentTestSuite+"_"+tcName+"_TS"+currentTestStepID+"_"+(currentTestDataSetID-1),
								//		keyword_execution_result);
								
								//capturescreenShot_method.invoke(keywords,
								//		currentTestSuite+"_"+tcName+"_TS"+j+"_"+(currentTestDataSetID-1),
								//		keyword_execution_result);
								capturescreenShot_method.invoke(keywords,fileName,keyword_execution_result);
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						}
					} 
					ReportUtil.addKeyword(stepDescription, currentKeyword, keyword_execution_result, CONFIG.getProperty("ResultsPath").trim()+"/"+fileName+".png",data);
					if(!keyword_execution_result.equals(Constants.KEYWORD_PASS)){
						//ReportUtil.addKeyword(stepDescription, currentKeyword, keyword_execution_result, CONFIG.getProperty("screenshotPath").trim()+"/"+fileName+".png",data);
						//ReportUtil.addKeyword(stepDescription, currentKeyword, keyword_execution_result, CONFIG.getProperty("screenshotPath").trim()+"/"+fileName+".png",data);
						results="Fail";
						rc = 1;
						if(!exeOnFailureFlag.equalsIgnoreCase("y")){
							return results;
						}
					}
				//	}else{
				//		ReportUtil.addKeyword(stepDescription, currentKeyword, keyword_execution_result, CONFIG.getProperty("screenshotPath").trim()+"/"+fileName+".png",data);
				//		//ReportUtil.addKeyword(stepDescription, currentKeyword, keyword_execution_result, "PASS");
				//	}
			//	}
		//	}//end for
					
		}
		
	//return keyword_execution_result;
		if(rc!=0){
			results = "Fail";
		}
	return results;
}
	
	
	
	

}

