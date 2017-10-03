package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.List;

import com.test.selenium.utils.Xls_Reader;

public class Helper {
	//public Xls_Reader currentTestSuiteXLS;
	public List<TestSteps> getSteps(Xls_Reader currentTestSuiteXLS){
		String tmp="";
		List<TestSteps> tsList = new ArrayList<TestSteps>();
		TestSteps ts = null;
		//TestCase tt = new TestCase();
		//String tc = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, 2);
		String tc="";
		for(int i=2;i<=currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET);i++){
			if(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i).equalsIgnoreCase(tc)){
				ts.addKeyWord(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,i));
				ts.addObject(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,i));
				ts.addData(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,i));
			}else{
				if(i!=2){
					tsList.add(ts);
				}
				
				tc = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i);
				ts = new TestSteps(tc);
				ts.addKeyWord(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,i));
				ts.addObject(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,i));
				ts.addData(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA,i));
			}
		}
		tsList.add(ts);
		return tsList;
	}
	public TestSteps getTCSteps(String tcName,Xls_Reader currentTestSuiteXLS){
		TestSteps ts = null;
		List<TestSteps> tsList = getSteps(currentTestSuiteXLS);
		for(TestSteps t:tsList){
			if(t.getTCName().equals(tcName)){
				return t;
			}
		}
		return ts;
	}
	public static void main(String[] args){
		Helper help = new Helper();
		Xls_Reader currentTestSuiteXLS=new Xls_Reader("C:/ALTestSuites/TestSuites/PAM_Dashboard_TestSuite.xlsx");
		/*
		List<TestSteps> ts = help.getSteps(currentTestSuiteXLS);
		for(TestSteps t : ts){
			List<String> kw = t.getKeywords();
			List<String> ob = t.getObjects();
			List<String> dt = t.getData();
			for(int i=0;i<kw.size();i++){
				System.out.println(t.getTCName()+"--"+kw.get(i)+"--"+ob.get(i)+"--"+dt.get(i));
			}
		}
		*/
		TestSteps ts = help.getTCSteps("testDashboard", currentTestSuiteXLS);
		List<String> dt = ts.getData();
		for(int i=0;i<dt.size();i++){
			System.out.println(ts.getTCName()+"--"+ts.getKeywords().get(i)+"--"+ts.getObjects().get(i)+"--"+ts.getData().get(i));
		}
	}
}
