package com.verizon.automation.configuration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.lang.reflect.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IClass;
import org.testng.IRetryAnalyzer;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.testng.xml.XmlSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.itextpdf.text.DocumentException;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.verizon.automation.constants.ApplicationConstants;
import com.verizon.automation.logger.SeleniumLogWrapper;
import com.verizon.automation.util.PropertyUtility;
import com.verizon.automation.util.ReportUtil;
import com.verizon.automation.util.DataUtil;
import com.verizon.automation.util.FrameworkUtil;
import com.verizon.automation.util.MongoDBUtil;

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
 * @author Harish
 * @description Interface class used to store APPLICATION CONSTANTS
 */

@SuppressWarnings("all")
public abstract class SuiteSetup implements ApplicationConstants {

	public static HashMap<String, String> apmDeviceModel = new HashMap<String, String>();
	public static HashMap<String, String> deviceProp = new HashMap<String, String>();
	public static Multimap<String, String> reproduceSteps = ArrayListMultimap.create();
	public static Multimap<String, String> retryModules = ArrayListMultimap.create();
	public static Map<String, String> fileMetMap = new HashMap<String, String>();
	public static Map<String, String> flowMetMap = new HashMap<String, String>(); /* shahed */
	public static List<String> failedTests = new LinkedList<String>();
	public static List<String> passedTests = new LinkedList<String>();
	public static Multimap<String, String> testMap = ArrayListMultimap.create();
	public static List<String> deviceRunning = new LinkedList<>();
	public static SoftAssert sAssert = new SoftAssert();
	public static Map attachedEmulators = new HashMap();
	public static Map<String, String> tcModule = null;
	public static Map<String, String> MOB_OS_VERSION;
	public static ArrayList<String> testmethodsExec;
	public static Map<String, String> tcMap = null;
	public static WebDriverWait waitForObject;
	public static List<String> device_ids;
	public static String[] testSheet;
	public static String[] environmentParams;
	public ITestContext suiteContext = null;

	public AppiumDriver appiumDriver;
	public RemoteWebDriver pmDriver;
	public AppiumDriver mob_Driver;
	public RemoteWebDriver br_Driver;

	public static String PM_REPOSITORY_PATH;
	public static boolean isPerfectoMobile;
	public static String APP_INSTALLER_URL;
	public static String execRegion;
	public static String DEV_OS_Deploy;
	public static String APP_PASSWORD;
	public static String APP_ACTIVITY;
	public static String PM_PASSWORD;
	public static String MOB_OS;

	public static String APP_NAME;
	public static String APP_USER;

	public static String EMU_PORT;

	public static String PM_HOST;
	public static String PM_USER;

	public static String APM_HOST;
	public static String APM_PORT;
	public static String APP_PACKAGE;

	public static String deviceType;
	public static String testPlatform;
	public static File resultfile;
	public static int emulatorCnt = 0;
	public static int tcCount;

	public String APP_VERSION = null;
	public String modulename = null;
	public String execStatus = "Pass";
	public String execDescription = null;
	public String device = null;
	public String device_os = null;
	public String dev_port = null;
	public String device_name = null;
	public String exec_platform = null;
	public String stepsReproduce = null;
	public String tcMethodName;
	// public String gridNode;

	public int teststepno = 0;
	public static int waitTime = 0;
	public int iterationCnt = 0;
	public int retryCount = 0;
	public int stepCnt = 1;

	public static String prop_GridExec;
	public static String prop_ExecPlatform;
	public static String prop_GridHub;
	public static String prop_DevList;
	public static String prop_BrowserInst;
	public static String prop_ProjPortfolio;
	public static String prop_ProjName;
	public static String prop_ProjSuite;
	public static String prop_ProxyReq;
	public static String prop_ProxyUser;
	public static String prop_ProxyPass;
	public static String prop_ProxyHost;
	public static String prop_ProxyPort;
	public static String prop_ProxyAutoConfigUrl;
	public static String prop_ProxyIgnore;
	public static String prop_ProxyType;
	public static String prop_Screenshot;
	public static String prop_ModSuite;
	public static String prop_Buildtool;
	public static String prop_Apptype;
	public static String prop_appiumserver;
	public static String prop_apppath;

