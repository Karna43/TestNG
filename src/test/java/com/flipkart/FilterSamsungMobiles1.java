package com.flipkart;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.common.reflection.qual.GetClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.beust.jcommander.Parameter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FilterSamsungMobiles1 {
	public static WebDriver driver;
	public static List<WebElement> mobileNames;
	public static File file;
	
	@BeforeSuite(groups = "default")
	public void beforeSuite() {
		System.out.println("Before Suite");
	}
	@AfterSuite(groups = "default")
	public void afterSuite() {
		System.out.println("After Suite");
	}
	@BeforeTest(groups = "default")
	public void beforeTest() {
		System.out.println("Before Test");
	}
	@AfterTest(groups = "default")
	public void afterTest() {
		System.out.println("After Test");
	}
	@BeforeMethod(groups = "default")
	public void beforeMethod() {
		System.out.println("Before Method");
	}
	@AfterMethod(groups = "default")
	public void afterMethod() {
		System.out.println("After Method");
	}
	@Parameters({"browser"})
	@BeforeClass(groups = "default")
	public static void launch_flipkart_website(String browserName) {
		if(browserName.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized");
			driver = new ChromeDriver(options);
		}
		else {
			WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			options.addArguments("start-maximized");
			driver = new EdgeDriver(options);
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.flipkart.com/");
		System.out.println("Before Class");
	}
	@AfterClass(groups = "default")
	public void browserClose() {
	System.out.println("After Class");
	driver.quit();
	}
	@Parameters({"sendKeys"})
	@Test(priority=0, groups = "filter")
	public void search_Mobiles(@Optional("default") String value) {
		WebElement search = driver.findElement(By.xpath("//input[contains(@placeholder,'Search')]"));
		search.sendKeys(value, Keys.ENTER);
	}
	
	@Test(priority=1, groups = "filter")
	public void check_Samsung_Brand(){
			driver.findElement(By.xpath("//div[text()='SAMSUNG']")).click();
	}
	
	@Test(priority=2, groups = "filter")
	public void get_Filtered_Mobiles(){
		try {
			Thread.sleep(3000);
			mobileNames = driver.findElements(By.xpath("//div[contains(@class,'rR01T')]"));
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority=3, groups = "filter", dependsOnMethods = "get_Filtered_Mobiles")
	public void validate_Filtered_Data(){
		for(WebElement i : mobileNames) {
			String mobileName = i.getText();
			if (mobileName.contains("SAMSUNG")) {
				System.out.println(mobileName+" : Related Product");
			}
			else {
				System.out.println(mobileName+" : Not a Related Product");
			}
		}
	}
	@Test(priority=4, groups = "excel")
	public void create_Excel_File(){
		try {
			Thread.sleep(2000);
			file = new File("D:\\TestNG\\Flipkart\\writeData.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test(priority=5, groups = "excel")
	public void write_Filtered_Data_In_Excel(){
		try {
			FileOutputStream writeFile = new FileOutputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Samsung Mobiles");
			for(int i=0;i<mobileNames.size();i++) {
				XSSFRow row = sheet.createRow(i);
				XSSFCell cell = row.createCell(0);
				cell.setCellValue(mobileNames.get(i).getText());
			}
			workbook.write(writeFile);
			writeFile.close();
			System.out.println("Samsung Mobile names updated in Excel");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}