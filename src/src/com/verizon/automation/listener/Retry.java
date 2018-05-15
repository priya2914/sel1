package com.verizon.automation.listener;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.verizon.automation.configuration.SuiteSetup;
import com.verizon.automation.util.DataUtil;
import com.verizon.automation.util.PropertyUtility;

@SuppressWarnings("all")
public class Retry extends SuiteSetup implements IRetryAnalyzer {

	private int retryCnt = 0;
	private int maxRetryCount = 1;
	
	DataUtil dutil;
	PropertyUtility putil = new PropertyUtility();
	Logger logger = Logger.getLogger(Retry.class);

	public boolean retry(ITestResult result) {
		boolean flag=false;
		maxRetryCount=Integer.valueOf(putil.getTestEnvironmentProperty("failed_retry_count"));
		if (retryCnt < maxRetryCount) {
			logger.info("Retrying test " + result.getName() + " with status "
	                    + getResultStatusName(result.getStatus()) + " for the " + (retryCnt+1) + " time(s).");
			retryCnt++;
			dutil=new DataUtil(retryCnt);
			flag=true;
		}
	    return flag;
	}
	
	    
    public String getResultStatusName(int status) {
    	String resultName = null;
    	if(status==1)
    		resultName = "SUCCESS";
    	if(status==2)
    		resultName = "FAILURE";
    	if(status==3)
    		resultName = "SKIP";
		return resultName;
    }

}
