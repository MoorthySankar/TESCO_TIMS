package framework;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import testScripts.TIMS;

public class Driver_Script {

	private WebDriver driver;

	@BeforeTest
	public void Setup() {
		
//		System.setProperty("webdriver.gecko.driver", ".\\Lib\\drivers\\geckodriver.exe");

    	System.setProperty("webdriver.firefox.marionette",".\\Lib\\drivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
//		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//		capabilities.setCapability("marionette",true);
//		driver= new FirefoxDriver(capabilities);
		
//		System.setProperty("webdriver.ie.driver", ".\\Lib\\drivers\\IEDriverServer.exe");
//		DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//		caps.setCapability("ignoreZoomSetting", true);
//		caps.setCapability("nativeEvents",false);
//		driver = new InternetExplorerDriver(caps);
//		driver = new InternetExplorerDriver();
//		driver.manage().deleteAllCookies();
//		driver.manage().window().maximize();

//		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
//	    capabilities.setCapability("InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION", true);
//	    capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//	    capabilities.setCapability("requireWindowFocus", true);
//	    capabilities.setCapability("ignoreZoomSetting", true);
//	    capabilities.setCapability("ignoreProtectedModeSettings", true);
//	    driver = new InternetExplorerDriver(capabilities);
//	    driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.get("https://b2btims-test.tesco.co.uk/TIMS/");
	}

	@Test
	public void InvokeTest() throws InterruptedException, AWTException, IOException {	
		TIMS.ASN_Creation(driver);
	}
}
