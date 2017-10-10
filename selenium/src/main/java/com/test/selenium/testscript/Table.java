package com.test.selenium.testscript;


import static com.ge.selenium.testscript.TestDriver.UIMap;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static com.ge.selenium.testscript.TestDriver.APP_LOGS;
import static com.ge.selenium.testscript.TestDriver.CONFIG;
import static com.ge.selenium.testscript.TestDriver.UIMap;

public class Table {
	
	public WebDriver driver;
	//public Keywords kw = new Keywords();
	private List<TableEntity> entities;
	public enum NavigateButton {FIRST, PREVIOUS, NEXT, LAST};
	
	/*
	public String object;
	public String childObject;
	public String data;
	*/
	
	public String tableHeader;
	public String table;
	public String childObject;
	public String data;
	
	/*
	public Table(WebDriver driver,String tableobject, String data) {
		this.driver = driver;
		this.entities = new ArrayList<TableEntity>();
		this.object = tableobject.split(",")[0];
		this.data = data;
		this.childObject = tableobject.split(",")[1];
	}
	*/
	public Table(WebDriver driver,String th, String tbl, String chld, String data) {
		System.out.println("hello table");
		this.driver = driver;
		this.entities = new ArrayList<TableEntity>();
		this.tableHeader = th;
		this.table = tbl;
		this.childObject = chld;
		this.data = data;
		System.out.println("hello table");
	}
	
	
	public void addEntity(TableEntity TableEntity) {
		entities.add(TableEntity);
	}
	
	public TableEntity getEntity(int index) {
		/*updateTableRow(index);*/
		return entities.get(index-1);
	}

	public void removeEntity(int index) {
		this.entities.remove(index);
	}
	
	public void removeEntities(int[] indexes) {
		List<TableEntity> tmp = entities.subList(indexes[0]-1, indexes[0]-1);
		for(int i=1; i<indexes.length; i++) {
			tmp.add(entities.get(indexes[i]));
		}
		entities.removeAll(tmp);
	}
	
