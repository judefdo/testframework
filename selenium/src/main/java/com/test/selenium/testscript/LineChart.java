package com.test.selenium.testscript;

import static com.ge.selenium.testscript.TestDriver.APP_LOGS;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import com.test.selenium.testscript.*;
import com.test.selenium.testscript.Point;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class LineChart extends com.test.selenium.testscript.BaseChart {

	
    private WebElement trackerElement;
    private WebElement seriesRectElement;
    private WebElement seriesElement;
    private WebElement seriesBaseElement;
    //private WebElement xAxis;
    public String errMessage;
    
    
    public LineChart(WebDriver driver, WebElement chart,String series) {
        	super(driver, chart,series);
        	enableSeries();
        	this.trackerElement = chart.findElement(By.cssSelector("g.highcharts-tracker > g:nth-child("+seriesIndex+") > path"));
            //this.seriesElement = chart.findElement(By.cssSelector("g.highcharts-series-group > g:nth-child("+seriesIndex+") > path:nth-child(2)"));
        	this.seriesElement = chart.findElement(By.cssSelector("g.highcharts-series-group > g:nth-child("+seriesIndex+") > path:nth-child(1)"));
            //this.plotLineElement = chart.findElement(By.cssSelector("g.highcharts-series:nth-child("+seriesIndex+") > path:nth-child(2)"));
            this.seriesBaseElement = chart.findElement(By.cssSelector("g.highcharts-series-group > g:nth-child("+seriesIndex+")"));
            //this.xAxis = chart.findElements(By.cssSelector("g.highcharts-axis-labels")).get(0);
            //this.xAxis = chart.findElements(By.cssSelector("g.highcharts-axis")).get(0);
            //for PAM
            //this.seriesRectElement = chart.findElement(By.cssSelector("rect:nth-child(2)"));
            this.seriesRectElement = trackerElement;
            this.xAxis = getXAxisElement();
            if(this.xAxis == null) throw new org.openqa.selenium.NoSuchElementException("xaxis not found");
		
    }
    
    public boolean isTrackerDisplayed() {
        return wait.until(visibilityOf(chart.findElement(By.cssSelector("g.highcharts-tracker > g:nth-child("+seriesIndex+") > path")))) != null;
    }
    
    //public void hoverOverPointOfGraphAtXAxisLabel(String xAxisLabelValue) {
    public void mouseOverOnPointAtXAxisLabel(String xAxisLabel) {
        int labelIndex = getXAxisLabelsText().indexOf(xAxisLabel);
        mouseOverOnPointAtXAxisPosition(labelIndex);
    }
    public void mouseOverOnPointAtXAxisPosition(int point) {
    
    	int xRect = ((Locatable) seriesRectElement).getCoordinates().inViewPort().getX();
        int yRect = ((Locatable) seriesRectElement).getCoordinates().inViewPort().getY();
        int xHoverPoint = xRect + getXAxisOffset(point).getX();
        int yHoverPoint = yRect + getSeriesOffset().getY() + getPlotPoint(point).getY();
        
        //For browsers not supporting native events
        //javascript.callEmbeddedSelenium(driver, "triggerEvent", elementToHoverOver, "mouseover");
        //For browsers supporting native events
        
        xHoverPoint = xHoverPoint - ((Locatable) trackerElement).getCoordinates().inViewPort().getX();
        yHoverPoint = yHoverPoint - ((Locatable) trackerElement).getCoordinates().inViewPort().getY();
        performAction.moveToElement(seriesElement).moveToElement(trackerElement, xHoverPoint, yHoverPoint).perform();
        
    }

    private com.test.selenium.testscript.Point getPlotPoint(int point){
    	HashMap<Integer, com.test.selenium.testscript.Point> pp = getSeriesPoints();
    	if (point < 0 || point >= pp.size()) {
        	return new com.test.selenium.testscript.Point(new BigDecimal(0.0), new BigDecimal(0.0));
        }
        return getSeriesPoints().get(point);
    }

    public HashMap<Integer, com.test.selenium.testscript.Point> getSeriesPoints() {
        HashMap<Integer, com.test.selenium.testscript.Point> seriesPoints = new HashMap<Integer, com.test.selenium.testscript.Point>();
        String[] seriesPointsArray = seriesElement.getAttribute("d").replaceAll("M", "").split("L|C");
        for (int seriesPoint = 0; seriesPoint < seriesPointsArray.length; seriesPoint++) {
            String[] pointData = seriesPointsArray[seriesPoint].trim().split(" ");
            seriesPoints.put(seriesPoint, new com.test.selenium.testscript.Point(BigDecimal.valueOf(Double.valueOf(pointData[pointData.length - 2])), BigDecimal.valueOf(Double.valueOf(pointData[pointData.length - 1]))));
        }
        return seriesPoints;
    }

    private com.test.selenium.testscript.Point getSeriesOffset() {
    	
    	String[] points = seriesBaseElement.getAttribute("transform").split(",");
        BigDecimal xOffset = BigDecimal.valueOf(Integer.valueOf(points[0].replaceAll("[^\\d]", "")).doubleValue());
        BigDecimal yOffset = BigDecimal.valueOf(Integer.valueOf(points[1].replaceAll("[^\\d]", "")).doubleValue() - 1);
        return new com.test.selenium.testscript.Point(xOffset, yOffset);
    }
    
    //enable the series plot that we are interested in
    
    public void enableSeries(){
    	List<WebElement> legendEls = legendElement.findElements(By.cssSelector("text tspan"));
    	for(int i=0;i<legendEls.size();i++){
    		WebElement tracker = driver.findElement(By.cssSelector("g.highcharts-tracker > g:nth-child("+(i+1)+") > path"));
    		if(tracker.getAttribute("visibility").equals("visible")){
    			legendEls.get(i).click();
    		}
    	}
    	legendEls.get(seriesIndex-1).click();
    	
    }
    
    private com.test.selenium.testscript.Point getXAxisOffset(int pointNumber) {
    	List<WebElement> xAxisLabels = xAxis.findElements(By.cssSelector("text"));
        WebElement xPoint = xAxisLabels.get(pointNumber);
        BigDecimal xOffset = BigDecimal.valueOf(Double.parseDouble(xPoint.getAttribute("x")));
        BigDecimal yOffset = BigDecimal.valueOf(Double.parseDouble(xPoint.getAttribute("y")));
        return new com.test.selenium.testscript.Point(xOffset, yOffset);
    }
}
