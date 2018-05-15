package com.verizon.automation.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.verizon.automation.configuration.SuiteSetup;
import com.verizon.automation.logger.SeleniumLogWrapper;


@SuppressWarnings("all")
public class ForeseePop extends SuiteSetup {
	
	RemoteWebDriver fDriver;
	XmlParser xmlParse;
	FrameworkUtil util;
	Logger logger = Logger.getLogger(ForeseePop.class);
		
	public ForeseePop(RemoteWebDriver rDriver) {
		this.fDriver=rDriver;
		xmlParse=new XmlParser(fDriver);
		util=new FrameworkUtil(fDriver);
	}
	
	public void cancelForeSee(int timeout) {
		
		String osType=null;
		FluentWait await = new FluentWait(fDriver)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
		
		try {
			String plat=fDriver.getCapabilities().getPlatform().toString();
			if (plat.equalsIgnoreCase("android") || plat.equalsIgnoreCase("ios")) {
				try {
					String browser=fDriver.getCapabilities().getCapability("browserName").toString();
					osType=plat;
				} catch (Exception t) {
					
				}
			} else {
				osType="Desktop";
			}
		} catch (Exception ex) {}
		
		try {
			//By foreseeCancel=By.xpath(xmlParse.getXpath(osType, "foreseeCancel"));
			util.turnOffImplicitWaits();
			By foreseeCancel = By.xpath(".//div[@class='fsrDeclineButtonContainer']/a[text()='No, thanks']");
			WebElement foreSee=fDriver.findElement(foreseeCancel);
			await.until (ExpectedConditions.visibilityOf(foreSee));
			foreSee.click();
			util.turnOnImplicitWaits();
			logger.info("Foresee Popup appeared and clicked 'No, thanks' button");
		} catch (Exception e) {}
	}
}
