package com.verizon.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.verizon.automation.configuration.SuiteSetup;


@SuppressWarnings("all")

public class DataUtil extends SuiteSetup {
	
	private final static Logger logger = Logger.getLogger(DataUtil.class);
	private static int retryValue=0;
	private static String modList=null;
	private static String envList=null;
	private static String flowList=null;
	private static LinkedListMultimap<String, String> dataValues;
	public DataUtil() {}
	
	public DataUtil(int ret){
		retryValue=ret;
	}
		
	public int getRetryCount() {
		return retryValue;
	}
	
	
	public String getExecEnvironment() {
		return envList;
	}
	
	public String getExecModules() {
		return modList;
	}
		
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the column number of the header
	 * @param 
	 */
	
	public int getHeaderColumnIndex(org.apache.poi.ss.usermodel.Sheet sheet, String Name) {
		int colNum=0;
		try {
			Map<String, Integer> map = new HashMap<String,Integer>();
			Row row = sheet.getRow(0);
			int minColIx = row.getFirstCellNum();
			int maxColIx = row.getLastCellNum();
			for(int colIx=minColIx; colIx<maxColIx; colIx++) {
			   org.apache.poi.ss.usermodel.Cell cell = row.getCell(colIx);
			   String value = cell.getStringCellValue();
			   if (value.equalsIgnoreCase(Name)) {
				   colNum = colIx;
				   break;
			   }
			}
		} catch (Exception e) {
			
		}
		return colNum;
	}
	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the row number of a value
	 * @param 
	 */
	