	public boolean mdotcom = false;
	public boolean contExecution = true;
	public boolean isHomePage = false;

	public static MongoClient mongodbclient;
	public static DB mongodb;	
	
	private final static String ModuleKey = "module";
	private final static String ModuleDefault = "";

	public static List<ITestNGMethod> passedTestMethods;
	public static List<ITestNGMethod> failedTestMethods;
	public static List<ITestNGMethod> skippedTestMethods;

	public static Map<String, LinkedListMultimap<String, String>> dataMap;
	public LinkedListMultimap<String, String> dataValues;

	protected static final SeleniumLogWrapper slog = new SeleniumLogWrapper();
	protected final static Logger logger = Logger.getLogger(SuiteSetup.class);
	protected static final ReportUtil util = new ReportUtil();
	protected static final MongoDBUtil mutil = new MongoDBUtil();
	protected FrameworkUtil fr_util = null;
	protected static final PropertyUtility putil = new PropertyUtility();
	protected static final FrameworkUtil frameUtil = new FrameworkUtil();
	protected static final DataUtil dutil = new DataUtil();

	private final static String Module = System.getProperties().containsKey(ModuleKey) ? System.getProperty(ModuleKey)
			: ModuleDefault;

	public static String[] getModule() {
		return Module.split(",");
	}

	@DataProvider(name = "dataTable")
	public Object[][] dataTable(ITestNGMethod tmethod) throws Exception {
		Object[][] retObjArr = null;
		String dataPath = null;
		String module = null;
		String mname = null;
		try {
			String xlFile = this.getClass().getName();
			xlFile = xlFile.substring(0, xlFile.lastIndexOf("."));
			xlFile = xlFile.substring(xlFile.lastIndexOf(".") + 1);
			dataPath = TEST_DATA_FILEPATH + "/" + xlFile + ".xls";
			module = this.getClass().getSimpleName();
			mname = tmethod.getMethodName();
			Thread.sleep(1000);
			retObjArr = dutil.getTestHeader(dataPath, module, mname);
			return (retObjArr);
		} catch (Exception e) {
			util.reportLog("Error in setting up test data through Data Provider");
		}
		return retObjArr;
	}

	@BeforeSuite()
	public void beforeSuite(ITestContext context) {
		try {
			dataMap = new LinkedHashMap<String, LinkedListMultimap<String, String>>(2000);
			testmethodsExec = new ArrayList<String>(2000);
			frameUtil.getPropertyValues();
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
        
			
			mongodbclient = new MongoClient("143.91.240.167", 27017);
		    mongodb = mongodbclient.getDB("local");
		
			// Get the build tool from TestEnvironment.prop
		    prop_Buildtool="";
			if (prop_Buildtool.contains("Maven")) {
				String[] args = getModule();

				String execTestPath = PM_BUILD_WORKSPACE + "/logs/ExecutedTests";
				boolean suiteXML = Boolean.valueOf(putil.getTestEnvironmentProperty("xml_module_suite"));
				boolean grid = Boolean.valueOf(putil.getTestEnvironmentProperty("grid_Execution"));
				File file = new File(execTestPath);
				if (file.exists())
					FileUtils.deleteDirectory(file);
				file.mkdirs();

				// Clear Previous Run Logs/Reports/Files
				frameUtil.clearnPrevRunLogs();

				// Get the Test Modules to be Executed
				dutil.setEnvironmentModules(args);

				// Parse Test Input Params to decide the platform/device where
				// the test needs to be run
				frameUtil.parseInputParameters();

				suites = frameUtil.testngXMLSuiteGenerate_suiteArray();

			}

		} catch (Exception e) {
			util.reportLog("Error in getting the environment parameters");
		}
	}

