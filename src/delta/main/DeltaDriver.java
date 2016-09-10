package delta.main;

import java.net.MalformedURLException;

import java.util.concurrent.TimeUnit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
//import org.testng.annotations.AfterSuite;

import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeSuite;

import org.testng.annotations.Test;

import org.testng.xml.XmlTest;



import com.relevantcodes.extentreports.LogStatus;

import generics.Excel;
import generics.Property;
import generics.Utility;

public class DeltaDriver extends BaseDriver {
	public WebDriver driver;
	public String browser;
	
	//public ExtentReports eReport;
	//public ExtentTest testReport;
	//public String imageFolderPath="./screenShots";
	//public String reportFilePath="./report/results.html";
	//public String configpptPath="./config/config.properties";
	//public String scenariosPath="./scripts/Scenarios.xlsx";
	
	
	
	
	/*@BeforeTest
	public void initFrameWork()
	{
		eReport=new ExtentReports(reportFilePath);
		
	}*/
	
	@BeforeMethod
	public void launchApp(XmlTest x) throws MalformedURLException
	{
		
		String appURL=Property.getProperyValue(configpptPath, "URL");
		String timeOut=Property.getProperyValue(configpptPath, "TimeOut");
		//System.setProperty("webdriver.chrome.driver", "C:\\RS1\\cd.exe");
		browser=x.getParameter("browser");
		//String url=x.getParameter("url");
		System.out.println("opening"+browser);
		//URL remoteaddress =new URL(url);
		//DesiredCapabilities dc=new DesiredCapabilities();
		//dc.setBrowserName(browser);
		//dc.setPlatform(Platform.ANY);*/
		
		
		if(browser.equals("firefox"))
		{
			driver=new FirefoxDriver();
		}
		else if(browser.equals("chrome"))
			
		{
			System.out.println("opening chrome");
			System.setProperty("webdriver.chrome.driver", "C:/RS1/cd.exe");
			driver=new ChromeDriver();
			/*URL remoteaddress =new URL("http://127.0.0.1:4444/wd/hub");
			DesiredCapabilities dc=new DesiredCapabilities();
			dc.setBrowserName("firefox");
			driver=new RemoteWebDriver(remoteaddress,dc);
			}*/
		driver.get(appURL);
		driver.manage().timeouts().implicitlyWait(Long.parseLong(timeOut), TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	}
@Test(dataProvider="getScenarios",dataProviderClass=BaseDriver.class)
public void testScanarios(String scenarioName,String executionStatus)
{
	//String scenariosPath="./Scripts/Scenarios.xlsx";
	// scenarioSheet="Scenario1";
	
	testReport=eReport.startTest(browser+"_"+scenarioName);
	if(executionStatus.equalsIgnoreCase("yes"))
	{
	int stepCount=Excel.getRowCount(scenariosPath, scenarioName);
	System.out.println(stepCount);
	for(int i=1;i<=stepCount;i++)
	{
		
	
		String description=Excel.getCellValue(scenariosPath, scenarioName, i, 0);
	String action=Excel.getCellValue(scenariosPath, scenarioName, i, 1);
	String input1=Excel.getCellValue(scenariosPath, scenarioName, i, 2);
	String  input2=Excel.getCellValue(scenariosPath, scenarioName, i, 3);
	//System.out.println(decription+action+input1+input2);
	String msg="description:"+description+"action"+action+"input1"+input1+"input 2"+input2;
	
	testReport.log(LogStatus.INFO, msg);
	KeyWord.executeKeyWord(driver, action, input1, input2);
	
	}
	}
	else
	{
		testReport.log(LogStatus.SKIP, "Execution Status is 'NO'");
		throw new SkipException("Skipping this scenario");
	}
	}


@AfterMethod
public void quitApp(ITestResult test)
{
	if(test.getStatus()==2)
	{
		String pImage=Utility.getPageScreenShot(driver, imageFolderPath);
		String p=testReport.addScreenCapture("."+pImage);
		testReport.log(LogStatus.FAIL, "Page screen shot:"+p);
	}

	
	eReport.endTest(testReport);
	driver.close();
	
}
/*@AfterTest
public void endFrameWork()
{
	eReport.flush();
}*/
}
