package com.verizon.automation.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.verizon.automation.configuration.SuiteSetup;

@SuppressWarnings("all")
public class XmlParser extends SuiteSetup {
	
	String OR_PATH=null;
	public RemoteWebDriver xmldriver=null;
	
	public XmlParser (RemoteWebDriver driver) {
		this.xmldriver = driver;
	}
	
	
public String getXpath(String OStype, String property, String pageName) {
		
		String returnval = "";
		String plat=null;
		String appType=null;
		String testclass=null;
		String plat_OS = null;
		
		try {
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			
			try {
				String bName = xmldriver.getCapabilities().getCapability("browserName").toString();
				appType="browser";
			} catch (Exception e) {
				appType="native";
			}
			
			OR_PATH = OBJECTREPOSITORY_PATH + "/" + pageName + ".xml";
			
			File xmlDocument = new File(OR_PATH);
			InputSource inputSource = null;
		
			inputSource = null;
			inputSource = new InputSource(new FileInputStream(xmlDocument));
			
			if (OStype.toLowerCase().contains("android") || OStype.toLowerCase().contains("ios")) {
				try {
					String orient = xmldriver.getCapabilities().getCapability("orientation").toString();
					if (orient!=null) {
					if (orient.equalsIgnoreCase("portrait"))
						plat = "Smartphone";
					else 
						plat = "Tablet";
					} else {
						plat="Smartphone";
					}
				}catch (Exception e) {
					plat="Smartphone";
				}
			} else {
				plat="Desktop";
			}
			
			if (appType.equalsIgnoreCase("browser")) {
				if (plat.equalsIgnoreCase("smartphone")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Smartphone", inputSource);
				}
				if (plat.equalsIgnoreCase("tablet")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Tablet", inputSource);
				}
				if (plat.equalsIgnoreCase("desktop")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Desktop", inputSource);
				}
			} else if (appType.equalsIgnoreCase("native")) {
				if (OStype.equalsIgnoreCase("android")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Android", inputSource);
				}
				if (OStype.equalsIgnoreCase("ios")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../iOS", inputSource);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnval.replace(" &amp; ", " & ");
	}
		

	public String getXpath(String OStype, String property) {
		
		String returnval = "";
		String plat=null;
		String appType=null;
		String testclass=null;
		String plat_OS = null;
		
		try {
			
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
						
			StackTraceElement[] text = Thread.currentThread().getStackTrace();
			for (int i=0; i<text.length; i++) {
				if (text[i].toString().toLowerCase().contains(".testcases")) {
					String Val = text[i].toString();
					testclass=Val.substring(Val.indexOf(".testcases")+11);
					testclass=testclass.substring(0,testclass.indexOf("."));
					break;
				}
			}
			
			try {
				String bName = xmldriver.getCapabilities().getCapability("browserName").toString();
				appType="browser";
			} catch (Exception e) {
				appType="native";
			}
						
			OR_PATH = OBJECTREPOSITORY_PATH + "_" + testclass + ".xml";
			
			File xmlDocument = new File(OR_PATH);
			InputSource inputSource = null;
		
			inputSource = null;
			inputSource = new InputSource(new FileInputStream(xmlDocument));
			
			if (OStype.toLowerCase().contains("android") || OStype.toLowerCase().contains("ios")) {
				try {
					String orient = xmldriver.getCapabilities().getCapability("orientation").toString();
					if (orient!=null) {
					if (orient.equalsIgnoreCase("portrait"))
						plat = "Smartphone";
					else 
						plat = "Tablet";
					} else {
						plat="Smartphone";
					}
				}catch (Exception e) {
					plat="Smartphone";
				}
			} else {
				plat="Desktop";
			}
			
						
			if (appType.equalsIgnoreCase("browser")) {
				if (plat.equalsIgnoreCase("smartphone")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Smartphone", inputSource);
				}
				if (plat.equalsIgnoreCase("tablet")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Tablet", inputSource);
				}
				if (plat.equalsIgnoreCase("desktop")) {
					
					
					//String deviceBrowser = putil.getTestEnvironmentProperty("device_list");
					if(DEVICE_BROWSER.equalsIgnoreCase("chrome-mobile"))
						returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Smartphone", inputSource);
					else
						returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Desktop", inputSource);
					
				}
			} else if (appType.equalsIgnoreCase("native")) {
				if (OStype.equalsIgnoreCase("android")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../Android", inputSource);
				}
				if (OStype.equalsIgnoreCase("ios")) {
					returnval = xPath.evaluate("//Object/Name[.='" + property + "']/../iOS", inputSource);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnval.replace(" &amp; ", " & ");
	}
		
	
	/**
	 * This will take in a string and allow you to run xpath commands against in useful for RAW XML
	 * @param source - The string to be used
	 * @param xPathExpression - The xpath expression to run
	 * @return - The xpath expression result
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public String returnXpathExpressionResult(String source, String xPathExpression) throws 
		ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		InputStream is = new ByteArrayInputStream(source.getBytes());
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = builderFactory.newDocumentBuilder();
	    Document document = builder.parse(is);
	    XPathFactory xPathFactory = XPathFactory.newInstance();
	    XPath xpath = xPathFactory.newXPath();
	    XPathExpression xPathExpr = xpath.compile(xPathExpression);
	    String result = xPathExpr.evaluate(document);
	    return result;
		

	}	
	
	
}
