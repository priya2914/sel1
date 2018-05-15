package com.verizon.automation.util;

import com.verizon.automation.configuration.SuiteSetup;
import com.verizon.automation.logger.SeleniumLogWrapper;
import com.verizon.automation.constants.ApplicationConstants;
import com.verizon.automation.listener.TestListener;

import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.general.DefaultPieDataset;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.codec.binary.Base64;
import org.bson.Document;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.ClassLoader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;
import org.openqa.selenium.security.UserAndPassword;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.xml.Parser;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.Align;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

import com.github.genium_framework.appium.support.server.AppiumServer;
import com.github.genium_framework.server.ServerArguments;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.Session;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;


/**
 * @date 07/22/2015
 * @author Chandrasekar Murugesan
 * @description Class to manage applications and all the framework related functions 
 *
 */

@SuppressWarnings("all")
public class FrameworkUtil extends SuiteSetup {
	
	
	private static String runBrowser = null;
	private final static Logger logger = Logger.getLogger(FrameworkUtil.class);
	private static String devOS = null;
	private static List<XmlSuite> suites=null;
	private static XmlSuite suite=null;
	private static Map<String, String> suiteParams=null;
	private static XmlTest test=null;
	private static List<String> tcNames=null;
	private Map<String, String> module = new LinkedHashMap<String, String>();
	private List<String> tcMods=null;
	private static String MOB_DEVICE_OS;
	private static String APP_INSTALLER_FILE;
	private static String RUN_SERVER;
	private static String APP_FILE_NAME; 
	private AppiumServer apmServer;
	private static String APP_KIT_UPDATED;
	private static List<String> tmets;
	private int devcount=0;
	private String tSteps;
	private int itCount;
	private JSch jsch;
	private Session session;
	private ChannelExec channel;
	private String hostipaddress;
	private BufferedReader remotebufferedReader;
	private WebDriverWait waitForDriver=null;
	private boolean distributed=false;
	private String mTestName;
	private static String pass_percent;
	private static String fail_percent;
	private String testname;
	long end_milliseconds;
	String end_timestamp=null;
	DateUtility dateutil = new DateUtility();
	String start_timestamp=dateutil.getSystemStringDate("MM-dd-yyyy hh:mm:ss");
				
	public static long start_milliseconds = System.currentTimeMillis();
	private static PropertyUtility putil = new PropertyUtility();
		
	DataUtil dutil = new DataUtil();
	ReportUtil repUtil;
	RemoteWebDriver frameDriver;
	
	public FrameworkUtil() {}
	
	public FrameworkUtil(RemoteWebDriver remoteDriver) {
		repUtil = new ReportUtil(remoteDriver);
		this.frameDriver=remoteDriver;
	}
	
