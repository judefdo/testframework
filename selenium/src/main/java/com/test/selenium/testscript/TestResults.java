
package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.List;

public class TestResults {
	private String testSuite;
	private String testCaseName;
	private String testCaseDesc;
	private String userStory;
	private String testStartTime;
	private String testEndTime;
	private String testStatus;
	private String isAuto;
	private String priority;
	
	
	//public TestResults(String suite,String name,String desc,String us,String startTime,String endTime,String status) {
	public TestResults(String suite,String name,String desc,String us,String startTime,String endTime,String status, String isAuto, String priority) {
		
		this.testSuite = suite;
		this.testCaseName = name;
		this.testCaseDesc = desc;
		this.userStory = us;
		this.testStartTime = startTime;
		this.testEndTime = endTime;
		this.testStatus = status;
		this.isAuto = isAuto;
		this.priority = priority;
		
	}

	public String getSuite() {
		return testSuite;
	}
	
	public String getTCName() {
		return testCaseName;
	}
	
	public String getTCDesc() {
		return testCaseDesc;
	}
	
	public String getUS() {
		return userStory;
	}
	
	public String getStartTime() {
		return testStartTime;
	}
	
	public String getEndTime() {
		return testEndTime;
	}
	
	public String getStatus() {
		return testStatus;
	}
	
	public String isAutomated() {
		return isAuto;
	}
	
	public String getPriority() {
		return priority;
	}
}

