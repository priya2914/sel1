package com.verizon.automation.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.verizon.automation.beans.PropertyBean;
import com.verizon.automation.constants.ApplicationConstants;
import com.verizon.automation.logger.SeleniumLogWrapper;

/*
 * CODE CHANGES HISTORY
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * 	DATE		AUTHOR				METHODS MODIFIED/ADDED				CODE CHANGES DESCRIPTION
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *  07/29/2015  Chandrasekar Murugesan      										Class initial creation. 	
 *  07/31/2015  Chandrasekar Murugesan			getProperties()						Method used to read property file
 *  07/31/2015  Chandrasekar Murugesan			getEnvironmentProperty()			Method used to read the environment property file
 *  07/31/2015  Chandrasekar Murugesan			setProperties()						Method used to set the values in property file
 *  08/13/2015  Chandrasekar Murugesan			appendProperty()					Method added to append property key - value	
 *  08/20/2015	Chandrasekar Murugesan			getDevKitProperty(), 
 *  								getEnvironmentProperty(), 
 *  								getAppInstallProperty 				Added logger messages
*/


/**
 * @date 07/29/2015
 * @author Chandrasekar Murugesan
 * @description Class to Read and Write property file
 *
 */

@SuppressWarnings("all")
public class PropertyUtility implements ApplicationConstants {

	private final static Logger logger = Logger.getLogger(PropertyUtility.class);
		
	/**
	 * @date 05/13/2016
	 * @author Chandrasekar Murugesan
	 * @description Method used to read property file 
	 * @param String as property name for which needs to return property value
	 */
	private String getTestLinkProperty(String propertyKey){
		
		logger.info("PropertyUtility :: getTestLinkProperty, invoked...");
		String propertyValue=null;
		
		try {
			
			InputStream input =  null;
			Properties prop = new Properties();
			input = new FileInputStream(TESTLINK_FNAME);
			prop.load(input);					
			propertyValue=prop.getProperty(propertyKey);
			logger.info("PropertyUtility :: getTestLinkProperty, propertyKey:"+propertyKey+", propertyValue:"+propertyValue);
		}catch (IOException e) {
		    	logger.error("PropertyUtility :: getTestLinkProperty, Exception:"+e);
		}

		return propertyValue;
	}
	
	
	/**
	 * @date 08/13/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to read property file from URL
	 * @param String as property name for which needs to return property value
	 */
	
	public String getDevKitProperty(String fURL, String propertyName){
		
		logger.info("PropertyUtility :: getDevKitProperty, invoked...");
		String propertyValue=null;
				
		try {
				URL url = new URL(fURL);
				InputStream in = url.openStream();
				Reader reader = new InputStreamReader(in, "UTF-8"); 
				Properties prop = new Properties();
				prop.load(reader);
				propertyValue=prop.getProperty(propertyName);
				reader.close();
		    }catch (IOException e) {
		    	logger.error("PropertyUtility :: getDevKitProperty, propertyName:"+propertyName+", Exception:"+e);
		   }
		return propertyValue;
	}
	
	/**
	 * @date 07/31/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to read the environment property file and get property value for property key passed
	 * @param String as propertyKey for which needs to return property value
	 * @return propertyValues as String
	 */
	
	public String getMobileEnvironmentProperty(String propertyKey)
	{
		logger.info("PropertyUtility :: getMobileEnvironmentProperty, invoked...");
		String propertyValue=null;
		
		try {
			
				InputStream input =  null;
				
				Properties prop = new Properties();
				input = new FileInputStream(CLOUD_PROP_FNAME);
				
				prop.load(input);					
				propertyValue=prop.getProperty(propertyKey);
			}catch (IOException e) {
		    	logger.error("PropertyUtility :: getMobileEnvironmentProperty, Exception:"+e);
		   }

		return propertyValue;
		
	}
	
	
	/**
	 * @date 07/31/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to read the environment property file and get property value for property key passed
	 * @param String as propertyKey for which needs to return property value
	 * @return propertyValues as String
	 */
	
	public String getTestEnvironmentProperty(String propertyKey) {
		
		String propertyValue=null;
		try {
			InputStream input =  null;
			Properties prop = new Properties();
			input = new FileInputStream(TEST_PROP_FNAME);
			prop.load(input);					
			propertyValue=prop.getProperty(propertyKey);
		} catch (IOException e) {
		   	propertyValue="";
		   	logger.info("Error in getting the property value for the key : '" + propertyKey + "'");
		}

		return propertyValue;
		
	}
	
	
	
