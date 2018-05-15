package com.verizon.automation.listener;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.verizon.automation.configuration.SuiteSetup;

@SuppressWarnings("all")
public class TestListener extends SuiteSetup implements ITestListener {
	
	private final static Logger logger = Logger.getLogger(TestListener.class);
	
	@Override
	public void onFinish(ITestContext context) {
		
		Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
		Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
		Set<ITestResult> passedTests = context.getPassedTests().getAllResults();
		String result=null;
		String exception=null;
		
		//Boolean testLink=Boolean.valueOf(putil.getTestEnvironmentProperty("TestLinkIntegration"));
		
		if (passedTestMethods==null)
			passedTestMethods=new LinkedList<ITestNGMethod>();
		
		if (failedTestMethods==null)
			failedTestMethods=new LinkedList<ITestNGMethod>();
		
		if (skippedTestMethods==null)
			skippedTestMethods=new LinkedList<ITestNGMethod>();
		
		for (ITestResult temp : failedTests) {
			ITestNGMethod method = temp.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				failedTests.remove(temp);
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					failedTests.remove(temp);
				} else {
					if (!failedTestMethods.contains(method))
						failedTestMethods.add(method);
				}
			}
		}
		
		for (ITestResult temp : skippedTests) {
			ITestNGMethod method = temp.getMethod();
			if (context.getSkippedTests().getResults(method).size() > 1) {
				skippedTests.remove(temp);
			} else if (context.getPassedTests().getResults(method).size() > 0) {
					skippedTests.remove(temp);
			} else {
				if (context.getFailedTests().getResults(method).size() > 0) {
					skippedTests.remove(temp);
				} else {
					skippedTestMethods.add(method);
				}
			}
		}
		
		for (ITestResult temp : passedTests) {
			ITestNGMethod method = temp.getMethod();
			Set<ITestResult> res=context.getPassedTests().getResults(method);
			Iterator it=res.iterator();
			while(it.hasNext()) {
				List<String> out=Reporter.getOutput((ITestResult) it.next());
				String reportVal=out.toString();
				if (out.size()<2) {
					passedTests.remove(temp);
					break;
				} else {
					if (!passedTestMethods.contains(method))
						passedTestMethods.add(method);
				}
			}
		}
	}
  
    public void onTestStart(ITestResult result) {}
  
    public void onTestSuccess(ITestResult result) {}
  
    public void onTestFailure(ITestResult result) {}

    public void onTestSkipped(ITestResult result) {}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    public void onStart(ITestContext context) {}
}  
