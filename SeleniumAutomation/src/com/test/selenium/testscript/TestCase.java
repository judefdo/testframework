
package com.test.selenium.testscript;



public class TestCase {
	private String testCaseName;
	private String usName;
	private String testCaseDesc;
	private String runMode;
	private String dependency;
	private String status;
	private String priority;
	
	//public TestCase(String name,String us,String desc,String runMode,String dependency) {
	//public TestCase(String name,String us,String desc,String runMode,String dependency, String status) {
	//3/21 -- added priority
	public TestCase(String name,String us,String desc,String runMode,String dependency, String status, String priority) {
		
		this.testCaseName = name;
		this.usName = us;
		this.testCaseDesc = desc;
		this.runMode = runMode;
		this.dependency = dependency;
		this.status = status;
		this.priority = priority;
	}

	
	public String getTCName() {
		return testCaseName;
	}
	
	public String getUSName() {
		return usName;
	}
	
	public String getTCDesc() {
		return testCaseDesc;
	}
	
	
	public String getRunMode() {
		return runMode;
	}
	
	public String getDependency() {
		return dependency;
	}
	
	public String getStatus() {
		return status;
	}
	public String getPriority() {
		return priority;
	}
}

