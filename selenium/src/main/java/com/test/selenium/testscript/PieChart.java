package com.test.selenium.testscript;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PieChart extends BaseChart {
	
		public PieChart(WebDriver driver, WebElement chart, String series) {
	        super(driver, chart, series);
	    }
	    
	    public int getSliceCount(){
	    	return chart.findElements(By.cssSelector("g.highcharts-point")).size();
	    }
	    
	    public void mouseOverAtXAxisLabel(String xAxisLabel) {
	        //hoverOverColumnOrBarChartSeriesAtXAxisPosition(seriesIndex, xAxisLabel);
	        //int sliceNumber = getXAxisLabelsText().indexOf(xAxisLabel);
	    	int sliceNumber = getLegendText().indexOf(xAxisLabel);
	        WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-point")).get(sliceNumber);

	        //For browsers not supporting native events
	        //javascript.callEmbeddedSelenium(driver, "triggerEvent", pointToHoverOver, "mouseover");
	        //For browsers supporting native events
	        performAction.moveToElement(pointToHoverOver).perform();
	    }
}