	@BeforeMethod()
	public void beforeMethod(ITestContext context, Method method) {
		String installVal = null;
		String metName = null;
		String module = null;
		String hostIP = null;
		boolean grid = false;
		try {
			// initializing the driver
			br_Driver = null;

			// assigning context
			suiteContext = context;

			// get module and method name
			module = getModuleName(method);
			tcMethodName = getMethodName(method);

			// get the test data for the test iterations
			initializeTestData(module, tcMethodName);

			// Sync time
			Thread.sleep(1500);

			util.reportLog("Data Initialization for the test case - '" + tcMethodName + "' is done");

			if (prop_Buildtool.contains("Maven")) {
				this.br_Driver = getDriver(tcMethodName);
			}

		} catch (Exception e) {
			util.reportLog("Test Case Name : " + metName + " : Error in initializing the test data");
		}
	}

	@AfterMethod
	public void afterMethod(Method method) throws IOException, DocumentException, ParseException {
		String devName = null;
		String mName = null;
		try {
			mName = method.getName();
			ReportUtil rutil = new ReportUtil(br_Driver, mName);
			boolean testStat = util.getExecStatus(mName);
			if (testStat)
				rutil.reportlog("Execution Completed", "Test Case Execution completed", "Pass");
			else
				rutil.reportlog("Execution Completed", "Test Case Execution completed", "Fail");
			if (prop_DevList.toLowerCase().contains("android") || prop_DevList.toLowerCase().contains("ios")) {
				devName = br_Driver.getCapabilities().getCapability("deviceName").toString();
			} else {
				devName = br_Driver.getCapabilities().getBrowserName();
			}
			

		} catch (Exception e) {
			if (devName == null) {
				if (prop_DevList.equalsIgnoreCase("desktop")) {
					devName = StringUtils.capitalize(device);
				} else {
					devName = "Smartphones";
				}
			}
				
		} finally {
			String osName = System.getProperty("os.name");
			if (osName.toLowerCase().contains("win"))
				util.generateTestReportPDF(devName, mName);
			if (br_Driver != null) {
				if (!prop_Apptype.contains("native")) {
					br_Driver.close();
				}
				br_Driver.quit();
			}
		}
	}

	@AfterSuite
	public void afterSuite(ITestContext context) {
		String sReportJSON = null;
		boolean grid = Boolean.valueOf(prop_GridExec);

		try {

			if (br_Driver != null) {
				br_Driver.quit();
			}
		    mongodbclient.close();
			if (prop_Buildtool.contains("Maven")) {
				// uploading the results to mongoDB
				sReportJSON = util.generatepdfreport();
				mutil.uploadJSONtoMongo(sReportJSON);

				// copying log files
				util.copyLogFiles();

				// displaying the metrics
				frameUtil.getExecMetrics();

				// displaying the total execution time
				frameUtil.displayExecutionTime();

				// clearing local browser instances
				if (!grid)
					frameUtil.clearServerInstances();
			}
			

		} catch (Exception e) {

			if (testPlatform.equalsIgnoreCase("mobile")) {
				if (!Boolean.valueOf(isPerfectoMobile))
					frameUtil.stopAppiumNode();
			}
		}
	}

	/**
	 * @author MURUGCH
	 * @method getDeviceDetails
	 * @param context
	 */

	private void getDeviceDetails(ITestContext context) {
		try {
			device = context.getCurrentXmlTest().getParameter("device_host");
			device_os = context.getCurrentXmlTest().getParameter("device_os");
			dev_port = context.getCurrentXmlTest().getParameter("device_port");
			device_name = context.getCurrentXmlTest().getParameter("device_name");
		} catch (Exception e) {
		}
	}

