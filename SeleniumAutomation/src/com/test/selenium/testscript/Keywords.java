package com.test.selenium.testscript;


/*
import static testscript.DriverScript.APP_LOGS;
import static testscript.DriverScript.CONFIG;
import static testscript.DriverScript.OR;
import static testscript.DriverScript.currentTestSuiteXLS;
import static testscript.DriverScript.currentTestCaseName;
import static testscript.DriverScript.currentTestDataSetID;
 */

import static com.ge.selenium.testscript.TestDriver.APP_LOGS;
import static com.ge.selenium.testscript.TestDriver.CONFIG;
import static com.ge.selenium.testscript.TestDriver.UIMap;
import static com.ge.selenium.testscript.TestDriver.testResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.DesktopScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenRegion;
import org.sikuli.api.Target;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.desktop.DesktopMouse;

import com.test.selenium.utils.DBUtil;
import com.test.selenium.utils.TestUtil;
import com.google.common.base.Function;
import com.thoughtworks.selenium.Selenium;
//import com.ge.selenium.sikuli.ImageElement;
//import com.ge.selenium.sikuli.SikuliFirefoxDriver;
//import com.evse.util.DBUtil;
public class Keywords {

	public WebDriver driver;
	public EventFiringWebDriver e_driver;
	public DBUtil dbutil;
	public Wait<WebDriver> driverWait;
	public int globalX=50;
	public int globalY=0;
	public long implicitWaitTime;
	public ArrayList<ArrayList<String>> dbResult = new ArrayList<ArrayList<String>>();
	public HashMap<Integer,List<String>> barChartData;
	public List<String> chartLegend;
	public List<String> chartYAxis;
	public String seriesColor;
	public boolean demoMode=false;
	public Keywords(){
		//dbutil = new DBUtil(CONFIG.getProperty("DB_CONNECTION"),CONFIG.getProperty("DB_USER"),CONFIG.getProperty("DB_PWD"));
		//dbutil = new DBUtil();
	}

	/*##############################################################################

 Function Name : scrollDown
 Description   : Scroll the bar on the page
 Input Params  : Object(of the page),data(how many units you need to scroll)
 Output Params :
 Author        : Ravi Markil (9/3/13)
 Updated by    :  

 ############################################################################## */

	public String scrollDown(String object,String data){		
		APP_LOGS.debug("Scrolling down the side bar");
		//int data1 = Integer.parseInt(data);
		try{
			e_driver=new EventFiringWebDriver(driver);
			e_driver.executeScript("scroll(0,"+data+")");

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to scroll";
		}
		return Constants.KEYWORD_PASS;
	}


	/*##############################################################################

 Function Name : verifyParticularListElement
 Description   : Verify particular element present in the list
 Input Params  : Object(of the page),data(particular list element name)
 Output Params :
 Author        : Ravi Markil (9/3/13)
 Updated by    :  

 ############################################################################## */

	public String verifyParticularListElement(String object, String data){

		String locator=UIMap.getProperty(object);
		int c=0;
		APP_LOGS.debug("Verify particular element from a list -- Locator is "+locator);
		WebElement select = getWebElement(object,data);
		if(select!=null)
		{
			List<WebElement> droplist_contents = select.findElements(By.tagName("option"));

			for(int i=0;i<droplist_contents.size();i++)
			{
				if(data.equals(droplist_contents.get(i).getText().trim()))
				{	
					c++;
					return Constants.KEYWORD_PASS;

				}
			}

		}
		else
		{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Item not found in  List -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Item not found in  List -- Element "+locator+" Not Found";
		}	
		if (c==1)
		{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Item not found in  List -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Item not found in  List -- Element "+locator+" Not Found";
		}
		return data;

	}		

	/*##############################################################################

Function Name : openBrowser
Description   : open the browser
Input Params  : Object(of the page),data
Output Params :
Author        : Pradeep (12/3/12)
Updated by    :  

############################################################################## */

	public String openBrowser(String object,String data) {
		APP_LOGS.debug("Opening browser");
		String demoProp = CONFIG.getProperty("demoMode");
		if(demoProp!=null){
			if(demoProp.equalsIgnoreCase("y")){
				demoMode = true;
			}
		}
		String prof=CONFIG.getProperty("firefoxProfile");
		if(data.equalsIgnoreCase("firefox")){
			if(prof==null){
				driver=new FirefoxDriver();
			}else{
				/*
				File profileDirectory = new File(prof);
				FirefoxProfile profile = new FirefoxProfile(profileDirectory);  
				//profile.setEnableNativeEvents(true);
				driver = new FirefoxDriver(profile);
				 */
				//FirefoxBinary fb = new FirefoxBinary();
				//fb.setTimeout(java.util.concurrent.TimeUnit.SECONDS.toMillis(90));

				File profileDirectory = new File(prof);
				FirefoxProfile profile = new FirefoxProfile(profileDirectory);
				DesiredCapabilities capability = DesiredCapabilities.firefox();
				capability.setCapability(FirefoxDriver.PROFILE, profile);
				capability.setCapability("handlesAlerts", false);
				//CapabilityType.SUPPORTS_ALERTS
				//capability.setCapability(CapabilityType.SUPPORTS_ALERTS,true);
				capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,UnexpectedAlertBehaviour.ACCEPT);
				driver =new FirefoxDriver(capability);


			}
		}	
		else if(data.equalsIgnoreCase("ie")){
			String iepath=CONFIG.getProperty("iedriverPath");
			if(!iepath.endsWith("/") || !iepath.endsWith("//")){
				iepath=iepath+"/";
			}
			File file = new File(iepath+ "libraries/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			driver = new InternetExplorerDriver();
		} else if(data.equalsIgnoreCase("chrome")){
			String chromepath=CONFIG.getProperty("chromedriverPath");
			if(!chromepath.endsWith("/") || !chromepath.endsWith("//")){
				chromepath=chromepath+"/";
			}
			File file;
			if(System.getProperty("os.name").startsWith("Windows")){
				file = new File(chromepath+ "libraries/chromedriver.exe");
			}else{
				file = new File(chromepath+"chromedriver");
			}
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			driver=new ChromeDriver();
		} else if(data.equalsIgnoreCase("phantom")){
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
			capabilities.setCapability("browserType", "phantomjs");
		    capabilities.setCapability("driverName", "ghostdriver");
		    capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/bin/phantomjs");
			driver=new PhantomJSDriver(capabilities);
		}
		if(CONFIG.getProperty("implicitwait") != null){
			implicitWaitTime=Long.parseLong(CONFIG.getProperty("implicitwait"));
		}

		//driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		//maximizeAppWindow("","");
		//driver.manage().deleteAllCookies();

		driver.manage().window().maximize();
		return Constants.KEYWORD_PASS;

	}

	/*##############################################################################

	Function Name : selectFileUpload
	Description   : Upload a selected file(specific to ALSP proj)
	Input Params  : Object(of the page),data
	Output Params :
	Author        : Pradeep (12/3/12)
	Updated by    :  

	############################################################################## */
	public String selectFileUpload(String object,String data){		
		APP_LOGS.debug("Select file and upload");
		try{

			driver.findElement(By.id(object)).sendKeys(data);	

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- File upload failed";
		}
		return Constants.KEYWORD_PASS;
	}

	/*##############################################################################

	Function Name : navigate
	Description   : Navigate to specific URL()
	Input Params  : Object(of the page),data
	Output Params :
	Author        : Pradeep (12/3/12)
	Updated by    :  

	############################################################################## */

	public String navigate(String object,String data){		
		APP_LOGS.debug("Navigating to URL");
		try{
			driver.navigate().to(data);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}



	/*##############################################################################

	 Function Name : scrollUp
	 Description   : Scroll up the bar on the page
	 Input Params  : Object(of the page),data(how many units you need to scroll)
	 Output Params :
	 Author        : Ravi Markil (10/4/13)
	 Updated by    :  

	 ############################################################################## */
	public String scrollUp(String object,String data){
		APP_LOGS.debug("Scrolling down the side bar");
		//int data1 = Integer.parseInt(data);
		try{
			e_driver=new EventFiringWebDriver(driver);
			e_driver.executeScript("scroll("+data+",0)");

		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to scroll";
		}
		return Constants.KEYWORD_PASS;
	}



	/*##############################################################################

	Function Name : wait
	Description   : wait till object is visible
	Input Params  : Object(of the page),data
	Output Params :
	Author        : Ravi Markil (9/10/13)
	Updated by    :  

	############################################################################## */

	public String testWait(String object,String data){		
		APP_LOGS.debug("Waiting for the object to be visible");
		getWebElement(object,data);
		return Constants.KEYWORD_PASS;
	}

	/*##############################################################################

	Function Name : clickMenu
	Description   : Click or Select Menu item
	Input Params  : Object(of the page),data
	Output Params :
	Author        : Pradeep Aerram(12/3/12)
	Updated by    :  

	############################################################################## */
	public String clickMenu(String object,String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Clicking on Menu Item -- Locator is "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.click();
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to click on link -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to click on link -- Element "+locator+" Not Found";
		}	

	}

	/*##############################################################################

	Function Name : clickLink
	Description   : Click a link
	Input Params  : Object(of the page),data
	Output Params :
	Author        : Pradeep Aerram(12/3/12)
	Updated by    :  

	############################################################################## */