	/**
	 * @date 09/16/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to read the environment property file and get property value for property key passed
	 * @param String as propertyKey for which needs to return property value
	 * @return propertyValues as String
	 */
	public String getApplicationProperty(String propertyKey)
	{
		String propertyValue=null;
		
		try {
			InputStream input =  null;
			
			Properties prop = new Properties();
			input = new FileInputStream(APP_INFO_FNAME);
			
			prop.load(input);					
			propertyValue=prop.getProperty(propertyKey);
		} catch (IOException e) {
		    	logger.error("PropertyUtility :: getApplicationProperty, Exception:"+e);
		}

		return propertyValue;
	}
	
	
	/**
	 * @date 08/13/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get a application install property value
	 * @param propFName String as the property file name
	 * @param propKey String as the property key name
	 * @param propValue String as property value for the key passed
	 * @return No return value
	 */
	
	public String getAppInstallProperty(String propertyKey)
	{
		logger.info("PropertyUtility :: getAppInstallProperty, invoked...");
		String propertyValue=null;
		
		try {
			
			InputStream input =  null;
			
			Properties prop = new Properties();
			input = new FileInputStream(APP_BUILD_INFO_FNAME);
			
			prop.load(input);					
			propertyValue=prop.getProperty(propertyKey);
		}catch (IOException e) {
		    	logger.error("PropertyUtility :: getAppInstallProperty, Exception:"+e);
		}

		return propertyValue;
	}
	
	/**
	 * @date 08/13/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the property value for the property kay passed
	 * @param propertyFileName String as the property file name
	 * @param propertyKey String as the property key
	 * @return propertyValues as String
	 */
	private String getProperty(String propertyFileName, String propertyKey)
	{
		logger.info("PropertyUtility :: getPropertyValue, invoked...");
		String propertyValue=null;
		
		try {
			
			InputStream input =  null;
			
			Properties prop = new Properties();
			input = new FileInputStream(propertyFileName);
			
			prop.load(input);					
			propertyValue=prop.getProperty(propertyKey);
		}catch (IOException e) {
	    	logger.error("PropertyUtility :: getPropertyValue, propertyFileName:"+propertyFileName+", propertyKey:"+propertyKey+", Exception:"+e);
		}

		return propertyValue;
	}	
	
	/**
	 * @date 08/13/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to set a property key to the property file
	 * @param propFName String as the property file name
	 * @param propKey String as the property key name
	 * @param propValue String as property value for the key passed
	 * @return No return value
	 */
	
	public void setProperties(String propFName, String propKey, String propValue){
		
		try{
			final File propsFile = new File(propFName);
			Properties properties = new Properties();
			properties.load(new FileInputStream(propsFile));
			properties.setProperty(propKey, propValue);
			properties.store(new FileOutputStream(propsFile), "Updated Property File");
		}catch (IOException e) {
			//System.out.println("PropertyUtility :: setProperties(),  Error, e:"+e);
		}
	}
	
	/**
	 * @date 08/13/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to append a property key to the property file
	 * @param propertyPath String as the property file path
	 * @param propertyFileName String as the property file name
	 * @param propertyKey String as the property key name
	 * @param propertyValue String as property value for the key passed
	 * @return No return value
	 */
	private void appendProperty(String propertyPath, String propertyFileName, String propertyKey, String propertyValue){
		
		try{
				
			Properties pop = new Properties();
			pop.load(new FileInputStream(propertyPath+"/"+propertyFileName));
			pop.put(propertyKey, propertyValue);
			FileOutputStream output = new FileOutputStream(propertyPath+"/"+propertyFileName);
			pop.store(output, "This is overwrite file");
		} catch (Exception e) {
			logger.info("PropertyUtility :: setProperties(), propertyFileName:"+propertyFileName+" Exception: "+e);
		}
		
	}	
	
