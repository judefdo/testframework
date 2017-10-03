package com.test.selenium.sikuli;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.openqa.selenium.*;
import org.openqa.selenium.browserlaunchers.Proxies;
import org.openqa.selenium.firefox.ExtensionConnection;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.openqa.selenium.remote.*;
import org.sikuli.api.DefaultScreenRegion;
import org.sikuli.api.ImageTarget;
import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

import static org.openqa.selenium.remote.CapabilityType.*;

//extra

public class SikuliFirefoxDriver extends FirefoxDriver {

	static Logger logger = LoggerFactory.getLogger(SikuliFirefoxDriver.class);

	private static final int DEFAULT_WAIT_TIMEOUT_MSECS = 10000;
	ScreenRegion webdriverRegion;

	public SikuliFirefoxDriver(){
		com.test.selenium.sikuli.WebDriverScreen webDriverScreen;
		try {
			webDriverScreen = new com.test.selenium.sikuli.WebDriverScreen(this);
		} catch (IOException e1) {
			throw new RuntimeException("unable to initialize SikuliFirefoxDriver");
		}
		webdriverRegion = new DefaultScreenRegion(webDriverScreen);
	}
	public SikuliFirefoxDriver(Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
	    this(getBinary(desiredCapabilities), extractProfile(desiredCapabilities, requiredCapabilities), 
	        desiredCapabilities, requiredCapabilities);
	  }
	 public SikuliFirefoxDriver(FirefoxProfile profile) {
		    this(new FirefoxBinary(), profile);
		  }
	 public SikuliFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile) {
		    this(binary, profile, DesiredCapabilities.firefox());
		  }
	 
	public SikuliFirefoxDriver(Capabilities desiredCapabilities){
		this(getBinary(desiredCapabilities), extractProfile(desiredCapabilities, null), desiredCapabilities);
		com.test.selenium.sikuli.WebDriverScreen webDriverScreen;
		try {
			webDriverScreen = new com.test.selenium.sikuli.WebDriverScreen(this);
		} catch (IOException e1) {
			throw new RuntimeException("unable to initialize SikuliFirefoxDriver");
		}
		webdriverRegion = new DefaultScreenRegion(webDriverScreen);
	}
	 public SikuliFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, Capabilities capabilities) {
		    this(binary, profile, capabilities,null);
		  }
	 public SikuliFirefoxDriver(FirefoxBinary binary, FirefoxProfile profile, 
		      Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
		   /* super(new LazyCommandExecutor(binary, profile),
		      dropCapabilities(desiredCapabilities, BINARY, PROFILE), 
		      dropCapabilities(requiredCapabilities, BINARY, PROFILE));*/
		 //startClient();
		 //   startSession(desiredCapabilities, requiredCapabilities);
		    this.binary = binary;
		  }
	public WebElement findElementByLocation(int x, int y){
		return (WebElement) ((JavascriptExecutor) this).executeScript("return document.elementFromPoint(" + x + "," + y + ")");
	}

	public com.test.selenium.sikuli.ImageElement findImageElementX(URL imageUrl) {
		ImageTarget target = new ImageTarget(imageUrl);
		final ScreenRegion imageRegion = webdriverRegion.wait(target, DEFAULT_WAIT_TIMEOUT_MSECS);
		
		
		if (imageRegion != null){
			Rectangle r = imageRegion.getBounds();
			//logger.debug("image is found at {} {} {} {}", r.x, r.y, r.width, r.height);
		}else{
			logger.debug("image is not found");
			return null;
		}


		ScreenLocation center = imageRegion.getCenter();
		WebElement foundWebElement = findElementByLocation(center.getX(), center.getY());
		Rectangle r = imageRegion.getBounds();
		return new com.test.selenium.sikuli.DefaultImageElement(this, foundWebElement,
				r.x,
				r.y,
				r.width,
				r.height);
	}
	
	public ImageElement findImageElement(File imageUrl) {
		ImageTarget target = new ImageTarget(imageUrl);
		final ScreenRegion imageRegion = webdriverRegion.wait(target, DEFAULT_WAIT_TIMEOUT_MSECS);
		
		
		if (imageRegion != null){
			Rectangle r = imageRegion.getBounds();
			//logger.debug("image is found at {} {} {} {}", r.x, r.y, r.width, r.height);
			System.out.println("image is found at {} {} {} {}"+ r.x+","+ r.y+","+ r.width+","+ r.height);
		}else{
			System.out.println("image is not found");
			logger.debug("image is not found");
			return null;
		}


		ScreenLocation center = imageRegion.getCenter();
		WebElement foundWebElement = findElementByLocation(center.getX(), center.getY());
		Rectangle r = imageRegion.getBounds();
		System.out.println("CORDS "+r.x+" "+r.y);
		return new DefaultImageElement(this, foundWebElement,
				r.x,
				r.y,
				r.width,
				r.height);
	}
	private static FirefoxBinary getBinary(Capabilities capabilities) {
	    if (capabilities != null && capabilities.getCapability(BINARY) != null) {
	      Object raw = capabilities.getCapability(BINARY);
	      if (raw instanceof FirefoxBinary) {
	        return (FirefoxBinary) raw;
	      }
	      File file = new File((String) raw);
	      return new FirefoxBinary(file);
	    }
	    return new FirefoxBinary();
	  }
	private static FirefoxProfile extractProfile(Capabilities desiredCapabilities, 
		      Capabilities requiredCapabilities) {
		    
		    FirefoxProfile profile = null;
		    Object raw = null;
		    if (desiredCapabilities != null && desiredCapabilities.getCapability(PROFILE) != null) {
		      raw = desiredCapabilities.getCapability(PROFILE);
		    }
		    if (requiredCapabilities != null && requiredCapabilities.getCapability(PROFILE) != null) {
		      raw = requiredCapabilities.getCapability(PROFILE);
		    }
		    if (raw != null) {
		      if (raw instanceof FirefoxProfile) {
		        profile = (FirefoxProfile) raw;
		      } else if (raw instanceof String) {
		        try {
		          profile = FirefoxProfile.fromJson((String) raw);
		        } catch (IOException e) {
		          throw new WebDriverException(e);
		        }
		      }
		    }
		    profile = getProfile(profile);
		    
		    populateProfile(profile, desiredCapabilities);
		    populateProfile(profile, requiredCapabilities);
		    
		    return profile;
		  }
	private static FirefoxProfile getProfile(FirefoxProfile profile) {
	    FirefoxProfile profileToUse = profile;
	    String suggestedProfile = System.getProperty("webdriver.firefox.profile");
	    if (profileToUse == null && suggestedProfile != null) {
	      profileToUse = new ProfilesIni().getProfile(suggestedProfile);
	      if (profileToUse == null) {
	        throw new WebDriverException("Firefox profile '" + suggestedProfile
	            + "' named in system property 'webdriver.firefox.profile' not found");
	      }
	    } else if (profileToUse == null) {
	      profileToUse = new FirefoxProfile();
	    }
	    return profileToUse;
	  }
	static void populateProfile(FirefoxProfile profile, Capabilities capabilities) {
	    if (capabilities == null) {
	      return;
	    }
	    if (capabilities.getCapability(SUPPORTS_WEB_STORAGE) != null) {
	      Boolean supportsWebStorage = (Boolean) capabilities.getCapability(SUPPORTS_WEB_STORAGE);
	      profile.setPreference("dom.storage.enabled", supportsWebStorage.booleanValue());
	    }
	    if (capabilities.getCapability(ACCEPT_SSL_CERTS) != null) {
	      Boolean acceptCerts = (Boolean) capabilities.getCapability(ACCEPT_SSL_CERTS);
	      profile.setAcceptUntrustedCertificates(acceptCerts);
	    }
	    if (capabilities.getCapability(LOGGING_PREFS) != null) {
	      LoggingPreferences logsPrefs = 
	          (LoggingPreferences) capabilities.getCapability(LOGGING_PREFS);
	      for (String logtype : logsPrefs.getEnabledLogTypes()) {
	        profile.setPreference("webdriver.log." + logtype, 
	            logsPrefs.getLevel(logtype).intValue());
	      }
	    }

	    if (capabilities.getCapability(HAS_NATIVE_EVENTS) != null) {
	      Boolean nativeEventsEnabled = (Boolean) capabilities.getCapability(HAS_NATIVE_EVENTS);
	      profile.setEnableNativeEvents(nativeEventsEnabled);
	    }
	  }
	private static Capabilities dropCapabilities(Capabilities capabilities, String... keysToRemove) {
	    if (capabilities == null) {
	      return new DesiredCapabilities();
	    }
	    final Set<String> toRemove = Sets.newHashSet(keysToRemove);
	    DesiredCapabilities caps = new DesiredCapabilities(Maps.filterKeys(capabilities.asMap(), new Predicate<String>() {
	      public boolean apply(String key) {
	        return !toRemove.contains(key);
	      }
	    }));

	    // Ensure that the proxy is in a state fit to be sent to the extension
	    Proxy proxy = Proxies.extractProxy(capabilities);
	    if (proxy != null) {
	      caps.setCapability(PROXY, new BeanToJsonConverter().convert(proxy));
	    }

	    return caps;
	  }
	
	private static class LazyCommandExecutor implements CommandExecutor, NeedsLocalLogs {
	    private ExtensionConnection connection;
	    private final FirefoxBinary binary;
	    private final FirefoxProfile profile;
	    private LocalLogs logs = LocalLogs.getNullLogger();

	    private LazyCommandExecutor(FirefoxBinary binary, FirefoxProfile profile) {
	      this.binary = binary;
	      this.profile = profile;
	    }

	    public void setConnection(ExtensionConnection connection) {
	      this.connection = connection;
	      connection.setLocalLogs(logs);
	    }

	    public void quit() {
	      if (connection != null) {
	        connection.quit();
	        connection = null;
	      }
	      if (profile != null) {
	        //profile.cleanTemporaryModel();
	      }
	    }

	    public Response execute(Command command) throws IOException {
	      if (connection == null) {
	        if (command.getName().equals(DriverCommand.QUIT)) {
	          return new Response();
	        }
	        throw new SessionNotFoundException(
	            "The FirefoxDriver cannot be used after quit() was called.");
	      }
	      return connection.execute(command);
	    }

	    public void setLocalLogs(LocalLogs logs) {
	      this.logs = logs;
	      if (connection != null) {
	        connection.setLocalLogs(logs);
	      }
	    }
	  }
}
