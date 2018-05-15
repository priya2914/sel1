package com.verizon.automation.constants;

import java.io.File;

import com.verizon.automation.util.PropertyUtility;


/*
 * CODE CHANGES HISTORY
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 	DATE		AUTHOR				METHODS MODIFIED/ADDED				CODE CHANGES DESCRIPTION
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *  07/07/2015  Chandrasekar Murugesan      										Initial interface creation. 	
 *  08/18/2015	Chandrasekar Murugesan			Added Build Workspace Environment variable
 */


/**
 * @date 07/07/2015
 * @author Chandrasekar Murugesan
 * @description Interface class used to store APPLICATION CONSTANTS
 */

@SuppressWarnings("all")
public interface ApplicationConstants {

	static final String hubUrl="http://10.77.41.47:4444/wd/hub";
	
	static final String PM_BUILD_WORKSPACE = getProjectName();
	
	static final String CLOUD_PROP_FNAME = PM_BUILD_WORKSPACE + "/environment/MobileEnvironment.properties";
	
	static final String TEST_PROP_FNAME = PM_BUILD_WORKSPACE + "/environment/TestEnvironment.properties";
	
	static final String TESTLINK_FNAME = PM_BUILD_WORKSPACE + "/environment/TestLink.properties";			
	
	static final String APP_BUILD_INFO_FNAME = PM_BUILD_WORKSPACE + "/environment/AppBuildInfo.properties";	
	
	static final String APP_INFO_FNAME = PM_BUILD_WORKSPACE + "/environment/Application.properties";		
	
	static final String LOG_FILENAME = PM_BUILD_WORKSPACE + "/environment/log4j.properties";	
	
	static final String DOWNLOAD_PATH = PM_BUILD_WORKSPACE + "/DownloadedFiles";
	
	static final String OBJECTREPOSITORY_PATH = PM_BUILD_WORKSPACE + "/objectrepository/ObjectRepository";
	
	static final String TEST_DATA_FILEPATH = PM_BUILD_WORKSPACE + "/testdata";
	
	static final String APPIUM_LOG = PM_BUILD_WORKSPACE + "/logs/Appium";
	
	static final String PROPERTY_FILEPATH = PM_BUILD_WORKSPACE +"/data";		
	
	static final String ENVIRONMENT_FILENAME = "Environment.properties";
	
	static final String REPORTS_PATH = "./Execution_Reports";
	
	static final String PM_REPORTS_PATH = REPORTS_PATH +"/Perfecto_Reports";
	
	static final String DESK_REPORTS_PATH = REPORTS_PATH +"/Desktop_Reports";
	
	static final String VIDEO_REPORTS_PATH = REPORTS_PATH +"/Reports/Video_Reports";
	
	static final String PDF_REPORTS_PATH = REPORTS_PATH +"/PDF_Reports";
	
	static final String DEV_PROP_INFO_FILE = PM_BUILD_WORKSPACE + "/data";
	
	static final String APP_PATH = PM_BUILD_WORKSPACE + "/app_kits";
	
	static final String XML_PATH = PM_BUILD_WORKSPACE + "/logs/testNG.xml";
	
	static final String EXEC_METHODS=PM_BUILD_WORKSPACE + "/logs/ExecutedMethods.txt";
	
	static final String EXEC_RESULTS=PM_BUILD_WORKSPACE + "/logs/TestResults.txt";
	
	static final String TESTNG_SUITE_FILES = PM_BUILD_WORKSPACE + "/logs/SuiteFiles";
	
	static final String TESTNG_OUTPUT = "./test-output";
	
	static final String BROWSER_DRIVER_PATH = PM_BUILD_WORKSPACE + "/libraries/drivers";
	
	static final String REPOSITORY_KEY = "PRIVATE:MyFiOS";
	
	static final String DEVICE_BROWSER = getdeviceBrowser();
	
	
	public static String getProjectName()
	{
		String pname=null;
		String newPath=null;
		try {
			Class<?> clazz = ApplicationConstants.class;
			Package pack = clazz.getPackage();
			String packName = pack.getName();
			String fPath = "./" + packName.replace(".", "/");
			File file = new File(fPath);
			newPath = file.getAbsolutePath();
			newPath = newPath.substring(0,newPath.lastIndexOf(".")-1);
			if (newPath.indexOf("\\")>0)
				pname = newPath.substring(newPath.lastIndexOf("\\")+1);
			else
				pname = newPath.substring(newPath.lastIndexOf("/")+1);
		} catch (Exception e) {}
		return newPath;
	}
	
	
	public static String getdeviceBrowser(){
		return new PropertyUtility().getTestEnvironmentProperty("device_list");
	}
	
	
}
