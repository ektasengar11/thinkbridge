package Thinkbridge.Question2;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SignUp {
	//Declaring
	WebDriver driver;
	WebDriverWait wait;
	String baseURL = "http://jt-dev.azurewebsites.net/#/SignUp";
	WebElement chooseLanguage;
	List<WebElement> dropDownValues;
	WebElement fullName;
	WebElement organization;
	WebElement email;
	WebElement termsAndConditions;
	WebElement getStarted;
	WebElement OnForm;
	WebElement welcomeEmailSubjectLine;


	@BeforeTest
	public void initDriver() {
		//Setting Property
		System.setProperty("webdriver.chrome.driver", ".\\Drivers\\chromedriver.exe");
		//Initializing
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}
	@BeforeMethod
	public void initElements() {
		//Initializing WebElements
		driver.get(baseURL);
		chooseLanguage = driver.findElement(By.xpath("//span[contains(text(),'English')]"));
		fullName = driver.findElement(By.id("name"));
		organization = driver.findElement(By.id("orgName"));
		email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
		termsAndConditions  = driver.findElement(By.xpath("//span[contains(text(),'I agree to the')]"));
		getStarted  = driver.findElement(By.tagName("button"));

	}

	@Test(priority=1)
	public void checkingDropDownValues() {

		String[] languages = {"English","Dutch"};
		chooseLanguage.click();
		//dropDownValues can only be located once options are visible
		dropDownValues = driver.findElements(By.xpath("//div[@ng-bind-html='language']"));
		wait.until(ExpectedConditions.elementToBeClickable(dropDownValues.get(0)));
		if(dropDownValues.size()==2)
		{

			for(int i=0;i<dropDownValues.size();i++)
			{			  
				Assert.assertEquals(dropDownValues.get(i).getText(), languages[i],"Dropdown values do not match");
			}
		}
	}

	@Test(priority=2)
	public void checkingWelcomeEmail() {
		//Filling up the Sign Up Form
		chooseLanguage.click();
		dropDownValues = driver.findElements(By.xpath("//div[@ng-bind-html='language']"));
		wait.until(ExpectedConditions.elementToBeClickable(dropDownValues.get(0)));
		dropDownValues.get(0).click();
		fullName.sendKeys("Ekta Sengar");
		organization.sendKeys("Mindtree");	
		email.sendKeys("ektasengar11@armyspy.com");
		termsAndConditions.click();
		getStarted.click();		
		//Checking Success Message on submission of the form
		OnForm = driver.findElement(By.xpath("//span[contains(text(),'A welcome email has been sent. Please check your email.')]"));
		Assert.assertEquals(OnForm.getText(),"A welcome email has been sent. Please check your email.","Error on the Form");
		//Switching to New Tab
		((JavascriptExecutor)driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		//Opening Fake Email Generator
		driver.get("http://www.fakemailgenerator.com/#/armyspy.com/ektasengar11/");
		welcomeEmailSubjectLine = driver.findElement(By.xpath("//p[contains(text(),'Hi Ekta Sengar - Please Complete JabaTalks Account')]"));
		wait.until(ExpectedConditions.elementToBeClickable(welcomeEmailSubjectLine));
		//Checking if the Welcome Email was received
		Assert.assertEquals(welcomeEmailSubjectLine.getText(),"Hi Ekta Sengar - Please Complete JabaTalks Account","User did not receive the Welcome Email");
	}




}