	/**
	 * @author MURUGCH
	 * @method initializeTestData
	 * @param module
	 * @param metName
	 * @return
	 */

	private void initializeTestData(String module, String metName) {

		int Val = 0;
		dataValues = LinkedListMultimap.create(255);
		try {
			// get Retry count
			Val = validateRetryCount(module, metName);

			// creating a new LinkedListMultiMap for data
			if (dataMap == null)
				dataMap = new LinkedHashMap<String, LinkedListMultimap<String, String>>(10000);

			// Creating a new ArrayList to hold the test methods
			if (testmethodsExec == null)
				testmethodsExec = new ArrayList<String>(10000);

			// adding method to the ArrayList for different iterations to get
			// the iteration count in other class methods
			if (Val == 0) {
				testmethodsExec.add(metName);
				if (!testmethodsExec.contains(metName))
					testmethodsExec.add(metName);
			} else {
				if (dataMap != null && dataMap.size() > 0) {
					if (dataMap.containsKey(metName))
						dataMap.remove(metName);
				}
			}

			// gets the data value from the data sheet
			Thread.sleep(1000);
			dataValues = dutil.getTestData(module, metName);

			// assigns the data value to the data list
			Thread.sleep(1000);
			if (!dataMap.containsKey(metName))
				dataMap.put(metName, dataValues);
		} catch (Exception e) {
			logger.info("Error in initializing the test data : " + e.getMessage());
		}
	}

	/**
	 * @author MURUGCH
	 * @method validateRetryCount
	 * @param module
	 * @param metName
	 * @return Val
	 */

	private int validateRetryCount(String module, String metName) {

		int Val = 0;
		try {
			// instantiate FrameworkUtil
			fr_util = new FrameworkUtil();

			// getting the failure retry count
			Val = dutil.getRetryCount();

			// checking if the test retry for the same iteration
			if (Val == retryCount)
				Val = 0;

			if (Val > 0) {

				String flowName = flowMetMap.get(metName);
				String runFold = fr_util.getTheNewestFolder(REPORTS_PATH);
				String path = runFold + "/Platform-" + testPlatform + "/" + flowName;
				File fold = new File(path);
				for (File f : fold.listFiles()) {
					String fName = f.getName();
					if (fName.contains(metName) && fName.endsWith("txt")) {
						PrintWriter writer = new PrintWriter(f);
						writer.print("");
						writer.close();
					}
				}

				if (Val > 1)
					module = module + "_Failed" + Val;
				else
					module = module + "_Failed";
				reproduceSteps.removeAll(metName);

				if (failedTestMethods != null) {
					if (failedTestMethods.contains(metName)) {
						failedTestMethods.remove(metName);
					}
				}

				if (failedTests != null) {
					if (failedTests.contains(metName)) {
						failedTests.remove(metName);
					}
				}
			}

			// reassigning the failure retry count
			retryCount = Val;

			logger.info("Retry Count for the test case - '" + metName + "' is :" + retryCount);
		} catch (Exception e) {
			// System.out.println("Error in getting the Validate Retry Count");
			logger.info("Error in getting the Validate Retry Count");
		}
		return Val;
	}

	/**
	 * @author MURUGCH
	 * @method getMethodName
	 * @param method
	 * @return metName
	 */

	private String getMethodName(Method method) {
		String metName = null;
		String module = null;
		try {
			// getting the class and test method name
			module = this.getClass().getName();
			module = module.substring(0, module.lastIndexOf("."));
			module = module.substring(module.lastIndexOf(".") + 1);
			metName = method.getName().toString();
		} catch (Exception e) {
		}
		return metName;
	}

	/**
	 * @author MURUGCH
	 * @method getModuleName
	 * @param method
	 * @return module
	 */

	private String getModuleName(Method method) {
		String metName = null;
		String module = null;
		try {
			// getting the class and test method name
			module = this.getClass().getName();
			module = module.substring(0, module.lastIndexOf("."));
			module = module.substring(module.lastIndexOf(".") + 1);
			metName = method.getName().toString();
		} catch (Exception e) {
		}
		return module;
	}