	public FrameworkUtil(RemoteWebDriver remoteDriver, String test) {
		this.frameDriver=remoteDriver;
		this.testname = test;
	}
	
		
	/**
	 * @author MURUGCH
	 * @param args
	 * @description PARSES THE INPUT PARAMETER FROM THE ARGUMENT AND RE-ROUTING TO COMPATIBLE PARAMETER
	 */
	public void parseInputParameters() {
		
		String platform=null;
		String dev_val=null;
		try	{
			String ePlat=prop_ExecPlatform;
			devOS=prop_DevList;
			
			// 08/18/2016 : Chala
			runBrowser = devOS;
						
			if (ePlat.equalsIgnoreCase("desktop"))
				testPlatform="Desktop";
			else if (ePlat.toLowerCase().contains("desk")) {
				testPlatform="All";
				if (ePlat.toLowerCase().contains("perfecto"))
					isPerfectoMobile=true;
				else if (ePlat.toLowerCase().contains("appium"))
					isPerfectoMobile=false;
			} else {
				testPlatform="Mobile";
				if (ePlat.toLowerCase().contains("perfecto"))
					isPerfectoMobile=true;
				else if (ePlat.toLowerCase().contains("appium"))
					isPerfectoMobile=false;
			}
			
			if (devOS.toLowerCase().contains("android"))
    			MOB_OS = "Android";
    		else if (devOS.toLowerCase().contains("ios"))
    			MOB_OS = "iOS";
    		else if (devOS.toLowerCase().contains("emulator"))
    			MOB_OS = "Android";
	    	else if (devOS.toLowerCase().contains("simulator"))
	    		MOB_OS = "iOS";
	    	else
	    		MOB_OS="";
    		
    		if (testPlatform.equalsIgnoreCase("mobile")) {
    			if (devOS.equals(""))
					devOS="Android";
    			else {
	    			if (isPerfectoMobile) {
				    	if (devOS.equalsIgnoreCase("ios"))
				    		devOS = "all_ios_devices";
				    	else if (devOS.equalsIgnoreCase("android"))
				    		devOS = "all_android_devices";
				    } else {
		    			if (devOS.equalsIgnoreCase("ios"))
				    		devOS = "ios_devices";
				    	else if (devOS.equalsIgnoreCase("android"))
				    		devOS = "android_devices";
				    	else if (devOS.equalsIgnoreCase("emulator"))
				    		devOS = "all_emulators";
				    	else if (devOS.equalsIgnoreCase("simulator"))
				    		devOS = "all_simulators";
				    	else if (devOS.equalsIgnoreCase("all_android"))
				    		devOS = "all_android_devices";
				    	else if (devOS.equalsIgnoreCase("all_ios"))
				    		devOS = "all_ios_devices";
		    		} 
    			}
			} else if (testPlatform.equalsIgnoreCase("desktop")){
				if (devOS.equals(""))
					devOS="Firefox";
				else if (devOS.equalsIgnoreCase("all"))
					devOS="Firefox;IE;Chrome";
			} else {
				if (devOS.equals("")) {
					if (isPerfectoMobile)
						devOS="Firefox;all_android_devices";
					else
						devOS="Firefox;android_devices";
				}
				String[] devs=devOS.split(";");
				for (int i=0; i<devs.length; i++) {
					if (dev_val==null)
						dev_val="";
					if (devs[i].contains("all_browsers")) {
						dev_val=dev_val+"Firefox;Chrome;IE";
					} else if (devs[i].toLowerCase().contains("android")) {
						if (isPerfectoMobile)
							dev_val=dev_val+"all_android_devices";
						else	
							dev_val=dev_val+"android_devices";
					} else if (devs[i].toLowerCase().contains("ios")) {
						if (isPerfectoMobile)
							dev_val=dev_val+"all_ios_devices";
						else	
							dev_val=dev_val+"ios_devices";
					} else
						dev_val=dev_val+devs[i];
					
					if (i<(devs.length)-1)
						dev_val=dev_val+";";
				}
				devOS=dev_val;
			}
    				    	
	    	logger.info("devOS:"+devOS);
		}
		catch(Exception e) 
		{
			logger.error("FrameworkUtil :: parseInputParameters() Exception:"+e);
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
	}

	
	public void reportLog(String text) {
		repUtil.reportLog(text);
	}
	
	
	public void stepsToReproduce(String step) {
		repUtil.stepsToReproduce(step);
	}
		
	
	public void reportComments(String desc, String Step, boolean Flag) {
		String test=dutil.getTestName();
		if (test.isEmpty() || test==null)
			test = this.testname;
		ReportUtil rutil = new ReportUtil(frameDriver,test);
		rutil.reportComments(desc, Step, Flag);
	}
	
	public void reportlog(String text, String stepDesc, String status) {
		String test=dutil.getTestName();
		if (test.isEmpty() || test==null)
			test = this.testname;
		ReportUtil rutil = new ReportUtil(frameDriver,test);
		rutil.reportlog(text, stepDesc, status);
	}
	
	public void turnOffImplicitWaits() {
		frameDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	public void turnOnImplicitWaits() {
		frameDriver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}
	
	/**
	 * @author MURUGCH
	 * @description CREATES A TESTNG LISTENER AND TRIGERS THE EXECUTION THROUGH THE TESTNG SUITE XML
	 * @param suites
	 */
	
	public void triggerTestExecution(List<XmlSuite> suites) {
		try {
			TestListenerAdapter tla = new TestListenerAdapter();
		    TestNG tng = new TestNG();
		    tng.setUseDefaultListeners(false);
		    tng.setXmlSuites(suites);
		    tng.addListener(tla);
		    tng.run();
		} catch(Exception e) {
			logger.error("FrameworkUtil :: triggerTestExecution() Exception:"+e);
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
	}
	
	
	public List mergeTestSuites(List<XmlSuite> suites) {
		String suitePath=null;
		List<String> suiteFiles=null;
		try {
			XmlSuite suite = new XmlSuite();
			suiteFiles = new ArrayList<String>();
			File sFile = new File(TESTNG_SUITE_FILES);
			String[] extension = new String[]{"xml"};
			List<File> files = (List<File>) FileUtils.listFiles(sFile, extension, true);
			for (File file : files) {
				if (!file.getName().contains("Merged")) {
					String path = file.getAbsolutePath();
					suiteFiles.add(path);
				}
			}
			suite.setName("MergedSuite");
			suite.setThreadCount(suites.size());
			suite.setSuiteFiles(suiteFiles);
			
			suitePath = TESTNG_SUITE_FILES + "/MergedSuite.xml";
			FileWriter fw = new FileWriter(new File(suitePath));
	    	fw.write(suite.toXml());
	    	fw.flush();
	    	fw.close();
			
	    } catch (Exception e) {
			logger.info("FrameworkUtil :: mergeTestSuites() -- Error in merging the suite Files");
		}
		return suiteFiles;
	}
	
	
	public void triggerSuiteExecution(List<XmlSuite> suites) {
		try {
			TestNG tng = new TestNG();
			List<String> testSuite = new ArrayList<String>();
			testSuite = mergeTestSuites(suites);
			TestListenerAdapter tla = new TestListenerAdapter();
			tng.setUseDefaultListeners(false);
			tng.addListener(tla);
			tng.setTestSuites(testSuite);
		    tng.setSuiteThreadPoolSize(suites.size());
		    tng.run();
		} catch(Exception e) {
			logger.error("FrameworkUtil :: triggerTestExecution() Exception:"+e);
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate the testng suite.xml for execution
	 * @param 
	 */
			
	public List<XmlSuite> testngXMLSuiteGenerate() {
		
		//variable declarations
		suites = new ArrayList<XmlSuite>();
		tcMap = new HashMap<String, String>();
		Integer numberOfHosts;
		tcNames = new LinkedList<>();
		tcModule=new HashMap<String, String>();
		FileInputStream objFile;
		Workbook objWorkbook;
		Sheet objSheet;
		String[] gridDev = null;
		
		try {
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities = new DesiredCapabilities("", "", Platform.ANY);
			int browser_inst = Integer.valueOf(prop_BrowserInst);
			String[] devs_list = prop_DevList.split(";");
			int browserCnt = devs_list.length;
			boolean gridExec=Boolean.valueOf(prop_GridExec);
			
			//getting the test modules for execution  
			getExecutableTestCases();
			
			//getting mobile grid devices
			if (!testPlatform.equalsIgnoreCase("desktop")) {
				int size = tmets.size();
				gridDev = new String[size];
				for (int m=0; m<size; m++) {
					gridDev[m]="Device-"+String.valueOf(m+1);
				}
			}
			
	    	if (testPlatform.equalsIgnoreCase("mobile")) {
				//getting the list of available devices for execution in PerfectoMobile
				if(!isPerfectoMobile) {
					
					logger.info("FrameworkUtil :: testngXMLSuiteGenerate(), getting available devices from Appium Cloud!");
					if (gridExec) {
						if (device_ids==null)
							device_ids=new LinkedList<String>();
						for (int k=0; k<gridDev.length; k++) {
							device_ids.add(gridDev[k]);
						}
					} else { 
						device_ids = getAPMDevices(devOS);
					}
				}
			} else if (testPlatform.equalsIgnoreCase("all")) {
				
				String[] dev=devOS.split(";");
				for (int i=0; i<dev.length; i++) {
					if (!dev[i].equalsIgnoreCase("firefox") && !dev[i].equalsIgnoreCase("ie") && !dev[i].equalsIgnoreCase("chrome") && !dev[i].equalsIgnoreCase("safari")) {
						if (tcModule.containsValue("Browser") || !tcModule.containsValue("Desktop")) {
							if(!isPerfectoMobile) {
								
								//getting the list of connected devices on Appium Machines
								logger.info("FrameworkUtil :: testngXMLSuiteGenerate(), getting available devices from Appium Cloud!");
								logger.info("FrameworkUtil :: testngXMLSuiteGenerate(), getting available devices from Appium Cloud!");
								if (gridExec) {
									if (device_ids==null)
										device_ids=new LinkedList<String>();
									for (int k=0; k<gridDev.length; k++) {
										device_ids.add(gridDev[k]);
									}
								} else { 
									device_ids = getAPMDevices(dev[i]);
								}
							}
						}
					}
				}
								
				if (tcModule.containsValue("Browser") || tcModule.containsValue("Desktop")) {
					if (device_ids==null)
						device_ids=new LinkedList<String>();
					
					for (int i=0; i<dev.length; i++) {
						if (dev[i].equalsIgnoreCase("firefox") || dev[i].equalsIgnoreCase("ie") || dev[i].equalsIgnoreCase("chrome") || dev[i].equalsIgnoreCase("safari")) {
							device_ids.add(dev[i]);
						}
					}
				}
			} else {
				if (device_ids==null)
					device_ids=new LinkedList<String>();
				
				String[] dev = devOS.split(";");
				
				for (int i=0; i<dev.length; i++) {
					//Chala : 08/18/2016
					// added chrome-tablet
					if (dev[i].contains("chrome-mobile") || dev[i].contains("chrome-tablet") || dev[i].equalsIgnoreCase("firefox") || dev[i].equalsIgnoreCase("ie") || dev[i].equalsIgnoreCase("chrome") || dev[i].equalsIgnoreCase("safari")) {
						device_ids.add(dev[i]);
					}
				}
			}
					
			//getting the total number of devices available for execution
	    	numberOfHosts = device_ids.size();
	        	        
	        //if devices are not available
	        if (numberOfHosts == 0)
	           	repUtil.reportLog("Devices are not available for execution, please try the execution later!");
	       	        
	        //creating a new Suite
	        suite = new XmlSuite();
	        suite.setName("ParallelExecution");
	        suite.setTimeOut("3600000");
	        suite.setParallel(ParallelMode.TRUE);
	        suite.setParallel(ParallelMode.TESTS);
	        suite.setThreadCount(tmets.size());
	        suite.setDataProviderThreadCount((tmets.size()*10));
		        	
		    //new list for the parameters
        	suiteParams = new HashMap<String, String>();
	        
	        //adding a listener
	        suite.addListener("org.uncommons.reportng.HTMLReporter");
	        suite.addListener("org.uncommons.reportng.JUnitXMLReporter");
	        suite.addListener("com.verizon.automation.listener.RetryListener");
	        suite.addListener("com.verizon.automation.listener.TestListener");
	        
	        //creating the test suite
	        distributedTestSuite();
	        
	        // Add suite to the list
	        suites.add(suite);
        	
	        //exporting the testng-suite.xml
        	FileWriter fw = new FileWriter(new File(PM_BUILD_WORKSPACE+ "/logs/testNG-Suite.xml"));
        	fw.write(suite.toXml());
        	fw.flush();
        	fw.close();
        	logger.info("FrameworkUtil :: testngXMLSuiteGenerate() -- testNG.xml suite created");
		} catch(Exception e) {
			logger.error("FrameworkUtil :: testngXMLSuiteGenerate() Exception:"+e);
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
		return suites;
	}
	
			
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to create the TestNG suite.xml for distributed execution
	 * @param 
	 */
	public void distributedTestSuite() {
		String OS_Param=null;
		String dev_name=null;
		String testname;
		Method[] method=null;
		ClassLoader myClassLoader=null;
		Class myClass=null;
		int devNum=1;
		boolean desk=false;
		boolean loop=false;
		String tempPath = PM_BUILD_WORKSPACE+"/logs/ExecutedTests";
		try {
			
			//getting the environment property values from the property files
			int dev_port = Integer.valueOf(APM_PORT);
			tcMods=new LinkedList<String>();
			int instance=Integer.valueOf(prop_BrowserInst);
			boolean grid=Boolean.valueOf(prop_GridExec);
			FileWriter filew = new FileWriter(new File(EXEC_METHODS),true);
			PrintWriter printer = new PrintWriter(filew);
			
			String packageName = getTestPackageName();
			if ((testPlatform.equalsIgnoreCase("all") || testPlatform.equalsIgnoreCase("desktop")) && 
					(tcModule.containsValue("Browser") || tcModule.containsValue("Desktop"))) {
				if (instance>1) {
					String[] devs=device_ids.toArray(new String[device_ids.size()]);
					for (int i=1; i<instance; i++) {
						for (int j=0; j< devs.length; j++) {
							if (devs[j].equalsIgnoreCase("firefox") || devs[j].equalsIgnoreCase("chrome") || devs[j].equalsIgnoreCase("ie") || devs[j].equalsIgnoreCase("safari") || devs[j].equalsIgnoreCase("chrome-mobile") || devs[j].equalsIgnoreCase("chrome-tablet") )
								device_ids.add(devs[j]+(i+1));
						}
					}
				}
			}
			
			
			for (int tloop=0; tloop<2; tloop++) {
				
				List<String> devices=new LinkedList<String>();
				
				if (tloop==0) {
					for (String devid : device_ids) {
						if (devid.toLowerCase().contains("firefox") || devid.toLowerCase().contains("chrome") || devid.toLowerCase().contains("ie") || devid.toLowerCase().contains("safari") || devid.equalsIgnoreCase("chrome-mobile") || devid.equalsIgnoreCase("chrome-tablet"))
							devices.add(devid);
					}
				} else {
					for (String devid : device_ids)	{
						if (!devid.equalsIgnoreCase("firefox") && !devid.equalsIgnoreCase("chrome") && !devid.equalsIgnoreCase("ie") && !devid.equalsIgnoreCase("safari") && !devid.equalsIgnoreCase("chrome-mobile") && !devid.equalsIgnoreCase("chrome-tablet"))
							devices.add(devid);
					}
				}
				
				if (devices.isEmpty()) {
					for (String devid : device_ids)	{
						devices.add(devid);
					}
				}
				
				//getting the total number of devices
				int totDevices=devices.size();
				
				String[] metVals=tmets.toArray(new String[tmets.size()]);
				
				//initializing iVal
	        	int iVal=0;
	        	
	        	tcMods=new LinkedList<String>();
	        	
		    	for (String devids : devices) {
		    		
		    		// add parameters to the list
		    		if (testPlatform.equalsIgnoreCase("all")) {
		    			if (devids.toLowerCase().contains("firefox") || devids.toLowerCase().contains("chrome") || devids.toLowerCase().contains("ie") || devids.toLowerCase().contains("safari"))
			    			desk=true;
			    		else
			    			desk=false;
		    		} else {
			    		if (devids.toLowerCase().contains("firefox") || devids.toLowerCase().contains("chrome") || devids.toLowerCase().contains("ie") || devids.toLowerCase().contains("safari"))
			    			desk=true;
			    		else
			    			desk=false;
		    		}
		    		
		    		if (!desk) {
			        	if (!isPerfectoMobile) {
			        		if (prop_GridExec.equals("false")) {
					        	OS_Param = getDeviceOS(devids);
				        	} else {
			        			if (prop_DevList.toLowerCase().contains("android")) {
			        				OS_Param = "Android";
			        			} else {
			        				OS_Param = "iOS";
			        			}
			        		}
			        		dev_name = devOS;
			        	}
		    		} else {
		    			OS_Param = System.getProperty("os.name");
		    			dev_name=devids;
		    		}
		        	
		        	//new list for the test parameters
		        	Map<String, String> testParams = new HashMap<String, String>();
		        	
		        	if(!desk) {
			        	testParams.put("device_host", devids);
			        	testParams.put("device_os", MOB_OS);
			        	testParams.put("device_name", dev_name.trim());
			        	testParams.put("device_port", String.valueOf(dev_port));
			        	testParams.put("isPerfecto", String.valueOf(isPerfectoMobile));
			       		testParams.put("testname", "Suite");
		        	} else {
		        		testParams.put("device_host", devids.replaceAll("[0-9]",""));
			        	testParams.put("device_os", OS_Param);
			        	testParams.put("device_name", "Desktop");
			        }
		        	
		       		//new list for the classes
			        List<XmlClass> classes = new ArrayList<XmlClass>();
			        int temp=iVal;
			        int tempVal;
			        String tcName=null;
			        Multimap<String, String> multiMap = ArrayListMultimap.create();
			        
			        int j=iVal;
			        while (j<metVals.length) {
			        	tcName = metVals[j];
		       			Collection mods = testMap.get(tcName);
			        	Iterator it = mods.iterator();
			        	while (it.hasNext()) {
			        		String mod = it.next().toString();
			        		multiMap.put(mod,tcName);
			        	}
			        	j=j+totDevices;
			        }
			        
			        Set keySet = multiMap.keySet();
			        Iterator keyIterator = keySet.iterator();
			        
			        while (keyIterator.hasNext()) {
			            String key = (String) keyIterator.next();
			            boolean flag;
			            if (!desk) {
			            	flag=!tcModule.get(key).toLowerCase().contains("desktop");
			            } else {
			            	flag=(tcModule.get(key).toLowerCase().contains("desktop") || tcModule.get(key).toLowerCase().contains("browser"));
			            }
			            
			            if (flag) {
				            List<String> values = (List) multiMap.get(key);
				            String modName = module.get(key);
			       			String packName=packageName+"."+modName.toLowerCase();
			       			String cName=packName+"."+key;
			       			testname = tcName + "_" + devids;
				        	XmlClass testClass = new XmlClass(cName);
				        	
				        	ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
				        	ArrayList<XmlInclude> methodsToIgnore = new ArrayList<XmlInclude>();
				        	
				        	myClassLoader= ClassLoader.getSystemClassLoader();
							myClassLoader= this.getClass().getClassLoader();
				        	myClass=myClassLoader.loadClass(cName);
				        	
				        	for(String val:values) {
				        		boolean exist=false;
				        		method=myClass.getDeclaredMethods();
				        		int counter=0;
					        	for (Method m : method) {
					        		String mName=m.getName();
					        		if (mName.equalsIgnoreCase(val)) {
					        			methodsToRun.add(new XmlInclude(mName));
					        			File f = new File(tempPath+"/"+val+".temp");
							        	f.createNewFile();
							        	exist=true;
					        			break;
					        		} else {
					        			String missFilePath = tempPath + "/MissedTC/" + key;
					        			if (!new File(missFilePath).exists())
					        				new File(missFilePath).mkdirs();
					        			
					        			if (counter==(method.length)-1) {
					        				printer.println("Test Case Name : " + val + "--->  Java Class Name : " + key);
					        				File notAdded = new File(missFilePath+"/"+val+".miss");
					        				notAdded.createNewFile();
					        			}
					        		}
					        		counter++;
					        	}
					        	//if (exist)
					        	//	methodsToRun.add(new XmlInclude(val));
					        	tcMods.add(val);
					        	
				        	}
				        					        	
				        	if (methodsToRun.size()>0) {
				        		testClass.setIncludedMethods(methodsToRun);
					        	classes.add(testClass);
				        	}
			            }
			        }
		       		
		       		//add classes to test
			        if (classes.size()>0) {
			        	test = new XmlTest(suite);
			        	test.setParameters(testParams);
			       		test.setPreserveOrder("true");
			       		if (desk)
			       			test.setName("TestExecution_"+devids.replaceAll("[0-9]","")+devNum);
			       		else
			       			if (grid)
			       				test.setName("TestExecution_MobDevice"+devNum);
			       			else
			       				test.setName("TestExecution_"+devids);
			       		
			       		test.setThreadCount(30);
			        	test.setClasses(classes);
			        	devNum++;
			        }
			        
		        	iVal++;
		        	
		        	//incrementing the port for different appium instances
		        	if(!desk) 
			        	dev_port ++;
			        
		        	if (tcMods.size()==tmets.size())
		        		break;
			    }
		    	
		    	if (!tcModule.containsValue("Browser"))
		    		break;
		    	
		    	printer.close();
			}
			
		} catch (Exception e) {
			//System.out.println("Error : " + e.getMessage());
		}
	}
	

	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the package name of the executing test case
	 * @param 
	 */
	private String getTestPackageName()
	{
		String pName=null;
		try {
			String dir = PM_BUILD_WORKSPACE;
			Collection files = (Collection) org.apache.commons.io.FileUtils.listFiles(new File(dir), new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
			Iterator iter = files.iterator();
			while (iter.hasNext())
			{
				File file = (File) iter.next();
				String name = file.getAbsolutePath();
				if (name.toLowerCase().contains("testcases")) {
					if (name.indexOf("\\")>0)
						name=name.replace("\\", ".");
					else
						name=name.replace("/", ".");
					if (name.toLowerCase().contains("bin"))
						pName=name.substring(name.indexOf("bin.")+4);
					else if (name.toLowerCase().contains("src"))
						pName=name.substring(name.indexOf("src.")+4);
					pName=pName.substring(0, pName.lastIndexOf("testcases."));
					pName=pName+"testcases";
					break;
				}
			}
		} catch (Exception e) {
			//System.out.println("Could not find the package name");
		}
		return pName;
	}


	/**
	 * @date 09/04/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to stop the already running Appium server
	 * @param Appium PORT as String
	 */
	   
	public RemoteWebDriver createDriver(String metName, String device, String device_os, String dev_port) {
		
		   RemoteWebDriver aDriver = null;
		   boolean grid=false;
		   String classname=null;
		   try
		   {
			   StackTraceElement[] text = Thread.currentThread().getStackTrace();
			   for (int i=0; i<text.length; i++) {
				   if (text[i].toString().toLowerCase().contains(".testcases")) {
					   classname = Thread.currentThread().getStackTrace()[i].getClassName();
					   classname = classname.substring(classname.lastIndexOf(".")).replace(".", "");
					   break;
				   }
			   }
			   
			   if (tcModule.get(classname).equalsIgnoreCase("mdot") || tcModule.get(classname).equalsIgnoreCase("browser"))
				   mdotcom=true;
			   else
				   mdotcom=false;
			   
			   if (!Boolean.valueOf(isPerfectoMobile)) {
				   aDriver=appiumSetup(metName, dev_port, device, device_os);
			   }	
			   	
			   if (mdotcom)
				  aDriver.manage().deleteAllCookies();
			   	
			   deviceRunning.add(device);
		   }catch(Exception e)	{}
		   return aDriver;
	   }


	public RemoteWebDriver appiumSetup(String methodName, String port, String device, String device_os) throws IOException  {
	   
	   	DesiredCapabilities capabilities;
	  	AndroidDriver and_driver=null;
	  	RemoteWebDriver appDriver=null;
	  	IOSDriver ios_driver=null;
	  	String strAppURL=null;
	  	String newPath=null;
	  	String appPath=null;
	  	String HOST=null;
	  	boolean apmStarted=false;
	  	String browserName=null;
	  	String url=null;
	  	boolean grid=false;
	  	try{
	  		
	  		//grid execution
	  		grid=Boolean.valueOf(prop_GridExec);
	  		
	  		//updating application build property file
			MOB_OS=device_os;
			
			if (device_os.toLowerCase().contains("android"))
				browserName="Chrome";
			else
				browserName="Safari";
			
			//Define the capabilities to use the local Appium server
			capabilities = new DesiredCapabilities();
			
			if (!grid) {
				
				if (device_os.toLowerCase().contains("android"))
					HOST = putil.getMobileEnvironmentProperty("appium_server_android");
				else
					HOST = putil.getMobileEnvironmentProperty("appium_server_ios");
				
				if (!HOST.equals(""))
	  				APM_HOST=HOST;
				
				apmStarted=APM_Server(device,port);
					  		
		  		if(apmStarted) {
		  			
		  			url = "http://" + APM_HOST + ":" + port + "/wd/hub";
			  		APP_NAME=putil.getApplicationProperty("appname");
					MOB_OS=device_os;
					deviceType = device;
					
					//Define the device to work on
					capabilities.setCapability("browserName", browserName);
					capabilities.setCapability("platformName", device_os);
					capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
					capabilities.setCapability("deviceName",device);
				} else {
		  			repUtil.reportLog("Appium Server Failed to Start for Device - '" + device + "' on port : '" + port + "'");
		  		}
			} else {
				url=prop_GridHub;
				capabilities.setCapability("browserName", browserName);
				capabilities.setCapability("platformName", device_os);
				if (devOS.toLowerCase().contains("smartphone")) {
					capabilities.setCapability("version", "5.0.0");
					capabilities.setCapability("deviceName", "smartphone");
				} else if (devOS.toLowerCase().contains("tablet")) {
					capabilities.setCapability("version", "7.0.0");
					capabilities.setCapability("deviceName", "tablet");
				}
			}
				
			if (MOB_OS.equalsIgnoreCase("android")) {
				and_driver = new AndroidDriver(new URL(url), capabilities);
				appDriver = and_driver;
			} else {
				ios_driver = new IOSDriver(new URL(url), capabilities);
				appDriver = ios_driver;
			}
			
			File logFile=null;
			if (appDriver!=null) {
				logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".pass");
	   		} else {
				logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".fail");
	   		}
	  		logFile.createNewFile();
			
	  	}catch(Exception e) {
	  		logger.info("[["+methodName + "]] : Error in Appium Capabilities : " + e.getMessage());
	  		if (!grid) {
	  			repUtil.reportLog("Error in creating Appium Driver for the device : '" + device + "' on port : '" + port + "'");
	  		} else {
	  			repUtil.reportLog("Error in creating Appium Capabilities for the test case : " + methodName);
	  		}
	  		File logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".fail");
   			logFile.createNewFile();
	  	}
		
		return appDriver;
   }
   
   

   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to install the desktop browser servers required for execution using selenium 
	 * @param 
	 */
   
   	private void getBrowserDrivers() {
		try {
			File oFile = new File(BROWSER_DRIVER_PATH);
			for (File f: oFile.listFiles())
			{
				String fName = f.getName();
				if (fName.endsWith(".txt"))
				{	
					File newFile = new File(BROWSER_DRIVER_PATH + "/"+fName.replace(".txt", ".exe"));
					f.renameTo(newFile);
				}
			}
		} catch (Exception e) {}
   }

   
   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to create and setup proxy information for desktop browser
	 * @param 
	 */
   
   private Proxy proxySetup(String browser) {
	   
		Proxy proxy=null;
		byte[] decodedBytes=null;
		
		try {
			
			//geting the property information
			String[] byPass=prop_ProxyIgnore.split(";");
						
			//decoding the proxy username
			if (prop_ProxyUser!=null && !prop_ProxyUser.equals("")) {
			   	decodedBytes = Base64.decodeBase64(prop_ProxyUser.getBytes());
			   	prop_ProxyUser = new String(decodedBytes);
			}
		   	
		   	//decoding the proxy password
			if (prop_ProxyPass!=null && !prop_ProxyPass.equals("")) {
			   	decodedBytes = Base64.decodeBase64(prop_ProxyPass.getBytes());
			   	prop_ProxyPass = new String(decodedBytes);
			}
		   	
		   	//setting up proxy
			proxy = new Proxy();
			
			if (browser.toLowerCase().contains("firefox")) {
				if (prop_ProxyType.equalsIgnoreCase("noproxy")) {
					proxy.setProxyType(ProxyType.DIRECT);
				} else if (prop_ProxyType.equalsIgnoreCase("autodetect")) {
					proxy.setProxyType(ProxyType.AUTODETECT);
			   		proxy.setAutodetect(true);
				} else if (prop_ProxyType.equalsIgnoreCase("autoconfig")) {
					proxy.setProxyType(ProxyType.PAC);
		   			proxy.setProxyAutoconfigUrl(prop_ProxyAutoConfigUrl);
				} else if (prop_ProxyType.equalsIgnoreCase("system")) {
					proxy.setProxyType(ProxyType.SYSTEM);
				} else if (prop_ProxyType.equalsIgnoreCase("manual")) {
					String proxyURL = prop_ProxyHost + ":" + prop_ProxyPort;
			   		proxy.setProxyType(ProxyType.MANUAL);
				   	proxy.setHttpProxy(proxyURL).setFtpProxy(proxyURL).setSslProxy(proxyURL).setSocksProxy(proxyURL);
				   	proxy.setSocksUsername(prop_ProxyUser).setSocksPassword(prop_ProxyPass);
				   	for (int p=0;p<byPass.length;p++) {
				   		proxy.setNoProxy(byPass[p]);
				   	}
				   	if (!prop_ProxyIgnore.contains("localhost"))
				   		proxy.setNoProxy("localhost");
				}
			} else if (browser.toLowerCase().contains("ie") || browser.toLowerCase().contains("chrome")) {
				if (prop_ProxyType.equalsIgnoreCase("autodetect")) {
					proxy.setProxyType(ProxyType.AUTODETECT);
			   		proxy.setAutodetect(true);
				} else if (prop_ProxyType.equalsIgnoreCase("autoconfig")) {
					proxy.setProxyType(ProxyType.PAC);
		   			proxy.setProxyAutoconfigUrl(prop_ProxyAutoConfigUrl);
				} else if (prop_ProxyType.equalsIgnoreCase("manual")) {
					String proxyURL = prop_ProxyHost + ":" + prop_ProxyPort;
			   		proxy.setProxyType(ProxyType.MANUAL);
				   	proxy.setHttpProxy(proxyURL).setFtpProxy(proxyURL).setSslProxy(proxyURL).setSocksProxy(proxyURL);
				   	proxy.setSocksUsername(prop_ProxyUser).setSocksPassword(prop_ProxyPass);
				   	for (int p=0;p<byPass.length;p++) {
				   		proxy.setNoProxy(byPass[p]);
				   	}
				   	if (!prop_ProxyIgnore.contains("localhost"))
				   		proxy.setNoProxy("localhost");
				} else if (prop_ProxyType.equalsIgnoreCase("noproxy")) {
					proxy.setProxyType(ProxyType.DIRECT);
				}
			}
			
		} catch (Exception e) {
			//System.out.println("Error in setting up proxy!");
			logger.info("Error in setting up proxy!");
		}
		return proxy;
   }
   
   
   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to setup the webdriver for desktop browsers 
	 * @param 
	 */

   public RemoteWebDriver browserSetup(String methodName, String browser) throws IOException {
	   
	   RemoteWebDriver rDriver=null;
	   DesiredCapabilities dCap=null;
	   String hubURL = null;
	   String prop_ProxyType=null;
	   String user=null;
	   String hostname=null;
	   String tName=null;
	   String portfolio=null;
	   boolean gridExec=false;
	   boolean proxy_req=false;
	   Proxy proxy=null;
	   int value=0;
	   try {
			//check if the execution is on grid
		   	gridExec=Boolean.valueOf(prop_GridExec);
		   	proxy_req=Boolean.valueOf(prop_ProxyReq);
		   	repUtil = new ReportUtil();
		   	
		   	String download = DOWNLOAD_PATH+"/"+methodName;
		   	File file = new File(download);
		   	if (!file.exists())
		   		file.mkdirs();
		   	
		   	//check if the execution is at grid
		   	if (gridExec)
		   		hubURL = prop_GridHub;
		   	
		   	//getting the browser driver files for chrome and ie
		   	if(!gridExec)
		   		getBrowserDrivers();
		   	
		   	//getting the proxy settings
		   	try {
			   	if (proxy_req) {
			   		if (prop_ProxyType.equalsIgnoreCase("manual"))
			   			proxy = proxySetup(browser);
			   		else
			   			proxy_req=false;
			   	}			   		
			   	user=System.getProperty("user.name");
			   	hostname=java.net.InetAddress.getLocalHost().getHostName();
			   	tName=prop_ProjPortfolio+"-"+user+"-"+hostname+"-"+methodName;
		   	} catch (Exception e) {
		   		proxy_req=false;
		   	}
		   	
		   	if (browser.toLowerCase().contains("firefox")) {
		   		
		   		dCap = DesiredCapabilities.firefox();
		   		FirefoxProfile firefoxProfile = new FirefoxProfile();
		   		dCap.setPlatform(Platform.ANY);
		   		
		   		if (proxy_req)
		   			dCap.setCapability(CapabilityType.PROXY, proxy);
		   		
		   		if (!gridExec) {
		   			FirefoxBinary ffBinary=null;
		   			String path = System.getenv("APPDATA") + "/Local/Mozilla Firefox/firefox.exe";
		   			File pathToBinary = new File(path);
		   			if (pathToBinary.exists()) {
		   				ffBinary = new FirefoxBinary(pathToBinary);
		   				rDriver = new FirefoxDriver(ffBinary,firefoxProfile,dCap);
		   			} else {
		   				rDriver = new FirefoxDriver(dCap);
		   			}
		   		} else {
		   			
		   			dCap.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
		   		}
		   		
		   	} else if (browser.toLowerCase().contains("ie")) {
		   		
		   		dCap = new DesiredCapabilities().internetExplorer();
		   		dCap.setPlatform(Platform.ANY);
		   		dCap.setCapability("requireWindowFocus", true);
	   			dCap.setCapability("enablePersistentHover", true);
		   		dCap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		   		
		   		if (proxy_req) {
			   		prop_ProxyType=proxy.getProxyType().toString();
			   		dCap.setCapability(CapabilityType.PROXY, proxy);
			   		if (prop_ProxyType.equalsIgnoreCase("autodetect")) {
			   			dCap.setCapability("ie.usePerProcessProxy", true);
			   		} else if (prop_ProxyType.equalsIgnoreCase("autoconfigure")) {
			   			dCap.setCapability("ie.usePerProcessProxy", true);
			   		} else if (prop_ProxyType.equalsIgnoreCase("manual")) {
			   			dCap.setCapability("ie.usePerProcessProxy", false);
			   			dCap.setCapability("ie.setProxyByServer", true);
			   		} else if (prop_ProxyType.equalsIgnoreCase("noproxy")) {
			   			dCap.setCapability("ie.usePerProcessProxy", false);
			   			dCap.setCapability("ie.setProxyByServer", false);
			   		}
		   		}
		   		
	   			if (!gridExec) {
	   				System.setProperty("webdriver.ie.driver", BROWSER_DRIVER_PATH + "/IEDriverServer.exe" );
	   				rDriver = new InternetExplorerDriver(dCap);
	   			} 
	   				
	   		}  
	   		
		   	// Added by Chala 08/17/2016
	   		// To launch the browser with user agent as tablet
	   		else if (browser.toLowerCase().contains("chrome-tablet")) {
	   				   		
		   		Map<String, String> mobileEmulation = new HashMap<String, String>();
				mobileEmulation.put("deviceName", "Apple iPad");
				Map<String, Object> chromeOptions = new HashMap<String, Object>();
				chromeOptions.put("mobileEmulation", mobileEmulation);
				
				dCap = DesiredCapabilities.chrome();
				dCap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		   		
		   		if (proxy_req) {
		   			dCap.setCapability("proxy", proxy);
		   		}
		   		
		   		if (!gridExec) {
		   			System.setProperty("webdriver.chrome.driver", BROWSER_DRIVER_PATH + "/chromedriver.exe" );
	   				rDriver = new ChromeDriver(dCap);
		   		}	   				
		    }
		    
		   	
		 // Added by Chala 08/23/2016
	   	 // To launch the browser with user agent as tablet
	   		else if (browser.toLowerCase().contains("chrome-mobile")) {
	   			
	   			Map<String, String> mobileEmulation = new HashMap<String, String>();
	   			mobileEmulation.put("deviceName", "Apple iPhone 6");
				Map<String, Object> chromeOptions = new HashMap<String, Object>();
				chromeOptions.put("mobileEmulation", mobileEmulation);
				
				dCap = DesiredCapabilities.chrome();
				dCap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				
		   		if (proxy_req) {
		   			dCap.setCapability("proxy", proxy);
		   		}
		   		
		   		if (!gridExec) {
		   			System.setProperty("webdriver.chrome.driver", BROWSER_DRIVER_PATH + "/chromedriver.exe" );
	   				rDriver = new ChromeDriver(dCap);
		   		} 
		    }
		   	
		   	
	   		else if (browser.toLowerCase().contains("chrome")) {
	   			dCap = new DesiredCapabilities().chrome();
		   		dCap.setPlatform(Platform.ANY);
		   	
	    		 
	    		//dCap.setCapability("DevopsTrackInfo", tName);
		   		if (proxy_req) {
		   			dCap.setCapability("proxy", proxy);
		   		}
		   		
		   		if (!gridExec) {
		   			System.setProperty("webdriver.chrome.driver", BROWSER_DRIVER_PATH + "/chromedriver.exe" );
	   				rDriver = new ChromeDriver(dCap);
		   		} 
	   				
		    }
		   	
		    else if (browser.toLowerCase().contains("safari")) {
		    	dCap = new DesiredCapabilities().safari();
		    	System.setProperty("webdriver.safari.noinstall", "true");
		    	SafariOptions options = new SafariOptions();
		    	options.setUseCleanSession(true);
		    	dCap.setPlatform(Platform.MAC);
		    	dCap.setCapability(SafariOptions.CAPABILITY, options);
		    	if (!gridExec)
	   				rDriver = new SafariDriver(dCap);
	   			else {
	   				dCap.setBrowserName("safari");
			    }
	   		}
		   	
		   	if (gridExec) {
		   		try {
		   			rDriver = new RemoteWebDriver(new URL(hubURL), dCap);
		   		} catch (Exception e) {
		   			logger.info("[[" + methodName + "]] : Capabilities failed initially; retrying the capabilities again!");
		   			rDriver = new RemoteWebDriver(new URL(hubURL), dCap);
		   		}
		   		if (rDriver!=null) {
		   			File logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".pass");
		   			logFile.createNewFile();
		   			String grid_host=getGridHostInfo(rDriver);
		   			if (grid_host!=null) {
		   				repUtil.reportLog("[["+methodName+"]] : Test is started running on NODE : " + grid_host);
		   			} else {
		   				repUtil.reportLog("[["+methodName+"]] : Test is started running on NODE : getGridHostInfo() didnt return value");
		   			}
		    	} else {
		    		repUtil.reportLog("Error in executing Test Case : " + methodName);
		    		File logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".fail");
		   			logFile.createNewFile();
		    	}
		   	}
		} catch (Exception e) {
			repUtil.reportLog("Error in creating WebDriver for the Browser : '" + browser);
			logger.info("Error : " + e.getMessage());
			logger.info("Error - Stacktrace info : " +e.getStackTrace());
			File logFile = new File (PM_BUILD_WORKSPACE+"/logs/ExecutedTests/"+methodName+".fail");
   			logFile.createNewFile();
		}
	   	return rDriver;
   }
   
	
   /**
	 * @date 09/08/2015
	 * @author Satish Kannian
	 * @description Method used to get the devices attached to the avd
	 * @param 
	 */
	    
	private List<String> getAPMDevices(String devOS) throws IOException, InterruptedException {
	   List<String> appdevices = new LinkedList<>();
	   Map deviceOS = new HashMap();
	   String devName=null;
	   String devModel=null;
	   String strdevicename[]=null;
	   String line = "null";
	   int endPort=0;
	   int startPort=0;
	   String appiumServer=null;
	   String androidHome= System.getenv("ANDROID_HOME");
	   String cmd=null;
	   Process pr=null;
	   
	   try {
		   
		   Runtime run = Runtime.getRuntime();
		   String deviceServer=putil.getMobileEnvironmentProperty("device_server");
		   if (!deviceServer.equals("") & deviceServer!=null) {
			   endPort = Integer.valueOf(putil.getMobileEnvironmentProperty("device_endport"));
			   startPort = Integer.valueOf(putil.getMobileEnvironmentProperty("device_startport"));
		   }
		   
		   if (devOS.toLowerCase().contains("android"))
			   appiumServer= putil.getMobileEnvironmentProperty("appium_server_android");
		   else if (devOS.toLowerCase().contains("ios"))
			   appiumServer= putil.getMobileEnvironmentProperty("appium_server_ios");
		   hostipaddress=Inet4Address.getLocalHost().getHostAddress();
	
		   //kill the already running adb server
		   killADBServer();
		   
		   //Get the devices connected
	       if (devOS.toLowerCase().contains("emulator")) {
	    	   cmd = "\"" + androidHome+"/tools/android.bat\"" + " list avd";
		       pr = run.exec(cmd);
		       pr.waitFor();
	
		       BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		       while ((line=buf.readLine())!=null) {
		          if ( line.toLowerCase().contains("name:")) {
		                 strdevicename = line.toLowerCase().split("name:");
		                 devName=strdevicename[1].trim();
		                 appdevices.add(devName);
		                 //System.out.println("Device Name : " + devName);
		                 logger.info("Device Name : " + devName);
		          }
		          if ( line.toLowerCase().contains("device:")) {
		                 strdevicename = line.toLowerCase().split("device:");
		                 devModel=strdevicename[1].trim();
		                 devModel=devModel.split("(")[0];
		                 devModel=devModel.replace(" ", "_");
		                 //System.out.println("Device Model : " + devModel);
		                 logger.info("Device Name : " + devModel);
		          }
		          apmDeviceModel.put(devName, devModel);
		       }
	       } else if (devOS.toLowerCase().contains("all_android")) {
			   if (deviceServer!=null && deviceServer!="") {
		    	   for( int dPort = startPort;dPort <= endPort;dPort++) {
		    		   cmd = "\"" + androidHome+"/platform-tools/adb\"" + " connect "+ deviceServer+":"+dPort;
		        	   pr = run.exec(cmd);
		    	       pr.waitFor();
		    	   }
		    	   Thread.sleep(1000);
	    	   }
	    	   
		       for (int i=1;i<=2;i++) {
			       if (i==1)
			    	   cmd = "\"" + androidHome+"/tools/android.bat\"" + " list avd";
			       else
			    	   cmd = "\"" + androidHome+"/platform-tools/adb\"" + " devices -l";
			       pr = run.exec(cmd);
			       pr.waitFor();
		
			       BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			       if (i==1) {
			    	   while ((line=buf.readLine())!=null) {
				          if ( line.toLowerCase().contains("name:")) {
				                 strdevicename = line.toLowerCase().split("name:");
				                 devName=strdevicename[1].trim();
				                 appdevices.add(devName);
				                 //System.out.println("Device Name : " + devName);
				                 logger.info("Device Name : " + devName);
				          }
				          if ( line.toLowerCase().contains("device:")) {
				                 strdevicename = line.toLowerCase().split("device:");
				                 devModel=strdevicename[1].trim();
				                 devModel=devModel.split("(")[0];
				                 devModel=devModel.replace(" ", "_");
				                 //System.out.println("Device Model : " + devModel);
				                 logger.info("Device Name : " + devModel);
				          }
				          apmDeviceModel.put(devName, devModel);
				       }
			       } else {
			    	   while ((line=buf.readLine())!=null) {
				          if ( line.toLowerCase().contains("device product:")) {
				        	  if (!line.toLowerCase().contains("offline")) {
				        	  	 if (!line.toLowerCase().contains("sdk")) {
				        	  		 strdevicename = line.toLowerCase().split("device product:");
				        	  		 devName = strdevicename[0].trim();
				        	  		 appdevices.add(devName);
				        	  		 strdevicename = line.toLowerCase().split("model:");
				        	  		 strdevicename = strdevicename[1].split("device");
				        	  		 devModel = strdevicename[0].trim();
				        	  		 devModel=devModel.split("__")[0].trim();
				        	  		 apmDeviceModel.put(devName, devModel);
				        	  	 }
				        	  		 
				                 //System.out.println("Device Name :" + devName);
				                 //System.out.println("Device Model :" + devModel);
				              }
				          }
				      }
			      }
		      }
	      } else if (devOS.toLowerCase().contains("android_device")) {
	    	  BufferedReader buf=null;
	    	  if (deviceServer!=null && !deviceServer.equals("")) {
	    		  String[] devServer=deviceServer.split("##");
	    	   
	    		  for(int srvCnt = 0; srvCnt < devServer.length; srvCnt++) {
		    		   if (!hostipaddress.contains(appiumServer)) {
		    			   //connectremoteserver();
		    		   } else {
		    			   for(int deviceport = startPort;deviceport <= endPort;deviceport++) {
		    	    		   cmd = "\"" + androidHome+"/platform-tools/adb\"" + " connect "+ devServer[srvCnt]+":"+deviceport;
		    	    		   pr = run.exec(cmd);
		    	    		   //System.out.println("adb device port:"+deviceport);
		    	    		   pr.waitFor();
		    	    		   Thread.sleep(5000);
		    	    	   }
		    		   }
		    	   }
	    	   }
	    	   
	    	   if (hostipaddress.contains(appiumServer) || appiumServer.equals("")) {
	    		   APM_HOST=putil.getMobileEnvironmentProperty("appium_default_host");
	    		   cmd = "\"" + androidHome+"/platform-tools/adb\"" + " devices -l";
	        	   pr = run.exec(cmd);
	    	       pr.waitFor();
	    	       buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	    	       while ((line=buf.readLine())!=null) {
	    	          if (line.toLowerCase().contains("device product:")) {
	    	        	 if (line.toLowerCase().contains("offline")) {
	    	        		 //System.out.println("deivce offline");
	    	        	 } else {
	    	                 strdevicename = line.toLowerCase().split("device product:");
	    					 if (!strdevicename[0].toLowerCase().contains("emulator")) {	
	    						 devName = strdevicename[0].trim();
			        	  		 appdevices.add(devName);
			        	  		 strdevicename = line.toLowerCase().split("model:");
			        	  		 strdevicename = strdevicename[1].split("device");
			        	  		 devModel = strdevicename[0].trim();
			        	  		 devModel=devModel.split("__")[0].trim();
			        	  		 apmDeviceModel.put(devName, devModel);
			        	  		 
			        	  		 //System.out.println("Device Name :" + devName);
				                 //System.out.println("Device Model :" + devModel);
	    					 }
	    	        	  }
	    	          }
	    	       }
	    	   }
	    	   
	       } else {
	    	   	String dev_ids[]=devOS.split(";");
				Integer devCount=dev_ids.length;
				for(int count=0;count<devCount;count++) {
					appdevices.add(dev_ids[count]);
				}
	       }
	   }catch(Exception e) {
		   logger.info("Could not get the device list! - Error : " + e.getMessage());
	   }
	
	   return appdevices;
	}


	private String getDeviceOS(String device) {
		String OS=null;
		String line=null;
		String cmd=null;
		Process pr=null;
		Runtime run = Runtime.getRuntime();
		String androidHome= System.getenv("ANDROID_HOME");
		boolean flag=false;
		try {
			cmd = "\"" + androidHome+"/tools/android.bat\"" + " list avd";
			pr = run.exec(cmd);
			pr.waitFor();
	
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((line=buf.readLine())!=null) {
				if ( line.toLowerCase().contains(device.toLowerCase())) {
					flag=true;
					break;
				}
			}
	       
			if(!flag) {
				cmd = "\"" + androidHome+"/platform-tools/adb\"" + " devices -l";
				pr = run.exec(cmd);
				pr.waitFor();
				buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				while ((line=buf.readLine())!=null) {
					if ( line.toLowerCase().contains(device.toLowerCase())) {
						flag=true;
						break;
					}
				}
			}
			if (flag)
				OS="Android";
			else
				OS="iOS";
		}catch(Exception e){}
		return OS;
	}
	
	
	private boolean APM_Server(String devicename, String server_port) throws ExecuteException, IOException,InterruptedException {
		 ServerArguments server = new ServerArguments();
		 String APM_Home = System.getenv("APPIUM_PATH");
		 try {
			 server.setArgument("--address", APM_HOST);
			 server.setArgument("--port", server_port);
			 server.setArgument("--local-timezone", true);
			 server.setArgument("--platform-name", "Android");
			 server.setArgument("--bootstrap-port", String.valueOf(Integer.valueOf(server_port)+10));
			 server.setArgument("--session-override", false);
			 //server.setArgument("--log", APPIUM_LOG+"_"+devicename+"_"+test+".log");
			 if (!mdotcom) {
				 server.setArgument("--full-reset", true);
				 server.setArgument("--app-pkg", APP_PACKAGE);
				 server.setArgument("--app-activity", APP_ACTIVITY);
			 }
			 server.setArgument("--device-ready-timeout", "15");
			 server.setArgument("--device-name", devicename);
			 apmServer = new AppiumServer(new File(APM_Home),server);
			 apmServer.stopServer();
			 Thread.sleep(2000);
			 long start_time=System.currentTimeMillis();
			 apmServer.startServer(300000);
			 while(!apmServer.isServerRunning()){}
			 long end_time=System.currentTimeMillis();
			 repUtil.reportLog("Appium Server Started for device - " + devicename + "in port - " + server_port);
			 repUtil.reportLog("Appium Server took - '" + (end_time-start_time)/1000 + "' seconds to start for device - " + devicename + " in port - " + server_port);
			 return true;
		 } catch(Exception e) {
			 repUtil.reportLog("Fail to start Appium Server for device - " + devicename + "in port - " + server_port);
			 //System.out.println("Error Message : " + e.getMessage());
			 logger.info("Error Message : " + e.getMessage());
			 return false;
		 }
	 }


	/**
	 * @date 09/08/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to kill the adb server
	 * @param 
	 */
		
	private void killADBServer()	{
		String cmd=null;
		Process pr=null;
		String line=null;
		Runtime run = Runtime.getRuntime();
		String androidHome= System.getenv("ANDROID_HOME");
		try {
			cmd = "\"" + androidHome+"/platform-tools/adb\"" + " kill-server";
			pr = run.exec(cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((line=buf.readLine())!=null)
			{}
		} catch (Exception e) {}
	}
	
		
	/**
	 * @date 10/05/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to take a copy of the existing file in different name
	 * @param Name and path of the file to be copied
	 */
   
	private String createTempInstaller(String source) {
	   String destn=null;
	   try {
		   Date dNow = new Date( );
		   SimpleDateFormat ft = new SimpleDateFormat("yy-MM-dd'_'hh-mm-ss-SSS");
		   String date=ft.format(dNow);
		   if (source.indexOf("\\")>0)
			   destn=source.substring(0,source.lastIndexOf("\\")+1);
		   else 
			   destn=source.substring(0,source.lastIndexOf("/")+1);
		   String fileFormat = source.substring(source.lastIndexOf("."));
		   File src=new File(source);
		   destn=destn+"temp"+"_"+date+fileFormat;
		   File newdest=new File(destn);
		   FileUtils.copyFile(src, newdest);
		   while(!newdest.exists()){}
		   //System.out.println("Temporary installer file - " + destn + " created!");
		   logger.info("Temporary installer file - " + destn + " created!");
	   } catch(Exception e) {}
	   return destn;
	}

   
	/**
	 * @date 10/05/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to delete the temporary files created during execution
	 */
	
	public void deleteTempFiles() throws IOException 
	{
		String strAppPath=null;
		try
		{
	        if (MOB_OS.toLowerCase().contains("ios"))
				strAppPath = APP_PATH + "/iOS";
	        else if (MOB_OS.toLowerCase().contains("android"))
	        	strAppPath = APP_PATH + "/Android";
			else
				strAppPath = APP_PATH + "/Common";
	        
	        //creates a folder to download the file if not exists and if exists, deletes the existing app kits
			File AppDirectory = new File(strAppPath);
			File[] files = AppDirectory.listFiles();
			if (files.length>0)
			{
				for (File f:files)
				{
					if (f.isFile()) 
					{
						if (f.getName().toLowerCase().contains("temp"))
						{
							f.delete();
						}
					}
				}
			}
			repUtil.reportLog("All the temporary installer files are deleted!");
		} catch(Exception e) {
			//System.out.println("Error in deleting temporary installer file - Error : " + e.getMessage());
		}
		
	}
		
	
   /**
	 * @date 09/04/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to stop the already running Appium server
	 * @param Appium PORT as String
	 */
	
	 public void stopAppiumServer(String server_port)
	 {
		String sysVal = null;
	    try {
	    	
	    	String command = "cmd /c echo off & FOR /F \"usebackq tokens=5\" %a in" + " (`netstat -nao ^| findstr /R /C:\"" 
					+ server_port + "\"`) do (FOR /F \"usebackq\" %b in"
	                + " (`TASKLIST /FI \"PID eq %a\" ^| findstr /I node.exe`) do taskkill /F /PID %a)";
	    	Process process = Runtime.getRuntime().exec(command);
	        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        
	        // read the output from the command
	        //System.out.println("Standard output of the command:\n");
	        while ((sysVal = stdInput.readLine()) != null) 
	        {
	            //System.out.println(sysVal);
	            if (sysVal.toLowerCase().contains("stopped"))
	            {
	            	repUtil.reportLog("The Appium Server running on port : '" + server_port + "' is stopped");
	            	break;
	            }
	        }
	    } catch (IOException e) {
	        //System.out.println("exception happened: ");
	    } 
	 }


	/**
	 * @date 09/30/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to stop the already running Appium Node
	 * @param 
	 */
	
	 public void stopAppiumNode() {
		 String line;
		 String pidInfo ="";
		 try
		 {
			 Process p =Runtime.getRuntime().exec(System.getenv("windir") +"/system32/"+"tasklist.exe");
			 BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			 while ((line = input.readLine()) != null)
			 {
				 line = line.split("  ")[0];
			     if (line.toLowerCase().contains("node.exe"))
			       	 Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
			     else if (line.toLowerCase().contains("adb.exe"))
			    	 Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
			     else if (line.toLowerCase().contains("emulator"))
			    	 Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
			 }
			 repUtil.reportLog("All appium related processes are closed");
		 }catch(Exception e)
		 {
			 //System.out.println("Error in killing node.exe");
		 }
	 }


	/**
	 * @date 09/04/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to start the Appium server dynamically on a specific port
	 * @param Appium PORT as String
	 */
	 private void startAppiumServer(String server_port, String emu_port, String device)
	 {
		 String APM_NODE=null;
		 String APM_JS=null;
		 String APM_HOME=null;
		  try{
			 //System.out.println("Stopping Node.exe on port : " + server_port);
			 stopAppiumServer(server_port);
			 //System.out.println("Stopped Node.exe on port : " + server_port);
			 APM_HOME=System.getenv("APPIUM_PATH");
			 APM_NODE= APM_HOME+"/node.exe";
			 APM_JS= APM_HOME+"/node_modules/appium/bin/appium.js";
			 CommandLine command = new CommandLine("cmd");
			 command.addArgument("/c");
			 command.addArgument(APM_NODE);
			 command.addArgument(APM_JS);
			 command.addArgument("--address",false);
			 command.addArgument(APM_HOST);
			 command.addArgument("--port",false);
			 command.addArgument(server_port);
			 command.addArgument("--bootstrap-port",false);
			 command.addArgument(String.valueOf(Integer.valueOf(emu_port)));
			 /*command.addArgument("--udid");
			 command.addArgument(device);*/
			 command.addArgument("--session-override", true);
			 command.addArgument("--log");
			 command.addArgument(APPIUM_LOG);
			 command.addArgument("--local-timezone",true);
			 //System.out.println("Starting Appium Node.exe on port : " + server_port);
			 DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			 DefaultExecutor executor = new DefaultExecutor();
			 executor.setExitValue(1);
			 executor.execute(command, resultHandler);
			 if(!appiumListenerStatus(server_port))
				 Thread.sleep(30000);
			 //System.out.println("Appium Node.exe started on port : " + server_port);
			 Thread.sleep(20000);
		 } catch(Exception e)
		 {
		   //System.out.println("Error in APM starting");
		 }
	 }
	 
	
	 
	 /**
	 * @date 09/04/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to validate if the port has entered the listening state
	 * @param Appium PORT as String
	 */
	 
	 private boolean appiumListenerStatus(String port)
	 {	
		 boolean bStatus=false;
		 long new_time;
		 try{
			 
			//String Command = "adb devices";
	        String Command = "\"" + "netstat\"" + " -an";
	        String devstring=null;
	        long start_time = System.currentTimeMillis();
	        while(!bStatus)
	        {
	        	Process process = Runtime.getRuntime().exec(Command);
		        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		        
		        while ((devstring=stdInput.readLine())!=null) 
		        {
		        	if (devstring.toLowerCase().contains(APM_HOST+":"+port) && devstring.toLowerCase().contains("listening"))
			        {
						//System.out.println("Appium Listener started on port : " + port);
						bStatus = true;
						break;
			        }
		        }
		        new_time = System.currentTimeMillis();
		        if (((new_time-start_time)/1000)>=150)
		        {
		        	//System.out.println("Appium port is still getting ready to listen...");
		        	break;
		        }
	        }
			 
		 }catch(Exception e){}
		 return bStatus;
	 }
	 
	 
	 /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to read the files from the specified path
	 * @param 
	 */
	
	private byte[] readFile(File path) throws FileNotFoundException, IOException
	{
		int length = (int)path.length();
		byte[] content = new byte[length];
		InputStream inStream = new FileInputStream(path);
		try {
			inStream.read(content);
		}
		finally {
			inStream.close();
		}
		return content;
	}
   
	
	private JsonObject extractObject(HttpResponse resp) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		StringBuffer s = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			s.append(line);
		}
		rd.close();
		JsonParser parser = new JsonParser();
		JsonObject objToReturn = (JsonObject)parser.parse(s.toString());
		//System.out.println(objToReturn.toString());
		//System.out.println(objToReturn.get("proxyId"));
		return objToReturn;
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the host name of the execution node in Selenium GRID
	 * @param 
	 */
private String getHostName(SessionId session) throws UnknownHostException {
		
		String hostDetail=null;
		String hostName=null;
		String port=null;
		HttpClient client=null;
		HttpHost host=null;
		String errorMsg="Failed to acquire remote webdriver node and port info. Root cause: ";
 		try {
			String grid_hub=prop_GridHub;
			hostName=grid_hub.substring(0, grid_hub.lastIndexOf(":"));
			hostName=hostName.replace("http://", "");
			port=grid_hub.substring(grid_hub.lastIndexOf(":")+1);
			port=port.substring(0,port.indexOf("/"));
			host=new HttpHost(hostName, Integer.valueOf(port));
			grid_hub=grid_hub.substring(0,grid_hub.indexOf("wd/hub"));
			String hostURL=host+"/grid/api/testsession?session=" + session;
			client=HttpClientBuilder.create().build();
			URL sessionURL = new URL(hostURL);
			BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", sessionURL.toExternalForm());
			HttpResponse response = client.execute(host, r);
			JsonObject myjsonobject =extractObject(response);
			JsonElement url = myjsonobject.get("proxyId");
			//System.out.println(url.getAsString());
			URL myURL = new URL(url.getAsString());
			if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
				hostDetail = myURL.getHost();
			}
			logger.info("GRID HOST details : " + hostDetail);
 		} catch (Exception e) {
 			logger.info("Error in getting GRID HOST details : " + e.getMessage());
			throw new RuntimeException(errorMsg, e);
		}
		return hostDetail;
	}
		

	/**
	 * @date 08/26/2014
	 * @author Chandrasekar Murugesan
	 * @description Method to display TestCaseExecution Time
	 * @return
	 */	
	
	public String displayExecutionTime(){
		String end_timestamp = null;
		try{
			end_timestamp = dateutil.getSystemStringDate("MM-dd-yyyy hh:mm:ss");
			end_milliseconds = System.currentTimeMillis();
			logger.info("****************************************************************************************\n");
			logger.info("*  Total time for execution : " + (end_milliseconds - start_milliseconds) /1000 + " seconds\n");
			logger.info("*  Total Executed : " + (passedTests.size() + failedTests.size()) + "\n");
			logger.info("*  Total Passed : " + (passedTests.size()) + "\n");
			logger.info("*  Total Failed : " + (failedTests.size()) + "\n");
			logger.info("*  Pass Percentage : " + pass_percent + "\n");
			logger.info("*  Fail Percentage : " + fail_percent + "\n");
			logger.info("****************************************************************************************");
			
		}catch(Exception e){
			logger.error("FrameworkUtil :: displayExecutionTime() Exception:"+e);
		}
		
		return end_timestamp;
	}
	
	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to Clean the files in the test-output Results directory
	 * @param 
	 */
	
	private void cleanTestReportFolder()
	{
		File directory = new File(TESTNG_OUTPUT);
		try{
			if (directory.exists()) {
				for (File f: directory.listFiles()) {
					if (f.isDirectory()) 
						f.delete();
					else if (!f.getName().toLowerCase().startsWith("dpe"))
						f.delete();
				}
			}
			logger.info("cleanTestReportFolder(), cleaned successfully");
		} catch(Exception e) {
			logger.error("cleanTestReportFolder(), Exception :" + e );
		}
	}
	
	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to Clean the files in the PerfectoMobile Results directory
	 * @param 
	 */
	
	private void CleanPMReportsDir()
	{
		File pmdirectory = new File(PM_REPORTS_PATH);
		try{
			if (!pmdirectory.exists()) {
				pmdirectory.mkdirs();
			} else {
				FileUtils.deleteDirectory(pmdirectory);
			}
			logger.info("CleanPMReportsDir(), cleaned "+PM_REPORTS_PATH+" successfully");
		} catch(Exception e)	{
			logger.error("CleanPMReportsDir(), Exception :" + e );
		}
	}
	
	

   			

	/**
	 * @date 08/26/2014
	 * @author Chandrasekar Murugesan
	 * @description Method to delete all the previous executed PDF reports
	 * @return
	 */	
	public void cleanPDFReports() throws IOException {
		try {
			File file=new File(PDF_REPORTS_PATH);
			if (file.exists()) {
				FileUtils.deleteDirectory(file);
			}
			
			File file1=new File(REPORTS_PATH + "/TempResults");
			if (file1.exists()) {
				FileUtils.deleteDirectory(file1);
			}
		} catch (Exception e){}
	}
	
	
	/**
	 * @date 08/26/2014
	 * @author Chandrasekar Murugesan
	 * @description Method to delete the testng output folder
	 * @return
	 */	
	private void cleantestNGReports() throws IOException
	{
		try {
			File file=new File(TESTNG_OUTPUT);
			if (file.exists()) {
				FileUtils.deleteDirectory(file);
			}
		} catch (Exception e){}
	}
	
			
	/**
	 * @date 08/26/2014
	 * @author Chandrasekar Murugesan
	 * @description Method to display TestCaseExecution Time
	 * @return
	 */	
	public void clearnPrevRunLogs() throws IOException
	{
		try
		{
			repUtil = new ReportUtil();
			repUtil.createreportfile();
			cleanSuiteFiles();
			cleanPDFReports();
			cleantestNGReports();
			if (isPerfectoMobile)
				CleanPMReportsDir();
		} catch (Exception e){}
	}
	
	
	private void cleanSuiteFiles() {
		try {
			File file = new File(TESTNG_SUITE_FILES);
			if (file.exists())
				FileUtils.deleteDirectory(file);
		} catch (Exception e) {
			logger.info("Error in clearing the previous suite files");
		}
	}
	
	public void clearBrowserCookies() {
		try {
			frameDriver.manage().deleteAllCookies();
		} catch (Exception e) {
			logger.info("Error in deleting cookies");
		}
	}
	
	
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method that gets the path of latest modified folder/files 
	 * @param 
	 */
	
	public String getTheNewestFolder(String filePath) {
	    File theNewestFile = null;
	    String retVal=null;
	    try {
		    File dir = new File(filePath);
		    File[] files = dir.listFiles(File::isDirectory);
		    if (files.length > 0) {
		        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		        for (int i=0; i<files.length; i++) {
		        	if (!files[i].getName().startsWith("Temp") && !files[i].getName().startsWith("PDF")) {
		        		theNewestFile = files[i];
		        		break;
		        	}
		        }
		        retVal=theNewestFile.getCanonicalPath();
		    }
	    } catch (Exception e) {
	    	
	    }
	    return retVal;
	}
	
		
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the data value for a field / column
	 * @param 
	 */	
	public String getDataValue(String key) {
		String value=null;
		try {
			mTestName=dutil.getTestName();
			value=dutil.getDataValue(mTestName, key);
		} catch (Exception e) {
			//System.out.println("Error in getting the data value");
		}
		return value;
	}
	

	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to decode the Environment Variable values
	 * @param 
	 */
	
	public void decodeEnvironmentValues() {
		try	{
			//Variables Initialization
			String p_user=null;
			String p_host=null;
			String p_pwd=null;
			String appuser=null;
			String apppass=null;
			byte[] decodedBytes=null;
			
			//Getting the values from property files
			p_host = putil.getMobileEnvironmentProperty("pm_host");
			p_user = putil.getMobileEnvironmentProperty("pm_user");
			p_pwd = putil.getMobileEnvironmentProperty("pm_password");
			appuser = putil.getApplicationProperty("app_user");
			apppass = putil.getApplicationProperty("app_pass");
			
			//Decoding the PerfectoMobile Cloud URL
			decodedBytes = Base64.decodeBase64(p_host.getBytes());
			PM_HOST = new String(decodedBytes);
						
			//Decoding the PerfectoMobile Cloud Username
			decodedBytes = Base64.decodeBase64(p_user.getBytes());
			PM_USER = new String(decodedBytes);
						
			//Decoding the PerfectoMobile Cloud Password
			decodedBytes = Base64.decodeBase64(p_pwd.getBytes());
			PM_PASSWORD = new String(decodedBytes);
						
			//Decoding the MyFiOS application Username
			decodedBytes = Base64.decodeBase64(appuser.getBytes());
			APP_USER = new String(decodedBytes);
						
			//Decoding the MyFiOS application Password
			decodedBytes = Base64.decodeBase64(apppass.getBytes());
			APP_PASSWORD = new String(decodedBytes);
		
		}catch (Exception e) {}
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to wait till the page is loaded
	 * @param 
	 */
	
	private void waitForLoad() {
		waitForDriver=new WebDriverWait(frameDriver,120);
	    ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver driver) {
	                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	            }
	        };
	    waitForDriver.until(pageLoadCondition);
	}
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description clear all the browser instances by killing the process
	 * @param 
	 */
	private void clearBrowserInstances() {
		String line;
		String pidInfo ="";
		try
		{
			Process p =Runtime.getRuntime().exec(System.getenv("windir") +"/system32/"+"tasklist.exe");
			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null)
			{
				line = line.split("  ")[0];
				if (line.toLowerCase().contains("ie.exe"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
				else if (line.toLowerCase().contains("chrome.exe"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
				else if (line.toLowerCase().contains("firefox.exe"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
				else if (line.toLowerCase().contains("google"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
				else if (line.toLowerCase().contains("ie"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
			}
			repUtil.reportLog("All the browser processes are closed");
		} catch(Exception e) {
			//System.out.println("Error in killing browser processes");
		}
	}
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to kill the server instance of IE and Chrome on Windows platform
	 * @param 
	 */
	public void clearServerInstances() {
		String line;
		String pidInfo ="";
		try
		{
			Process p =Runtime.getRuntime().exec(System.getenv("windir") +"/system32/"+"tasklist.exe");
			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null)
			{
				line = line.split("  ")[0];
				if (line.toLowerCase().contains("iedriverserver.exe"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
				else if (line.toLowerCase().contains("chromedriver.exe"))
					Runtime.getRuntime().exec("taskkill /F /IM " + line.trim());
			}
			repUtil.reportLog("All the browser server instances are closed");
		} catch(Exception e) {
			//System.out.println("Error in killing browser server instance");
		}
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to encode the secure value
	 * @param 
	 */
	public void encodeValues(String value) {
		byte[] encode=null;
		byte[] decode=null;
		try {
			encode = Base64.encodeBase64(value.getBytes());
			String enValue = new String(encode);
			decode =Base64.decodeBase64(enValue.getBytes());
			String deValue = new String(decode);
			//System.out.println("Original Value : " +deValue);
			//System.out.println("Encoded Value : " +enValue);
		} catch (Exception e) {
			//System.out.println("Error in Encoding the value");
		}
	}
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to decode the encoded values
	 * @param 
	 */
	public String decodeValues(String encodedValue) {
		byte[] decode=null;
		String deValue=null;
		try {
			decode =Base64.decodeBase64(encodedValue.getBytes());
			deValue = new String(decode);
		} catch (Exception e) {
			//System.out.println("Error in Decoding the value");
		}
		return deValue;
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the execution metrics
	 * @param 
	 */
	public void getExecMetrics() {
		PrintWriter writer=null;
		int passtest=0;
		int failedtest=0;
		passtest=passedTests.size();
		failedtest=failedTests.size();
		int tottests=passtest+failedtest;
		float pass_per=Math.round((passtest*100f)/tottests);
		float fail_per=Math.round((failedtest*100f)/tottests);
		int skippedtest=tottests-passtest-failedtest;
		float skip_per=Math.round((skippedtest*100f)/tottests);
		pass_percent=String.valueOf(pass_per)+"%";
		fail_percent=String.valueOf(fail_per)+"%";
		try {
			writer = new PrintWriter(EXEC_RESULTS, "UTF-8");
			writer.write("Total Tests Executed : " + tottests +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.write("Total Tests Passed : " + passtest +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.write("Total Tests Failed : " + failedtest +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.write("Total Tests Skipped : " + skippedtest +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.write("Pass Percentage : " + pass_percent +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.write("Fail Percentage : " + fail_percent +"\n");
			writer.write(System.getProperty("line.separator"));
			writer.close();
		} catch (Exception e) {
			writer.close();
		}
	}
	
	
	
	
	
	/**
	 * @author MURUGCH
	 * @method getGridHostInfo
	 * @param context
	 * @param metName
	 * @return hostIP
	 */
	
	public String getGridHostInfo(RemoteWebDriver frameDriver) {
		String hostIP=null;
		try {
		
			//getting session id
			SessionId sess=frameDriver.getSessionId();
			hostIP=getHostName(sess);
			logger.info("Selenium GRID node information Obtained : HOST IP = " + hostIP);
		} catch (Exception e) {
			logger.info("Error in getting the Selenium GRID node information");
			logger.info("Selenium GRID node information Error : " + e.getMessage());
		}
		return hostIP;
	}
	
	
	
	/**
	 * @author MURUGCH
	 * @method validate_PDFText
	 * @param URL or File Name,
	 * @param  Text to be Validated
	 * @return Boolean
	 * @throws IOException 
	 */
	
	public boolean validate_PDFText (String URLorFile, String text) throws IOException {

		PDDocument document = null;
		String output =null;
		boolean flag=false;
		try {
			if (URLorFile.contains("http")) {
				URL url = new URL(URLorFile);
				BufferedInputStream buffStream = new BufferedInputStream(url.openStream());
				document = PDDocument.load(buffStream);
			} else {
				File fileToParse=new File(URLorFile);
				document = PDDocument.load(fileToParse);
			}
	        
	        PDFTextStripper pdfStripper = new PDFTextStripper();
	        output = pdfStripper.getText(document);
	        //System.out.println(output);
	    } finally {
	        if( document != null ) {
	            document.close();
	        }
	    }

		if(output.contains(text)) {
			flag=true;
		}
		
		return flag;
	}
	
	
	/**
	 * @author MURUGCH
	 * @method get_PDFPageCount
	 * @param URL or File Name,
	 * @return Int
	 * @throws IOException 
	 */
	
	public int get_PDFPageCount (String URLorFile) throws IOException {

		PDDocument document = null;
		int pgCount = 0;
		try {
			if (URLorFile.contains("http")) {
				URL url = new URL(URLorFile);
				BufferedInputStream buffStream = new BufferedInputStream(url.openStream());
				document = PDDocument.load(buffStream);
			} else {
				File fileToParse=new File(URLorFile);
				document = PDDocument.load(fileToParse);
			}
	        
	        pgCount = document.getNumberOfPages();
	    } finally {
	        if( document != null ) {
	            document.close();
	        }
	    }

		return pgCount;
	}
	
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the list of the test cases selected for execution on each modules / application
	 * @param 
	 * @throws IOException 
	 */	
	
	private void getExecutableTestCases() throws IOException {
				
		Workbook workbook = null;
		Sheet worksheet=null;
		PrintWriter writer=null;
		List<String> tcSheet=new LinkedList<String>();
		Map<String, String> tempMap = new HashMap<String, String>();
		String envVals = dutil.getExecEnvironment();
		String envMods = dutil.getExecModules();
		String tempList=null;
		String region=null;
		
		try	{
			
			String exePlat=prop_ExecPlatform;
			String devPlat=prop_DevList;
			
			//create new text file with test methods details
			writer = new PrintWriter(EXEC_METHODS, "UTF-8");
			
			//creating a excel object
			String excelFilePath = TEST_DATA_FILEPATH + "/TestData.xls";
			FileInputStream fileInputStream = new FileInputStream(excelFilePath);
			
			if (excelFilePath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fileInputStream);
			} else {
				workbook = new HSSFWorkbook(fileInputStream);
			}
	       
			worksheet = workbook.getSheet("TestCaseDashboard");
		
			//getting the test case modules that are selected for execution
			int iRow = 1;
	    	int iRowCnt = worksheet.getLastRowNum();
	    	
	    	int iTest = dutil.getHeaderColumnIndex(worksheet, "TestCase_Name");
	    	int iPlat = dutil.getHeaderColumnIndex(worksheet, "TestPlatform");
	    	
	    	String[] mVals=null;
	    	
	    	for (int iR=iRow; iR<=iRowCnt; iR++) {
	    		
	    		String TCVal= dutil.getCellValue(workbook, worksheet, iR, iTest);
	    		String ePlat = dutil.getCellValue(workbook, worksheet, iR, iPlat);
	    		boolean execFlag=false;
	    		if (envMods!="" || !envMods.isEmpty()) {
	    			mVals=envMods.split(",");
	    		}
	    		
	    		if (mVals==null) {
	    			execFlag=true;
	    		} else {
		    		for (int iV=0; iV<mVals.length; iV++) {
		    			if (TCVal.equalsIgnoreCase(mVals[iV])) {
		    				execFlag=true;
		    				break;
		    			}
		    		}
	    		}
	    		
	    		if (execFlag) {
	    			if (testPlatform.equalsIgnoreCase("mobile")) {
	    				if (!ePlat.trim().equalsIgnoreCase("desktop") || ePlat.trim().equalsIgnoreCase("browser")) {
		    				tcSheet.add(TCVal.trim());
		    				tempMap.put(TCVal.trim(), ePlat.trim());
		    			}
	    				else {
	    					tcSheet.add(TCVal.trim());
		    				tempMap.put(TCVal.trim(), ePlat.trim());
		    			}
	    				
	    			} else if (testPlatform.equalsIgnoreCase("desktop")) {
	    				if (ePlat.trim().equalsIgnoreCase("desktop") || ePlat.trim().equalsIgnoreCase("browser")) {
	    					tcSheet.add(TCVal.trim());
	    					tempMap.put(TCVal.trim(), ePlat.trim());
		    			}
	    			} else {
	    				tcSheet.add(TCVal.trim());
	    				tempMap.put(TCVal.trim(), ePlat.trim());
	    			}
	    		}
	    	}
	    	workbook.close();
	    	
	    	for (String tVal : tcSheet) {
	    		workbook = new HSSFWorkbook(new FileInputStream(TEST_DATA_FILEPATH + "/" + tVal + ".xls"));
	    		worksheet = workbook.getSheetAt(0);
	    		
				int iEx = dutil.getHeaderColumnIndex(worksheet, "Run");
	    		int iSN = dutil.getHeaderColumnIndex(worksheet, "Sheet_Name");
	    		int iFl = dutil.getHeaderColumnIndex(worksheet, "Flow_Name");
	    		
	    		int itcCnt=worksheet.getLastRowNum();
	    		for (int i=1; i<=itcCnt; i++) {
	    			String SheetName=dutil.getCellValue(workbook, worksheet, i, iSN);
	    			boolean execute=Boolean.valueOf(dutil.getCellValue(workbook, worksheet, i, iEx));
	    			String flowName=dutil.getCellValue(workbook, worksheet, i, iFl);
	    			if (execute) {
	    				tcNames.add(flowName);
	    				tcModule.put(flowName, tempMap.get(tVal));
	    				module.put(flowName, tVal);
	    			}
	    		}
	    		
	    		//resizing the array size for testcase modules
		    	tcCount = tcNames.size();
		    	testSheet = new String[tcNames.size()];
		    	
		    	//Mapping the test cases to the modules
		    	int it=0;
		    	tmets=new LinkedList<String>();
		    	
		    	for (String tc : tcNames) {
		    		
	    			tempList="";
	    			worksheet = workbook.getSheet(tc);
	    			if (worksheet!=null) {
	    				int iE = dutil.getHeaderColumnIndex(worksheet, "Test_Region");
	    				int iTE = dutil.getHeaderColumnIndex(worksheet, "Test_Name");
	    				int sCnt = worksheet.getLastRowNum();
	    				int itc=0;
	    				int tcEnd=0;
	    				int tcStart=0;
	    				List<String> tcMets=new LinkedList<String>();
	    				//shahed
	    				List<String> temptcMets=new LinkedList<String>();
	    				//
	    				Multimap<String, String> tcEnv = ArrayListMultimap.create();
	    				String tName = null;
	    				for (int ic=1; ic<=sCnt; ic++) {
	    					String testcaseName=dutil.getCellValue(workbook, worksheet, ic, iTE);
	    					if (!testcaseName.isEmpty() && !testcaseName.equalsIgnoreCase("Test_Name")) {
	    						tName = testcaseName;
	    						tcEnd = dutil.findRow(worksheet,testcaseName+"_End");
	    						if (exePlat.equalsIgnoreCase("desktop") && (!devPlat.toLowerCase().contains("mobile") && !devPlat.toLowerCase().contains("tablet"))) {
		    						if(testcaseName.toLowerCase().contains("mobile"))
		    							continue;
		    					} if (exePlat.equalsIgnoreCase("desktop") && (!devPlat.toLowerCase().contains("mobile") || !devPlat.toLowerCase().contains("tablet"))) {
		    						if (devPlat.toLowerCase().contains("mobile")) {
			    						if(!testcaseName.toLowerCase().contains("mobile"))
			    							continue;
		    						} else if (devPlat.toLowerCase().contains("tablet")) {
			    						if (testcaseName.toLowerCase().contains("mobile"))
			    							continue;
		    						}
	    						} else if (exePlat.equalsIgnoreCase("appium")) {
		    						if (devPlat.toLowerCase().contains("smartphone")) {
			    						if (!testcaseName.toLowerCase().contains("mobile"))
			    							continue;
		    						} else if (devPlat.toLowerCase().contains("tablet")) {
			    						if (testcaseName.toLowerCase().contains("mobile"))
			    							continue;
		    						}
			    				}
		    					tcMets.add(testcaseName);
		    					//shahed
		    					temptcMets.add(testcaseName);
		    					//
	    					}
	    					
	    					if (ic<tcEnd) {
    							String envVal = dutil.getCellValue(workbook, worksheet, ic, iE);
    							if (!envVal.isEmpty())
    								tcEnv.put(tName, envVal.toLowerCase());
    						}
	    				}
	    					    				
	    				//Shahed
	    				for (int j=0; j<tcNames.size(); j++){
	    					String flowname = tcNames.get(j);
	    					if(flowname.equalsIgnoreCase(tc))
	    					{	
	    						for (int K=0; K<temptcMets.size(); K++){
	    					
	    							flowMetMap.put(temptcMets.get(K),flowname);
	    							
	    						}	
	    						
	    					}
    					}
	    				temptcMets.clear();	
	    				//
	    				
	    				for (String tcaseName:tcMets) {	 
    						boolean exec=false;
    						String env[]=envVals.split(",");
    						for (int iEn=0; iEn<env.length; iEn++) {
    		    				if (tcEnv.get(tcaseName).contains(env[iEn].toLowerCase())) {
    		    					exec=true;
    		    					break;
    		    				}
    		    			}
	    					
	    		    		if (exec) {
			    				testMap.put(tcaseName,tc);
			    				fileMetMap.put(tcaseName, tVal);
			    				tmets.add(tcaseName);
			    				String execTestPath = PM_BUILD_WORKSPACE+"/logs/ExecutedTests";
			    				File file = new File(execTestPath);
			    				File f = new File(execTestPath+"/"+tcaseName+".test");
			    				f.createNewFile();
			    			}
	    		    	}
	    			}
	    			
	    			if (tempList!=null && tempList!="") {
	    				tempList=tVal+";"+tc+";"+tempList;
	    				if (tempList.endsWith(",")) {
	    					tempList=tempList.substring(0, tempList.length()-1);
	    					tempList=tempList+"\n";
	    				}
	    				writer.write(tempList);
	    			}
    			}
	    		testSheet[it]=tVal;
	    		it++;
	    		
	    		workbook.close();
	    	}
	    	
	    	//closing the objects
	    	writer.close();
    		
	    } catch (Exception e) {
	    	logger.info("Error in getting Executable Test Cases : " + e.getMessage());
	    } finally {
	    	
	    	if (workbook!=null)
	    		workbook.close();
	    	
	    	if (writer!=null)
	    		writer.close();
		}
		
	}

	/**
	 * @date 09/28/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the total number of test cases executing
	 * @param 
	 * @throws IOException 
	 */	
	
	private int getTotalExecutedTests() {
		int totCount=0;
		try {
			totCount = tmets.size();
		} catch (Exception e) {}
		return totCount;
	}

	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate the testng suite.xml for execution
	 * @param 
	 */
	
	public List<XmlSuite> testngXMLSuiteGenerate_suiteArray() {
		
		//variable declarations
		suites = new ArrayList<XmlSuite>();
		tcMap = new HashMap<String, String>();
		Integer numberOfHosts;
		tcNames = new LinkedList<>();
		tcModule=new HashMap<String, String>();
		FileInputStream objFile;
		Workbook objWorkbook;
		Sheet objSheet;
		String[] gridDev = null;
		
		try {
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities = new DesiredCapabilities("", "", Platform.ANY);
			int browser_inst = Integer.valueOf(prop_BrowserInst);
			String[] devs_list = prop_DevList.split(";");
			int browserCnt = devs_list.length;
			boolean gridExec=Boolean.valueOf(prop_GridExec);
						
			//getting the test modules for execution  
			getExecutableTestCases();
			
			//getting mobile grid devices
			if (!testPlatform.equalsIgnoreCase("desktop")) {
				int size = tmets.size();
				gridDev = new String[size];
				for (int m=0; m<size; m++) {
					gridDev[m]="Device-"+String.valueOf(m+1);
				}
			}
			
	    	if (testPlatform.equalsIgnoreCase("mobile")) {
				//getting the list of available devices for execution in PerfectoMobile
				if(!isPerfectoMobile) {
					
					logger.info("FrameworkUtil :: testngXMLSuiteGenerate_suiteArray(), getting available devices from Appium Cloud!");
					if (gridExec) {
						if (device_ids==null)
							device_ids=new LinkedList<String>();
						for (int k=0; k<gridDev.length; k++) {
							device_ids.add(gridDev[k]);
						}
					} else { 
						device_ids = getAPMDevices(devOS);
					}
				}
			} else if (testPlatform.equalsIgnoreCase("all")) {
				
				String[] dev=devOS.split(";");
				for (int i=0; i<dev.length; i++) {
					if (!dev[i].equalsIgnoreCase("firefox") && !dev[i].equalsIgnoreCase("ie") && !dev[i].equalsIgnoreCase("chrome") && !dev[i].equalsIgnoreCase("safari")) {
						if (tcModule.containsValue("Browser") || !tcModule.containsValue("Desktop")) {
							if(!isPerfectoMobile) {
								
								//getting the list of connected devices on Appium Machines
								logger.info("FrameworkUtil :: testngXMLSuiteGenerate_suiteArray(), getting available devices from Appium Cloud!");
								logger.info("FrameworkUtil :: testngXMLSuiteGenerate_suiteArray(), getting available devices from Appium Cloud!");
								if (gridExec) {
									if (device_ids==null)
										device_ids=new LinkedList<String>();
									for (int k=0; k<gridDev.length; k++) {
										device_ids.add(gridDev[k]);
									}
								} else { 
									device_ids = getAPMDevices(dev[i]);
								}
							}
						}
					}
				}
								
				if (tcModule.containsValue("Browser") || tcModule.containsValue("Desktop")) {
					if (device_ids==null)
						device_ids=new LinkedList<String>();
					
					for (int i=0; i<dev.length; i++) {
						if (dev[i].equalsIgnoreCase("firefox") || dev[i].equalsIgnoreCase("ie") || dev[i].equalsIgnoreCase("chrome") || dev[i].equalsIgnoreCase("safari")) {
							device_ids.add(dev[i]);
						}
					}
				}
			} else {
				if (device_ids==null)
					device_ids=new LinkedList<String>();
				
				String[] dev = devOS.split(";");
				
				for (int i=0; i<dev.length; i++) {
					//Chala : 08/18/2016
					// added chrome-tablet
					if (dev[i].contains("chrome-mobile") || dev[i].contains("chrome-tablet") || dev[i].equalsIgnoreCase("firefox") || dev[i].equalsIgnoreCase("ie") || dev[i].equalsIgnoreCase("chrome") || dev[i].equalsIgnoreCase("safari")) {
						device_ids.add(dev[i]);
					}
				}
			}
					
			//getting the total number of devices available for execution
	    	numberOfHosts = device_ids.size();
	        	        
	        //if devices are not available
	        if (numberOfHosts == 0)
	           	repUtil.reportLog("Devices are not available for execution, please try the execution later!");
	       	        
	        //creating a new Suite
	        XmlSuite[] suiteArray = new XmlSuite[tcNames.size()];
	        int suiteCnt=0;
	        for (String mods : tcNames) {
	        	
	        	//creating a suite array
	        	suiteArray[suiteCnt] = new XmlSuite();
	        	suiteArray[suiteCnt].setName(mods);
	        	
	        	//new list for the parameters
		    	suiteParams = new HashMap<String, String>();
		        
		        //adding a listener
		    	suiteArray[suiteCnt].addListener("org.uncommons.reportng.HTMLReporter");
		    	suiteArray[suiteCnt].addListener("org.uncommons.reportng.JUnitXMLReporter");
		    	suiteArray[suiteCnt].addListener("com.verizon.automation.listener.RetryListener");
		    	suiteArray[suiteCnt].addListener("com.verizon.automation.listener.TestListener");
		    	
		    	//creating the test suite
		        int mCount = distributedTestSuite(suiteArray[suiteCnt], mods);
		        
		        //adding suite arguments
	        	suiteArray[suiteCnt].setTimeOut("3600000");
	        	suiteArray[suiteCnt].setParallel(ParallelMode.TESTS);
	        	//suiteArray[suiteCnt].setThreadCount(mCount);
	        	//suiteArray[suiteCnt].setDataProviderThreadCount(mCount*10);
		        
		        //Add suite to the list
		        suites.add(suiteArray[suiteCnt]);
		        
		        //exporting the testng-suite.xml
		        File sFile = new File(TESTNG_SUITE_FILES);
		        if (!sFile.exists())
		        	sFile.mkdirs();
		    	FileWriter fw = new FileWriter(new File(TESTNG_SUITE_FILES + "/" + mods + "-Suite.xml"));
		    	fw.write(suiteArray[suiteCnt].toXml());
		    	fw.flush();
		    	fw.close();
		    	logger.info("FrameworkUtil :: testngXMLSuiteGenerate_suiteArray() -- testNG.xml suite created");
		        
		    	//increment the suite counter
		        suiteCnt++;
	        }
	    
		} catch(Exception e) {
			logger.error("FrameworkUtil :: testngXMLSuiteGenerate_suiteArray() Exception:"+e);
			Reporter.setEscapeHtml(false);
			Reporter.log("Execution Failed - " + e.getMessage());
		}
		return suites;
	}


	

	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to create the TestNG suite.xml for distributed execution
	 * @param 
	 */
	
public int distributedTestSuite(XmlSuite suite, String tcModuleName) {
		
		String OS_Param=null;
		String dev_name=null;
		Method[] method=null;
		ClassLoader myClassLoader=null;
		Class myClass=null;
		int devNum=1;
		int methodCount=0;
		boolean desk=false;
		boolean loop=false;
		String tempPath = PM_BUILD_WORKSPACE+"/logs/ExecutedTests";
		
		try {
			
			//getting the environment property values from the property files
			APM_PORT=putil.getMobileEnvironmentProperty("appium_port");
			int dev_port = Integer.valueOf(APM_PORT);
			tcMods=new LinkedList<String>();
			int instance=Integer.valueOf(prop_BrowserInst);
			boolean grid=Boolean.valueOf(prop_GridExec);
			FileWriter filew = new FileWriter(new File(EXEC_METHODS),true);
			PrintWriter printer = new PrintWriter(filew);
			
			String packageName = getTestPackageName();
			if ((testPlatform.equalsIgnoreCase("all") || testPlatform.equalsIgnoreCase("desktop")) && 
					(tcModule.containsValue("Browser") || tcModule.containsValue("Desktop"))) {
				if (instance>1) {
					String[] devs=device_ids.toArray(new String[device_ids.size()]);
					for (int i=1; i<instance; i++) {
						for (int j=0; j< devs.length; j++) {
							if (devs[j].equalsIgnoreCase("firefox") || devs[j].equalsIgnoreCase("chrome") || devs[j].equalsIgnoreCase("ie") || devs[j].equalsIgnoreCase("safari") || devs[j].equalsIgnoreCase("chrome-mobile") || devs[j].equalsIgnoreCase("chrome-tablet") )
								device_ids.add(devs[j]+(i+1));
						}
					}
				}
			}
			
			
			for (int tloop=0; tloop<2; tloop++) {
				
				List<String> devices=new LinkedList<String>();
				
				if (tloop==0) {
					for (String devid : device_ids) {
						if (devid.toLowerCase().contains("firefox") || devid.toLowerCase().contains("chrome") || devid.toLowerCase().contains("ie") || devid.toLowerCase().contains("safari") || devid.equalsIgnoreCase("chrome-mobile") || devid.equalsIgnoreCase("chrome-tablet"))
							devices.add(devid);
					}
				} else {
					for (String devid : device_ids)	{
						if (!devid.equalsIgnoreCase("firefox") && !devid.equalsIgnoreCase("chrome") && !devid.equalsIgnoreCase("ie") && !devid.equalsIgnoreCase("safari") && !devid.equalsIgnoreCase("chrome-mobile") && !devid.equalsIgnoreCase("chrome-tablet"))
							devices.add(devid);
					}
				}
				
				if (devices.isEmpty()) {
					for (String devid : device_ids)	{
						devices.add(devid);
					}
				}
				
				//getting the total number of devices
				int totDevices=devices.size();
				
				String[] metVals=tmets.toArray(new String[tmets.size()]);
				
				//initializing iVal
	        	int iVal=0;
	        	
	        	tcMods=new LinkedList<String>();
	        	
		    	for (String devids : devices) {
		    		
		    		// add parameters to the list
		    		if (testPlatform.equalsIgnoreCase("all")) {
		    			if (devids.toLowerCase().contains("firefox") || devids.toLowerCase().contains("chrome") || devids.toLowerCase().contains("ie") || devids.toLowerCase().contains("safari"))
			    			desk=true;
			    		else
			    			desk=false;
		    		} else {
			    		if (devids.toLowerCase().contains("firefox") || devids.toLowerCase().contains("chrome") || devids.toLowerCase().contains("ie") || devids.toLowerCase().contains("safari"))
			    			desk=true;
			    		else
			    			desk=false;
		    		}
		    		
		    		if (!desk) {
			        	if (!isPerfectoMobile) {
				        	OS_Param = MOB_OS;
			        		dev_name = devOS;
			        	}
		    		} else {
		    			OS_Param = System.getProperty("os.name");
		    			dev_name=devids;
		    		}
		        	
		        	MOB_OS = OS_Param;
			        	
		        	//new list for the test parameters
		        	Map<String, String> testParams = new HashMap<String, String>();
		        	
		        	if(!desk) {
			        	testParams.put("device_host", devids);
			        	testParams.put("device_os", OS_Param);
			        	testParams.put("device_name", dev_name.trim());
			        	testParams.put("device_port", String.valueOf(dev_port));
			        	testParams.put("testname", "Suite");
		        	} else {
		        		testParams.put("device_host", devids.replaceAll("[0-9]",""));
			        	testParams.put("device_os", OS_Param);
			        	testParams.put("device_name", "Desktop");
			        }
		        	
		       		//new list for the classes
			        List<XmlClass> classes = new ArrayList<XmlClass>();
			        int temp=iVal;
			        int tempVal;
			        //String tcName=null;
			        int j=iVal;
			        Multimap<String, String> multiMap=ArrayListMultimap.create();
			        int tempCount=0;
			        while (j<metVals.length) {
			        	String tcName = metVals[j];
		       			Collection mods = testMap.get(tcName);
			        	Iterator it = mods.iterator();
			        	while (it.hasNext()) {
			        		String mod = it.next().toString();
			        		if (mod.equalsIgnoreCase(tcModuleName)) {
			        			multiMap.put(mod,tcName);
			        			methodCount++;
			        		}
			        	}
			        	j=j+totDevices;
			        }
			        
			        Set keySet = multiMap.keySet();
			        Iterator keyIterator = keySet.iterator();
			        
			        while (keyIterator.hasNext()) {
			        	
			            String key = (String) keyIterator.next();
			            boolean flag;
			            if (!desk) {
			            	flag=!tcModule.get(key).toLowerCase().contains("desktop");
			            } else {
			            	flag=(tcModule.get(key).toLowerCase().contains("desktop") || tcModule.get(key).toLowerCase().contains("browser"));
			            }
			            
			            if (flag) {
			            	String modName = module.get(key);
			            	List<String> values = (List) multiMap.get(key);
				            String packName=packageName+"."+modName.toLowerCase();
			       			String cName=packName+"."+key;
			       			XmlClass testClass = new XmlClass(cName);
				        	
				        	ArrayList<XmlInclude> methodsToRun = new ArrayList<XmlInclude>();
				        	ArrayList<XmlInclude> methodsToIgnore = new ArrayList<XmlInclude>();
				        	
				        	myClassLoader= ClassLoader.getSystemClassLoader();
							myClassLoader= this.getClass().getClassLoader();
				        	myClass=myClassLoader.loadClass(cName);
				        	method=myClass.getDeclaredMethods();
				        	for(String val:values) {
				        		boolean exist=false;
				        		int counter=0;
				        		String mName=null;
				        		for (Method m : method) {	
				        			mName=m.getName();
					        		if (mName.equalsIgnoreCase(val)) {
					        			methodsToRun.add(new XmlInclude(val));
					        			File f = new File(tempPath+"/"+val+".temp");
							        	f.createNewFile();
							        	exist=true;
					        			break;
					        		} else {
					        			String missFilePath = tempPath + "/MissedTC/" + key;
					        			if (!new File(missFilePath).exists())
					        				new File(missFilePath).mkdirs();
					        			
					        			if (counter==(method.length)-1) {
					        				printer.println("Test Case Name : " + mName + "--->  Java Class Name : " + key);
					        				File notAdded = new File(missFilePath+"/"+mName+".miss");
					        				notAdded.createNewFile();
					        			}
					        		}
					        		counter++;
					        	}
				        		//if (exist)
					        	//	methodsToRun.add(new XmlInclude(val));
				        		if (!tcMods.contains(mName))
				        			tcMods.add(mName);
					        }
				        					        	
				        	if (methodsToRun.size()>0) {
				        		testClass.setIncludedMethods(methodsToRun);
					        	classes.add(testClass);
				        	}
				        }
			        }
		       		
		       		//add classes to test
			        if (classes.size()>0) {
			        	test = new XmlTest(suite);
			        	test.setParameters(testParams);
			       		test.setPreserveOrder("true");
			       		if (desk)
			       			test.setName(tcModuleName + "_" + devids.replaceAll("[0-9]","") + devNum);
			       		else
			       			if (grid)
			       				test.setName(tcModuleName + "_MobDevice" + devNum);
			       			else
			       				test.setName(tcModuleName + "_" + devids);
			       		
			       		test.setThreadCount(30);
			        	test.setClasses(classes);
			        	devNum++;
			        }
			        
		        	iVal++;
		        	
		        	//incrementing the port for different appium instances
		        	if(!desk) 
			        	dev_port ++;
			        
		        	if (tcMods.size()==tmets.size())
		        		break;
			    }
		    	
		    	if (!tcModule.containsValue("Browser"))
		    		break;
		    	
		    	printer.close();
			}
			
		} catch (Exception e) {
			//System.out.println("Error : " + e.getMessage());
		}
		return methodCount;
	}
	
	
	public void getPropertyValues() {
		try {
			prop_ProjPortfolio=putil.getTestEnvironmentProperty("project_portfolio").trim();
			prop_ProjName=putil.getTestEnvironmentProperty("project_name").trim();
			prop_ProjSuite=putil.getTestEnvironmentProperty("project_suite_name").trim();
    		prop_GridExec=putil.getTestEnvironmentProperty("grid_execution").trim();
    		prop_ExecPlatform=putil.getTestEnvironmentProperty("execution_platform").trim();
    		prop_DevList=putil.getTestEnvironmentProperty("device_list").trim();
    		prop_GridHub=putil.getTestEnvironmentProperty("grid_hub").trim();
    		prop_BrowserInst=putil.getTestEnvironmentProperty("browser_instance").trim();
    		prop_Screenshot=putil.getTestEnvironmentProperty("TakeScreenshotAllSteps").trim();
			prop_ModSuite=putil.getTestEnvironmentProperty("xml_module_suite").trim();
			DEV_OS_Deploy=putil.getTestEnvironmentProperty("dev_deployregion").trim();
			MOB_OS=putil.getTestEnvironmentProperty("dev_deployplatform").trim();
			APM_PORT=putil.getMobileEnvironmentProperty("appium_port").trim();
			
    		String wait=putil.getTestEnvironmentProperty("wait_time").trim();
    		if (wait==null||wait.isEmpty())
    			wait="30";
    		waitTime=Integer.valueOf(wait);
    		
    		prop_Apptype = putil.getTestEnvironmentProperty("app_type").trim();
			prop_Buildtool = putil.getTestEnvironmentProperty("build_tool").trim();
			prop_appiumserver = putil.getTestEnvironmentProperty("appium_server").trim();
			prop_apppath = putil.getTestEnvironmentProperty("app_path").trim();
    		prop_ProxyReq=putil.getTestEnvironmentProperty("proxy_required").trim();
    		prop_ProxyType=putil.getTestEnvironmentProperty("proxy_type").trim();		
    		prop_ProxyHost=putil.getTestEnvironmentProperty("proxy_host").trim();
		   	prop_ProxyPort=putil.getTestEnvironmentProperty("proxy_port").trim();
   			prop_ProxyUser=putil.getTestEnvironmentProperty("proxy_user").trim();
			prop_ProxyPass=putil.getTestEnvironmentProperty("proxy_password").trim();
			prop_ProxyIgnore=putil.getTestEnvironmentProperty("proxy_bypass").trim();
			prop_ProxyAutoConfigUrl=putil.getTestEnvironmentProperty("proxy_autoconfig_url").trim();
		} catch (Exception e) {
			
		}
	}

	
	
	
	public static void main(String[] args){
		//createDriver("");
	}
	
	
	

	public RemoteWebDriver createDriver(String metName, String device) {

		String sBrowser = device.toString().toLowerCase();
		boolean gridExec = Boolean.valueOf(prop_GridExec);
		String url = prop_GridHub;
		RemoteWebDriver rDriver = null;
		
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		
		DesiredCapabilities dCap = new DesiredCapabilities();

		switch (sBrowser) {

		case "chrome-tablet":
			mobileEmulation.put("deviceName", "Apple iPad");

		case "chrome-mobile":
			if (mobileEmulation.size() == 0)
				mobileEmulation.put("deviceName", "Apple iPhone 6");

		case "chrome":
			dCap = new DesiredCapabilities().chrome();
			dCap.setPlatform(Platform.ANY);
			if (mobileEmulation.size() > 0) {
				chromeOptions.put("mobileEmulation", mobileEmulation);
				dCap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			}
			if (!gridExec) {
				System.setProperty("webdriver.chrome.driver", BROWSER_DRIVER_PATH + "/chromedriver");
				rDriver = new ChromeDriver(dCap);
			} else {
				try {
					rDriver = new RemoteWebDriver(new URL(prop_GridHub), dCap);
				} catch (MalformedURLException e2) {
					logger.info("[[" + metName + "]] : Capabilities failed !");
				}
			}
			break;

		case "firefox":
			dCap = DesiredCapabilities.firefox();
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			dCap.setPlatform(Platform.ANY);
			if (!gridExec) {
				File pathToBinary = new File(System.getenv("APPDATA") + "/Local/Mozilla Firefox/firefox.exe");
				if (!pathToBinary.exists())
					rDriver = new FirefoxDriver(dCap);
				else {
					FirefoxBinary ffBinary = null;
					ffBinary = new FirefoxBinary(pathToBinary);
					rDriver = new FirefoxDriver(ffBinary, firefoxProfile, dCap);
				}
			} else {
				dCap.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
				try {
					rDriver = new RemoteWebDriver(new URL(prop_GridHub), dCap);
				} catch (MalformedURLException e2) {
					logger.info("[[" + metName + "]] : Capabilities failed !");
				}
			}
			break;

		case "ie":
			dCap = new DesiredCapabilities().internetExplorer();
			dCap.setPlatform(Platform.ANY);
			dCap.setCapability("requireWindowFocus", true);
			dCap.setCapability("enablePersistentHover", true);
			dCap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			dCap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			if (!gridExec) {
				System.setProperty("webdriver.ie.driver", BROWSER_DRIVER_PATH + "/IEDriverServer.exe");
				rDriver = new InternetExplorerDriver(dCap);
			}
			break;

		case "safari":
			dCap = DesiredCapabilities.safari();
			SafariOptions safariOptions;
			safariOptions = new SafariOptions();
			safariOptions.setUseCleanSession(true);
			dCap.setCapability(SafariOptions.CAPABILITY, safariOptions);
			rDriver = new SafariDriver(dCap);
			break;

		case "ios-smartphones":
			dCap.setCapability("browserName", "Safari");
			dCap.setCapability("platformName", "ios");
			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "5.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				url = "http://" + prop_appiumserver + "/wd/hub";

			}
			try {
				rDriver = new IOSDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "ios-tablet":
			dCap = new DesiredCapabilities();
			dCap.setCapability("browserName", "Safari");
			dCap.setCapability("platformName", "ios");
			if (gridExec) {
				dCap.setCapability("deviceName", "tablet");
				dCap.setCapability("version", "7.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}
			try {
				rDriver = new IOSDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "ios-nativeapp-mobile":
			dCap = new DesiredCapabilities();
			dCap.setCapability("browserName", "");
			dCap.setCapability("platformName", "ios");
		
			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "5.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				dCap.setCapability("app", System.getProperty("user.dir")+prop_apppath);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}
			try {
				rDriver = new IOSDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "ios-nativeapp-tablet":

			dCap.setCapability("version", "7.0.0");
			dCap.setCapability("platformName", "ios");

			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "7.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				dCap.setCapability("app", System.getProperty("user.dir")+prop_apppath);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}
			try {
				rDriver = new IOSDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "android-smartphones":
			dCap.setCapability("browserName", "Chrome");
			dCap.setCapability("platformName", "android");

			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "5.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}

			try {
				rDriver = new AndroidDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "android-nativeapp-mobile":
			dCap.setCapability("browserName", "");
			dCap.setCapability("platformName", "android");
			dCap.setCapability("appActivity",".ui.activities.TestActivity");
			dCap.setCapability("intentAction", "android.intent.action.LAUNCH_MVM_TESTSCREEN");

			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "5.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				dCap.setCapability("app", System.getProperty("user.dir")+prop_apppath);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}
			try {
				rDriver = new AndroidDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;

		case "android-nativeapp-tablet":
			dCap.setCapability("browserName", "");
			dCap.setCapability("platformName", "android");
			dCap.setCapability("appActivity",".ui.activities.TestActivity");
			dCap.setCapability("intentAction", "android.intent.action.LAUNCH_MVM_TESTSCREEN");
			if (gridExec) {
				dCap.setCapability("deviceName", "smartphone");
				dCap.setCapability("version", "7.0.0");
			} else {
				dCap.setCapability("deviceName", device);
				dCap.setCapability("app", System.getProperty("user.dir")+prop_apppath);
				url = "http://" + prop_appiumserver + "/wd/hub";
			}
			try {
				rDriver = new IOSDriver(new URL(url), dCap);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;
			
		 default:
             return null;

		}

		return rDriver;
	}

	
		
}