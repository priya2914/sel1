package com.verizon.automation.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.verizon.automation.configuration.SuiteSetup;
import com.verizon.automation.logger.SeleniumLogWrapper;

/**
 * @date 08/25/2015
 * @author Harish
 * @description Date Utility Class
 */

@SuppressWarnings("all")
public class DateUtility extends SuiteSetup {
	
	private final static Logger logger = Logger.getLogger(DateUtility.class);
	
	/**
	 * @date 08/24/2015
	 * @author Chandrasekar Murugesan
	 * @param dateFormat as String, example:MMddyyyy_hhhmmss 
	 * @return String as system date
	 */
	public String getSystemStringDate(String dateFormat){
		
		logger.debug("DateUtility :: getSystemStringDate() invoked... dateFormat:"+dateFormat);
	    Date date = new Date();
	    String str_date=null;
	    //String dateFormat="MMddyyyy_hhhmmss";
		try{
			
		    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    
		    str_date = sdf.format(date);
		    logger.debug("Today is " + str_date);
		    logger.debug("DateUtility :: getSystemStringDate(), str_date:"+str_date);
			
		}catch(Exception e){
			logger.error("DateUtility :: getSystemStringDate(), dateFormat:"+dateFormat+", Exception: "+e);
			logger.error("Please check the date format passed");
		}
		
		return str_date;
		
	}
	
	/**
	 * @date 08/24/2015
	 * @author: Chandrasekar Murugesan
	 * @description: This method is used to convert String to java Date
	 * @param strDate as String
	 * @param dateFormat as String
	 * @return java.util.Date
	 */
	public java.util.Date convertStringToDateFormat(String strDate, String dateFormat){
		
		DateFormat formatter = null;
		Date javaDate = null;
		
		try {  
			
			//formatter = new SimpleDateFormat("MMM-yyyy"); //MMddyyyy
			formatter = new SimpleDateFormat("dateFormat");
			
			javaDate = (Date)formatter.parse(strDate); 
			logger.debug(" Conversion: strDate:"+strDate+" Converted to DATE: " + javaDate);
			String formattedDate = formatter.format(javaDate);
			logger.debug("formattedDate ==>"+formattedDate);
			
		     	    
		} catch (ParseException e){
			logger.error("DateUtility :: convertStringToDateFormat(), Exception :"+e.getMessage());  
		} 
		
		return javaDate;
	}		
	
	

}