	public List<TableEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<TableEntity> entities) {
		this.entities = entities;
	}

	public boolean equals(Table obj) {
		return entities.equals(obj.getEntities());
	}
	/**
	 * Selects row checkbox
	 * @param row number
	 */
	public boolean selectRowCheckBox(int row) {
		//if(isRowChecked(row)) return;
		String data="";
		// get object locator ex: //*[@id='1wellSubscribe'] ==>//*[@id='ROWwellSubscribe'] 
		//String locator = UIMap.getProperty(childObject).replace("ROW", String.valueOf(row));
		String locator = childObject.replace("ROW", String.valueOf(row));
		System.out.println("child locator "+locator);
		//kw.checkCheckBox(locator, data);
		try{
			// true or null
			String checked=driver.findElement(By.xpath(locator)).getAttribute("checked");
			if(checked==null)// checkbox is unchecked
				driver.findElement(By.xpath(locator)).click();
		}catch(Exception e){
			System.out.println(" - Could not find checkbox");
			return false;
		}
		/* TO DO for now just replace the ROW in object
		SeleniumSession.getHandler().clickAt(locator, 2, 2);
		*/
		return true;
	}
	/**
	 * Selects row checkbox with specified value
	 * @param name to select
	 * @return true if selected
	 */
	public boolean selectRowCheckBox() {
        int row = -1;
        //int columns = getColumnsNames(object).size();
        int columns = getColumnsNames(tableHeader).size();
        //waitForLoading();
        do {
            for(int j=0; j<columns; j++) {
        	    row = getColumn(j+1).indexOf(data);
                System.out.println("row = "+row);
                if(row != -1) {
                    break;
                }
            }
            if (row != -1){
            	break;
            }
            /*
            if (row != -1 || isNavigateButtonAbsent() || isNavigateButtonDisabled(NavigateButton.NEXT)) {
            	break;
            } else {
            	nextPage();		
           	}
           	*/
        } while(true);
        if(row == -1){
        	System.out.println(data+" was NOT found!!!");
        	return false;
        }
		selectRowCheckBox(row+1);
    	return true;
    }
	/**
	 * Selects row checkbox with specified value if we know the COL Name
	 * @param column name to search for given data
	 * @return true if selected
	 */
	public boolean selectRowCheckBox(String colName) {
		int row = -1;
		while(true){
			row = getColumn(colName).indexOf(data);
			if (row != -1 || isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		selectRowCheckBox(row+1);
		return true;
	}
	
	/**
	 * Verifies is specified row checked
	 * @param row number
	 * @return true if checked
	 */
	public boolean isRowChecked(int row) {
		//String locator = window + uiMap.getLocator("row").replace("ROW", String.valueOf(row));
		//return SeleniumSession.getHandler().getAttribute(locator+"@class").contains("selected");
		boolean flag=false;
		String data="";
		String locator = childObject.replace("ROW", String.valueOf(row));
		try{
			// true or null
			String checked=driver.findElement(By.xpath(locator)).getAttribute("checked");
			if(checked==null)// checkbox is unchecked
				flag = false;
			else
				flag = true;
		}catch(Exception e){
			System.out.println(" - Could not find checkbox");
		}
		
		return flag;
	}

	/**
	 * Verifies is row with specified value checked
	 * @param name specified value to identify row
	 * @return true if checked
	 */
	public boolean isRowChecked() {
        int row = -1;
        int columns = getColumnsNames(tableHeader).size();
        for(int j=0; j<columns; j++) {
        	row = getColumn(j+1).indexOf(data);
            if(row != -1) {
            	break;
            }
        }
        if (row == -1) {
        	System.out.println(data+" was NOT found!!!");
        }
        
        return isRowChecked(row+1);
    }
	
	/**
	 * Verifies is row with specified value checked
	 * @param name specified value to identify row
	 * @return true if checked
	 */
	public boolean isRowChecked(String colName) {
        int row = -1;
        while(true){
			row = getColumn(colName).indexOf(data);
			if (row != -1){
	           	break;
	        }
		}
        if (row == -1) {
        	System.out.println(data+" was NOT found!!!");
        }
        
        return isRowChecked(row+1);
    }
	/**
	 * Gets columns names
	 * @return list of column names
	 */
	public List<String> getColumnsNames(String object) {
		//String locator=UIMap.getProperty(object);
		List<String> lsColumns = new ArrayList<String>();
		try{
			//List<WebElement> ele_list = driver.findElements(By.xpath(locator));
			List<WebElement> ele_list = driver.findElements(By.xpath(object));
			for(WebElement ele: ele_list){
				lsColumns.add(ele.getText().trim());
			}
		}catch(Exception e){
			APP_LOGS.debug("Table Header Object not found ..."+e);
		}
		
		return lsColumns;
	}
	
	/**
	 * Updates table column content from UI
	 * @param index number of column
	 */
	public void updateTableColumn(int index) {
		int i = 0, j = index;
		entities.clear();
		//String cell = "//*[@id='userWellTable']/tbody/tr[ROW]/td[COLUMN]";
		//String locator = cell.replace("COLUMN", String.valueOf(j));
		String locator = table.replace("COLUMN", String.valueOf(j));
		while (true) {
			i++;
			String tmp = locator.replace("ROW", String.valueOf(i));
			if (isElementPresent(tmp)) {
				if(entities.size()<i) entities.add(new TableEntity());
				while(entities.get(i-1).size()<index) {
					entities.get(i-1).addItem("");
				}
				entities.get(i-1).setItem(index, driver.findElement(By.xpath(tmp)).getText().trim());
			} else {
				break;
			}
		}
	}
	
	public boolean isElementPresent(String xpathLocator) {
        try {
		   	if(xpathLocator.startsWith("//")){
        		driver.findElement(By.xpath(xpathLocator));
        	}else{
        		driver.findElement(By.id(xpathLocator));
        	}
        	
        } catch (org.openqa.selenium.NoSuchElementException Ex) {
        		System.out.println("Unable to locate Element: " + xpathLocator);             
        	return false;         
		 }      
        return true;
	}
	
	/**
	 * Gets column content 
	 * @param index of column
	 * @return column content
	 */
	public List<String> getColumn(int index) {
		updateTableColumn(index);
		List<String> content = new ArrayList<String>();
		System.out.println("entity size = "+entities.size());
		for(int i=0; i<entities.size(); i++) {
			content.add(entities.get(i).getItem(index));
			System.out.println("Values = "+entities.get(i).getItem(index));
		}
		System.out.println("content size = "+content.size());
		
		return content;
	}

	/**
	 * Gets column content 
	 * @param sColumn name of column
	 * @return column content
	 */
	public List<String> getColumn(String sColumn) {
		//List<String> lsColumns = getColumnsNames(object);
		List<String> lsColumns = getColumnsNames(tableHeader);
		int i = lsColumns.indexOf(sColumn);
		if(i == -1) {
			APP_LOGS.error("Column name " + sColumn + " was not found in list of column names " + lsColumns);
		}
		
		updateTableColumn(i+1);
		List<String> content = new ArrayList<String>();
		for(i=0; i<entities.size(); i++) {
			content.add(entities.get(i).getItem(lsColumns.indexOf(sColumn)+1));
		}
		return content;
	}
	
	

	/**
	 * Gets column content with pagination
	 * @param sColumn name of column
	 * @return column content
	 */
	public List<String> getAllColumnData(String sColumn) {
		//List<String> lsColumns = getColumnsNames(object);
		List<String> lsColumns = getColumnsNames(tableHeader);
		int i = lsColumns.indexOf(sColumn);
		if(i == -1) {
			APP_LOGS.error("Column name " + sColumn + " was not found in list of column names " + lsColumns);
		}
		List<String> content = new ArrayList<String>();
		while(true){
			updateTableColumn(i+1);
			
			for(i=0; i<entities.size(); i++) {
				content.add(entities.get(i).getItem(lsColumns.indexOf(sColumn)+1));
			}
			if (isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
	        	nextPage();		
	       	}
		}
		
		return content;
	}
	
	/**
	 * Clicks on link in specified cell
	 * @param row number
	 * @param column number
	 */
	public String clickOnLinkInCell(int row, int column) {
		//childObject
		//String locator = UIMap.getProperty("cell_link").replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(column));
		String locator = childObject.replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(column));
		//String locator = UIMap.getProperty("cell_button").replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(column));
		try {
			driver.findElement(By.xpath(locator)).click();
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+"Unable to Click on Cell Link"+e.getMessage();		
		}
		return Constants.KEYWORD_PASS;	
		//logger.perfomance("Time to navigate IP address:", strTime);
	}

	/**
	 * Clicks on link in cell with specified name 
	 * @param link text
	 */
	public String clickOnLinkInCell(String colName,String searchData) {
		int row = -1;
		int column = getColumnsNames(tableHeader).indexOf(colName);
		while(true){
			row = getColumn(colName).indexOf(searchData);
			if (row != -1 || isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		if(row == -1)System.out.println(searchData+" was NOT found!!!");
		return clickOnLinkInCell(row+1, column+1);
	}
	/**
	 * Clicks on link in cell with specified name 
	 * @param link text
	 */
	public String clickOnLinkInCell(String colName,String searchData, String colToClick) {
		int row = -1;
		if(colToClick.equals("")){
			colToClick=colName;
		}
		int column = getColumnsNames(tableHeader).indexOf(colToClick);
		while(true){
			row = getColumn(colName).indexOf(searchData);
			if (row != -1 || isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		if(row == -1)System.out.println(searchData+" was NOT found!!!");
		return clickOnLinkInCell(row+1, column+1);
	}
	/**
	 * Gets RowData for a specified cell
	 * @param row number
	 */
	public List<String> getRowData(int row) {
		//String locator = UIMap.getProperty("cell_link").replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(column));
		
		List<String> rowData = new ArrayList<String>();
		String locator = table.replace("ROW", String.valueOf(row));
		int colSize = getColumnsNames(tableHeader).size();
		for(int i=0;i<colSize;i++){
			String tmp = locator.replace("COLUMN", String.valueOf(i+1));
			if (isElementPresent(tmp)) {
				rowData.add(driver.findElement(By.xpath(tmp)).getText());
			}else{
				break;
			}
		}
		
		return rowData;	
	}

	/**
	 * Gets row data based on search sting & column  
	 * @param column name
	 * @param search data to look for in the column
	 */
	public List<String> getRowData(String colName,String searchData) {
		int row = -1;
		int column = getColumnsNames(tableHeader).indexOf(colName);
		while(true){
			row = getColumn(colName).indexOf(searchData);
			if (row != -1 || isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		if(row == -1)System.out.println(searchData+" was NOT found!!!");
		return getRowData(row+1);
	}
	
	/**
	 * Gets Cell Data for a specified cell
	 * @param row number
	 * @param col number
	 */
	public String getCellData(int row, int col) {
		//String locator = UIMap.getProperty("cell_link").replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(column));
		
		String cellData = "";
		String locator = table.replace("ROW", String.valueOf(row)).replace("COLUMN", String.valueOf(col));
		if (isElementPresent(locator)) {
			cellData=driver.findElement(By.xpath(locator)).getText();
		}else{
			APP_LOGS.error("Could not get Data from Cell");
		}
		
		return cellData;	
	}

	/**
	 * Gets cell data based on search sting,column from required column   
	 * @param column name
	 * @param search data to look for in the column
	 * @param column to get
	 */
	public String getCellData(String colName,String searchData,String columnToGet) {
		int row = -1;
		int column = getColumnsNames(tableHeader).indexOf(columnToGet);
		while(true){
			row = getColumn(colName).indexOf(searchData);
			if (row != -1 || isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		if(row == -1)System.out.println(searchData+" was NOT found!!!");
		return getCellData(row+1,column+1);
	}
	/**
	 * Verifies is navigate button disabled
	 * @param button type
	 * @return true if disabled
	 */
	public boolean isNavigateButtonDisabled(NavigateButton button) {
		String locator = null;
		switch(button) {
		case FIRST:
			locator = UIMap.getProperty("footer_first_dis_btn");
			break;
		case PREVIOUS:
			locator = UIMap.getProperty("footer_prev_dis_btn");
			break;
		case NEXT:
			locator = UIMap.getProperty("footer_next_dis_btn");
		    
			break;
		case LAST:
			locator = UIMap.getProperty("footer_last_dis_btn");
			break;
		}
		
		return isElementPresent(locator);
	}
	
	/**
	 * Navigates to specified table page
	 * @param button type
	 * @return true if table content was uptated
	 */
	public void goToPage(NavigateButton button) {
		String locator = null;
		switch(button) {
		case FIRST:
			locator = UIMap.getProperty("footer_first_btn");
			break;
		case PREVIOUS:
			locator = UIMap.getProperty("footer_prev_btn");
			break;
		case NEXT:
			locator = UIMap.getProperty("footer_next_btn");
			break;
		case LAST:
			locator = UIMap.getProperty("footer_last_btn");
			break;
		}
		if(isNavigateButtonDisabled(button)) {
			APP_LOGS.error(button+ " is disabled");
			return; 
		}
		System.out.println("Locator = "+locator);
		driver.findElement(By.xpath(locator)).click();
	}
	/**
	 * Goes to first page
	 * @return true if content is changed
	 */
	public void firstPage() {
		goToPage(NavigateButton.FIRST);
	}
	
	/**
	 * Goes to previous page
	 * @return true if content is changed
	 */
	public void previousPage() {
		goToPage(NavigateButton.PREVIOUS);
	}
	
	/**
	 * Goes to next page
	 * @return true if content is changed
	 */
	public void nextPage() {
		goToPage(NavigateButton.NEXT);
	}
	
	/**
	 * Goes to last page
	 * @return true if content is changed
	 */
	public void lastPage() {
		goToPage(NavigateButton.LAST);
	}
	public int getRowCount(){
		int rowCount=0;
		//*[@id='DataTables_Table_0']/tbody/tr[ROW]/td[COLUMN]
		String table_locator = table.split("/tbody")[0];
		String row_ele = table_locator+"/tbody/tr";
		String select_locator = UIMap.getProperty("show_entries_select");
		WebElement list_ele = null;
		System.out.println("Table Locator "+table_locator);
		if(select_locator != null){
			try{
				list_ele=driver.findElement(By.xpath(select_locator));
				WebElement option = list_ele.findElement(By.xpath("//option[contains(text(),'100')]"));
				option.click();
			}catch(org.openqa.selenium.NoSuchElementException exc){
				APP_LOGS.debug("Show Entries List Box Doesn't exist ..."+exc);
			}
		}
		
		while(true){
			rowCount = rowCount + driver.findElements(By.xpath(row_ele)).size();
			System.out.println("Count = "+rowCount);
			if (isNavigateButtonDisabled(NavigateButton.NEXT)){
	           	break;
	        }else {
            	nextPage();		
           	}
		}
		return rowCount;
	}
}