	public String clickLink(String object,String data){

		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Clicking on link -- Locator is "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.click();
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to click on link -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to click on link -- Element "+locator+" Not Found";
		}	

	}


	/*
	public String clickLink(String object,String data){
        APP_LOGS.debug("Clicking on link ");
        String locator = UIMap.getProperty(object);
        if(!data.equals("")){
        	locator = locator.replace("DATA", data);
        	System.out.println("Locator = "+locator+" Data "+data);
        }
        try{
        	driver.findElement(By.xpath(locator)).click();
        }catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
        }

		return Constants.KEYWORD_PASS;
	}
	 */

	/*##############################################################################

	Function Name : clickLink_linkText
	Description   : Click link with particular text
	Input Params  : Object,data(text of the link)
	Output Params :
	Author        : Pradeep Aerram(12/3/12)
	Updated by    :  

	############################################################################## */


	public String clickLink_linkText(String object,String data){
		APP_LOGS.debug("Clicking on link ");
		try{
			driver.findElement(By.linkText(UIMap.getProperty(object))).click();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}
	/*
	public String clickMenu(String object,String data){
        APP_LOGS.debug("Clicking on Menu Item ");
        String locator = UIMap.getProperty(object);
        System.out.println("Object = "+object);
        System.out.println("Locator = "+locator);
        //driver.findElement(By.linkText(OR.getProperty(object))).click();
     	try {
			if(locator.startsWith("//")){
				if(waitForElement(locator))
				driver.findElement(By.xpath(locator)).click();
	    	}else{
	    		if(waitForElement(locator))
				driver.findElement(By.id(locator)).click();
	    	}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to click on link"+e.getMessage();

		}	
		return Constants.KEYWORD_PASS;
	}
	 */

	/*##############################################################################

	Function Name : writeInInput
	Description   : Set value in edit field
	Input Params  : Object,data(text to be set)
	Output Params :
	Author        : Pradeep Aerram(12/3/12)
	Updated by    :  

	############################################################################## */

	public  String writeInInput(String object,String data){
		//String locator=UIMap.getProperty(object.split(",")[0]);
		String locator=getLocator(object);
		APP_LOGS.debug("Writing in text box:: Object is "+locator+" Data is "+data);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.clear();
			element.sendKeys(data);
			return Constants.KEYWORD_PASS;
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Unable to write  -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Unable to write  -- Element "+locator+" Not Found";
		}

	}

	protected String getLocator(String object){
		String locator="";
		String obj;
		obj = object.split(",")[0];
		if(obj.startsWith("testid")){
			locator="//*[@testid='"+obj.split(":")[1]+"']";
		}else{
			locator = UIMap.getProperty(obj);
		}
		return locator;
	}
	/*##############################################################################

	Function Name : getElementByLocator
	Description   : Get element by locator
	Input Params  : locator
	Output Params :
	Author        : Pradeep Aerram(12/3/12)
	Updated by    :  

	############################################################################## */

	protected By getElementByLocator(String locator)
	{
		By by = null;

		if(locator.startsWith("//"))
			by = By.xpath(locator);
		else if(locator.startsWith("css:"))
			by = By.cssSelector(locator.substring(4));
		else
			by = By.id(locator);

		return by;
	}

	protected WebElement getWebElement(final String object,String data)
	{
		//driverWait = new WebDriverWait(driver, implicitWaitTime)
		String explicitwait = CONFIG.getProperty("explicitwait");
		long waittime=0;
		if(explicitwait == null){
			waittime=15;//default wait time 
		}else{
			//waittime = Integer.parseInt(explicitwait);
			waittime = Long.valueOf(explicitwait).longValue();
		}
		//driverWait = new WebDriverWait(driver, 25)
		driverWait = new WebDriverWait(driver, waittime)
		//.withTimeout(15, TimeUnit.SECONDS)
		//.pollingEvery(5, TimeUnit.SECONDS)
		.ignoring(StaleElementReferenceException.class)
		.ignoring(org.openqa.selenium.TimeoutException.class);
		String locator="";
		boolean flag;
		WebElement element = null;
		//code to read objects directly from xl 10-21-13
		String obj = object.split(",")[0];
		if(obj.startsWith("testid")){
			locator="//*[@testid='"+obj.split(":")[1]+"']";
		}else{
			locator = UIMap.getProperty(obj);
		}

		//locator=UIMap.getProperty(object.split(",")[0]);
		if(locator == null){
			APP_LOGS.debug("Object property not defined in UIMap or not passed in XL");
			return element;
		}
		APP_LOGS.debug("Locator = "+locator+" Data "+data);
		if(!data.equals("")){
			locator = locator.replace("DATA", data);
			APP_LOGS.debug("Locator = "+locator+" Data "+data);
		}
		final By by = getElementByLocator(locator);
		if(object.contains(",")){
			/*
				try{
					element = driverWait.until(new ExpectedCondition<List<WebElement>>() {  
					 	public List<WebElement> apply(WebDriver d) {  
						APP_LOGS.debug("Waiting for Elements to completely load ...");
						try{
							return d.findElements(by);
						}catch(org.openqa.selenium.NoSuchElementException exc){
							APP_LOGS.debug("Failed to Load Element ..."+exc);
							return null;
						}	
						}
					 }).get(Integer.parseInt(object.split(",")[1]));
				}catch(org.openqa.selenium.TimeoutException ex){
					APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
					return null;
				}
			 */
			try{
				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {  
						APP_LOGS.debug("Waiting for Elements to completely load ...");  
						APP_LOGS.debug("element = "+d.findElements(by).get(Integer.parseInt(object.split(",")[1])).getText());
						return d.findElements(by).get(Integer.parseInt(object.split(",")[1])).isDisplayed();
					}  
				});
			}catch(org.openqa.selenium.TimeoutException ex){
				APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
				return null;
			}
			if(flag){
				element = driver.findElements(by).get(Integer.parseInt(object.split(",")[1]));
			}
		}else{
			try{
				//element = driverWait.until(new ExpectedCondition<WebElement>() {
				//element = driverWait.until(ExpectedConditions.elementToBeClickable(by));

				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {  
						APP_LOGS.debug("Waiting for Elements to completely load ...");  
						//try{
						return d.findElement(by).isDisplayed();
						//return d.findElement(by).isEnabled();
						//	}catch(org.openqa.selenium.NoSuchElementException exc){
						//		APP_LOGS.debug("Failed to Load Element ..."+exc);
						//		return false;
						//	}
					}  
				});
			}catch(org.openqa.selenium.TimeoutException ex){
				APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
				return null;
			}
			if(flag){
				element = driver.findElement(by);
				if(demoMode){
					highlightElement(element);
				}
			}
		}
		return element;
	}

	protected boolean getElementDisplayStatus(final String object,String data)
	{
		//driverWait = new WebDriverWait(driver, implicitWaitTime)
		driverWait = new WebDriverWait(driver, 25)
		//.withTimeout(15, TimeUnit.SECONDS)
		//.pollingEvery(5, TimeUnit.SECONDS)
		.ignoring(org.openqa.selenium.TimeoutException.class);
		String locator="";
		boolean flag;
		//code to read objects directly from xl 10-21-13
		String obj = object.split(",")[0];

		if(obj.startsWith("testid")){
			locator="//*[@testid='"+obj.split(":")[1]+"']";
		}else{
			locator = UIMap.getProperty(obj);
		}

		//locator=UIMap.getProperty(object.split(",")[0]);
		if(locator == null){
			APP_LOGS.debug("Object property not defined in UIMap or not passed in XL");
			return false;
		}
		//locator=UIMap.getProperty(object.split(",")[0]);
		APP_LOGS.debug("Locator = "+locator+" Data "+data);
		if(!data.equals("")){
			locator = locator.replace("DATA", data);
			APP_LOGS.debug("Locator = "+locator+" Data "+data);
		}
		final By by = getElementByLocator(locator);
		if(object.contains(",")){
			try{
				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {  
						APP_LOGS.debug("Waiting for Elements to completely load ...");  
						APP_LOGS.debug("element = "+d.findElements(by).get(Integer.parseInt(object.split(",")[1])).getText());
						return d.findElements(by).get(Integer.parseInt(object.split(",")[1])).isDisplayed();
					}  
				});
			}catch(org.openqa.selenium.TimeoutException ex){
				APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
				return false;
			}
		}else{
			try{
				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {  
						APP_LOGS.debug("Waiting for Elements to completely load ...");  
						//return d.findElement(by).isDisplayed();
						if(demoMode){
							highlightElement(d.findElement(by));
						}
						return d.findElement(by).isEnabled();
					}  
				});
			}catch(org.openqa.selenium.TimeoutException ex){
				APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
				return false;
			}
		}
		return flag;
	}


	protected List<WebElement> getWebElements(String locator)
	{
		final By by = getElementByLocator(locator);
		driverWait = new WebDriverWait(driver, implicitWaitTime)
		.withTimeout(15, TimeUnit.SECONDS)
		.pollingEvery(5, TimeUnit.SECONDS)
		.ignoring(org.openqa.selenium.TimeoutException.class);
		List<WebElement> eleList = null;
		try{
			eleList = driverWait.until(new ExpectedCondition<List<WebElement>>() {  
				public List<WebElement> apply(WebDriver d) {  
					APP_LOGS.debug("Waiting for Elements to completely load ...");
					try{
						return d.findElements(by);
					}catch(org.openqa.selenium.NoSuchElementException exc){
						APP_LOGS.debug("Failed to Load Element ..."+exc);
						return null;
					}	
				}
			});
		}catch(org.openqa.selenium.TimeoutException ex){
			APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
			return null;
		}
		System.out.println("Size = "+eleList.size());
		return eleList;
	}

	/*
	protected List<WebElement> getWebElements(String locator)
	{
		final By by = getElementByLocator(locator);
		List<WebElement> eleList = null;
		if(waitForElement(by)){
			try{
				eleList = driver.findElements(getElementByLocator(locator));
			}catch(org.openqa.selenium.NoSuchElementException Ex){
				APP_LOGS.debug("Failed to Load Element ..."+Ex);
			}
		}
		return eleList;
	}
	 */
	public boolean waitForElement(final By by) {

		boolean flag = true;
		try{
			driverWait = new WebDriverWait(driver, implicitWaitTime);
			WebElement ele = driverWait.until(new ExpectedCondition<WebElement>() {  
				public WebElement apply(WebDriver d) {  
					APP_LOGS.debug("Waiting for Elements to completely load ...");  
					return d.findElement(by);
				}  
			});  
		}catch(org.openqa.selenium.NoSuchElementException Ex){
			flag = false;
			APP_LOGS.debug("Failed to Load Element ..."+Ex);
		}
		return flag;

	}


	public  String clickButton(String object,String data){

		//String locator=UIMap.getProperty(object);
		String locator=getLocator(object);
		APP_LOGS.debug("Clicking on Button ");
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.click();
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to click on Button -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to click on Button -- Element "+locator+" Not Found";
		}
	}


	public String sendKeystroke(String object, String data){
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE);
		return Constants.KEYWORD_PASS;
	}

	public String setListItem(String object, String item){
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Selecting Item from List -- Locator is "+locator);
		WebElement select = getWebElement(object,item);
		if(select != null){
			//WebElement option = select.findElement(By.xpath("//option[text()='" + item + "']"));
			try{
				WebElement option = select.findElement(By.xpath("//option[contains(text(),'" + item.trim() + "')]"));
				option.click();
				return Constants.KEYWORD_PASS;
			}catch(org.openqa.selenium.NoSuchElementException e){
				return Constants.KEYWORD_FAIL+" Item not found in List -- Locator is "+locator +" Error = " +e.getMessage();
			}
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to Select Item From List -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to Select Item From List -- Element "+locator+" Not Found";
		}

	}

	public  String selectList(String object, String data){
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Selecting from list -- Locator is "+locator);
		WebElement select = getWebElement(object,data);
		if(select != null){
			select.sendKeys(data.trim());
			return Constants.KEYWORD_PASS;
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to Select Item From List -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to Select Item From List -- Element "+locator+" Not Found";
		}
	}

	/**
	 * This keyword help you to verify the attribute of any element
	 *
	 * @param  object  <Element object to verify the attribute>
	 * @param  data <Pass the attribute to find it out in the object>
	 * @return PASS / FAIL
	 *  Author : Vijetha Gopisetty (3/18/13)
	 */         
	public String verifyElementAttr(String object, String data){

		String attrName;
		if(data.contains(",")){
			String[] tokens = data.split(",");
			attrName = tokens[0];
			data = tokens[1];
		}else{
			attrName = data;
			data = "TRUE";
		}

		APP_LOGS.debug("verifing attribute " + attrName + " for element " + object);
		try{

			WebElement element = driver.findElement(getElementByLocator(UIMap.getProperty(object)));
			Boolean verifyTrue = "TRUE".equalsIgnoreCase(data);

			if(element == null){
				return Constants.KEYWORD_FAIL + " -- Element " + UIMap.getProperty(object) + " not found.";
			}else{
				if(element.getAttribute(attrName) != null){
					if(verifyTrue){
						return Constants.KEYWORD_PASS;
					}else{
						APP_LOGS.debug(" -- Element " + UIMap.getProperty(object) + " has " + attrName + " attribute");
						return Constants.KEYWORD_FAIL + " -- Element " + UIMap.getProperty(object) + " has " + attrName + " attribute";
					}
				}else{
					if(verifyTrue){
						APP_LOGS.debug(" -- Element " + UIMap.getProperty(object) + " has no attribute " + attrName);
						return Constants.KEYWORD_FAIL + " -- Element " + UIMap.getProperty(object) + " has no attribute " + attrName;
					}else{
						return Constants.KEYWORD_PASS;
					}
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Error trying to locate element";
		}  

	}


	public String verifyAllListElements(String object, String data) throws NumberFormatException, InterruptedException{
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Verifying all the list elements --Locator "+locator);

		WebElement select = getWebElement(object,data);
		if(select != null){
			Wait("","500");
			List<WebElement> droplist_contents = select.findElements(By.tagName("option"));
			//Remove if there is any header in the list box [like select customer or Select Manager
			if(droplist_contents.get(0).getText().startsWith("Select") || droplist_contents.get(0).getText().startsWith("select"))
				droplist_contents.remove(0);
			String temp=data;
			String allElements[]=temp.split(",");
			// check if size of array == size if list
			if(allElements.length != droplist_contents.size()){
				return Constants.KEYWORD_FAIL +"- size of UI list "+ droplist_contents.size() +" do not match with expected "+allElements.length;
			}
			for(int i=0;i<droplist_contents.size();i++){
				if(!allElements[i].trim().equals(droplist_contents.get(i).getText().trim())){
					return Constants.KEYWORD_FAIL +"- List Item Value "+droplist_contents.get(i).getText()+" did not match with Expected value - "+allElements[i];
				}
			}
			return Constants.KEYWORD_PASS;
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find List Element -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL +" - Could not find List Element. "+locator;
		}
	}

	public  String verifyListSelection(String object,String data){
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Verifying the selection of the list -- Locator "+locator);
		WebElement select = getWebElement(object,data);
		if(select != null){
			String expectedVal=data;
			List<WebElement> droplist_cotents = select.findElements(By.tagName("option"));
			String actualVal=null;
			for(int i=0;i<droplist_cotents.size();i++){
				String selected_status=droplist_cotents.get(i).getAttribute("selected");
				if(selected_status!=null)
					actualVal = droplist_cotents.get(i).getText();			
			}

			if(!actualVal.equals(expectedVal)){
				return Constants.KEYWORD_FAIL + "Value not in list - "+expectedVal;
			}
			return Constants.KEYWORD_PASS;		
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find List Element -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL +" - Could not find List Element. "+locator;
		}
	}


	public  String selectRadio(String object, String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Selecting a radio button --Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.click();
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to find radio button -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to find radio button -- Element "+locator+" Not Found";
		}
	}

	public  String verifyRadioSelected(String object, String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Verify Radio Selected Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			String checked=element.getAttribute("checked");
			if(checked==null){
				return Constants.KEYWORD_FAIL+"- Radio not selected";
			}
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to find radio button -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to find radio button -- Element "+locator+" Not Found";
		}
	}


	public  String checkCheckBox(String object,String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Checking checkbox -- Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			String checked=element.getAttribute("checked");
			if(checked==null){
				element.click();
			}
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found";
		}
	}

	public String unCheckCheckBox(String object,String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Unchecking checkBox -- Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			String checked=element.getAttribute("checked");
			if(checked!=null){
				element.click();
			}
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found";
		}
	}


	public  String verifyCheckBoxSelected(String object,String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Verifying checkbox selected --Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			String checked=element.getAttribute("checked");
			if(checked!=null){
				//code here 04/15
				if(!data.equalsIgnoreCase("False")){
					APP_LOGS.debug(Constants.KEYWORD_PASS+" Check Box Should be Checked and Its Checked");
					return Constants.KEYWORD_PASS;
				}else{
					APP_LOGS.debug(Constants.KEYWORD_FAIL+" Check Box Should NOT be Checked BUT Its Checked");
					return Constants.KEYWORD_FAIL+" Check Box Should NOT be Checked BUT Its Checked";
				}
			}else{
				if(data.equalsIgnoreCase("False")){
					APP_LOGS.debug(Constants.KEYWORD_PASS+" Check Box Should NOT be Checked and Its NOT Checked");
					return Constants.KEYWORD_PASS;
				}else{
					APP_LOGS.debug(Constants.KEYWORD_FAIL+" Check Box Should NOT be Checked BUT Its Checked");
					return Constants.KEYWORD_FAIL+" Check Box Should NOT be Checked BUT Its Checked";
				}
			}
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find checkbox -- Element "+locator+" Not Found";
		}

	}


	public String verifyText(String object, String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Verifying the text --Locator "+locator);
		WebElement element = getWebElement(object,data);
		String actual="";
		String expected=data.trim();
		if(element != null){
			actual=element.getText().trim();
			//if(actual.contains(expected)){
			if(actual.equals(expected)){
				return Constants.KEYWORD_PASS;
			}else{
				return Constants.KEYWORD_FAIL+" -- text not verified Actual "+actual+" Expected -- "+expected;
			}
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found";
		}
	}

	//hrer

	public  String writeInFileInput(String object,String data){
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Writing in file input box --Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.sendKeys(data);
			return Constants.KEYWORD_PASS;
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found";
		}
	}	

	public  String verifyTextinInput(String object,String data){
		String locator=UIMap.getProperty(object);
		APP_LOGS.debug("Verifying the text in input box --Locator "+locator);
		String actual;
		WebElement element = getWebElement(object,data);
		if(element != null){
			actual=element.getAttribute("value").trim();
			String expected=data.trim();

			if(actual.equals(expected)){
				return Constants.KEYWORD_PASS;
			}else{
				APP_LOGS.debug(Constants.KEYWORD_FAIL+" Expected Data "+data+ " Did Not Match with Actual Data "+actual);
				return Constants.KEYWORD_FAIL+" Expected Data "+data+ " Did Not Match with Actual Data "+actual;
			}
		}
		else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Could not find Element "+locator+" Not Found";
		}
	}

	public  String clickImage(){
		APP_LOGS.debug("Clicking the image");

		return Constants.KEYWORD_PASS;
	}

	public  String verifyFileName(){
		APP_LOGS.debug("Verifying inage filename");

		return Constants.KEYWORD_PASS;
	}


	public  String verifyTitle(String object, String data){
		APP_LOGS.debug("Verifying title");
		try{
			String actualTitle= driver.getTitle();
			String expectedTitle=data;
			if(actualTitle.equals(expectedTitle))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL+" -- Title not verified "+expectedTitle+" -- "+actualTitle;
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Error in retrieving title";
		}		
	}


	public String isElementExist(String object, String data) {
		//String locator=UIMap.getProperty(object);
		String locator=getLocator(object);
		String mainData = data;
		APP_LOGS.debug("Verifying the text in input box --Locator "+locator);
		//if data field has element data & true or false like Dashboard,TRUE
		data = data.split(",")[0];
		WebElement element = getWebElement(object,data);
		if(mainData.contains(",")){
			data = mainData.split(",")[1];
		}
		if(element != null){
			if(!data.equalsIgnoreCase("False")){
				APP_LOGS.debug(Constants.KEYWORD_PASS+" -- Element "+locator+" Should Exist and Found");
				return Constants.KEYWORD_PASS;
			}else{
				APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Element "+locator+" Should Not Exist But Found");
				return Constants.KEYWORD_FAIL+" -- Element "+locator+" Should Not Exist But Found";
			}
		}
		else{
			if(data.equalsIgnoreCase("False")){
				APP_LOGS.debug(Constants.KEYWORD_PASS+" -- Element "+locator+" Should Not Exist");
				return Constants.KEYWORD_PASS;
			}else{
				APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Element "+locator+" Should Exist But Not Found");
				return Constants.KEYWORD_FAIL+" -- Element "+locator+" Should Exist But Not Found";
			}
		}	
	}

	public String isElementEnabled(String object, String data) {
		//String locator=UIMap.getProperty(object);
		String locator=getLocator(object);
		String mainData = data;
		APP_LOGS.debug("Verifying the text in input box --Locator "+locator);
		//if data field has element data & true or false like Dashboard,TRUE
		data = data.split(",")[0];
		boolean flag = getElementDisplayStatus(object,data);
		if(mainData.contains(",")){
			data = mainData.split(",")[1];
		}
		if(flag){
			if(!data.equalsIgnoreCase("False")){
				APP_LOGS.debug(Constants.KEYWORD_PASS+" -- Element "+locator+" Should be Enabled");
				return Constants.KEYWORD_PASS;
			}else{
				APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Element "+locator+" Should be Enabled But Disabled");
				return Constants.KEYWORD_FAIL+" -- Element "+locator+" Should be Enabled But Disabled";
			}
		}
		else{
			if(data.equalsIgnoreCase("False")){
				APP_LOGS.debug(Constants.KEYWORD_PASS+" -- Element "+locator+" Should be Disabled");
				return Constants.KEYWORD_PASS;
			}else{
				APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Element "+locator+" Should be Disabled but its Enabled");
				return Constants.KEYWORD_FAIL+" Element "+locator+" Should be Disabled but its Enabled";
			}
		}	
	}

	public String existInMany(String object,String data){
		APP_LOGS.debug("Checking existance of element in Multiple Elements");
		String locator=UIMap.getProperty(object);
		List<WebElement> ele_list;
		List<String> eleText = new ArrayList<String>();

		try {
			if(locator.startsWith("//")){
				ele_list = driver.findElements(By.xpath(locator));
			}else{
				ele_list = driver.findElements(By.id(locator));
			}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+" Object doest not exist";
		}

		for(WebElement ele: ele_list){
			eleText.add(ele.getText().trim());
		}
		if(eleText.contains(data)){
			return Constants.KEYWORD_PASS; 
		}else{
			return Constants.KEYWORD_FAIL+" "+object+" Object doest not exist";
		}


	}

	public static String Wait(String object, String data) throws NumberFormatException, InterruptedException{

		//if(data!=null){
		if(!data.trim().equals("")){
			APP_LOGS.debug("Waiting for  "+ data);
			Thread.sleep(Long.parseLong(data)); 
		}else{
			APP_LOGS.debug("Waiting for  5000");
			Thread.sleep(5000);
		}
		return Constants.KEYWORD_PASS;
	}

	public  String click(String object,String data){
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Clicking on any element --Locator "+locator);
		WebElement element = getWebElement(object,data);
		if(element != null){
			element.click();
			return Constants.KEYWORD_PASS;
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Not able to click on -- Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Not able to click on  -- Element "+locator+" Not Found";
		}
	}

	public  String synchronize(String object,String data){
		APP_LOGS.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript(
				"function pageloadingtime()"+
						"{"+
						"return 'Page has completely loaded'"+
						"}"+
				"return (window.onload=pageloadingtime());");

		return Constants.KEYWORD_PASS;
	}

	public  String waitForElementVisibility(String object,String data){
		APP_LOGS.debug("Waiting for an element to be visible");
		int start=0;
		int time=(int)Double.parseDouble(data);
		try{
			while(time == start){
				if(driver.findElements(By.xpath(UIMap.getProperty(object))).size() == 0){
					Thread.sleep(1000L);
					start++;
				}else{
					break;
				}
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	public  String closeBrowser(String object, String data){
		APP_LOGS.debug("Closing the browser");
		try{
			driver.quit();
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+"Unable to close browser. Check if its open"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	public String navigateBack(String object,String data)
	{
		String result;

		String currentTitle= driver.getTitle();

		driver.navigate().back();

		if(driver.getTitle().equals(currentTitle))
			result = Constants.KEYWORD_FAIL;
		else
			result = Constants.KEYWORD_PASS;

		return result;
	}

	/**
	 *	Method to reload the page
	 */
	public String refreshThePage(String object, String data){

		driver.navigate().refresh();
		APP_LOGS.debug("Page refreshed");
		return Constants.KEYWORD_PASS;
	}

	public String pause(String object, String data) throws NumberFormatException, InterruptedException{
		long time = (long)Double.parseDouble(object);
		Thread.sleep(time*1000L);
		return Constants.KEYWORD_PASS;
	}

	public String maximizeAppWindow(String object, String data)
	{
		try
		{
			driver.manage().window().setPosition(new Point(0,0));
			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
			driver.manage().window().setSize(dim);
		}
		catch(Exception e)
		{
			return Constants.KEYWORD_FAIL;
		}

		return Constants.KEYWORD_PASS;
	}

	public String updateDB(String object,String data) throws SQLException{
		//dbutil.runUpdateSQL(data);
		DBUtil.runUpdateSQL(data);
		return Constants.KEYWORD_PASS;
	}

	public String dataSetup(String object, String data) throws SQLException{
		String file="";
		String sqlFilePath=CONFIG.getProperty("SqlFilePath");
		if(data.contains(".sql")){
			if(sqlFilePath!= null){
				if(!sqlFilePath.endsWith("/") || !sqlFilePath.endsWith("//")){
					sqlFilePath=sqlFilePath+"/";
				}
				file=sqlFilePath+data.trim();
			}else{
				file=data.trim();
			}
			//return dbutil.runSQLFile(file);
			return DBUtil.runSQLFile(file);
		}else{
			//return dbutil.runUpdateSQL(data);
			return DBUtil.runUpdateSQL(data);
		}
	}
	public String handlePopUp(String object, String data){
		/*
		try {
			Alert alert = driver.switchTo().alert();
			WebDriver pp = driver.switchTo().defaultContent();
			alert.accept();
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+"Unable to Handler the PopUp. Check if its open"+e.getMessage();		
		}
		 */
		WebDriver popup = null;
		try {
			String pop = driver.getWindowHandle();
			popup = driver.switchTo().window(pop);
			popup.findElement(By.xpath("//button[contains(text(),'"+data+"')]")).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Constants.KEYWORD_FAIL+"Unable to Handler the PopUp. Check if its open"+e.getMessage();
		}


		return Constants.KEYWORD_PASS;
	}

	public String verifyNewPageExist(String object,String data){
		//WebDriverWait wait = new WebDriverWait(driver, 5);
		//wait.until(isNewPageExist())
		try{
			boolean flag = driverWait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {  
					APP_LOGS.debug("Waiting for Elements to completely load ...");  
					String base = driver.getWindowHandle();
					Set <String> set = driver.getWindowHandles();
					set.remove(base);
					if(set.size()==1)
					{
						driver.switchTo().window((String) set.toArray()[0]);
						driver.close();
						driver.switchTo().window(base);
						return true;
					}
					return false;
				}  
			});
		}catch(org.openqa.selenium.TimeoutException ex){
			APP_LOGS.debug("Failed to Load Element after 5 min ..."+ex);
			return null;
		}
		return Constants.KEYWORD_PASS;

	}

	public boolean isNewPageExist(){
		String base = driver.getWindowHandle();
		Set <String> set = driver.getWindowHandles();
		set.remove(base);
		if(set.size()==1)
		{
			driver.switchTo().window((String) set.toArray()[0]);
			driver.close();
			return true;
		}
		return false;
	}

	/*
	private static Function<WebDriver,WebElement> isNewPageExist() {
	    return new Function<WebDriver, WebElement>() {
	        @Override
	        public WebElement apply(WebDriver driver) {
	        	String base = driver.getWindowHandle();
	            Set <String> set = driver.getWindowHandles();
	            set.remove(base);
	            assert set.size()==1;
	            driver.switchTo().window((String) set.toArray()[0]);
	            driver.close();
	            return true;
	        }
	    };
	}
	 */

	public String handleAlert(String object, String data){
		//		JavascriptExecutor js = (JavascriptExecutor) driver; 
		//		js.executeScript("window.confirm =function(msg){return true;};");
		((JavascriptExecutor)driver).executeScript("window.confirm = function(msg){return true;};");
		try {
			Alert alert = driver.switchTo().alert();
			if(!data.equals("Reject")){
				System.out.println(alert.getText());
				alert.accept();
			}	
			else{
				alert.dismiss();
			}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+"Unable to Handler the Alert. Check if its open"+e.getMessage();		
		}

		return Constants.KEYWORD_PASS;
	}

	public String updatePage(String pageObject, String data) throws NumberFormatException, InterruptedException{
		//object ex: user_table,TextBox:List
		//data ex: FirstName,LastName:State,Country
		String[] childObjects = pageObject.split(",")[1].split(":");
		String[] input = data.split(":");
		String locator = UIMap.getProperty(pageObject.split(",")[0]);
		String inputLocator;
		WebElement parent;
		try{
			if(locator.startsWith("//")){
				parent = driver.findElement(By.xpath(locator));
			}else{
				parent = driver.findElement(By.id(locator));
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object doest not exist";
		}
		for(int i=0;i<childObjects.length;i++){
			String[] dataInput = input[i].split(",");
			//List<WebElement> ele_list = getChildElements(parent, "//*[contains(@class,'"+childObjects[i]+"')]");
			//List<WebElement> ele_list = getChildElements(parent, "//"+childObjects[i]);
			inputLocator=UIMap.getProperty(childObjects[i]);
			List<WebElement> ele_list = getChildElements(parent, inputLocator);
			System.out.println("SIZE "+ele_list.size());

			for(int j=0;j<ele_list.size();j++){
				//if(childObjects[i].equals("TextBox")){
				//if(inputLocator.contains("Text")){
				//if(inputLocator.toLowerCase().contains("text") || inputLocator.toLowerCase().contains("input")){
				if(ele_list.get(j).getTagName().equals("input")){
					System.out.println("TAG ="+ele_list.get(j).getTagName());
					Wait("","500");
					ele_list.get(j).clear();
					ele_list.get(j).sendKeys(dataInput[j].trim());
				}else if(inputLocator.toLowerCase().contains("select")){
					ele_list.get(j).findElement(By.xpath("//option[text()='" + dataInput[j].trim() + "']")).click();
				}
			}
		}
		return Constants.KEYWORD_PASS;
	}

	public String verifyPage(String pageObject, String data){
		String[] childObjects = pageObject.split(",")[1].split(":");
		String[] input = data.split(":");
		String locator = UIMap.getProperty(pageObject.split(",")[0]);
		String inputLocator;
		System.out.println("Locator "+locator);
		WebElement parent;
		String actual;
		String expected;

		try{
			if(locator.startsWith("//")){
				parent = driver.findElement(By.xpath(locator));
			}else{
				parent = driver.findElement(By.id(locator));
			}
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" Object doest not exist";
		}
		for(int i=0;i<childObjects.length;i++){
			String[] dataInput = input[i].split(",");
			inputLocator=UIMap.getProperty(childObjects[i]);
			List<WebElement> ele_list = getChildElements(parent, inputLocator);

			for(int j=0;j<ele_list.size();j++){
				// check all text inputs except for password inputs
				if(inputLocator.toLowerCase().contains("text") || inputLocator.toLowerCase().contains("input")){
					actual = ele_list.get(j).getAttribute("value");
					expected = dataInput[j];
					if(!actual.trim().equals(expected.trim())){
						return Constants.KEYWORD_FAIL+" Actual Text "+actual +" is Not matching with Expected "+expected;
					}					  
				}else if(inputLocator.toLowerCase().contains("select")){					
					actual = getListItem(ele_list.get(j).getAttribute("name"));
					expected = dataInput[j];
					if(!actual.trim().equals(expected.trim())){
						return Constants.KEYWORD_FAIL+" Actual List Item "+actual +" is Not matching with Expected "+expected;
					}					  
				}
			}
		}
		return Constants.KEYWORD_PASS;
	}	
	public void extra(String object,String data){
		String[] childObjects = object.split(":")[0].split(",");
		String subTask = object.split(":")[1];


	}
	public String tableOperation(String object,String data){
		//String[] childObjects = object.split(",");
		String tableHeader="";
		String table="";
		String childObject="";
		String subTask="";
		String colToGet="";
		String expString="";
		String column="";
		String searchString = "";
		if(data.contains(":")){
			//column = data.split(":")[1];
			column = data.split(":")[0];
			//searchString = data.split(":")[0];
			searchString = data.split(":")[1];
			//if(data.split(":").length>2){
			if(data.split(":").length==4){
				//expString = data.split(":")[2];
				//colToGet = data.split(":")[3];
				expString = data.split(":")[3];
				colToGet = data.split(":")[2];
			}
			if(data.split(":").length==3){
				//expString = data.split(":")[2];
				//colToGet = data.split(":")[3];
				expString = data.split(":")[2];
				//colToGet = data.split(":")[2];
			}
		}
		if(expString.startsWith("select") || expString.startsWith("SELECT")){
			try {
				//data=dbutil.getDBData(data);
				expString=DBUtil.getDBData(expString);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				APP_LOGS.debug(" Failed to executed SQL Query "+expString);
				return Constants.KEYWORD_FAIL+" Failed to executed SQL Query "+expString;
				//e.printStackTrace();
			}
		}

		String[] childObjects = object.split(":")[0].split(",");
		subTask = object.split(":")[1];
		for(String child: childObjects){
			if(UIMap.getProperty(child).contains("thead")){
				tableHeader=UIMap.getProperty(child);
			}else if(UIMap.getProperty(child).contains("tbody")){// fix this 
				table=UIMap.getProperty(child);
			}else{
				childObject=UIMap.getProperty(child);
			}
		}
		/*
		for(String s: childObjects){
			if(!s.contains(":")){
				if(UIMap.getProperty(s).contains("thead")){
					tableHeader=UIMap.getProperty(s);
				}else if(UIMap.getProperty(s).contains("tbody")){
					table=UIMap.getProperty(s);
				}else{
					subTask=s;
				}
			}else{
				childObject=UIMap.getProperty(s.split(":")[0]);
				subTask=s.split(":")[1];
			}
		}
		 */
		String locator = table.replace("ROW", "1").replace("COLUMN", "1");
		try {

			if(waitForElement(locator)){
				Table tbl = new Table(driver,tableHeader,table,childObject,searchString);
				if(subTask.toLowerCase().contains("clickcheckbox")){
					if(!column.equals("")){
						if(!tbl.selectRowCheckBox(column))return Constants.KEYWORD_FAIL+" Could not find CheckBox";
					}else{
						if(!tbl.selectRowCheckBox())return Constants.KEYWORD_FAIL+" Could not find CheckBox";
					}
				}else if(subTask.toLowerCase().contains("verifycheckbox")){
					if(!column.equals("")){
						if(!tbl.isRowChecked(column)){
							return Constants.KEYWORD_FAIL+" CheckBox was not Checked";
						}
					}else{
						if(!tbl.isRowChecked()){
							return Constants.KEYWORD_FAIL+" CheckBox was not Checked";
						}
					}
				}else if(subTask.toLowerCase().contains("click")){
					//return tbl.clickOnLinkInCell(column, searchString);
					return tbl.clickOnLinkInCell(column, searchString,colToGet);
				}else if(subTask.toLowerCase().contains("verifycelldata")){
					APP_LOGS.debug(" Executing VerifyCelldata ");
					String cellData = tbl.getCellData(column, searchString,colToGet);
					if(!cellData.equals(expString))
						return Constants.KEYWORD_FAIL+" Cell Data "+cellData+" did not match with Exp data "+expString;
				}else if(subTask.toLowerCase().contains("verifycolumns")){
					APP_LOGS.debug(" Executing VerifyColumns ");
					List<String> colList = tbl.getColumnsNames(tableHeader);
					String[] expCol = data.split(",");
					for(String col:expCol){
						if(!colList.contains(col.trim())){
							APP_LOGS.debug(" Column "+col+" Not Found In the Table ");
							return Constants.KEYWORD_FAIL+" Column "+col+" Not Found In the Table ";
						}
					}
				}else if(subTask.toLowerCase().contains("verifyrowdata")){
					APP_LOGS.debug(" Executing Verifyrowdata ");
					List<String> rowData = tbl.getRowData(column, searchString);
					List<String> expRowData = TestUtil.getList(expString, ",");
					if(rowData == null || rowData.size() == 0){
						APP_LOGS.debug(" Column Search value "+searchString+" Not Found In the Table ");
						return Constants.KEYWORD_FAIL+"  Column Search value "+searchString+" Not Found In the Table ";
					}else{
						//						for(String col:rowData){
						//							if(!expRowData.contains(col.trim())){
						//								APP_LOGS.debug(" Column volue "+col+" Not Found In the Row ");
						//								return Constants.KEYWORD_FAIL+" Column Value "+col+" Not Found In the Row ";
						//							}
						//						}
						for(String col:expRowData){
							if(!rowData.contains(col.trim())){
								APP_LOGS.debug(" Column volue "+col+" Not Found In the Row ");
								return Constants.KEYWORD_FAIL+" Column Value "+col+" Not Found In the Row ";
							}
						}
					}

				}else if(subTask.toLowerCase().contains("verifyrowcount")){
					APP_LOGS.debug(" Executing Verifyrowcount ");
					int rowCount = tbl.getRowCount();
					if(Integer.parseInt(data)!=rowCount){
						APP_LOGS.debug(" Expected Row Count "+data+" Did not Match with actual Row Count  "+rowCount);
						return Constants.KEYWORD_FAIL+ "Expected Row Count "+data+" Did not Match with actual Row Count  "+rowCount;
					}

				}
				return Constants.KEYWORD_PASS;
			}
		}catch (Exception e){
			return Constants.KEYWORD_FAIL+" -- No Rows Found in the Table or Table Does not exist"+e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}



	public String dragAnddrop(String object,String data) throws InterruptedException{
		////*[@id='accordion']/div[1]/div[1]/a/i
		/*
		String src = UIMap.getProperty(object.split(",")[0]);
		String dest = UIMap.getProperty(object.split(",")[1]);
		 */
		String src = object.split(",")[0];
		String dest = object.split(",")[1];
		//WebElement drag,to;
		WebElement drag = getWebElement(src,data);
		WebElement to = getWebElement(dest,data);
		/*
		try {
	   		//driver.findElement(By.xpath("//*[@id='accordion']/div[1]/div[1]/a/i")).click();
	   		//drag = driver.findElement(By.xpath("//*[@id='accordion1']/img[1]"));
	   		drag = driver.findElement(By.xpath(src));
	   		//to = driver.findElement(By.xpath("//*[@class='module-body']"));
	   		to = driver.findElement(By.xpath(dest));

		} catch (org.openqa.selenium.NoSuchElementException Ex) {
    		System.out.println("Unable to locate Element: " );             
    		return Constants.KEYWORD_FAIL;         
		}
		 */
		if(drag != null){
			drag.click();

			Wait("","2000");

			Actions builder = new Actions(driver);

			Action dragAndDrop = builder.moveToElement(drag)
					//.moveByOffset(-10, 0)
					.clickAndHold()
					.moveToElement(to)
					.release(to)
					.build();

			Wait("","2000");

			dragAndDrop.perform();
			Wait("","10000");
			return Constants.KEYWORD_PASS;
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Unable to drag & drop -- Element "+src+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Unable to drag & drop  -- Element "+src+" Not Found";
		}


		/*
		try {
		   		//driver.findElement(By.xpath("//*[@id='accordion']/div[1]/div[1]/a/i")).click();
		   		//drag = driver.findElement(By.xpath("//*[@id='accordion1']/img[1]"));
		   		drag = driver.findElement(By.xpath(src));
		   		//to = driver.findElement(By.xpath("//*[@class='module-body']"));
		   		to = driver.findElement(By.xpath(dest));

        } catch (org.openqa.selenium.NoSuchElementException Ex) {
        		System.out.println("Unable to locate Element: " );             
        		return Constants.KEYWORD_FAIL;         
		}

        //WebElement drag1 = driver.findElement(By.xpath("//*[@id='accordion1']/img[2]"));

		String xto=Integer.toString(to.getLocation().x+globalX);
		String yto=Integer.toString(to.getLocation().y+globalY);
		System.out.println("to location "+xto+","+yto);
		((JavascriptExecutor)driver).executeScript("function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\"HTMLEvents\"?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\"on\"+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; " +
		"simulate(arguments[0],\"mousedown\",0,0); simulate(arguments[0],\"mousemove\",arguments[1],arguments[2]); simulate(arguments[0],\"mouseup\",arguments[1],arguments[2]); ",
		drag,xto,yto);

		globalY=globalY+100;
		 */
		//return Constants.KEYWORD_PASS;
	}

	public String login(String object, String data) throws NumberFormatException, InterruptedException{
		//navigate(object,CONFIG.getProperty("testSiteURL"));
		try{
			//if(waitForElement(UIMap.getProperty("user_name"))){
			Wait("","2000");
			writeInInput("user_name",CONFIG.getProperty("User"));
			writeInInput("password",CONFIG.getProperty("Password"));
			Wait("","2000");
			clickButton("submit_btn","");
			//}else{
			//	return Constants.KEYWORD_FAIL+" Failed to Load page properly";
			//	}
		}catch(org.openqa.selenium.NoSuchElementException Ex){
			Ex.printStackTrace();
			return Constants.KEYWORD_FAIL+" Failed to login with User "+CONFIG.getProperty("User")+" "+Ex;
			//
		}
		Wait("","10000");
		if(!isElementPresent(UIMap.getProperty("login_verification_ele"))){
			return Constants.KEYWORD_FAIL+" Failed to login with User "+CONFIG.getProperty("User");
		}
		return Constants.KEYWORD_PASS;
	}

	public String logout(String object, String data){
		if(isElementPresent(UIMap.getProperty("logout_btn"))){
			clickButton("logout_btn","");
			waitForElement(UIMap.getProperty("logout_link"));
			clickButton("logout_link","");
		}else{
			return Constants.KEYWORD_FAIL+" Logout Button Not found ... is User logged in? " ;
		}
		try {
			Wait("","");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!isElementPresent(UIMap.getProperty("user_name"))){
			return Constants.KEYWORD_FAIL+" Failed to logout ";
		}
		return Constants.KEYWORD_PASS;
	}


	public String testLineChart(String object,String data){
		String locator=UIMap.getProperty(object.split(",")[0]);
		String series=object.split(",")[2];
		WebElement chart = getWebElement(object,data);
		if(chart != null){
			LineChart lc = null;
			try{
				lc = new LineChart(driver,chart,series);
				List<String> xlbl = lc.getXAxisLabelsText();
				HashMap<Integer, com.test.selenium.testscript.Point> pp = lc.getSeriesPoints();
				System.out.println("PP Size = "+pp.size());
				System.out.println("Lable Size = "+xlbl.size());
				if(pp.size()>=xlbl.size()){
					for(String x: xlbl){
						System.out.println("XLable = "+x);
						lc.mouseOverOnPointAtXAxisLabel(x);
						try {
							Wait("","500");
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println("ToolTip = "+lc.getToolTipLine(1));
						System.out.println("ToolTip = "+lc.getToolTip());
					}
				}else{
					for(int i=0;i<pp.size();i++){
						lc.mouseOverOnPointAtXAxisLabel(xlbl.get(i));
						//System.out.println("ToolTip = "+lc.getToolTipLine(1));
						System.out.println("ToolTip = "+lc.getToolTip());
						//Wait("","500");
					} 
				}
				return Constants.KEYWORD_PASS;
			}catch(org.openqa.selenium.NoSuchElementException exc){
				APP_LOGS.debug("Failed to Find one or more Graph Elements ..."+exc);
				return Constants.KEYWORD_FAIL+" -- Failed to Find one or more Graph Elements ..."+exc;
			}	
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Graph Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Unable to write  -- Element "+locator+" Not Found";
		}
	}

	public String testBarChart(String object,String data) throws InterruptedException{
		barChartData = new HashMap<Integer,List<String>>();
		String locator=UIMap.getProperty(object.split(",")[0]);
		String series="";
		List<String> toolTip;
		//String toolTip;
		if(object.split(",").length>2){
			series=object.split(",")[2];
		}
		WebElement chart = getWebElement(object,data);
		if(chart != null){
			BarChart bc = null;
			try{
				bc = new BarChart(driver,chart,series);
				List<String> xlbl = bc.getXAxisLabelsText();
				chartLegend = bc.getLegendText();
				//chartYAxis = bc.getYAxisLabelsText();
				int seriesCount = bc.getSeriesCount();
				int barCount = bc.getBarCount();
				System.out.println("Bar Count = "+barCount);
				System.out.println("color = "+bc.getLegendColor().asHex());
				seriesColor = bc.getLegendColor().asHex();
				for(int i=0;i<barCount;i++){
					bc.mouseOverAtBar(i);
					toolTip = bc.getToolTipList();
					barChartData.put(i, toolTip);
					bc.mouseOverAtLegend();
					Thread.sleep(2000);
					for(String s: toolTip){
						System.out.println("ToolTip = "+s); 
					}
				}
				return Constants.KEYWORD_PASS;
			}catch(org.openqa.selenium.NoSuchElementException exc){
				APP_LOGS.debug("Failed to Find one or more Graph Elements ..."+exc);
				return Constants.KEYWORD_FAIL+" -- Failed to Find one or more Graph Elements ..."+exc;
			}	
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Graph Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+"-- Graph Element "+locator+" Not Found";
		}
	}

	public HashMap<Integer,List<String>> getBarChartData(){
		return barChartData;
	}
	public List<String> getChartLegend(){
		return chartLegend;
	}
	public List<String> getChartYAxis(){
		return chartYAxis;
	}
	public String getSeriesColor(){
		return seriesColor;
	}
	public String testPieChart(String object,String data){
		String locator=UIMap.getProperty(object.split(",")[0]);
		String series="";
		if(object.split(",").length>2){
			series=object.split(",")[2];
		}
		WebElement chart = getWebElement(object,data);
		if(chart != null){
			PieChart pc = null;
			try{
				pc = new PieChart(driver,chart,series);
				List<String> xlbl = pc.getLegendText();
				int sliceCount = pc.getSliceCount();
				//System.out.println("Slice Count = "+sliceCount);
				if(sliceCount>=xlbl.size()){
					for(String x: xlbl){
						//System.out.println("XLable = "+x);
						pc.mouseOverAtXAxisLabel(x);
						//System.out.println("ToolTip = "+pc.getToolTip());
						//System.out.println("ToolTip = "+bc.getToolTipLine(2));
					}
				}else{
					for(int i=0;i<sliceCount;i++){
						pc.mouseOverAtXAxisLabel(xlbl.get(i));
						//System.out.println("ToolTip = "+pc.getToolTip());
					} 
				}
				return Constants.KEYWORD_PASS;
			}catch(org.openqa.selenium.NoSuchElementException exc){
				APP_LOGS.debug("Failed to Find one or more Graph Elements ..."+exc);
				return Constants.KEYWORD_FAIL+" -- Failed to Find one or more Graph Elements ..."+exc;
			}	
		}else{
			APP_LOGS.debug(Constants.KEYWORD_FAIL+" -- Graph Element "+locator+" Not Found");
			return Constants.KEYWORD_FAIL+" -- Unable to write  -- Element "+locator+" Not Found";
		}
	}

	public String runJMeter(String object,String data){
		//String command = "java -v";
		String command = "java -jar "+object+"\\ApacheJMeter.jar -n -t "+object+"\\"+data+".jmx";

		try {
			//Process p = Runtime.getRuntime().exec(command);
			//java -jar D:\Tools\jakarta-jmeter-2.5.1\jakarta-jmeter-2.5.1\bin\ApacheJMeter.jar -n -t Test1.jmx
			//ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "java -version");
			//ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "java -jar D:\\Tools\\jakarta-jmeter-2.5.1\\jakarta-jmeter-2.5.1\\bin\\ApacheJMeter.jar -n -t Test1.jmx");
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			//Scanner kb = new Scanner(p.getInputStream());
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return Constants.KEYWORD_FAIL+" "+e;
		}


		return Constants.KEYWORD_PASS;
	}


	/************************FOLLOWING METHODS ARE NOT KEYWORDS-- HELPER METHODS ********************************/	

	public String getAttribute(String object,String data){
		String locator = UIMap.getProperty("footer_next_btn");

		/*
		String locator="/..//*[contains(@class,'prev')]";
		locator = "//section/div/form/..//div[3]/div[2]/div[2]/ul/li[contains(@class,'disabled')]"+locator;

		String locator="/..//*[contains(@class,'next')]";
		locator = "//section/div/form/..//div[3]/div[2]/div[2]/ul/li[contains(@class,'disabled')]"+locator;
		 */
		//System.out.println("Class = "+driver.findElement(By.xpath(locator)).getAttribute("class"));
		System.out.println("Class = "+isElementPresent(locator));
		System.out.println("Text = "+driver.findElement(By.xpath(locator)).getText());
		System.out.println("Tag = "+driver.findElement(By.xpath(locator)).getTagName());
		driver.findElement(By.xpath(locator)).click();
		return Constants.KEYWORD_PASS;
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


	public List<WebElement> getChildElements(WebElement ele, String cls){
		//List<WebElement> ele_list = ele.findElements(By.xpath("//input[contains(@class,'TextBox')]"));
		List<WebElement> ele_list = ele.findElements(By.xpath(cls));
		//List<WebElement> ele_list = ele.findElements(By.cssSelector("input:([class~='TextBox']"));

		return ele_list;	
	}

	public boolean waitForElement(final String locator) {

		boolean flag = true;
		try{
			driverWait = new WebDriverWait(driver, implicitWaitTime);
			flag = driverWait.until(new ExpectedCondition<Boolean>() {  
				public Boolean apply(WebDriver d) {  
					APP_LOGS.debug("Waiting for Elements to completely load ...");  
					if(locator.startsWith("//")){
						return d.findElement(By.xpath(locator)).isEnabled();
					}else{
						return d.findElement(By.id(locator)).isEnabled();
					}
				}  
			});  
			APP_LOGS.debug("Element "+locator+ "Enabled");
		}catch(org.openqa.selenium.NoSuchElementException Ex){
			flag = false;
			APP_LOGS.debug("Failed to Load Element ..."+Ex);
		}
		return flag;

	}

	public void highlightElement(WebElement element) { 
		for (int i = 0; i < 1; i++) { 
			JavascriptExecutor js = (JavascriptExecutor) driver; 
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 5px solid yellow;");
			try {
				Wait("","1000");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, ""); 
		}
	} 

	/************************APPLICATION SPECIFIC KEYWORDS
	 * @throws InterruptedException 
	 * @throws NumberFormatException ********************************/

	/*
	public String provisionDataSetUp(String object,String data){
		String ssn=data;
		String csn="C"+data;
		String pin="7777";

		dbutil.runUpdateSQL("delete from TB_DIGITAL_SIGNATURE where STATION_SERIAL_NUMBER='"+ssn+"'");
		dbutil.runUpdateSQL("delete from TB_EVS0107_CONTROLLER_PULSE where CONTROLLER_SERIAL_NUMBER = (select controller_serial_number from TB_EVS0105_CONTROLLERS where STATION_SERIAL_NUMBER='"+ssn+"')");
		dbutil.runUpdateSQL("delete from TB_EVS0105_CONTROLLERS where STATION_SERIAL_NUMBER='"+ssn+"'");
		dbutil.runUpdateSQL("delete from tb_evs0122_station_group_items where STATION_SERIAL_NUMBER='"+ssn+"'");
		dbutil.runUpdateSQL("delete from tb_station_update where STATION_SERIAL_NUMBER='"+ssn+"'");
		dbutil.runUpdateSQL("delete from TB_EVS0104_STATIONS where STATION_SERIAL_NUMBER='"+ssn+"'");
		dbutil.runUpdateSQL("delete from TB_EWALLET_GE_STATION_SUBJECTS where STATION_SERIAL_NUMBER='"+ssn+"'");

		String qry = "INSERT INTO TB_DIGITAL_SIGNATURE (STATION_SERIAL_NUMBER, SBC_SERIAL_NUMBER, QR_CODE, PIN_CODE, CREATE_DATE, UPDATE_TIME) VALUES ('"+ssn+"', '"+csn+"', '"+ssn+"', '"+pin+"', TO_TIMESTAMP('30-NOV-11 12.00.00.000000000 AM', 'DD-MON-RR HH.MI.SS.FF AM'), TO_TIMESTAMP('30-NOV-11 12.00.00.000000000 AM', 'DD-MON-RR HH.MI.SS.FF AM'))";
		dbutil.runUpdateSQL(qry);
		qry = "INSERT INTO TB_EVS0104_STATIONS (STATION_SERIAL_NUMBER, DEVICE_NAME, STATION_TYPE_KEY, ACCESS_TYPE_KEY, UTILITY_SERVICE_KEY, OPERATIONAL_MODE_KEY, REGION_KEY, OWNER_ID, MAX_CURRENT) VALUES ('"+ssn+"', '"+ssn+"', 'db.Watt Station', 'db.stationNotProvisioned', 'U1', 'MODE1', 'db.notapplicable', '', '30')";
		dbutil.runUpdateSQL(qry);
		qry = "INSERT INTO TB_EVS0105_CONTROLLERS (CONTROLLER_SERIAL_NUMBER, STATION_SERIAL_NUMBER, PLUG_ID, PORT_NUMBER, UPDATE_FREQUENCY) VALUES ('"+csn+"', '"+ssn+"', '1', '1', '300')";
		dbutil.runUpdateSQL(qry);
		qry = "INSERT INTO tb_evs0107_controller_pulse (pulse_id,controller_serial_number,status_key,updated_date,created_date,confirmed) values (SQ_EVS0107_PULSE_ID.NEXTVAL, '"+csn+"','db.controllerIdle',sysdate,sysdate,'')";
		dbutil.runUpdateSQL(qry);
		return Constants.KEYWORD_PASS;

	}

	 */



	// not a keyword

	public void captureScreenshot(String filename, String keyword_execution_result) throws IOException, NumberFormatException, InterruptedException{
		// take screen shots

		if(CONFIG.getProperty("screenshot_everystep").equalsIgnoreCase("y")){
			// capturescreen
			if(driver!=null){
				try{
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					//FileUtils.copyFile(scrFile, new File(CONFIG.getProperty("screenshotPath")+"/"+filename+".png"));
					FileUtils.copyFile(scrFile, new File(CONFIG.getProperty("ResultsPath")+"/"+filename+".png"));
				} catch(org.openqa.selenium.UnhandledAlertException e){

				}
				//FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") +"//screenshots//"+filename+".jpg"));
			}

		}else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL) && CONFIG.getProperty("screenshot_error").equalsIgnoreCase("y") ){
			// capture screenshot
			if(driver!=null){
				try{
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					//FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") +"//screenshots//"+filename+".png"));
					//FileUtils.copyFile(scrFile, new File(CONFIG.getProperty("screenshotPath")+"/"+filename+".png"));
					FileUtils.copyFile(scrFile, new File(CONFIG.getProperty("ResultsPath")+"/"+filename+".png"));
				} catch(org.openqa.selenium.UnhandledAlertException e){

				}
			}
		}
	}




	public String getElementText(String locator){
		String text;
		StringBuffer msg = new StringBuffer("Trying perform getText() action with parameter(s): ").append(locator);

		try {
			if(locator.startsWith("//")){
				text = driver.findElement(By.xpath(locator)).getText();
			}else{
				text = driver.findElement(By.id(locator)).getText();
			}
		}catch (org.openqa.selenium.NoSuchElementException Ex){
			throw Ex;
		}
		return text;
	}


	public String getListItem(String listBox){

		//WebElement select = driver.findElement(By.id(listBox));
		WebElement select = driver.findElement(By.name(listBox));
		List<WebElement> options = select.findElements(By.tagName("option"));
		for (WebElement option : options) {
			if (option.isSelected()){
				return option.getText();
			}
		}
		return null;
	}



	public String clickGroupElements(String object, String data)
	{
		APP_LOGS.debug("Executing clickGroupElements keyword");

		List<WebElement> group = findElementsInGroup(object, data);
		if(group==null)
			return Constants.KEYWORD_FAIL+" Error-->Group elements not found";

		try
		{
			for(int i=0; i<group.size(); i++)
			{
				group.get(i).click();
				Thread.sleep(1000);
			}
		}
		catch(Exception e)
		{
			return Constants.KEYWORD_FAIL+" Error -->"+e.getMessage();
		}

		return Constants.KEYWORD_PASS;
	}

	/**
	 *  Verifies text from all elements of certain type in a group (labels, menu items etc)
	 *  @param object: xpath+/label     
	 *  @param data:   Username|Password 
	 */
	public String verifyGroupElementsText(String object, String data)
	{
		APP_LOGS.debug("Executing verifyGroupElementsText keyword");

		List<WebElement> group = findElementsInGroup(object, data);
		if(group==null)
			return Constants.KEYWORD_FAIL+" Error-->Group elements not found";

		//String temp[]=data.split(Constants.DATA_SPLIT);
		String temp[]=data.split(",");

		List<String> res = new ArrayList<String>();

		// Get text from the elements into array
		try
		{
			for(WebElement e : group) 
			{
				String tmp = e.getText();
				System.out.println("text = "+tmp);
				if(tmp.isEmpty()) // elements like menu divider have no text
					continue;
				res.add(new String(tmp));
			}
		}
		catch(Exception e)
		{
			return Constants.KEYWORD_FAIL+" Error -->"+e.getMessage();
		}

		String arr[] = res.toArray(new String[res.size()]);
		for(String s: arr){
			System.out.println("value = "+s);
		}
		String ret = Arrays.equals(arr,temp) ? 
				Constants.KEYWORD_PASS : 
					Constants.KEYWORD_FAIL+"Error-->App data does not match expected";

		return ret;

	}

	public String verifyElementsCount(String object, String data)
	{
		APP_LOGS.debug("Executing verifyElementsCount keyword");

		List<WebElement> group = findElementsInGroup(object, data);
		if(group==null)
			return Constants.KEYWORD_FAIL+" Error-->Group elements not found";

		String temp[]=data.split(Constants.DATA_SPLIT);

		String ret = group.size()== Integer.parseInt(data) ? 
				Constants.KEYWORD_PASS : 
					Constants.KEYWORD_FAIL+"Error-->App data "+data+" does not match expected " +group.size();

		return ret;

	}

	protected List<WebElement> findElementsInGroup(String object, String data) 
	{
		return getWebElements(UIMap.getProperty(object));
	}



	public String login_negative(String object, String data) throws NumberFormatException, InterruptedException
	{
		//Original values
		String[] orig = {CONFIG.getProperty("User"), CONFIG.getProperty("Password")};

		if(data.isEmpty())
			return Constants.KEYWORD_FAIL+" Add Login arguments to spreadsheet. ";
		else
		{
			String[] args = data.split(Constants.DATA_SPLIT);
			CONFIG.setProperty("User", args[0]);
			CONFIG.setProperty("Password", args[1]);
		}

		String result = login(object,data);

		//Restore original values
		CONFIG.setProperty("User", orig[0]);
		CONFIG.setProperty("Password", orig[1]);

		if(result==Constants.KEYWORD_PASS)
			return Constants.KEYWORD_FAIL+" Login allowed for not authorized User "+CONFIG.getProperty("User");
		else
			return Constants.KEYWORD_PASS;

	}


	public String testWebServices(String object, String data) throws IOException{
		//String url=UIMap.getProperty(object.split("\\|")[0]);
		CONFIG.setProperty("screenshot_error", "N");
		RestAPIUtil rs = new RestAPIUtil();

		return rs.testService(object,data);
	}

	public String clearBrowserCache(String object,String data){
		Selenium selenium = new WebDriverBackedSelenium(driver, data); 
		driver.manage().deleteAllCookies();
		selenium.refresh();
		driver.navigate().to("file:///C:/ALTestSuites/ClearCacheFirefox.html");

		/*
		String jScript_ = "" +
		"Response.Cache.SetExpires(DateTime.Parse(DateTime.Now.ToString()))\n" +
		"Response.Cache.SetCacheability(HttpCacheability.Private)\n" +
		"Response.Cache.SetNoStore()\n" +
		"Response.AppendHeader(\"Pragma\", \"no-cache\")";

		//((JavascriptExecutor)driver).getEval(jScript_);
		selenium.getEval(jScript_);
		 */
		return Constants.KEYWORD_PASS;

	}

	public String getDBData(String object, String data){
		try {
			dbResult=DBUtil.executeSelectStmt(data);
			//dbResult=dbutil.executeSelectStmt(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.KEYWORD_FAIL+ "DB ERROR";
		}

		return Constants.KEYWORD_PASS;
	}

	public String verifyDBResults(String object, String data){
		String results=Constants.KEYWORD_PASS;
		ArrayList<ArrayList<String>> expData = new ArrayList<ArrayList<String>>();
		//1,2,3|4,5,6|7,8,9
		String[] input = data.split(Constants.DATA_SPLIT);
		for(int i=0;i<input.length;i++){
			ArrayList<String> val = (ArrayList<String>) TestUtil.getList(input[i], ",");
			expData.add(val);
		}
		if(dbResult.size()!=expData.size()){
			results = Constants.KEYWORD_FAIL+"DB counts "+dbResult.size()+ " did not match with Exp Counts"+expData.size();
			System.out.println("Results "+results);
		}
		for(int k=0;k<dbResult.size();k++){
			for(int kk=0;kk<dbResult.get(k).size();kk++){
				System.out.println("DB Cell ="+dbResult.get(k).get(kk));
				if(!dbResult.get(k).get(kk).equals(expData.get(k).get(kk))){
					results = Constants.KEYWORD_FAIL+" DB Value:: "+dbResult.get(k).get(kk)+" did not match with Exp value:: "+expData.get(k).get(kk);
					System.out.println("DB Value:: "+dbResult.get(k).get(kk)+" did not match with Exp value:: "+expData.get(k).get(kk));
				}
			}
		}
		return results;
	}



	protected static <T> T getValue() 
	{
		return (T) testResults.get();
	}

	protected static <T> void setValue(T result) 
	{
		testResults.set((T)result);
	}
	/*
	protected By getElementByLocator(String locator)
	{
		By by = null;

		if(locator.startsWith("//"))
			by = By.xpath(locator);
		else if(locator.startsWith("css:"))
    		by = By.cssSelector(locator.substring(4));
		else
    		by = By.id(locator);

		return by;
	}

	protected WebElement getWebElement(String locator)
	{
		return driver.findElement(getElementByLocator(locator));
	}
	 */
	/*
	protected List<WebElement> getWebElements(String locator)
	{
		return driver.findElements(getElementByLocator(locator));
	}
	 */
	public String testMaps(String object, String data) throws MalformedURLException, NumberFormatException, InterruptedException{
		Wait("","55000");
		ScreenRegion s = new DesktopScreenRegion();
		Mouse mouse = new DesktopMouse();
		//URL imageURL = new URL("http://code.google.com/images/code_logo.gif");    
		//Target imageTarget = new ImageTarget(new File("C:\\ALTestSuites\\ObjectRepo\\code_logo.gif"));
		//Target imageTarget = new ImageTarget(new File("C:\\ALTestSuites\\ObjectRepo\\defective.PNG"));
		Target imageTarget = new ImageTarget(new File("C:\\ALTestSuites\\ObjectRepo\\ips.PNG"));

		//Target imageTarget = new ImageTarget(imageURL);
		ScreenRegion r = s.wait(imageTarget,5000);

		mouse.click(r.getCenter());
		Wait("","5000");
		return Constants.KEYWORD_PASS;
	}
	//	public String testMaps(String object, String data) throws MalformedURLException, NumberFormatException, InterruptedException{
	//		String prof=CONFIG.getProperty("firefoxProfile");
	//		File profileDirectory = new File(prof);
	//		FirefoxProfile profile = new FirefoxProfile(profileDirectory);
	//		DesiredCapabilities capability = DesiredCapabilities.firefox();
	//		capability.setCapability(SikuliFirefoxDriver.PROFILE, profile);
	//		
	//		SikuliFirefoxDriver driver = new SikuliFirefoxDriver(capability);         
	//		/*
	//		try
	//		{
	//			driver.manage().window().setPosition(new Point(0,0));
	//			java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	//			Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
	//			driver.manage().window().setSize(dim);
	//		}
	//		catch(Exception e)
	//		{
	//			return Constants.KEYWORD_FAIL;
	//		}
	//		*/
	//        // visit Google Map
	//        driver.get("https://maps.google.com/");
	//        
	//        // enter "Denver, CO" as search terms
	//        WebElement input = driver.findElement(By.id("gbqfq"));
	//        input.sendKeys("Denver, CO");           
	//        input.sendKeys(Keys.ENTER);
	//
	//        ImageElement image;
	//        Wait("test","2000");
	//        // find and click on the image of the lakewood area
	//        //Target imageTarget = new ImageTarget(new File("C:\\ALTestSuites\\ObjectRepo\\code_logo.gif"));
	//        
	//        image = driver.findImageElement(new File("C:\\ALTestSuites\\ObjectRepo\\denver1.png"));
	//        image.doubleClick()     ;               
	//        
	//        // find and click on the image of the kendrick lake area
	// //       image = driver.findImageElement(new File("C:\\ALTestSuites\\ObjectRepo\\kendrick_lake.png"));
	// //       image.doubleClick();
	//
	//        // find and click the Satellite icon to switch to the satellite view
	//        image = driver.findImageElement(new File("C:\\ALTestSuites\\ObjectRepo\\satellite.png"));             
	//        image.click();
	//
	//        // find and click the plus button to zoom in
	//        image = driver.findImageElement(new File("C:\\ALTestSuites\\ObjectRepo\\plus.png"));
	//        image.click();
	//        
	//        image = driver.findImageElement(new File("C:\\ALTestSuites\\ObjectRepo\\minus.png"));
	//        image.click();
	//        
	//        // find and click the link button
	//        WebElement linkButton = driver.findElement(By.id("link"));
	//        linkButton.click();
	//		return Constants.KEYWORD_PASS;
	//	}

	/* New methods added by Moumita */




	public String navigateURL(String object,String data){	
		String urlString = "";
		getDBData(object, data);
		if(dbResult.size() > 0) {
			urlString = parseDBData(object, data);
			CONFIG.setProperty("testSiteURL", urlString);
			System.out.println("testSiteURL: " + urlString);
		}
		APP_LOGS.debug("Navigating to URL");
		try{
			driver.navigate().to(urlString);
		}catch(Exception e){
			return Constants.KEYWORD_FAIL+" -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}


	public String parseDBData(String object, String data) {
		String urlString = "";
		System.out.println("str: parse db data; " + dbResult.get(0).get(0).trim().toString());
		if( dbResult.get(0).get(0).contains("http") ) {
			System.out.println("found db data");
			int index1 = dbResult.get(0).get(0).trim().indexOf("http");
			int index2 = dbResult.get(0).get(0).trim().indexOf("If");
			urlString = dbResult.get(0).get(0).trim().substring(index1, index2);
			System.out.println("url String: " + urlString);
		} else {
			// It doesn't start with your prefix
			System.out.println("didn't find the db data");
		}
		return urlString;
	}


	public String verifyDBURLResults(String object, String data){
		String urlString = null;
		String results=Constants.KEYWORD_PASS;
		String expData = data;

		urlString = parseDBData(object, data);
		System.out.println("db result: " + dbResult.size());
		System.out.println("actual result: " + urlString);
		System.out.println("expData result: " + expData);
		if(urlString == null){
			results = Constants.KEYWORD_FAIL+" DB counts "+dbResult.size()+ " did not match with Exp Counts";
			System.out.println("Results "+results);
		}
		if(!urlString.trim().equals(expData.trim())){
			results = Constants.KEYWORD_FAIL+" DB Value:: "+urlString+" did not match with Exp value:: "+expData;					//			 results = Constants.KEYWORD_FAIL+" DB Value:: "+dbResult.get(k).get(kk)+" did not match with Exp value:: "+expData.get(k).get(kk);
			System.out.println("DB Value:: "+urlString+" did not match with Exp value:: "+expData);
		}
		return results;
	}

	/* ----------- End methods added by Moumita --------------- */

}
