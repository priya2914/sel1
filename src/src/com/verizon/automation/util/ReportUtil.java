package com.verizon.automation.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
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
import com.verizon.automation.configuration.SuiteSetup;

@SuppressWarnings("all")
public class ReportUtil extends SuiteSetup {
	
	private final static Logger logger = Logger.getLogger(ReportUtil.class);
	private static String resultsfolder;
	private RemoteWebDriver repDriver;
	private String testValue=null;
	private String tcName;
	private static String stTime=null;
	
	public ReportUtil() {}
	
	
	public ReportUtil(RemoteWebDriver driver) {
		this.repDriver = driver;
	}
	
	public ReportUtil(RemoteWebDriver driver, String testName) {
		this.repDriver = driver;
		this.tcName = testName;
	}
		
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the cookie value of the page
	 * @param 
	 */
	
	private String getCookieValue() {
		String cookies=null;
		try {
			cookies="COOKIE INFORMATION : \r\n \r\n";
			Set<Cookie> allCookies = repDriver.manage().getCookies();
			for (Cookie loadedCookie : allCookies) {
				if (!loadedCookie.getName().contains("NSC_") && !loadedCookie.getName().contains("__g") && !loadedCookie.getName().contains("fsr.s")
						&& !loadedCookie.getName().contains("s_vi") && !loadedCookie.getName().contains("s_sess") && !loadedCookie.getName().contains("mbox")
						&& !loadedCookie.getName().contains("s_pers") && !loadedCookie.getName().contains("OC"))
					cookies=cookies+String.format("%s : %s", loadedCookie.getName(), loadedCookie.getValue())+"\n";
			}
			//System.out.println(cookies);
			logger.info(cookies);
		}catch (Exception e) {
			System.out.println("Error in getting the cookie information");
			logger.info("Error in getting the cookie information");
		}
		return cookies;
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to create a report file for PDF generation
	 * @param 
	 */
	
	public void createreportfile() throws IOException {
		
		String currentDir = PM_BUILD_WORKSPACE;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		String strfoldername = "Run-"+ dateFormat.format(date);		
		String platform = prop_ExecPlatform;
		if (!platform.equalsIgnoreCase("desktop"))
			platform = "Mobile";
		else
			platform = "Desktop";
		resultsfolder = REPORTS_PATH + "/" + strfoldername + "/Platform-" + platform;
		File tresultsfolder = new File(resultsfolder);
		if(!tresultsfolder.exists()){
			tresultsfolder.mkdirs();
		}
		File tresultsfile = new File(resultsfolder +"/results.txt");
		if(!tresultsfile.exists()) {
			tresultsfile.createNewFile();
	    }
		PrintWriter filew = new PrintWriter(new FileWriter(tresultsfile));
		File tfile1 = new File(currentDir + "/environment/report.ini");
		if(tfile1.exists()) {
			tfile1.delete();
			tfile1.createNewFile();			
			FileWriter tfile2 = new FileWriter(currentDir + "/environment/report.ini",true);
			PrintWriter filewa = new PrintWriter(tfile2);
			filewa.println(resultsfolder);
			filewa.close();
		}		
	}


	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to handle to failures
	 * @param 
	 */
	
	public void reportComments(String description, String Steps, boolean exit) {
		String value=null;
		String stepsToReproduce="";		
		try {
			
			//String tName=dutil.getTestName();
			
			String tName = this.tcName;
			
			if (failedTests.isEmpty() || !failedTests.contains(tcName))
				failedTests.add(tcName);
			
			if (Steps==null)
				Steps="Steps not available";
			
			if (description==null)
				description="Description not available";
						
			if (repDriver!=null) {
				reportLog(description);
				stepsToReproduce(Steps);
				
				Collection coll=reproduceSteps.get(tName);
				Iterator it=coll.iterator();
				while (it.hasNext()) {
					stepsToReproduce=stepsToReproduce+it.next()+"\r\n \r\n";
				}
				stepsReproduce=stepsToReproduce;
				try {
					value = repDriver.getCapabilities().getCapability("platformName").toString();
				} catch (Exception e) {
					value = repDriver.getCapabilities().getCapability("platform").toString();
				}
			} else {
				String plat=prop_DevList;
				if (plat.contains("android"))
					value="android";
				else if (plat.contains("ios"))
					value="ios";
			}
			
			if(!exit) {
				description = description + "\r\n \r\n" + "STEPS TO REPRODUCE : " + stepsReproduce;
				sAssert.assertTrue(false, description);
			} else {
				if (repDriver!=null) {
					String cookieVal=getCookieValue();
					if (!description.contains("STEPS TO REPRODUCE :"))
						execDescription = description + "\r\n \r\n" + "STEPS TO REPRODUCE : \r\n \r\n" + stepsReproduce;
					else
						execDescription = description + "\r\n \r\n" + stepsReproduce;
					
					execStatus="Fail";
					execDescription=execDescription+"\r\n \r\n"+cookieVal;
				} else {
					execDescription="Error in Selenium RemoteWebDriver setup";
				}
				Assert.assertTrue(false, execDescription);
			}
		} catch (Exception e) {
			reportLog("Error in creating steps to reproduce details and exiting the test");
			logger.info("ReportUtil :: reportComment - Error : " + e.getMessage());
		}
	}
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to build the steps to reproduce
	 * @param 
	 */
	
	public void stepsToReproduce(String steps) {
		try {
			String tName=dutil.getTestName();
			int sCount=reproduceSteps.get(tName).size();
			String sReproduce = (sCount+1) + ". " + steps;
			reproduceSteps.put(tName, sReproduce);
		} catch (Exception e) {}
	}
		
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method that writes all the log information required to generate PDF file
	 * @param 
	 */
	public void reportlog(String strtext,String teststepdesc,String stepresult) {

		String resultstext=null;
		String platformVersion=null;
		String platformname=null;
		String strmodulename=null;
		String strOSName=null;
		String region=null;
		String firstStep=null;
		String devType=null;
		boolean bstepresult=true;
		String stepDetails=null;
		String screenfile=null;
		String execPlat=null;
		FileWriter fWrite=null;
		PrintWriter pWrite=null;
			
		
		Date date = new Date();
		int value=0;
		int iterCnt=0;
		
		try {
			
			region=dutil.getDataValue(this.tcName,"Test_Region");
			if (region==null) {
				region=execRegion;
			}
				
			execPlat=prop_ExecPlatform;
			String screen = prop_Screenshot;
			if (screen==null) {
				screen="false";
			}
			
			if (stepresult==null)
				stepresult="Fail";
			
			//getting the step result
			if (stepresult.equalsIgnoreCase("pass"))
				bstepresult=true;
			else if (stepresult.equalsIgnoreCase("fail"))
				bstepresult=false;
			else if (stepresult.equalsIgnoreCase("warn"))
				bstepresult=true;
			
			if (strtext==null)
				strtext="No information provided";
			
			if (teststepdesc==null)
				teststepdesc="No step description provided";
						
			strtext = strtext.replace("\r", " ").replace("\n", " ");
			teststepdesc=teststepdesc.replace("\n"," ").replace("\r", " ");
			
			
			if (repDriver!=null) {
				if (execPlat.toLowerCase().contains("appium") || execPlat.toLowerCase().contains("perfecto")) {
					try {
						if (MOB_OS.toLowerCase().contains("android"))
							devType = repDriver.getCapabilities().getCapability("deviceName").toString();
						else
							devType = repDriver.getCapabilities().getCapability("udid").toString();
						
						if(!isPerfectoMobile) {
							platformname=repDriver.getCapabilities().getCapability("platformName").toString();
							if (platformname.equalsIgnoreCase("android"))
								platformVersion=repDriver.getCapabilities().getCapability("platformVersion").toString();
							else
								platformVersion=repDriver.getCapabilities().getCapability("version").toString();
						} 
					} catch (Exception e) {
						if (prop_DevList.toLowerCase().contains("tablet")) {
							devType = "Tablets";
						} else {
							devType = "Smartphones";
						}
						platformVersion = "Any";
					}
					
					if (platformname==null) {
						platformname=MOB_OS;
					}
					
					strOSName=platformname;
				} else if (execPlat.equalsIgnoreCase("desktop")) { 
					try {
						platformname=repDriver.getCapabilities().getBrowserName();
						devType=StringUtils.capitalize(platformname);
						strOSName=repDriver.getCapabilities().getCapability("platform").toString();
						platformVersion=repDriver.getCapabilities().getVersion();
					} catch (Exception e1) {
						platformname = prop_DevList.toLowerCase();
						platformname = platformname.replace("-", "").replace("mobile", "").replace("tablet", "");
						devType=StringUtils.capitalize(platformname);
						platformVersion="Any";
						strOSName = System.getProperty("os.name");
					}
				}
			} else {
				if (execPlat.equalsIgnoreCase("desktop")) {
					platformname = prop_DevList.toLowerCase();
					platformname = platformname.replace("-", "").replace("mobile", "").replace("tablet", "");
					devType=StringUtils.capitalize(platformname);
					strOSName = System.getProperty("os.name");
				} else {
					platformname=MOB_OS;
					strOSName=platformname;
					if (prop_DevList.toLowerCase().contains("tablet")) {
						devType = "Tablets";
					} else {
						devType = "Smartphones";
					}
				}
				platformVersion="Any";
			}
			
			strmodulename = flowMetMap.get(this.tcName);
			if(devType==null)
				devType = "NullDriver";
			else
				devType=devType.replace(".", "-").replace(":", "-");
			String fName = devType + "-" + this.tcName;
			File dirResult = new File(resultsfolder +"/"+ strmodulename);
			if (!dirResult.exists())
				dirResult.mkdirs();
			
			File tresultsfile = new File(resultsfolder +"/"+ strmodulename + "/" + fName+"-results.txt");
			
			resultfile=tresultsfile;
			if(!tresultsfile.exists()) {
				if (this.tcName!=null)
					tresultsfile.createNewFile();
				String currentDir = PM_BUILD_WORKSPACE;
				String content="";
				String[] c = tresultsfile.getPath().toString().split("\\/");
				try { 
					content = new Scanner(new File(currentDir + "/environment/report.ini")).useDelimiter("\\Z").next(); 
				} catch(Exception e) { 
					content =  "./"+c[1]+"/"+c[2]+"/"+c[3];
				}
				fWrite = new FileWriter(currentDir + "/environment/report.ini",true);
				pWrite = new PrintWriter(fWrite);
				if (!content.contains(tresultsfile.toString()))
					pWrite.println(tresultsfile.toString());
				pWrite.close();
				fWrite.close();
	        }
			
			if (strtext.equalsIgnoreCase("execution completed") && bstepresult) {
				int totSteps = 0;
				BufferedReader buffRead = new BufferedReader(new FileReader(tresultsfile));
				String text = buffRead.readLine();
				while (text!=null) {
					text=buffRead.readLine();
					totSteps++;
				}
				if (totSteps<10) {
					if (passedTests.contains(this.tcName))
						passedTests.remove(this.tcName);
					
					if (!failedTests.contains(this.tcName))
						failedTests.add(this.tcName);
					
					bstepresult=false;
					teststepdesc="Test was interrupted and did not complete the E2E flow";
					logger.info("Total Number of steps executed is only : " + totSteps);
				}
			}
						
			if(bstepresult) {
				stepDetails = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext;
				
				// 10/10/2016 : Change to take screenshot for pass step as well
				if(screen.equalsIgnoreCase("true")){
					if(stepresult.equalsIgnoreCase("warn")){
						screenfile = takescreenshot(tresultsfile, strtext);
						resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;PASSED;;"+dateFormat.format(date)+ ";;"+ strmodulename +";;WARNING;;"+screenfile+ ";; "+platformname+";;"+platformVersion+";;"+strOSName;
					}
					else{
						screenfile = takescreenshot(tresultsfile, strtext);
						resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;PASSED;;" +dateFormat.format(date)+ ";;"+ strmodulename +";;"+screenfile+ ";; "+platformname+";;"+platformVersion+";;"+strOSName;
					}
				} else {
					if(stepresult.equalsIgnoreCase("warn")){
						resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;PASSED;;"+dateFormat.format(date)+ ";;"+ strmodulename +";;WARNING;;"+screenfile+ ";; "+platformname+";;"+platformVersion+";;"+strOSName;
					}
					else{
					resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;PASSED;;" +dateFormat.format(date)+ ";;"+ strmodulename + ";; "+platformname+";;"+platformVersion+";;"+strOSName;	
					}
				}	
			} else {
				stepDetails = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext;
				if (!strtext.equalsIgnoreCase("execution completed")) {
					screenfile = takescreenshot(tresultsfile, strtext);
					resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;FAILED;;" +dateFormat.format(date)+ ";;"+ strmodulename +";;"+screenfile+ ";; "+platformname+";;"+platformVersion+";;"+strOSName;
				} else {
					screenfile = "COMPLETED";
					resultstext = devType +";;"+ this.tcName +";;"+ "Step " + teststepno +";;"+ "" + strtext + ";;"+ teststepdesc +";;FAILED;;" +dateFormat.format(date)+ ";;"+ strmodulename +";;"+screenfile+ ";; "+platformname+";;"+platformVersion+";;"+strOSName;
				}
			}
		
			if (stTime==null) {
				stTime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(tresultsfile.lastModified()));
			}
			
			iterCnt=dutil.getIterationCount(tcName);
			if (iterCnt<1) iterCnt=1;
            resultstext=resultstext+";;"+region;
			resultstext=resultstext+";;Iteration-"+String.valueOf(iterCnt);
			fWrite = new FileWriter(tresultsfile,true);
			pWrite = new PrintWriter(fWrite);
			pWrite.println(resultstext);
			reportLog("[[" +this.tcName + "]] : " + teststepdesc);
			
			String mongoresults = putil.getTestEnvironmentProperty("UpdateResultsToMongoDB");
			if(mongoresults.equalsIgnoreCase("true")){
				
				HashMap<String, String> resultsdetails = new HashMap<String, String>();
				DateFormat repdateFormat =new SimpleDateFormat("MM/dd/yyyy");
				  String runiddetails = resultsfolder.split("/")[2];
				    String appName = fileMetMap.get(this.tcName);
				    resultsdetails.put("RunID",  runiddetails);
				    resultsdetails.put("TestFlowName", this.tcName);
				    resultsdetails.put("Sub-Module", strmodulename);
				    resultsdetails.put("Env", region);
				    resultsdetails.put("Module", appName.toUpperCase());
				    resultsdetails.put("Browser", devType);
				    resultsdetails.put("Platform", execPlat.toUpperCase());
				    resultsdetails.put("OS", strOSName);
				    resultsdetails.put("StepName", strtext);
				    resultsdetails.put("StepDescription", teststepdesc);
				    resultsdetails.put("ActionTime", dateFormat.format(date));
				    resultsdetails.put("RunDate", repdateFormat.format(date));
				    if(bstepresult){
				    	 resultsdetails.put("StepResult", "PASSED");
				    }else {
				    	 resultsdetails.put("StepResult", "FAILED");
				    }
				    if (!strtext.equalsIgnoreCase("execution completed")){
				    	 resultsdetails.put("Screenfile", "");
				    }
				   
				    String[] arg = environmentParams;
		              String bMultiModule = "false";

		               if(arg.length >= 3)
		                     if(arg[2].split(":")[1].split(",").length > 0 )
		                            bMultiModule = "ture";
		                     else
		                            bMultiModule = "false";
		                
		               resultsdetails.put("MultiModule", bMultiModule);       

				    MongoDBUtil mongoutil = new MongoDBUtil();
				    mongoutil.writingStepResultsToMongo(resultsdetails,mongodb);  
			}
			
			
		} catch (Exception e) {
			logger.info("Error in writing report log file for the test - '" + this.tcName + "' : " + e.getMessage());
		} finally {
			if (pWrite!=null)
				pWrite.close();
		}
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to capture the screenshot of the page
	 * @param 
	 */
	

	public String takescreenshot(File repFile, String strText) {
		String returnVal=null;
		try {
			String strfilename;
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String currentDir = PM_BUILD_WORKSPACE;
			String strfilescr = repFile.toString();
			String scrfilename ="VerizonMobileAuto-"+ dateFormat.format(date)+".png";
			strfilename = strfilescr.replace("results.txt","")  + scrfilename;
			File screenshotFile = new File(strfilename);
			if (repDriver!=null) {
				Augmenter augmenter = new Augmenter();
				TakesScreenshot ts = (TakesScreenshot) augmenter.augment(repDriver);
				File file = repDriver.getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(file, screenshotFile);
				returnVal=screenshotFile.toString();
			} else {
				returnVal="";
			}
		} catch (IOException e) {
			logger.info("Error in taking screenshot for the test step - '" + strText + "'");
			e.printStackTrace();
			returnVal="Done";
		}
		return returnVal;
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to display the output on consoles
	 * @param 
	 */
	
	public void reportLog(String text) {
		try {
			if (text==null)
				text="no log information is provided";
			Reporter.setEscapeHtml(false);
			logger.info(text);
			Reporter.log(text);
		} catch (Exception e) {}
	}
	
	
	public boolean getExecStatus(String metName) {
		boolean testStat=false;
		try {
			if (!failedTests.contains(metName)) {
				testStat=true;
				passedTests.add(metName);
				logger.info("Execution status for the test case - '" + metName + "' is : PASS");
			} else {
				logger.info("Execution status for the test case - '" + metName + "' is : FAIL");
			}
			//System.out.println(failedTests.toString());
			
		} catch (Exception e) {
			logger.info("getExecStatus() :: Error in the test execution status!"); 
		}
		return testStat;
	}
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method that  
	 * @param 
	 */	
	private List<String> sortLastModifiedFiles(String filePath) {
		
	    File theNewestFile = null;
	    List<String> sortFile = new LinkedList<String>();
	    String retVal=null;
	    try {
		    File dir = new File(filePath);
		    if (dir.isDirectory()) {
			    File[] files = dir.listFiles();
			    if (files.length > 0) {
			        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			        for (int i=0; i<files.length; i++) {
			        	sortFile.add(files[i].getAbsolutePath());
			        }
			    }
		    }
		} catch (Exception e) {}
	    return sortFile;
	}
	
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate PDF report files
	 * @param 
	 */
	
	public String generatepdfreport() throws IOException, DocumentException, ParseException {
		
		Document document = new Document(PageSize.A4);
		Document tcdocument = null;
		BufferedReader buffile = null;
		PdfWriter writer = null;
		String currentDir = null;
		File tfilea = null;
		String endtime=null;
		String starttime=null;
		String sReportJSON = "Error In Generating Report";
		
		try {
			
			System.out.println("Generating PDF reports...");
			logger.info("Generating PDF reports...");
			
			currentDir = PM_BUILD_WORKSPACE;
			String projName = prop_ProjName;
			String projSuite = prop_ProjSuite;
			tfilea = new File(currentDir + "/environment/report.ini");
			buffile = new BufferedReader(new FileReader(tfilea));
			List<String> filelist = new LinkedList<String>();
			String [] filename;
			String execEnvironment=null;
			int ifilecount=0;
			String sumline1;
			sumline1 = buffile.readLine();
			String resultsfolder = sumline1.trim();
			
			File repfile = new File(PDF_REPORTS_PATH);
			if (repfile.exists()) {
				FileUtils.deleteDirectory(repfile);
			} else {			
				repfile.mkdirs();
			}
				
			while (sumline1 != null) {
				sumline1 = buffile.readLine();
				if (sumline1!=null) {
					if (!sumline1.isEmpty()) {
						filelist.add(sumline1);
					}
				}
			}
			
			buffile.close();
			
			//opening the results text file for report consolidation
			File file=new File(resultsfolder +"/results.txt");
		    FileWriter output=new FileWriter(file,true);
		    PrintWriter filewa = null;
		    
		    long lastMod = Long.MIN_VALUE;
		    long firstMod = Long.MAX_VALUE;
		    File repFold = new File(resultsfolder);
		    
		    for (File fold : repFold.listFiles()) {
		    	
		    	if (fold.isDirectory()) {
		    		
		    		//getting the module directory names
		    		String dirName = fold.getName();
		    		
		    		//sort the file based on the last modified date
					List<String> repFiles=sortLastModifiedFiles(resultsfolder + "/" + dirName);
					
					//store the file name in an array
					filename = repFiles.toArray(new String[filelist.size()]);
					
					for(String rFile : repFiles)	{
				    	File fexist=new File(rFile);
				    	if (filelist.toString().contains(fexist.getName())) {
					    	if ((fexist.exists()) && (FilenameUtils.getExtension(fexist.getAbsolutePath()).contains("txt")) && (!fexist.getName().replace(".txt", "").equalsIgnoreCase("results"))) {
					    		BufferedReader objBufferedReader = new BufferedReader(new FileReader(rFile));
						        String line;
						        int cnt=0;
						        filewa = new PrintWriter(output);
						        while ((line = objBufferedReader.readLine())!=null ) {
						          	filewa.println(line);
									cnt++;
						        }
						        objBufferedReader.close();
					    	}
				    	}
				    	if (fexist.lastModified() > lastMod) {
				    		lastMod = fexist.lastModified();
				    	}
				    	
				    	if (fexist.lastModified() < firstMod) {
				    		firstMod = fexist.lastModified();
				    	}
				    }
				}
		    }
		    
		    filewa.close();
		     
			document = new Document(PageSize.A4);
		
			File tfile = new File(resultsfolder + "/results.txt");
			BufferedReader buffile1 = new BufferedReader(new FileReader(tfile));
			buffile1.close();

			String resultspdf = resultsfolder+ "/results.pdf";
			File resultsfile = new File(resultsfolder + "/results.txt");
		
			writer = PdfWriter.getInstance(document, new FileOutputStream(resultspdf));
		    document.open();
		    document.setPageSize(PageSize.A4);
		    
		    Image image1 = Image.getInstance(currentDir+"/environment/Verizonlogo.png");
		    image1.scaleAbsolute(50, 50);
			document.add(image1);
		    Paragraph title1 = new Paragraph(projName, FontFactory.getFont(FontFactory.HELVETICA,18, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			
			Chapter chapter1 = new Chapter(title1, 1);
		    chapter1.setNumberDepth(0);
		    title1.setAlignment(Element.ALIGN_CENTER);
		    document.add(title1);
		    BufferedReader sumbuff = new BufferedReader(new FileReader(resultsfile));
			String sumline = null;
			String strsumtestcase=null;
			sumline = sumbuff.readLine();
			String[] sumstr =  sumline.split(";;");
		    String overallstarttime = null;
			String overallendtime;
			String env=sumstr[sumstr.length-2];
			String iter=sumstr[sumstr.length-1];
			String 	strtestcasename = sumstr[1]+"-"+sumstr[0]+"-"+env+"_"+iter;
			String execPlatform=null;
			String execIteration=null;
			int inttestcount;
			int inttestpassed;
			int inttestfailed;
			int intserialno;
			String resultstatus;
			inttestpassed=0;
			inttestfailed=0;
			inttestcount=0;
			//starttime=String.valueOf(firstMod);
			starttime="";
			overallstarttime=stTime;
			resultstatus="PASSED";
			endtime="";
			intserialno=1;
			
			String screen = prop_Screenshot;
			if (screen==null) {
				screen="false";
			}
			
			PdfContentByte cb = writer.getDirectContent();
		    PdfPTable table = new PdfPTable(12); // 3 columns.  //nov15, Shahed changed to 12 from 11
		    
	        table.setWidthPercentage(100); //Width 100%
	        float[] columnWidths = {2f,4f,5f,3f,4f,2f,2f,2f,5f,5f,3f,3f};  //nov15 Shahed adjusted column widths
	        table.setWidths(columnWidths);
	        table.setSpacingBefore(5f); 
		    table.setSpacingAfter(10f); 
	        
	        String strtext="Serial No";
	        Paragraph celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        PdfPCell tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	        
	        //shahed
	        strtext="Flow Name";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	        
	        strtext="Test Case Name";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	        
	        strtext="Environment";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	        
	        strtext="Execution Platform";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	        strtext="No.of Steps";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	        
	        strtext="Passed Steps";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	        strtext="Failed Steps";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	       
	        strtext="Start Time";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	        strtext="End Time";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	        strtext="Time Taken";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	        strtext="Status";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
			int testcasecount = 1;
			int testcasepassed = 0;
			int testcasefailed = 0;
			int allteststep = 1;
			int allpassedstep = 0;
			int allfailedstep =0;
			int allpassedtestcase=0;
			int allfailedtestcase=0;
			BaseColor bcolor = BaseColor.LIGHT_GRAY;
			
			starttime=sumstr[6];
			overallendtime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(lastMod));;
			
			String strFile = FileUtils.readFileToString(new File(resultsfolder + "/results.txt"),"UTF-8");
			String fileCont[]=strFile.split(System.getProperty("line.separator"));
			int iCount = 0;
			String currentTC = "";
			String prevTC = currentTC;
			String FlowName = null; //Shahed
			//while (sumline != null) {
			for(int iline=0; iline<fileCont.length; iline++) {
				sumline=fileCont[iline];
				sumstr =  sumline.split(";;");
				execPlatform = sumstr[sumstr.length-3];
				execIteration = sumstr[sumstr.length-1];

				prevTC = currentTC;
				currentTC = sumstr[1];
				
				if(strtestcasename.equalsIgnoreCase(sumstr[1]+"-"+sumstr[0]+"-"+sumstr[sumstr.length-2]+"_"+sumstr[sumstr.length-1])) { // && !(iline==(fileCont.length-1))) {  //nov15 Shahed added && !(iline==(fileCont.length-1)))
					
					inttestcount = inttestcount + 1;
					endtime=sumstr[6];
					FlowName = sumstr[7];  //Shahed
					if (sumstr[5].equalsIgnoreCase("PASSED")){
						inttestpassed = inttestpassed+1;
						allpassedstep=allpassedstep+1;
					} else {
						inttestfailed = inttestfailed+1;
						resultstatus = "FAILED";
						allfailedstep = allfailedstep +1;
					}
					allteststep = allteststep +1;
					strtestcasename = sumstr[1]+"-"+sumstr[0]+"-"+sumstr[sumstr.length-2]+"_"+sumstr[sumstr.length-1];
				} else {
					
					if (resultstatus.equalsIgnoreCase("passed")) {
						bcolor = BaseColor.GREEN;
					} else {
						bcolor = BaseColor.WHITE;
					}
					
					strtext=Integer.toString(intserialno);
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
		  			table.addCell(tcell);
		  			
		  			//shahed
					strtext=FlowName;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
					//
		  
		  			strtext=strtestcasename;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
					
					execEnvironment=strtestcasename.substring(0,strtestcasename.indexOf("_Iteration"));
			        execEnvironment=execEnvironment.substring(execEnvironment.lastIndexOf("-")+1);
					strtext=execEnvironment;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		         	
					strtext=execPlatform;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		  
		         	strtext=Integer.toString(inttestcount);
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		         
					strtext=Integer.toString(inttestpassed);
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		         
					strtext=Integer.toString(inttestfailed);
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		         				 
					strtext=starttime;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		        				 
					strtext=endtime;
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
					celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
		         
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

					Date d1 = null;
					Date d2 = null;

					d1 = format.parse(starttime);
					d2 = format.parse(endtime);

					//in milliseconds
					long diff = d2.getTime() - d1.getTime();

					long diffSeconds = diff / 1000 % 60;
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);

					System.out.print(diffDays + " days, ");
					System.out.print(diffHours + " hours, ");
					System.out.print(diffMinutes + " minutes, ");
					System.out.print(diffSeconds + " seconds.");
					String Timetaken = "";
					if (diffDays > 0){
						Timetaken = diffDays +" days, "+ diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
					} else {
						if (diffHours > 0){
							Timetaken =  diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
						} else {
							Timetaken = diffMinutes+" mins, "+diffSeconds+" secs";
						}
					}
											     
			        strtext=Timetaken;
			        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			        celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
			        tcell = new PdfPCell(celltext);
			        table.addCell(tcell);
			         
			    	strtext=resultstatus;
				    celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
				    celltext.setAlignment(Element.ALIGN_CENTER);
			        celltext.setAlignment(Element.ALIGN_MIDDLE);
				    tcell = new PdfPCell(celltext);
			        tcell.setBackgroundColor(bcolor);
			        table.addCell(tcell);
			         
					starttime=sumstr[6];
					intserialno=intserialno+1;
				
					inttestpassed=0;
					inttestfailed=0;
					inttestcount=0;
					starttime=sumstr[6];
					
					inttestcount = inttestcount + 1;
					
					if (!strtestcasename.contains(sumstr[1]+"-"+sumstr[0])) {
						if(resultstatus.equalsIgnoreCase("FAILED")){
							allfailedtestcase = allfailedtestcase +1;
						}
						if(resultstatus.equalsIgnoreCase("PASSED")){
							allpassedtestcase = allpassedtestcase +1;
						}
					}
			
					starttime=sumstr[6];
					if (sumstr[5].equalsIgnoreCase("PASSED")) {
						inttestpassed = inttestpassed+1;
						allpassedstep=allpassedstep+1;
					} else {
						inttestfailed = inttestfailed+1;
						allfailedstep = allfailedstep +1;
					}
					allteststep = allteststep +1;
					if (!strtestcasename.contains(sumstr[1]+"-"+sumstr[0])) 
						testcasecount=testcasecount+1;
					
					strtestcasename = sumstr[1]+"-"+sumstr[0]+"-"+sumstr[sumstr.length-2]+"_"+sumstr[sumstr.length-1];
					resultstatus="PASSED";
				}
				
				sumline = sumbuff.readLine();
			}
			
			//overallendtime=sumstr[6];
			
			if (resultstatus.equalsIgnoreCase("passed")) {
				bcolor = BaseColor.GREEN;
			} else {
				bcolor = BaseColor.WHITE;
			}
			
			strtext=Integer.toString(intserialno);
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
  			table.addCell(tcell);
  			
  			//shahed
			strtext=FlowName;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
			//
  
  			strtext=strtestcasename;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
			
			execEnvironment=strtestcasename.substring(0,strtestcasename.indexOf("_Iteration"));
	        execEnvironment=execEnvironment.substring(execEnvironment.lastIndexOf("-")+1);
			strtext=execEnvironment;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
         	
			strtext=execPlatform;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
  
         	strtext=Integer.toString(inttestcount);
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
         
			strtext=Integer.toString(inttestpassed);
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
         
			strtext=Integer.toString(inttestfailed);
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
         				 
			strtext=starttime;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
        				 
			strtext=endtime;
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			table.addCell(tcell);
         
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			Date d1 = null;
			Date d2 = null;

			d1 = format.parse(starttime);
			d2 = format.parse(endtime);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			String Timetaken = "";
			if (diffDays > 0){
				Timetaken = diffDays +" days, "+ diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
			} else {
				if (diffHours > 0){
					Timetaken =  diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
				} else {
					Timetaken = diffMinutes+" mins, "+diffSeconds+" secs";
				}
			}
									     
	        strtext=Timetaken;
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        table.addCell(tcell);
	         
	    	strtext=resultstatus;
		    celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
		    celltext.setAlignment(Element.ALIGN_CENTER);
	        celltext.setAlignment(Element.ALIGN_MIDDLE);
		    tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        table.addCell(tcell);
		         
			if(resultstatus.equalsIgnoreCase("FAILED")){
				allfailedtestcase = allfailedtestcase +1;
			}
			
			if(resultstatus.equalsIgnoreCase("PASSED")){
				allpassedtestcase = allpassedtestcase +1;
			}
			
			PdfPTable tableoverall = new PdfPTable(2); // 3 columns.
			tableoverall.setWidthPercentage(50); //Width 100%
			tableoverall.setSpacingBefore(12f); 
			tableoverall.setHorizontalAlignment(0);

			tableoverall.setSpacingAfter(20f); 
		    float[] columnWidthsoversum = {5f,5f};
	      
		    tableoverall.setWidths(columnWidthsoversum);
	         
	        int intfont = 6;
	        bcolor = BaseColor.WHITE;
			
	        sReportJSON = "{";
	        	        
	        strtext="Total Test Cases";
	        sReportJSON = sReportJSON + " \"TotalTestScripts\": ";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	        strtext=Integer.toString(testcasecount);
	        sReportJSON = sReportJSON + strtext+",";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
			        			         
			strtext="Passed (%)";
			sReportJSON = sReportJSON + "\"Passed\": ";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	     	allteststep=allteststep-1;
			float testcaseperc = (float)((allpassedtestcase*100.0f)/testcasecount);
			testcaseperc = BigDecimal.valueOf(testcaseperc).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			float testcasefailperc = (float)((allfailedtestcase*100.0f)/testcasecount);
			testcasefailperc = BigDecimal.valueOf(testcasefailperc).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			System.out.println(allfailedstep+","+allpassedstep+","+allteststep);
			float teststepperc = (float)((allpassedstep*100.0f)/allteststep);
			teststepperc = BigDecimal.valueOf(teststepperc).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			float teststepfailperc = (float)((allfailedstep*100.0f)/allteststep);
			teststepfailperc = BigDecimal.valueOf(teststepfailperc).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				
	        strtext=Integer.toString(allpassedtestcase)+"  (" + String.valueOf(testcaseperc)+"%)";
	        sReportJSON = sReportJSON + Integer.toString(allpassedtestcase)+",";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	        
	        strtext="Failed (%)";
			sReportJSON = sReportJSON + "\"Failed\": ";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	        	         
	    	strtext=Integer.toString(allfailedtestcase)+ "  (" + String.valueOf(testcasefailperc)+"%)";
	    	sReportJSON = sReportJSON + Integer.toString(allfailedtestcase)+",";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	        
	        sReportJSON = sReportJSON + "\"PassRate\": ";
	        sReportJSON = sReportJSON +String.valueOf(testcaseperc)+"%,";
	                 
			strtext="Start time";
			sReportJSON = sReportJSON + "\"StartTime\": ";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	      	strtext=overallstarttime;
	      	sReportJSON = sReportJSON + "\""+strtext+"\",";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	        sReportJSON = sReportJSON + "\"Date\": " + strtext.split(" ")[0] + ",";
	        
	        
			strtext="End time";
			sReportJSON = sReportJSON + "\"EndTime\": ";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	         
	    	strtext=overallendtime;
	    	sReportJSON = sReportJSON + "\""+strtext+"\",";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	         
	        strtext="Execution Time";
	        sReportJSON = sReportJSON + "\"ExecutionTime\": ";
	        celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,intfont, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	        celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
	        tcell = new PdfPCell(celltext);
	        tcell.setBackgroundColor(bcolor);
	        tableoverall.addCell(tcell);
	
	        format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
			d1 = null;
			d2 = null;
	
				
			d1 = format.parse(overallstarttime);
			d2 = format.parse(overallendtime);
	
					//in milliseconds
			diff = d2.getTime() - d1.getTime();
	
			diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000) % 24;
			diffDays = diff / (24 * 60 * 60 * 1000);
	
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			String overallTimetaken = "";
			
			if (diffDays > 0){
				overallTimetaken = diffDays +" days, "+ diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
			} else {
				if (diffHours > 0){
					overallTimetaken =  diffHours +" Hours, "+diffMinutes+" mins, "+diffSeconds+" secs";
				} else {
					overallTimetaken = diffMinutes+" mins, "+diffSeconds+" secs";
				}
			}
		
			 strtext=overallTimetaken;
			 sReportJSON = sReportJSON + "\""+strtext.replaceAll(",", "")+"\",";
	         celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
	         celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
	         tcell = new PdfPCell(celltext);
	         tcell.setBackgroundColor(bcolor);
	         tableoverall.addCell(tcell);
			  	         
	         String[] arg = environmentParams;
	         sReportJSON = sReportJSON + "\"Module\": \"" + arg[0] +"\",";
	         sReportJSON = sReportJSON + "\"Environment\": \"" + arg[1] +"\",";
	         if(arg.length >= 3)
	        	 sReportJSON = sReportJSON + "\"Sub-Module\": \"" + arg[2].split(":")[1].replaceAll(",", "-") +"\",";
	         else
	        	 sReportJSON = sReportJSON + "\"Sub-Module\": \"ALL\",";
	         
	         String sPlatform = prop_ExecPlatform;
	         String sDevice = prop_DevList;
	         
	         if(sPlatform.toString().contains("desktop")){
	        	 sPlatform = "DESKTOP";
	        	 if(sDevice.toString().contains("mobile"))
	        		 sPlatform = "MOBILE";
	        	 else if(sDevice.toString().contains("tablet"))
	        		 sPlatform = "TABLET";
	         }
	         else if(sPlatform.toString().contains("appium")){
	        	 if(sDevice.toString().contains("smartphone"))
	        		 sPlatform = "MOBILE";
	        	 else
	        		 sPlatform = "TABLET";
	         
	         }
	         
	         sReportJSON = sReportJSON + "\"Browser\": \"" + sDevice +"\",";
	         sReportJSON = sReportJSON + "\"Platform\": \"" + sPlatform +"\",";
	         
	      /*  String mr = putil.getExecutionEnvironmentProperty("MR");
	         String er = putil.getExecutionEnvironmentProperty("ER");
	         String ReleaseEnv = "";
	         if(mr.contains(arg[1]))
	        	 ReleaseEnv = mr.split("-")[1];
	         else if(er.contains(arg[1]))
	        	 ReleaseEnv = er.split("-")[1];
	         else*/
	        String	 ReleaseEnv = "PROD";
	         
	         
	         sReportJSON = sReportJSON + "\"Release\": \"XYZ\"";
	         sReportJSON = sReportJSON + "}";
	         
	         System.out.println(sReportJSON);
	         
	         title1 = new Paragraph(" ");
		       
	    	 document.add(title1);
	    	 document.add(title1);
			        	 
	    	 title1 = new Paragraph("Overall Execution Summary for '"+projSuite+"':\r\n"  ,FontFactory.getFont(FontFactory.HELVETICA,   9, Font.UNDERLINE, new CMYKColor(0, 255, 255,17)));
						
	    	 chapter1 = new Chapter(title1, 1);
	    	 title1.setAlignment(Element.ALIGN_CENTER);
	        		     
	    	 chapter1.setNumberDepth(0);
	    	 document.add(title1);
					
	    	 PdfContentByte over = writer.getDirectContent();
					           
        	 title1 = new Paragraph(" ",FontFactory.getFont(FontFactory.HELVETICA,   9, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
        	 document.add(title1);		
        	 document.add(title1);	
        	 document.add(title1);	
			 document.add(tableoverall);
			       
			 for (int i=0;i < 15;i++){
	        	  document.add(title1);
	         }
			       
			 cb = writer.getDirectContent();
			
			 float width = PageSize.A4.getWidth()/2;
			 float height = PageSize.A4.getHeight() / 4;
			
			 // Pie chart
			 PdfTemplate pie = cb.createTemplate(width, height);
			 Graphics2D g2d1 = new PdfGraphics2D(pie, width, height);
			 Rectangle2D r2d1 = new Rectangle2D.Double(0, 0, width, height);
			
		     DefaultPieDataset dataset = new DefaultPieDataset();
		     dataset.setValue("Passed" , testcaseperc);
		     dataset.setValue("Failed", testcasefailperc);
		     Color pircolor = new Color(200,0,0);
		     Color picolor = new Color(0,150,0);
		     JFreeChart chartpie =  ChartFactory.createPieChart3D("", dataset, true, true, false);
		     PiePlot plot = (PiePlot) chartpie.getPlot();
		         
		     plot.setBackgroundPaint(Color.WHITE);
		     plot.setSectionOutlinesVisible(true);
				      
		     java.awt.Font font = new java.awt.Font("HELVETICA.Bold", java.awt.Font.PLAIN, 7);
		     
		     TextTitle legendText = new TextTitle("Failed TestCase(s) - "+allfailedtestcase + " ("+testcasefailperc +"%)");
		     legendText.setFont(font);
		     legendText.setHorizontalAlignment(HorizontalAlignment.LEFT);
		       
		     legendText.setPosition(RectangleEdge.BOTTOM);
		     legendText.setPaint(pircolor);
		     chartpie.addSubtitle(1,legendText);
		        
		     legendText = new TextTitle("Passed TestCase(s) - "+allpassedtestcase + " ("+testcaseperc +"%)");
		     legendText.setPosition(RectangleEdge.BOTTOM);
		       
		     legendText.setPaint(picolor);
		       
		     legendText.setHorizontalAlignment(HorizontalAlignment.LEFT);
				        
			 legendText.setFont(font);
			 font = new java.awt.Font("HELVETICA.Bold", java.awt.Font.PLAIN,6);
			 plot.setLabelFont(font);
				   
			 plot.setBackgroundAlpha(0.3f);
			 plot.setOutlineVisible(true);
			 font = new java.awt.Font("HELVETICA.Bold", java.awt.Font.PLAIN,7);
			 chartpie.getLegend().setPosition(RectangleEdge.BOTTOM);
			 chartpie.getLegend().setItemFont(font);
			 
			 chartpie.addSubtitle(2,legendText);
			 chartpie.draw(g2d1, r2d1);
			 g2d1.dispose();
			 cb.addTemplate(pie, 300, 430);
				        
			 title1 = new Paragraph("Automation Execution Summary for '"+projSuite+"':\r\n"  ,FontFactory.getFont(FontFactory.HELVETICA,   9, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			 
			 chapter1 = new Chapter(title1, 1);
			 title1.setAlignment(Element.ALIGN_LEFT);
			 
			 chapter1.setNumberDepth(0);
			 document.add(title1);
			 		        
			 document.add(table);
		
			 BufferedReader resbuff = new BufferedReader(new FileReader(resultsfile));
			 String line=null;
			
			 String strtestcase=null;
			 document.setPageSize(PageSize.A4);
			 document.newPage();
			 line = resbuff.readLine();
			 String [] strline = line.split(";;");
					
			 String strtestcaseresult = resultspdf.replace("results.pdf", strline[1]+"_"+strline[0]+"_"+strline[strline.length-2]+"_"+strline[strline.length-1]+"_results.pdf");
			 tcdocument = new Document();
			 PdfWriter writer1 = PdfWriter.getInstance(tcdocument, new FileOutputStream(strtestcaseresult));
			 tcdocument.open();
			 tcdocument.setPageSize(PageSize.A4);
				
			 image1 = Image.getInstance(currentDir+"/environment/Verizonlogo.png");
				  
			 image1.scaleAbsolute(50, 50);
			 tcdocument.add(image1);
			 title1 = new Paragraph(projName, FontFactory.getFont(FontFactory.HELVETICA,   18, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			 chapter1 = new Chapter(title1, 1);
				        		      
			 chapter1.setNumberDepth(0);
			 title1.setAlignment(Element.ALIGN_CENTER);
			 tcdocument.add(title1);
				         
			 title1 = new Paragraph("Detailed steps for all the test cases  " , FontFactory.getFont(FontFactory.HELVETICA,   9, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			 title1.setAlignment(Element.ALIGN_LEFT);
			 chapter1 = new Chapter(title1, 1);
				        		      
			 chapter1.setNumberDepth(0);
			 tcdocument.add(title1);
				        
			 execPlatform = strline[strline.length-3];
			 execEnvironment = strline[strline.length-2];
				        
			 title1 = new Paragraph("TestCase Name : "+strline[1]+"_"+strline[0] + "                 Environment Region : "+execEnvironment + "                 Device Id : "+strline[0] + "                 OS : " + execPlatform + "\r\n" , FontFactory.getFont(FontFactory.HELVETICA,   7, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			 title1.setAlignment(Element.ALIGN_LEFT);
			 chapter1 = new Chapter(title1, 1);
					        		      
			 chapter1.setNumberDepth(0);
			 document.add(title1);
			 tcdocument.add(title1);
					       
			 table = new PdfPTable(6); 
			 table.setWidthPercentage(100); 
			 table.setSpacingBefore(2f); 
			 table.setSpacingAfter(10f); 
			 float[] columnWidthtcdetails = {2f,10f,10f,3f,5f,5f};
					      
			 table.setWidths(columnWidthtcdetails);
			 bcolor = BaseColor.WHITE;
			 strtext="Step No";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
			 
			 strtext="Step Name";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
			 
			 strtext="Step Description";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
					         
			 strtext="Result";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
			 
			 strtext="Action Time";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
			 
			 strtext="Screenshots";
			 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			 celltext.setAlignment(Element.ALIGN_CENTER);
		     celltext.setAlignment(Element.ALIGN_MIDDLE);
			 tcell = new PdfPCell(celltext);
			 tcell.setBackgroundColor(bcolor);
			 table.addCell(tcell);
					   	 
				         
			 int intstart = 1;
			 int intstepno = 1;
			 strtestcase=strline[1]+"-"+strline[0]+"-"+strline[strline.length-2]+"_"+strline[strline.length-1];
			 while ( line != null) {
				 
				 strline = line.split(";;");
				 if (strtestcase.equalsIgnoreCase(strline[1]+"-"+strline[0]+"-"+strline[strline.length-2]+"_"+strline[strline.length-1])){
					 
					 if (strline.length > 5){
								
						 for (int i = 2;i < 7;i++){
							 if(strline[5].equalsIgnoreCase("failed")) {
								 bcolor = BaseColor.RED;
							 } else {
								 if(strline[8].equalsIgnoreCase("warning")){
									 bcolor = BaseColor.ORANGE;
								 } else{
									 bcolor = BaseColor.BLACK; 
								 }
							 }
									 
							 if(i == 2) {
								 strtext=Integer.toString(intstepno);
							 } else {
								 strtext=strline[i];
							 }
							 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, bcolor));
							 celltext.setAlignment(Element.ALIGN_CENTER);
						     celltext.setAlignment(Element.ALIGN_MIDDLE);
							 tcell = new PdfPCell(celltext);
							 table.addCell(tcell);
						 }

						 if (strline[5].equalsIgnoreCase("failed")){
							 try{
								 if (!strline[8].equalsIgnoreCase("null") && !strline[3].equalsIgnoreCase("execution completed")) {
									 image1 = Image.getInstance(strline[8]);
									 image1.scalePercent(15);
									 table.addCell(image1);
								 } else {
									 table.addCell("");
								 }
							 }
							 catch (Exception e){
								 table.addCell("");
							 }
						 } else {
							// 10/10/2016 : Change to take screenshot for pass step as well
							 if(screen.equalsIgnoreCase("true")) {
								 try{
									 if (!strline[8].equalsIgnoreCase("null") && !strline[3].equalsIgnoreCase("execution completed")) {
										 image1 = Image.getInstance(strline[8]);
										 image1.scalePercent(15);
										 table.addCell(image1);
									 } else {
										 table.addCell("");
									 }
								 }catch(Exception e){
									 table.addCell("");
								 }
							 } else {
								 table.addCell("");
							 }
						 }
						 intstepno++;
						 intstart=0;
					 }
					 strtestcase=strline[1]+"-"+strline[0]+"-"+strline[strline.length-2]+"_"+strline[strline.length-1];
				 } else {
					 if(intstart == 0) {		
						 String stra="a";
						 document.add(table);
						 tcdocument.add(table);
						 
						 table = new PdfPTable(6); 
						 PdfPTable tabletc = table;
						 table.setWidthPercentage(100);
						 table.setSpacingBefore(2f); 
						 table.setSpacingAfter(10f); 
						 float[] columnWidthtcdetails1 = {2f,10f,10f,3f,5f,5f};
						 
						 table.setWidths(columnWidthtcdetails1);
						 intstepno=1;
							        
						 document.newPage();
						 tcdocument.close();
						 writer1.close();
						 File source = new File(strtestcaseresult);
						 
						 resultsfolder = PDF_REPORTS_PATH + "/" + strtestcase +".pdf";
							      
						 File dest = new File(resultsfolder);
						 FileUtils.copyFile(source, dest);
						 
						 strtestcaseresult = resultspdf.replace("results.pdf", strline[1]+"_"+strline[0]+"_"+strline[strline.length-2]+"_"+strline[strline.length-1]+"_results.pdf");
						 tcdocument = new Document();
						 writer1 = PdfWriter.getInstance(tcdocument, new FileOutputStream(strtestcaseresult));
						 tcdocument.open();
						 tcdocument.setPageSize(PageSize.A4);
						 
						 image1 = Image.getInstance(currentDir+"/environment/Verizonlogo.png");
								  
						 image1.scaleAbsolute(50, 50);
						 tcdocument.add(image1);
						 title1 = new Paragraph(projName, FontFactory.getFont(FontFactory.HELVETICA,   18, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
						 chapter1 = new Chapter(title1, 1);
								        		      
						 chapter1.setNumberDepth(0);
						 title1.setAlignment(Element.ALIGN_CENTER);
						 tcdocument.add(title1);
						 		         
						 title1 = new Paragraph("Detailed steps for test case :\r\n " , FontFactory.getFont(FontFactory.HELVETICA,   9, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
						 title1.setAlignment(Element.ALIGN_LEFT);
						 chapter1 = new Chapter(title1, 1);
								        		      
						 chapter1.setNumberDepth(0);
						 tcdocument.add(title1);
							        
						 title1 = new Paragraph("TestCase Name : "+strline[1]+"_"+strline[0] + "                 Environment Region : "+execEnvironment + "                 Device Id : "+strline[0] + "                 OS : " + execPlatform + "\r\n", FontFactory.getFont(FontFactory.HELVETICA,   7, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));

						 chapter1 = new Chapter(title1, 1);
								        		      
						 chapter1.setNumberDepth(0);
						 bcolor=BaseColor.WHITE;
						 document.add(title1);
						 tcdocument.add(title1);
								        
						 strtext="Step No";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
								         
						 strtext="Step Name";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
								         
						 strtext="Step Description";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
								         
						 strtext="Result";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
								         
						 strtext="Action Time";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
						 
								         
						 strtext="Screenshots";
						 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
						 celltext.setAlignment(Element.ALIGN_CENTER);
					     celltext.setAlignment(Element.ALIGN_MIDDLE);
						 tcell = new PdfPCell(celltext);
						 tcell.setBackgroundColor(bcolor);
						 table.addCell(tcell);
								   
						 for (int i = 2;i < 7;i++){
							 if(strline[5].equalsIgnoreCase("failed")){
								 bcolor = BaseColor.RED;
							 } else {
								 if(strline[8].equalsIgnoreCase("warning")){
									 bcolor = BaseColor.ORANGE;
								 }
								 else{
									 bcolor = BaseColor.BLACK; 
								 }
							 }
							 if(i == 2){
								 strtext=Integer.toString(intstepno);
							 } else {
								 strtext=strline[i];
							 }
							 celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, bcolor));
							 tcell = new PdfPCell(celltext);
							 table.addCell(tcell);
						 }
						 if (strline[5].equalsIgnoreCase("failed")){
							 try{
								 if (!strline[8].equalsIgnoreCase("null") && !strline[3].equalsIgnoreCase("execution completed")){
									 image1 = Image.getInstance(strline[8]);
									 image1.scalePercent(15);
									 table.addCell(image1);
								 } else {
									 table.addCell("");
								 }
							 }catch (Exception e){
								 table.addCell("");
							 }

						 } else{
							 // 10/10/2016 : Change to take screenshot for pass step as well
							 if(screen.equalsIgnoreCase("true")){
								 try{
									 if (!strline[8].equalsIgnoreCase("null") && !strline[3].equalsIgnoreCase("execution completed")) {
										 image1 = Image.getInstance(strline[8]);
										 image1.scalePercent(15);
										 table.addCell(image1);
									 } else {
										 table.addCell("");
									 }
								 }
								 catch (Exception e){
									 table.addCell("");
								 }
							 } else
								 table.addCell("");
						 }
						 intstepno++;
					 }
				 }
				 
				 if (strline.length > 5){
					 strtestcase=strline[1]+"-"+strline[0]+"-"+strline[strline.length-2]+"_"+strline[strline.length-1];
				 }
				 
				 line = resbuff.readLine();
				 if(line != null){
					 line = line.replace(PDF_REPORTS_PATH+"/screenshots", "screenshots" );
				 }
			 }
			 tcdocument.add(table);
			 tcdocument.close();
			 writer1.close();
			 File source = new File(strtestcaseresult);
			 resultsfolder = PDF_REPORTS_PATH + "/";
			 File dest = new File(resultsfolder + strtestcase +".pdf");
			 FileUtils.copyFile(source, dest);
			 
			 document.add(table);
			 document.close();
			 writer.close();
			 source = new File(strtestcaseresult);
			 resultsfolder = PDF_REPORTS_PATH + "/";
			 dest = new File(resultsfolder + strtestcase +".pdf");
			 FileUtils.copyFile(source, dest);
			 source = new File(resultspdf);
			 
			 resultsfolder = PDF_REPORTS_PATH + "/";
			 dest = new File(resultsfolder + "results.pdf");
			 FileUtils.copyFile(source, dest);

			 dest = new File(resultsfolder + "results.txt");
			 FileUtils.copyFile(tfile, dest);
			 logger.info("JSON File Content --> " + sReportJSON);
			 logger.info("Reports Generated Successfully");
			 return sReportJSON;
			 
		} catch (IOException x) {
			
			logger.info(x.getMessage());
			logger.info("generatepdfreport() :: Error in generating PDF reports!");
			return sReportJSON;
		}
					
	}


	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate PDF report files
	 * @param 
	 */
	
	

	public void generateTestReportPDF(String browser, String methodName) throws IOException, DocumentException, ParseException {
		
		logger.info("generating reports for Test - '" + methodName + "'");
		String modName=null;
		try {
			String currentDir = PM_BUILD_WORKSPACE;
			String projName = prop_ProjName;
			logger.info("Project Name : " + projName);
			String projSuite = prop_ProjSuite;
			logger.info("Project Suite Name : " + projSuite);
			File tfilea = new File(currentDir+"/environment/report.ini");
			String screen = prop_Screenshot;
			if (screen==null) {
				screen="false";
			}
			BufferedReader buffile = new BufferedReader(new FileReader(tfilea));
			File resultsfile=null;
			String sumline1 = buffile.readLine();
			String resultsfolder = sumline1;
			modName = flowMetMap.get(methodName);
			File repfile = new File(resultsfolder+"/"+modName);
			browser=browser.replace(".", "-").replace(":", "-");
			for (File fname:repfile.listFiles()) {
				String tempName=browser+"-"+methodName+"-results.txt";
				if (fname.getName().equalsIgnoreCase(tempName)) {
					resultsfile=fname;
					break;
				}
			}
			
			BufferedReader resbuff = new BufferedReader(new FileReader(resultsfile));
			String line=null;
			
			String strtestcase=null;
			line = resbuff.readLine();
			String [] strline = line.split(";;");
					
			String strtestcaseresult = strline[1]+"_"+strline[0]+"_"+strline[strline.length-2]+"_"+strline[strline.length-1]+"_results.pdf";
			Document tcdocument = new Document();
			String tempPath=REPORTS_PATH+"/TempResults/"+modName;
			File newTemp = new File(tempPath);
			if (!newTemp.exists()) newTemp.mkdirs();
			String tempFilePath = tempPath+"/"+strtestcaseresult; 
			logger.info("Temporary File Path : " + tempFilePath);
			
			PdfWriter writer1 = PdfWriter.getInstance(tcdocument, new FileOutputStream(tempFilePath));
			tcdocument.open();
			tcdocument.setPageSize(PageSize.A4);
			
		    Image image1 = Image.getInstance(currentDir+"/environment/Verizonlogo.png");
			image1.scaleAbsolute(50, 50);
			tcdocument.add(image1);
			Paragraph title1 = new Paragraph(prop_ProjName, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			Chapter chapter1 = new Chapter(title1, 1);
				        		      
			chapter1.setNumberDepth(0);
			title1.setAlignment(Element.ALIGN_CENTER);
			tcdocument.add(title1);
				         
			title1 = new Paragraph("Detailed steps for all the test cases  " , FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			title1.setAlignment(Element.ALIGN_LEFT);
			chapter1 = new Chapter(title1, 1);
				        		      
			chapter1.setNumberDepth(0);
			tcdocument.add(title1);
				        
			//execPlatform = strline[strline.length-1];
			String execPlatform = strline[strline.length-3];
			String execEnvironment = strline[strline.length-2];
				        
			title1 = new Paragraph("TestCase Name : "+strline[1]+"_"+strline[0] + "                 Environment Region : "+execEnvironment + "                 Device Id : "+strline[0] + "                 OS : " + execPlatform + "\r\n" , FontFactory.getFont(FontFactory.HELVETICA,   7, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));
			title1.setAlignment(Element.ALIGN_LEFT);
			chapter1 = new Chapter(title1, 1);
					        		      
			chapter1.setNumberDepth(0);
			tcdocument.add(title1);
					       
			PdfPTable table = new PdfPTable(6); 
			table.setWidthPercentage(100); 
			table.setSpacingBefore(2f); 
			table.setSpacingAfter(10f); 
			float[] columnWidthtcdetails = {2f,10f,10f,3f,5f,5f};
					      
			table.setWidths(columnWidthtcdetails);
			BaseColor bcolor = BaseColor.WHITE;
			String strtext="Step No";
			Paragraph celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
		    PdfPCell tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
			 
			strtext="Step Name";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
			 
			strtext="Step Description";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
					         
			strtext="Result";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
			 
			strtext="Action Time";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
			 
			strtext="Screenshots";
			celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.BOLDITALIC, new CMYKColor(0, 0, 0,250)));
			celltext.setAlignment(Element.ALIGN_CENTER);
		    celltext.setAlignment(Element.ALIGN_MIDDLE);
			tcell = new PdfPCell(celltext);
			tcell.setBackgroundColor(bcolor);
			table.addCell(tcell);
			
			int intstepno = 1;
			strtestcase=strline[1]+"-"+strline[0]+"-"+strline[strline.length-2]+"_"+strline[strline.length-1];
			while (line!= null) {
				strline = line.split(";;");
				for (int i=2; i<7; i++) {
					if(strline[5].equalsIgnoreCase("failed")){
						bcolor = BaseColor.RED;
					} else {
						bcolor = BaseColor.BLACK;
					}
					
					if (i==2) {
						strtext=Integer.toString(intstepno);
					} else {
						strtext=strline[i];
					}
					
					celltext = new Paragraph(strtext, FontFactory.getFont(FontFactory.HELVETICA,6, Font.NORMAL, bcolor));
					celltext.setAlignment(Element.ALIGN_CENTER);
				    celltext.setAlignment(Element.ALIGN_MIDDLE);
					tcell = new PdfPCell(celltext);
					table.addCell(tcell);
				}
				
				if (strline[5].equalsIgnoreCase("failed")){
					try{
						if (!strline[8].equalsIgnoreCase("null") && !strline[3].trim().equalsIgnoreCase("execution completed")) {
							image1 = Image.getInstance(strline[8].trim());
							image1.scalePercent(15);
							table.addCell(image1);
						} else {
							table.addCell("");
						}
					}
					catch(Exception e){
						table.addCell("");
					}
				} else {
					// 10/10/2016 : Change to take screenshot for pass step as well
					if(screen.equalsIgnoreCase("true")){
						try {
							if (!strline[8].equalsIgnoreCase("null") && !strline[3].trim().equalsIgnoreCase("execution completed")) {
								image1 = Image.getInstance(strline[8].trim());
								image1.scalePercent(15);
								table.addCell(image1);
							} else 
								table.addCell("");
						} catch (Exception e) {
							table.addCell("");
						}
					} 
					else {
						table.addCell("");
					}
				}
				intstepno++;
				line = resbuff.readLine();
			}
			tcdocument.add(table);
			tcdocument.close();
			writer1.close();
		} catch (Exception e) {
			logger.info("generateTestReportPDF() :: Error in generating report for the test case - '" + methodName + "'");
			logger.info("generateTestReportPDF() :: Error --- " + e.getMessage());
			e.printStackTrace();
		}
	}
		
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate PDF report files
	 * @param 
	 */
	
	public void createtestreportfile(ITestContext context, String testName) {
		
		try {
			FileWriter fWrite=null;
			PrintWriter pWrite=null;
			String devType = context.getCurrentXmlTest().getParameter("device_host");
						
			devType=StringUtils.capitalize(devType);
			devType=devType.replace(".", "-").replace(":", "-");
			String fName = devType + "-" + testName;
			File tresultsfile = new File(resultsfolder +"/"+fName+"-results.txt");
			
			if(!tresultsfile.exists()) {
				if (testName!=null)
					tresultsfile.createNewFile();
			}
			
			String currentDir = PM_BUILD_WORKSPACE;
			String content = new Scanner(new File(currentDir + "/environment/report.ini")).useDelimiter("\\Z").next();
			fWrite = new FileWriter(currentDir + "/environment/report.ini",true);
			pWrite = new PrintWriter(fWrite);
			if (!content.contains(tresultsfile.toString()))
				pWrite.println(tresultsfile.toString());
			pWrite.close();
			fWrite.close();
			
		} catch (Exception e) {
			logger.info("Exception in creating test result report file for the test - '" + testName + "' : " + e.getMessage());
		}
	}
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to generate PDF report files
	 * @param 
	 */
	
	public void copyLogFiles() {
		try {
			File f1=new File(PM_BUILD_WORKSPACE+ "/logs");
			for (File f:f1.listFiles()) {
				if (f.getName().endsWith(".out")) {
					f1=f;
					break;
				}
			}
			File f2=new File(PDF_REPORTS_PATH+ "/executionLogs.out");
			FileUtils.copyFile(f1, f2);
		} catch (Exception e) {
			logger.info("Error in copying logfile - " + e.getMessage());
		}
	}
	
	
}
