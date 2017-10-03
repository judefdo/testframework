package com.test.selenium.testscript;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

abstract class BaseChart {

    protected WebDriver driver;
    protected WebElement chart;
    protected WebDriverWait wait;
    protected Actions performAction;
    protected JavascriptLibrary javascript = new JavascriptLibrary();

   
    
    private WebElement axisLabels;
    protected WebElement legendElement;
    protected int seriesIndex;
    private WebElement toolTipElement;
    protected WebElement xAxis;

    public BaseChart(WebDriver driver, WebElement chart,String series) {
        //PageFactory.initElements(new DefaultElementLocatorFactory(chart), this);
        this.driver = driver;
        this.chart = chart;
        try{
        	this.legendElement = chart.findElement(By.cssSelector("g.highcharts-legend"));
        }catch(org.openqa.selenium.NoSuchElementException exc){
            
    	}
        
        this.toolTipElement = chart.findElement(By.cssSelector("g.highcharts-tooltip"));
        
        this.seriesIndex = getSeriesIndex(series)+1;
        if(this.seriesIndex == 0) throw new org.openqa.selenium.NoSuchElementException("Legend "+series+ " Not Found");
        int waitTimeoutInSeconds = 15;
        wait = new WebDriverWait(driver, waitTimeoutInSeconds, 100);
        performAction = new Actions(driver);
    }

    public boolean isChartDisplayed() {
        return wait.until(visibilityOf(this.chart)) != null;
    }

    public boolean isLegendDisplayed() {
        return legendElement.isDisplayed();
    }

    public boolean isTooltipDisplayed() {
        return wait.until(visibilityOf(toolTipElement)) != null;
    }
    
    public int getSeriesIndex(String seriesText){
    	if(seriesText == ""){
    		return 0;
    	}
    	return getLegendText().indexOf(seriesText);
    }
    
    public int getSeriesCount(){
    	return chart.findElements(By.cssSelector("g.highcharts-series-group > g > rect")).size();
    }
    public WebElement getXAxisElement(){
    	WebElement xaxisEle = null;
    	//WebElement xaxis;
    	try{
    		if(chart.findElement(By.cssSelector("g.highcharts-axis-labels")).isDisplayed()){
    			xaxisEle = chart.findElements(By.cssSelector("g.highcharts-axis-labels")).get(0);
    		} 
    	}catch(org.openqa.selenium.NoSuchElementException exc){
    	
    	}
    	if(xaxisEle == null){
    		try{
        		if(chart.findElement(By.cssSelector("g.highcharts-axis")).isDisplayed()){
        			xaxisEle = chart.findElements(By.cssSelector("g.highcharts-axis")).get(0);
        		} 
        	}catch(org.openqa.selenium.NoSuchElementException exc){
        
        	}
    	}
    	
    	return xaxisEle;
    }
    public List<String> getLegendText() throws NoSuchElementException {
        List<String> lines = new ArrayList<String>();
        List<WebElement> legendEls = legendElement.findElements(By.cssSelector("text tspan"));
        for (WebElement legendEl : legendEls) {
        	//legendEl.click();
            lines.add(legendEl.getText());
        }
        return lines;
    }
    
    public List<WebElement> getLegendElement() throws NoSuchElementException {
        List<String> lines = new ArrayList<String>();
        List<WebElement> legendEls = legendElement.findElements(By.cssSelector("text tspan"));
        
        return legendEls;
    }
    
    public Color getLegendColor() {
    	if(seriesIndex>1){
    		return Color.fromString(legendElement.findElements(By.cssSelector("rect")).get(seriesIndex-1).getAttribute("fill"));
    	}else{
    		//if series not found return invalid
    		return Color.fromString("white");
    	}
        
    }
    
    public String getToolTip() throws NoSuchElementException {
        List<String> lines = new ArrayList<String>();
        List<WebElement> toolTipLines = toolTipElement.findElements(By.cssSelector("text tspan"));
        if(toolTipLines.size() == 0){
        	this.toolTipElement = driver.findElement(By.cssSelector("div.highcharts-tooltip"));
        	toolTipLines = toolTipElement.findElements(By.cssSelector("b"));
        }
        for (WebElement toolTipLine : toolTipLines) {
            lines.add(toolTipLine.getText());
        }
        
        return lines.get(lines.size() - 1);
    }
    
    public String getToolTip(int index) throws NoSuchElementException {
        List<String> lines = new ArrayList<String>();
        List<WebElement> toolTipLines = toolTipElement.findElements(By.cssSelector("text tspan"));
        if(toolTipLines.size() == 0){
        	this.toolTipElement = driver.findElement(By.cssSelector("div.highcharts-tooltip"));
        	toolTipLines = toolTipElement.findElements(By.cssSelector("b"));
        }
        for (WebElement toolTipLine : toolTipLines) {
            lines.add(toolTipLine.getText());
        }
        
        return lines.get(index - 1);
    }
    
    public List<String> getToolTipList() throws NoSuchElementException {
        List<String> lines = new ArrayList<String>();
        List<WebElement> toolTipLines = toolTipElement.findElements(By.cssSelector("text tspan"));
        if(toolTipLines.size() == 0){
        	this.toolTipElement = driver.findElement(By.cssSelector("div.highcharts-tooltip"));
        	toolTipLines = toolTipElement.findElements(By.cssSelector("b"));
        }
        for (WebElement toolTipLine : toolTipLines) {
            lines.add(toolTipLine.getText());
        }
        
        return lines;
    }
    
    
    protected WebElement getXAxisLabels() {
        //return axisLabels.get(0);
    	return axisLabels;
    }

    public List<String> getXAxisLabelsText() {
        List<String> labels = new ArrayList<String>();
        List<WebElement> xAxisLabels = xAxis.findElements(By.cssSelector("text"));
        for (WebElement xAxisLabel : xAxisLabels) {
            labels.add(xAxisLabel.getText());
        }
        return labels;
    }

    public String[] getXAxisLabelsAsArray() {
        List<String> xAxisLabels = getXAxisLabelsText();
        return xAxisLabels.toArray(new String[xAxisLabels.size()]);
    }

    protected int extractXAttributeAsInteger(WebElement xAxisLabel) {
        Double xAttribute = Double.parseDouble(xAxisLabel.getAttribute("x"));
        return xAttribute.intValue();
    }

   
    protected WebElement getYAxisLabels() {
        //return axisLabels.get(1);
    	return axisLabels;
    }

    public List<String> getYAxisLabelsText() {
        List<String> labels = new ArrayList<String>();
        List<WebElement> yAxisLabels = getYAxisLabels().findElements(By.cssSelector("text"));
        for (WebElement yAxisLabel : yAxisLabels) {
            labels.add(yAxisLabel.getText());
        }
        return labels;
    }

    public String[] getYAxisLabelsAsArray() {
        List<String> yAxisLabels = getYAxisLabelsText();
        return yAxisLabels.toArray(new String[yAxisLabels.size()]);
    }

}
