package com.verizon.automation.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

import org.testng.Reporter;
import org.testng.xml.XmlSuite;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.verizon.automation.util.DataUtil;
import com.verizon.automation.util.FrameworkUtil;
import com.verizon.automation.util.MongoDBUtil;
import com.verizon.automation.util.PropertyUtility;
import com.verizon.automation.util.ReportUtil;
import com.verizon.automation.logger.SeleniumLogWrapper;
import com.verizon.automation.constants.ApplicationConstants;

/*
 * CODE CHANGES HISTORY
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 	DATE		AUTHOR				METHODS MODIFIED/ADDED				CODE CHANGES DESCRIPTION
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *  08/11/2015  CHANDRASEKAR      										Class initial creation and implementing  EndecaTest(), beforeClass() and afterClass() methods
 */


/**
 * @date 08/10/2015
 * @author CHANDRASEKAR
 */


@SuppressWarnings("all")
public class ExecuteTest implements ApplicationConstants {
	
	public static void main(String[] args) {}
			
	public void executeTest(String[] args) {
		
		Logger logger = Logger.getLogger(ExecuteTest.class);
		FrameworkUtil util = new FrameworkUtil();
		ReportUtil rutil = new ReportUtil();
		PropertyUtility putil = new PropertyUtility();
		DataUtil dutil = new DataUtil();
		MongoDBUtil mutil = new MongoDBUtil();
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		String sReportJSON=null;
		try
		{
			String execTestPath = PM_BUILD_WORKSPACE+"/logs/ExecutedTests";
			boolean suiteXML = Boolean.valueOf(putil.getTestEnvironmentProperty("xml_module_suite"));
			boolean grid = Boolean.valueOf(putil.getTestEnvironmentProperty("grid_Execution"));
			File file = new File(execTestPath);
			if (file.exists()) FileUtils.deleteDirectory(file);
			file.mkdirs();
			
			// Get Values from TestEnvironment.prop
			util.getPropertyValues();
			
			// Clear Previous Run Logs/Reports/Files
			util.clearnPrevRunLogs();
			
			// Get the Test Modules to be Executed
			dutil.setEnvironmentModules(args);
			
			// Parse Test Input Params to decide the platform/device where the test needs to be run
			util.parseInputParameters();
			
			//creating and running TESTNG.xml suite
			if (suiteXML) {
				suites = util.testngXMLSuiteGenerate_suiteArray();
				util.triggerSuiteExecution(suites);
			} else {
				suites = util.testngXMLSuiteGenerate();
				util.triggerTestExecution(suites);
			}
			
			//uploading the results to mongoDB
			sReportJSON = rutil.generatepdfreport();
			mutil.uploadJSONtoMongo(sReportJSON);
			
			//copying log files
			rutil.copyLogFiles();
			
			//displaying the metrics
			util.getExecMetrics();
			
			//displaying the total execution time
			util.displayExecutionTime();
			
			//clearing local browser instances
			if (!grid) util.clearServerInstances();
			
		} catch (Exception e) {
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
	}
	
}


