package weathermap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WmtestSteps {
	WebDriver browser;
	ArrayList<String> days;
	
	public WmtestSteps() {
		
	}
	
	@Before
	public void beforeScenario() 
	{
		System.setProperty("webdriver.gecko.driver", "drivers//geckodriver-v0.14.0-win64//geckodriver.exe");
		System.setProperty("webdriver.chrome.driver", "C:\\DriversForSelenium\\chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		days = new ArrayList<String>() ;
		days.add("Mon");
		days.add("Tue");
		days.add("Wed");
		days.add("Thu");
		days.add("Fri");
		days.add("Sat");
		days.add("Sun");
	}

	@After
	public void afterScenario() 
	{
		 browser.close(); 
		 browser.quit();
	}
	
	@Given("^i am on the weather forecast site at (.*)$")
	public void OpenBrowser(String siteUrl) {
		browser.get(siteUrl);
	}
	
	@When("^i enter city name as (.*)$")
	public void EnterCityName(String city) throws InterruptedException {
		WebElement cityElement = browser.findElement(By.id("city")); // FindElemnt(browser, By.id("city"));
		cityElement.clear();
		cityElement.sendKeys(city);
		cityElement.sendKeys(Keys.RETURN);
	}
	
	@Then("^site should load 5 day forecast for the given city$")
	public void VerifyFiveDayForecast() throws InterruptedException {
		ArrayList<WebElement> summaries = (ArrayList<WebElement>) browser.findElements(By.className("summary"));
		assertEquals(summaries.size(), 5);
		ArrayList<WebElement> details = (ArrayList<WebElement>) browser.findElements(By.xpath("//div[@class='details' and contains(@style,'max-height: 0px')]"));
		assertEquals(details.size(), 5);
		
		//assert 5 day forecast summary details
		AssertForecastData(summaries.get(0),true);
	}
	
	@When("^i click on first day of weather summary$")
	public void ClickOnFirstSummary() throws InterruptedException {
		ArrayList<WebElement> summaries = (ArrayList<WebElement>) browser.findElements(By.className("summary"));
		summaries.get(0).click();
		Thread.sleep(1000);
	}
	
	@Then("^i should be able to see three hour forecast$")
	public void VerifyThreeHourForecast() {
		ArrayList<WebElement> details = (ArrayList<WebElement>) browser.findElements(By.xpath("//div[@class='details' and not(contains(@style,'max-height: 0px'))]"));
		//only one details section must be displayed
		assertEquals(details.size(), 1);
		
		//find sub details
		ArrayList<WebElement> subDetails = (ArrayList<WebElement>) details.get(0).findElements(By.className("detail"));
		assertTrue(subDetails.size()>0);
		
		//assert 3 hour forecast summary for selected day
		AssertForecastData(subDetails.get(0),false);
	}
	
	@Then("^three hour forecast section must be hidden$")
	public void VerifyThreeHourSectionHidden() {
		ArrayList<WebElement> details = (ArrayList<WebElement>) browser.findElements(By.xpath("//div[@class='details' and not(contains(@style,'max-height: 0px'))]"));
		//no details section must be displayed
		assertEquals(details.size(), 0);
	}
	
	public void AssertForecastData(WebElement subDetails,boolean isDaySummary) {
		//find cells
		ArrayList<WebElement> cells = (ArrayList<WebElement>) subDetails.findElements(By.className("cell"));
		assertEquals(cells.size(), 5);
		
		if(isDaySummary) {
			//assert day field present
			WebElement day = cells.get(0).findElement(By.className("name"));
			assertTrue(day!=null);
			assertEquals(day.getText().length(),3);
			assertTrue(days.contains(day.getText()));
			System.out.println(day.getText());
		}else {
			//assert hour field present
			WebElement hour = cells.get(0).findElement(By.className("hour"));
			assertTrue(hour!=null);
			assertEquals(hour.getText().length(),4);
		}
		
		
		//assert icon
		WebElement weatherIcon = cells.get(1).findElement(By.xpath("//*[local-name() = 'svg']"));
		assertTrue(weatherIcon!=null);
		
		//assert temp max and min
		WebElement maxMin = cells.get(2).findElement(By.className("max"));
		assertTrue(maxMin!=null);
		assertTrue(IsWholeNumber(TrimRadient(maxMin.getText())));
		
		//assert wind speed
		WebElement speed = cells.get(3).findElement(By.className("speed"));
		assertTrue(speed!=null);
		assertTrue(IsWholeNumber(TrimKph(speed.getText())));
		
		//assert rainfall
		WebElement rainFall = cells.get(4).findElement(By.className("rainfall"));
		assertTrue(rainFall!=null);
		assertTrue(IsWholeNumber(TrimMm(rainFall.getText())));
	}
	
	public String TrimRadient(String str) {
		 System.out.println(str);
		return str.replace("�", "");
	}
	
	public String TrimKph(String str) {
		 System.out.println(str);
		return str.replace("kph", "");
	}
	
	public String TrimMm(String str) {
		 System.out.println(str);
		return str.replace("mm", "");
	}
	
	public boolean IsWholeNumber(String str) {
		return str.matches("-?\\d+");
	}
}
