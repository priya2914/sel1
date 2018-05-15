package com.verizon.automation.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.verizon.automation.configuration.SuiteSetup;

@SuppressWarnings("all")
public class MongoDBUtil extends SuiteSetup {
	
	PropertyUtility putil = new PropertyUtility();
	private final static Logger logger = Logger.getLogger(MongoDBUtil.class);
	
	public boolean insertDoc(Document doc){
		
		boolean flag = false;
		try {
			MongoClient  mongo = new MongoClient ("143.91.240.167", 27017);
			MongoDatabase db = mongo.getDatabase("local");
			MongoCollection<Document> table = db.getCollection("TestCollection");
			table.insertOne(doc);
			mongo.close();
			flag = true;
		} catch (Exception e){}
		return flag;
		
	}
	
	
	public  void writingStepResultsToMongo(HashMap<String, String> resultDetails,DB mongodb){
		try {
			
				String stepname = resultDetails.get("StepName");
				if(!stepname.equalsIgnoreCase("execution completed") ) {
					 DBCollection dbcollection = mongodb.getCollection("TestStepResults");
						BasicDBObject searchQuery = new BasicDBObject();
						BasicDBObject writedata = new BasicDBObject();
						searchQuery.put("RunID",  resultDetails.get("RunID"));
						searchQuery.put("TestFlowName", resultDetails.get("TestFlowName"));
					 	
						DBCursor cursor = dbcollection.find(searchQuery);
						cursor.size();
						int stepno = cursor.size()+1;
					writedata.put("Sub-Module", resultDetails.get("Sub-Module"));
					writedata.put("Env", resultDetails.get("Env"));
					writedata.put("TestFlowName", resultDetails.get("TestFlowName"));
					writedata.put("RunID", resultDetails.get("RunID"));
					writedata.put("Module", resultDetails.get("Module"));
					writedata.put("Browser", resultDetails.get("Browser"));
					writedata.put("Platform", resultDetails.get("Platform"));
					writedata.put("OS", resultDetails.get("OS"));
					writedata.put("StepNo", stepno);
					
					writedata.put("StepName", stepname);
					writedata.put("StepDescription", resultDetails.get("StepDescription"));
					writedata.put("ActionTime", resultDetails.get("ActionTime"));
				//	writedata.put("RunID",resultDetails.get("RunID"));
					String teststepresult=resultDetails.get("StepResult");
					String screenfile=resultDetails.get("Screenfile");
					writedata.put("StepResult", teststepresult);
					writedata.put("RunDate",resultDetails.get("RunDate"));
					writedata.put("MultiModule", resultDetails.get("MultiModule"));
					
					if(teststepresult.toLowerCase().equalsIgnoreCase("failed")) {
						if(!stepname.equalsIgnoreCase("execution completed") ) {
					  
						File imagefile = new File(resultDetails.get("Screenfile"));
						if(imagefile.exists()){
							GridFS gfsPhoto = new GridFS(mongodb, "TestStepResults");
							GridFSInputFile gfsFile;
							try {
								if(!stepname.equalsIgnoreCase("step failed")){
									String newFileName=resultDetails.get("RunID")+"-"+resultDetails.get("TestFlowName")+"_"+stepno;
									gfsFile = gfsPhoto.createFile(imagefile);
									gfsFile.setFilename(newFileName);
									gfsFile.save();
									writedata.put("ScreenShotFilenname", newFileName);
								}
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
							
						}
						
						}else {
							
						}
					}else {
						writedata.put("ScreenShotFilenname", "");
					}
					
					dbcollection.insert(writedata);
				dbcollection = null;
				searchQuery = null;
				writedata = null;
				cursor=null;
				}	
		}catch(Exception e){
			System.out.println("Unable to write mongodb data" + e.getMessage() +"-"+ resultDetails.toString());
		}
	   
	}
	
	
	public void uploadJSONtoMongo(String sReportJSON) {
		String modArg=null;
		try {
			logger.info("Inserting the reports to Mongo DB");
			String sUpdateResultsToMongoDB = putil.getTestEnvironmentProperty("UpdateResultsToMongoDB");
			if(sUpdateResultsToMongoDB.equalsIgnoreCase("true")) {
				Date d1 = new Date();
				Document doc = new Document();
				sReportJSON = sReportJSON.replace("{", "");
				sReportJSON = sReportJSON.replace("}", "");
				sReportJSON = sReportJSON.replaceAll("\"", "");
				String[] reportJSON = sReportJSON.split(",");
				for(int i=0; i<reportJSON.length;i++){
					String[] keyValue = reportJSON[i].split(":", 2);
					doc.append(keyValue[0].trim(), keyValue[1].trim());
				}
				insertDoc(doc);
				Date d2 = new Date();
				long diff = d2.getTime()-d1.getTime();
				
				logger.info("Reports insterted into Mongo DB");
			}
		} catch (Exception e) {
			logger.info("Error in uploading the JSON file to Mongo DB : " + e.getMessage());
		}
	}
	
	
	
}