	/**
	 * @author MURUGCH
	 * @method createBrowserDrivers
	 * @description Creates the RemoteWebDriver capabilities for the test case
	 *              execution
	 * @param metName
	 */

	public RemoteWebDriver createBrowserDrivers(String metName) {

		RemoteWebDriver tempDriver = null;
		File dir = null;
		fr_util = new FrameworkUtil();
		ReportUtil rep = null;
		String text = "Create WebDriver";
		String step = "Create the selenium remotewebdriver for the execution";
		String desc = null;
		try {
			tcMethodName = metName;
			device_name = suiteContext.getCurrentXmlTest().getParameter("device_name");
			device = suiteContext.getCurrentXmlTest().getParameter("device_host");
			device_os = suiteContext.getCurrentXmlTest().getParameter("device_os");
			dev_port = suiteContext.getCurrentXmlTest().getParameter("device_port");
			if (br_Driver == null) {
				if (device_name.equalsIgnoreCase("desktop")) {
					//tempDriver = fr_util.browserSetup(metName, device);
					tempDriver = fr_util.createDriver(metName, device);
					tempDriver.manage().window().maximize();
				} else {
					//tempDriver = fr_util.createDriver(metName, device, device_os, dev_port);
					tempDriver = fr_util.createDriver(metName, prop_DevList);
				}
				tempDriver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
			}
			rep = new ReportUtil(tempDriver, metName);
			if (tempDriver != null) {
				desc = "RemoteWebDriver is successfully created!";
				rep.stepsToReproduce(step);
				rep.reportlog(text, desc, "Pass");
			} else {
				desc = "Failed to create the RemoteWebDriver!";
				rep.reportlog(text, desc, "Fail");
				rep.reportComments(desc, step, true);
				fr_util.reportlog(text, desc, "Fail");
				fr_util.reportComments(desc, step, true);
			}

			// Logging
			util.reportLog("[[" + metName + "]] : Application is launched for the Test case");
		} catch (Exception e) {
			util.reportLog("[[" + metName + "]] : Error in launching Application for the Test case");
			util.reportLog("Error in creating the webdriver");
			logger.info("WebDriver Creation error : " + e.getMessage());
			fr_util.reportlog(text, desc, "Fail");
			fr_util.reportComments(desc, step, true);
		}
		return tempDriver;
	}

	/**
	 * @author Chala
	 * @method getDriver
	 * @description Creates the RemoteWebDriver capabilities for the test case
	 *              execution
	 * @param metName
	 */

	public RemoteWebDriver getDriver(String metName) {

		RemoteWebDriver tempDriver = null;
		fr_util = new FrameworkUtil();
		ReportUtil rep = null;
		String text = "Create WebDriver";
		String step = "Create the selenium remotewebdriver for the execution";
		String desc = null;
		try {
			if (br_Driver == null)
				tempDriver = fr_util.createDriver(metName, prop_DevList);

			rep = new ReportUtil(tempDriver, metName);
			if (tempDriver != null) {
				desc = "RemoteWebDriver is successfully created!";
				rep.stepsToReproduce(step);
				rep.reportlog(text, desc, "Pass");
				util.reportLog("[[" + metName + "]] : Application is launched for the Test case");
			} else {
				desc = "Failed to create the RemoteWebDriver!";
				rep.reportlog(text, desc, "Fail");
				rep.reportComments(desc, step, true);
				util.reportLog("[[" + metName + "]] : Error in driver creation");
			}

		} catch (Exception e) {
			util.reportLog("[[" + metName + "]] : Error in launching Application for the Test case");
			util.reportLog("Error in creating the webdriver");
			logger.info("WebDriver Creation error : " + e.getMessage());
		}
		return tempDriver;
	}

}