	/**
	 * @date 08/14/2015
	 * @author Chandrasekar Murugesan
	 * @description appendDevKitPropertyList ethod added to read Dev Kit htmlUrl and append build info property List to property file
	 * @param htmlUrl
	 */
	private void appendDevKitPropertyList(String htmlUrl){
		
		logger.info("PropertyUtility :: appendDevKitPropertyList(), invoked...");
        String inputLine; 
        int rowIndex=0;
        PropertyBean bean = null;
        List<PropertyBean> propList = null;
        
        try {
        	
            URL htmlContent = new URL("http://mstg.verizon.com/mobileapps/ios/Thebe/auto_testing/PROD/build_info.prop");
    	    //URL htmlContent = new URL(htmlUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(htmlContent.openStream()));
            propList = new ArrayList<PropertyBean>();
            
            while ((inputLine = in.readLine()) != null) {
            	logger.info(inputLine);
            	logger.info("------------------------------");
            	logger.info("");
                // Process each line.
            	if(inputLine.contains("=")){
            		String[] tokens = inputLine.split("=");
            		
            		logger.info("tokens[0]:"+tokens[0]+", tokens[1]:"+tokens[1]);
            		bean = new PropertyBean();
            		bean.setPropertyKey(tokens[0]);
            		bean.setPropertyValue(tokens[1]);
            		propList.add(rowIndex,bean);
            		rowIndex++;
            	}
            }
	             
           displayPropertyList(propList);
           appendPropertyList(propList, PROPERTY_FILEPATH, ENVIRONMENT_FILENAME);
            
           //closing the InputStreamReader
           in.close();
 
        } catch (MalformedURLException me) {
            logger.error("PropertyUtility :: appendDevKitPropertyList(), MalformedURLException: "+me); 
 
        } catch (IOException ioe) {
            logger.error("PropertyUtility :: appendDevKitPropertyList(), IOException: "+ioe);
        } catch (Exception e) {
            logger.error("PropertyUtility :: appendDevKitPropertyList(), Exception: "+e);
        }finally{
        	inputLine=null;
        	bean=null;
        	propList = null;
        }
	}
	
	
	/**
	 * @date 08/14/2015
	 * @author Chandrasekar Murugesan
	 * @description Method to display the Property List
	 * @param propList as List to display
	 */
	private void displayPropertyList(List<PropertyBean>  propList){
		
		PropertyBean bean = null;
		logger.info("propList.size():"+propList.size());
		
		try{
			logger.info("");
			for(int i=0; i<=propList.size(); i++){
				bean = new PropertyBean();
				bean=propList.get(i);
				logger.info("propertyName: "+bean.getPropertyKey()+", PropertyValue:"+bean.getPropertyValue());
			}
			
		}catch(Exception e){
			logger.error("PropertyUtility :: displayPropertyList(), Exception: "+e);
		}
		
	}
	

	/**
	 * @date 08/14/2015
	 * @author Chandrasekar Murugesan
	 * @description Method to Insert Property List
	 * @param propList
	 */
	private void insertPropertyList(List<PropertyBean>  propList, String propertyPath, String propertyFileName){
		
		Properties pop = new Properties();
		
		PropertyBean bean = null;
		Iterator<PropertyBean> itr = propList.iterator(); 
		
		try{
			
			pop.load(new FileInputStream(propertyPath+"/"+propertyFileName));
			
			while(itr.hasNext()) {      
				bean = new PropertyBean();
				bean = itr.next();
				pop.put(bean.getPropertyKey(), bean.getPropertyValue());
			}
			
			FileOutputStream output = new FileOutputStream(propertyPath+"/"+propertyFileName);
			pop.store(output, "Property file created");
			
		}catch(Exception e){
			logger.error("PropertyUtility :: insertPropertyList(), Exception: "+e);
		}
		
	}	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description appendPropertyList Method to append Property List
	 * @param propList as List<PropertyBean>
	 * @param propertyPath as String
	 * @param propertyFileName as String
	 */
	private void appendPropertyList(List<PropertyBean>  propList, String propertyPath, String propertyFileName){
		
		Properties pop = new Properties();

		PropertyBean bean = null;
		
		try{			
			
			logger.info("appendPropertyList invoked... propList.size():"+propList.size());
			pop.load(new FileInputStream(propertyPath+"/"+propertyFileName));
			if(propList.size()>0){
				for(int i=0; i<=propList.size()-1; i++){
					bean = new PropertyBean();
					bean=propList.get(i);
					pop.put(bean.getPropertyKey(), bean.getPropertyValue());
				}
				
				FileOutputStream output = new FileOutputStream(propertyPath+"/"+propertyFileName);
				pop.store(output, "This is overwrite file");
			}
			
		}catch(Exception e){
			
			logger.error("PropertyUtility :: appendPropertyList(), Exception: "+e);;
		
		}
		
	}


	public String getExecutionEnvironmentProperty(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	

}