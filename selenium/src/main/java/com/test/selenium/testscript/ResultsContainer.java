package com.test.selenium.testscript;

public class ResultsContainer<T> 
{
	private T data;
	
	public void set(T data)
	{
		this.data=data;
	}
	
	public T get()
	{
		return data;
	}

}
