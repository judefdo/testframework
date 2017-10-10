package com.test.selenium.testscript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

public class BarChart extends BaseChart {
	
	private List<String> labelText;
    public BarChart(WebDriver driver, WebElement chart, String series) {
        super(driver, chart, series);
        this.xAxis = getXAxisElement();
        if(this.xAxis == null) throw new org.openqa.selenium.NoSuchElementException("xaxis not found");
        setXAxisOrder();
    }
    
    public int getBarCount(){
    	//List<WebElement> bars = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect"));
    	List<WebElement> bars = chart.findElements(By.cssSelector("g.highcharts-tracker > rect"));
    	//List<WebElement> barsWithData = new ArrayList<WebElement>();
    	int barCount = 0;
    	
    	for(WebElement ele : bars){
    		double x = Double.parseDouble(ele.getAttribute("x"));
    		int height = Integer.parseInt(ele.getAttribute("height"));
    		if(x>0 && height > 0){
    			barCount=barCount+1;
    		}
    		//System.out.println("x =" +ele.getAttribute("x"));
    	}
    	//return chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).size();
    	return barCount;
    }
    
    public void mouseOverAtXAxisLabel(String xAxisLabel) {
        //hoverOverColumnOrBarChartSeriesAtXAxisPosition(seriesIndex, xAxisLabel);
        //int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
    	
    	int barNumber = labelText.indexOf(xAxisLabel);
        WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).get(barNumber);
        
        //For browsers not supporting native events
        //javascript.callEmbeddedSelenium(driver, "triggerEvent", pointToHoverOver, "mouseover");
        //For browsers supporting native events
        performAction.moveToElement(pointToHoverOver).perform();
    }
    public void mouseOverAtXAxisLabel(String xAxisLabel,int seriesIndex) {
        //hoverOverColumnOrBarChartSeriesAtXAxisPosition(seriesIndex, xAxisLabel);
        //int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
    	
    	int barNumber = labelText.indexOf(xAxisLabel);
        WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).get(barNumber);
        
        //For browsers not supporting native events
        //javascript.callEmbeddedSelenium(driver, "triggerEvent", pointToHoverOver, "mouseover");
        //For browsers supporting native events
        performAction.moveToElement(pointToHoverOver).perform();
    }
    
    public void mouseOverAtBar(int barNumber) {
        //hoverOverColumnOrBarChartSeriesAtXAxisPosition(seriesIndex, xAxisLabel);
        //int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
    	
    	//int barNumber = labelText.indexOf(xAxisLabel);
    	//change on 01/16/14
        //WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).get(barNumber);
    	WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > rect")).get(barNumber);
        
        //For browsers not supporting native events
        //javascript.callEmbeddedSelenium(driver, "triggerEvent", pointToHoverOver, "mouseover");
        //For browsers supporting native events
        performAction.moveToElement(pointToHoverOver).perform();
    }
    
    public void mouseOverAtLegend(){
    	//WebElement legendElement = chart.findElement(By.cssSelector("g.highcharts-legend"));
    	performAction.moveToElement(legendElement).perform();
    }
    public Color getSeriesColor(String xAxisLabel,int seriesIndex) {
        //hoverOverColumnOrBarChartSeriesAtXAxisPosition(seriesIndex, xAxisLabel);
        //int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
    	
    	int barNumber = labelText.indexOf(xAxisLabel);
        return Color.fromString(chart.findElements(By.cssSelector("g.highcharts-series-group > g > rect")).get(seriesIndex-1).getAttribute("fill"));
       
    }
    
    public void setXAxisOrder(){
    	labelText = getXAxisLabelsText();
    	WebElement bar1;
    	WebElement bar2;
    	int xValue;
    	int xValue1;
    	/*
    	bar1 = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).get(0);
    	bar2 = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + seriesIndex + ") > rect")).get(1);
    	xValue = extractXAttributeAsInteger(bar1);
    	xValue1 = extractXAttributeAsInteger(bar2);
    	if(xValue1<xValue){
    		Collections.reverse(labelText);
    		for(String s:labelText){
    			//System.out.println("VAlue "+s);
    		}
    	}
    	*/
    }
    
}
