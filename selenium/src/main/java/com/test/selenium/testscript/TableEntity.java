package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.List;

public class TableEntity {
	private List<String> items;

	public TableEntity() {
		this.items = new ArrayList<String>();
	}

	public TableEntity(List<String> items) {
		this.items = items;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> entity) {
		this.items = entity;
	}
	
	public void addItem(String item) {
		items.add(item);
	}
	
	public String getItem(int index) {
		return items.get(index-1);
	}
	
	public int size() {
		return items.size();
	}

	public boolean equals(Object obj) {
		return items.equals(((TableEntity)obj).getItems());
	}

	public void setItem(int index, String text) {
		items.set(index-1, text);
	}
}

