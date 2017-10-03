package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.List;

public class TestSteps {
	private String tcName;
	//private String tcDesc;
	private List<String> tcDesc = new ArrayList<String>();
	private List<String> keywords = new ArrayList<String>();
	private List<String> objects = new ArrayList<String>();
	private List<String> data = new ArrayList<String>();
	private List<String> execFlag = new ArrayList<String>();
	public TestSteps(){
		
	}
	public TestSteps(String name){
		this.tcName = name;
	}
	public void addTCDesc(String desc){
		this.tcDesc.add(desc);
	}
	public void addKeyWord(String keyword){
		this.keywords.add(keyword);
	}
	public void addObject(String object){
		this.objects.add(object);
	}
	public void addData(String data){
		this.data.add(data);
	}
	public void addExecFlag(String flag){
		this.execFlag.add(flag);
	}
	public String getTCName(){
		return this.tcName;
	}
	public List<String> getTCDesc(){
		return this.tcDesc;
	}
	public List<String> getKeywords(){
		return this.keywords;
	}
	public List<String> getObjects(){
		return this.objects;
	}
	public List<String> getData(){
		return this.data;
	}
	public List<String> getExecFlag(){
		return this.execFlag;
	}
}