	public static int findRow(org.apache.poi.ss.usermodel.Sheet sheet, String cellContent) {
	    for (Row row : sheet) {
	        for (org.apache.poi.ss.usermodel.Cell cell : row) {
	            if (cell.getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING) {
	                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
	                    return row.getRowNum();  
	                }
	            }
	        }
	    }               
	    return -1;
	}
	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the column number of a value
	 * @param 
	 */
	
	public static int findColumn(org.apache.poi.ss.usermodel.Sheet sheet, String cellContent) {
		int row = findRow(sheet,cellContent);
		Row r = sheet.getRow(row);
		for (int cn=0; cn<=r.getLastCellNum(); cn++) {
		   Cell cell = r.getCell(cn);
		   DataFormatter formatter = new DataFormatter();
		   String cellValue="";
		   if (cell!=null) {
	    		int cellStyle = cell.getCellType();
	    		if (cellStyle==Cell.CELL_TYPE_STRING) {
	    			cellValue = cell.getStringCellValue();
	    		} else if (cellStyle==Cell.CELL_TYPE_BOOLEAN) {
	    			cellValue = String.valueOf(formatter.formatCellValue(cell));
	    		} else if (cellStyle==Cell.CELL_TYPE_NUMERIC) {
	    			cellValue = String.valueOf(formatter.formatCellValue(cell));
	    		} else {
	    			cellValue = "";
	    		}
	    		if (cellValue.equalsIgnoreCase(cellContent)) {
	 			   return cn;
	 		   	}
	   		}
		}
		return -1;
	}
	
	
	/**
	 * @date 08/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the total number of test cases
	 * @param 
	 */
	
	public int getTotalTestMethods() {
		int totalCnt=0;
		int count=0;
		try {
			for(String mod : dataMap.keySet()){
				LinkedListMultimap<String, String> tempMapVal=dataMap.get(mod);
				Set headVal=tempMapVal.keySet();
				Iterator it=headVal.iterator();
				while (it.hasNext()){
					List<String> val = tempMapVal.get(it.next().toString());
					count=val.size();
					totalCnt+=count;
					break;
				}
			}
		} catch (Exception e) {
			util.reportLog("Error in getting the total iterations for test methods for execution");
		}
		return totalCnt;
	}
	
	    
    	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to select the modules for execution
	 * @param
	 * @Revised Author Chandrasekar Murugesan
	 * @Revised Date 09/24/2016
	 */
	
	public List selectExecutionModules(String modList) throws IOException {
		
		List<String> mods=new LinkedList<String>();
		Map<String, String> modVals=new HashMap<String,String>();
		Workbook wBook=null;
		FileInputStream inStream = null;
		boolean Yes = true;
		boolean No = false;
		try {
			String exPlat=putil.getTestEnvironmentProperty("execution_platform");
			inStream = new FileInputStream(TEST_DATA_FILEPATH + "/TestData.xls");
			wBook = new HSSFWorkbook(inStream);
			Sheet oSheet = wBook.getSheet("TestCaseDashboard");
			String[] apps=modList.split(",");
			for (int i=0; i<apps.length; i++) {
				modVals.put("application"+Integer.toString(i) ,apps[i].toLowerCase());
			}
			int tcCol=findColumn(oSheet, "TestCase_Name");
			int flgCol=findColumn(oSheet, "ExecuteTest");
			int pltCol=findColumn(oSheet, "TestPlatform");
			int rowCnt = oSheet.getLastRowNum();
			
			for (int i=1; i<=rowCnt; i++) {
				Cell tcName = oSheet.getRow(i).getCell(tcCol);
				Cell flag = oSheet.getRow(i).getCell(flgCol);
				Cell plat = oSheet.getRow(i).getCell(pltCol);
				String tcVal = getCellValue(wBook, oSheet, i, tcCol);
				if (modList.equalsIgnoreCase("all") || modList.isEmpty()) {
					flag.setCellValue(Yes);
					if (exPlat.toLowerCase().contains("appium") || exPlat.toLowerCase().contains("perfecto")) {
						plat.setCellValue("MDOT");
					} else {
						plat.setCellValue("Desktop");
					}
					mods.add(tcVal);
				} else {
					if (modVals.containsValue(tcVal.toLowerCase())) {
						flag.setCellValue(Yes);
						if (exPlat.toLowerCase().contains("appium") || exPlat.toLowerCase().contains("perfecto")) {
							plat.setCellValue("MDOT");
						} else {
							plat.setCellValue("Desktop");
						}
						mods.add(tcVal);
					} else {
						flag.setCellValue(No);
					}
				}
			}
			inStream.close();
			FileOutputStream outStream =new FileOutputStream(new File(TEST_DATA_FILEPATH + "/TestData.xls"));
			wBook.write(outStream);
			outStream.close();
			
		} catch (Exception e) {
			if (wBook!=null)
				wBook.close();
			logger.info("FrameworkUtil :: selectExecutionModules() -  Error in selecting the modules for execution ::" + e.getMessage());
		}
		return mods;
	}
	
	

	/**
	 * @date 08/08/2016
	 * @author Chala Panagala
	 * @description Method used to select the flows for the each selected module for execution
	 * @param 
	 */
	
	public void selectExecutionFlows(String flowList) throws IOException {
		
		String[] flows = flowList.split(";");
		List<String> PostPayFlows= new ArrayList<String>();
		List<String> PrePayFlows= new ArrayList<String>();
		List<String> ViewBillFlows= new ArrayList<String>();
		List<String> OpalFlows= new ArrayList<String>();
		
		for(String s : flows){
			
			if(s.contains("postpay")){
				
				String[] a = s.split(":")[1].split(",");
				for (String b : a)
					PostPayFlows.add(b);
				
				setFlowsToExecute("Postpay",PostPayFlows);
			}
			else if(s.toLowerCase().contains("prepay")){
				String[] a = s.split(":")[1].split(",");
				for (String b : a)
					PrePayFlows.add(b);
				
				setFlowsToExecute("Prepay",PrePayFlows);
			}
			else if(s.toLowerCase().contains("viewbill")){
				String[] a = s.split(":")[1].split(",");
				for (String b : a)
					ViewBillFlows.add(b);
				
				setFlowsToExecute("viewbill",ViewBillFlows);
			}
			else if(s.toLowerCase().contains("opal")){
				String[] a = s.split(":")[1].split(",");
				for (String b : a)
					OpalFlows.add(b);
				
				setFlowsToExecute("opal",OpalFlows);
			}
		}

	}
	
	
	
	/**
	 * @date 08/09/2016
	 * @author Chala Panagala
	 * @description Method used to select the flows for execution
	 * @param 
	 * @Revised Author Chandrasekar Murugesan
	 * @Revised Date 09/24/2016
	 */
	
	
	public void setFlowsToExecute(String ModName, List<String> Flows) throws IOException {
	
		Multimap<String, String> modVals=ArrayListMultimap.create();
		Workbook wBook=null;
		FileInputStream inStream = null;
		boolean Yes = true;
		boolean No = false;

		try {
			inStream = new FileInputStream(TEST_DATA_FILEPATH + "/"+ModName+".xls");
			wBook = new HSSFWorkbook(inStream);
			Sheet oSheet = wBook.getSheet("Driver");
			String[] apps=modList.split(",");
			for (String fl:Flows) {
				modVals.put("Flow",fl.toLowerCase());
			}
			int tcCol=findColumn(oSheet, "Flow_Name");
			int flgCol=findColumn(oSheet, "Run");
			int rowCnt = oSheet.getLastRowNum();
			
			for (int i=1; i<=rowCnt; i++) {
				Cell tcName = oSheet.getRow(i).getCell(tcCol);
				Cell flag = oSheet.getRow(i).getCell(flgCol);
				String tcVal = getCellValue(wBook, oSheet, i, tcCol);
				if (modVals.get("Flow").size()==1 && modVals.get("Flow").contains("all")) {
					flag.setCellValue(Yes);
				} else {
					if (modVals.containsValue(tcVal.toLowerCase())) {
						flag.setCellValue(Yes);
					} else {
						flag.setCellValue(No);
					}
				}
			}
			inStream.close();
			FileOutputStream outStream =new FileOutputStream(new File(TEST_DATA_FILEPATH + "/"+ModName+".xls"));
			wBook.write(outStream);
			outStream.close();
			
		} catch (Exception e) {
			inStream.close();
			wBook.close();
			logger.info("DataUtil :: setFlowsToExecute() -  Error in selecting the flows for execution");
		}
	}
	
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to select the environments for execution
	 * @param 
	 */
	
	public void setEnvironmentModules(String[] args) {
		int len=0;
		
		try {
			if (args.length>0) {
				environmentParams=args[0].split(";");
				String[] envInfo=args[0].split(";", 3);
				len=envInfo.length;
				
				// Chala 
				// Added logic to handle flow selection in the modules
				if(len>2){
					
					modList=envInfo[0];
					envList=envInfo[1];
					flowList=envInfo[2];
				}
				else if (len>1) {
					modList=envInfo[0];
					envList=envInfo[1];
				} else if (len>0) {
					modList=envInfo[0];
					envList="PROD";
				} else {
					modList="";
					envList="PROD";
				}
			} else {
				modList="";
				envList="PROD";
			}
			
			if (!envList.contains(",")) {
				execRegion=envList;
			}
			
			List<String> selMod=selectExecutionModules(modList);
			
			// Chala : Set Flows
			if(len>2)
				selectExecutionFlows(flowList);
			
		} catch (Exception e) {
			logger.info("Error in selecting the modules and environments for execution");
		}
	}
	
	
	
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to select the modules for execution
	 * @param
	 * @Revised Author Chandrasekar Murugesan
	 * @Revised Date 09/24/2016
	 */
	
    public LinkedListMultimap<String,String> getTestData(String fileName, String testName) throws IOException {
    	
		LinkedListMultimap<String, String> dataValue=LinkedListMultimap.create(1000);
		FileInputStream objFile = null;
		Workbook objWorkbook = null;
		Sheet objSheet = null;
		String execFlag = null;
		FileInputStream fileInputStream = null;
		try {
			int stRow=0;
			int stCol=0;
			int endRow=0;
			int endCol=0;
			int cj=0;
			int eTest=0;
			Cell tableStart=null;
			Cell tableEnd=null;
			
			String xlFilePath = TEST_DATA_FILEPATH + "/" + fileName + ".xls";
			fileInputStream = new FileInputStream(xlFilePath);
			
			if (xlFilePath.endsWith(".xlsx")) {
				objWorkbook = new XSSFWorkbook(fileInputStream);
			} else {
				objWorkbook = new HSSFWorkbook(fileInputStream);
			}
			
			List<String> sValues = (List) testMap.get(testName);
			for (String sVal:sValues) {
				objSheet = objWorkbook.getSheet(sVal);
				stRow = findRow(objSheet, testName);
				endRow = findRow(objSheet, testName+"_End");
				stCol = findColumn(objSheet, testName);
				endCol = findColumn(objSheet, testName+"_End");
				if (stRow>=0 && endRow>stRow) {
					for (int j=stRow;j<endRow;j++) {
						cj=0;
						eTest = getHeaderColumnIndex(objSheet, "Test_Region");
						String cellHead = getCellValue(objWorkbook,objSheet,j,eTest);
						Boolean eFlag=false;
						String[] environment=envList.split(",");
						for (int m=0; m<environment.length; m++) {
							if (cellHead.equalsIgnoreCase(environment[m])) {
								for (int k=stCol+1;k<endCol;k++,cj++) {
									cellHead = getCellValue(objWorkbook,objSheet,0,k);
									String cellVal = getCellValue(objWorkbook,objSheet,j,k);
									dataValue.put(cellHead, cellVal);
								}
							}
						}
					}
					break;
				}
			}
			fileInputStream.close();
			objWorkbook.close();
		} catch (Exception e) {
			fileInputStream.close();
			objWorkbook.close();
			logger.info("Error in pulling the test data values for the test : " + testName);
		}
		return dataValue;
	}
    
    
    /**
	 * @author MURUGCH
	 * @method getCellValue
	 * @param worksheet, row #, column #
	 * @return cellValue
	 */
    
    public String getCellValue (Workbook objWorkbook, Sheet sheet, int row, int colIndex) {
    	String cellValue="";
    	try {
    		Cell cell = sheet.getRow(row).getCell(colIndex);
    		DataFormatter formatter = new DataFormatter();
    		FormulaEvaluator evaluator = objWorkbook.getCreationHelper().createFormulaEvaluator();
    		
    		if (cell!=null) {
    			int cellStyle = cell.getCellType();
    			if (cellStyle==Cell.CELL_TYPE_STRING || cellStyle==Cell.CELL_TYPE_BOOLEAN || cellStyle==Cell.CELL_TYPE_NUMERIC) {
	    			cellValue = String.valueOf(formatter.formatCellValue(cell));
	    		} else if (cellStyle==Cell.CELL_TYPE_FORMULA) {
	    			CellValue cellVal = evaluator.evaluate(cell);
	    		    cellValue = String.valueOf(formatter.formatCellValue(cell, evaluator));
	    		} else {
	    			cellValue = "";
	    		}
    		}
    	} catch (Exception e) {
    		System.out.println("Error in getting the Cell Value for worksheet : " + sheet.getSheetName());
    	}
    	return cellValue;
    }
    
    
	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the rows of test data for a specific testcase from excel sheet
	 * @param FilePath as String, SheetName as String, TestName as String
	 */
		   
	public Object[][] getTestHeader(String xlFilePath, String sheetName, String testName) {
		
		String[][] DATA_HEADERS=null;
		String execVal=null;
		Workbook workbook=null;
		FileInputStream fileInputStream=null;
		Sheet sheet=null;
		try {
			int headStart=0;
			int startRow=0;
			int startCol=0;
			int endRow=0;
			int endCol=0;
			int ci=0;
			int cj=0;
			fileInputStream = new FileInputStream(xlFilePath);
			if (xlFilePath.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fileInputStream);
			} else {
				workbook = new HSSFWorkbook(fileInputStream);
			}
			sheet = workbook.getSheet(sheetName);
			String[] vals=envList.split(",");
			  
			//getting the start and end numbers for row & column
			startRow=findRow(sheet, testName);
			startCol=findColumn(sheet, testName);
			endRow=findRow(sheet,testName+"_End");
			endCol=findColumn(sheet,testName+"_End");
			int sCol=getHeaderColumnIndex(sheet, "Test_Region");  
			int RowCnt=0;
			if (startRow>=0 && endRow>startRow) {
				for (int iR=startRow;iR<endRow; iR++) {
					execVal=sheet.getRow(iR).getCell(sCol).getStringCellValue();
					for (int i=0; i<vals.length; i++) {
						if (execVal.equalsIgnoreCase(vals[i]))
							RowCnt++;
					}
				}
				
				DATA_HEADERS=new String[RowCnt][1];
				
				ci=0;
				int itVal=0;
				for (int iR=startRow;iR<endRow;iR++) {
					itVal=ci+1;
					cj=0;
					for (int k=sCol-1;k<sCol;k++) {
						execVal = sheet.getRow(iR).getCell(sCol).getStringCellValue();
						for (int iT=0; iT<vals.length; iT++) {
							if (execVal.equalsIgnoreCase(vals[iT])) {
								DATA_HEADERS[ci][cj]=testName + "_" + "Iteration-" + itVal;
								ci++; cj++;
							} 
						}
					}
				}
			} else {
				logger.info("DataUtil :: getTestData() :: Test Case Name --> " + testName + "--- is not found the test data file");
			}
			fileInputStream.close();
			workbook.close();
		} catch (Exception e) {
			try {
				fileInputStream.close();
				workbook.close();
			} catch (Exception e1) {}
			System.out.println("DataUtil :: getTestData() :: Test Case Name --> " + testName + "--- Error in Reading the test data file");
			logger.info("DataUtil :: getTestData() :: Test Case Name --> " + testName + "--- Error in Reading the test data file");
			logger.info("Test Case --> " + testName + "--- Total Iterations Count --> " + DATA_HEADERS.length);
	    }
		return DATA_HEADERS;
	}


	/**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the value from the 
	 * @param FilePath as String, SheetName as String, TestName as String
	 */
   
	
   public String getDataValue(String key) {
	   
	   String Value=null;
	   String testMethodVal=null;
	   try
	   {
		   StackTraceElement[] text = Thread.currentThread().getStackTrace();
		   for (int i=0; i<text.length; i++) {
			   if (text[i].toString().toLowerCase().contains(".testcases")) {
				   testMethodVal = Thread.currentThread().getStackTrace()[i].getMethodName();
				   if (!testMethodVal.toLowerCase().contains("inittest") && !testMethodVal.toLowerCase().contains("initmobiletest"))
					   break;
			   }
		   }
		   int itCount = getIterationCount(testMethodVal);
		   if (itCount<1) {
			   itCount=1;
		   }
		   Value=dataMap.get(testMethodVal).get(key).get(itCount-1);
		   if (Value.contains(";")) {
			   String[] tempVal=Value.split(";");
			   Value=tempVal[retryCount];
		   }
			   
	   } catch(Exception e) {
		   System.out.println("Error in getting the test data value for the Field : "+ key);
		   logger.info("Error in getting the test data value for the Field : "+ key);
		   if (key.equalsIgnoreCase("test_region")) {
			   Value=execRegion;
		   }
	   }
	   return Value;
   }
   
   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the current test case name
	 * @param
	 */
   
   public String getDataValue(String testMethodVal, String key) {  
	   
	   String Value=null;
	   try {
		   int itCount = getIterationCount(testMethodVal);
		   if (itCount<1) {
			   itCount=1;
		   }
		   if (dataMap.get(testMethodVal).containsKey(key)) {
			   Value=dataMap.get(testMethodVal).get(key).get(itCount-1);
			   if (Value.contains(";")) {
				   String[] tempVal=Value.split(";");
				   Value=tempVal[retryCount];
			   }
		   } else {
			   Value = "";
		   }
	   } catch(Exception e) {
		   if (key.equalsIgnoreCase("test_region")) {
			   Value=execRegion;
		   }
		   if (testMethodVal!=null && Value.isEmpty()) {
			   logger.info("DataUtil :: getDataValue() :: Test Case --> " + testMethodVal + "---- Error getting data value for the field - '"+key+"'");
		   }
	   }
	   return Value;
   }
   
   
   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the current test case name
	 * @param
	 */
   
   public String getTestName() {
	   String tName=null;
	   try {
		   StackTraceElement[] text = Thread.currentThread().getStackTrace();
		   for (int i=0; i<text.length; i++) {
			   if (text[i].toString().toLowerCase().contains(".testcases")) {
				   tName = Thread.currentThread().getStackTrace()[i].getMethodName();
				   if (!tName.toLowerCase().contains("inittest") && !tName.toLowerCase().contains("initmobiletest"))
					   break;
			   }
		   }
	   } catch (Exception e) {
		   logger.info("Error in getting the test name");
	   }
	   return tName;
   }
   
   
   
   /**
	 * @date 09/17/2015
	 * @author Chandrasekar Murugesan
	 * @description Method used to get the iteration count of a test case
	 * @param
	*/
   
   public int getIterationCount(String testMethodVal) {
	   
	   int Value=0;
	   int itCount=0;
	   try {
		   for (String mets : testmethodsExec) {
			   if (mets.trim().equalsIgnoreCase(testMethodVal.trim()))
				   itCount++;
		   }
		   if (itCount<1)
			   Value = itCount;
		   else
			   Value = 1;
	   } catch(Exception e) {
		   logger.info("Error in getting the iteration count");
	   }
	   return Value;
   }
 
}
