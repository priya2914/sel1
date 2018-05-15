package com.verizon.automation.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.verizon.automation.constants.ApplicationConstants;

public class SeleniumLogWrapper implements ApplicationConstants {
	
public static org.apache.log4j.Logger logger = Logger.getLogger(SeleniumLogWrapper.class);
	
	public SeleniumLogWrapper(){
		initializeLogger();
	}
		
	public static void initializeLogger(){
		PropertyConfigurator.configure(LOG_FILENAME);
		logger.info("SeleniumLogWrapper Logging initialized");
	}
	
}
