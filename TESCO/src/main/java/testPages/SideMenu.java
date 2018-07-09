package testPages;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SideMenu {
	
	private static WebDriver driver;
	private static Actions action;
	private WebDriverWait wait;

    @FindBy(id = "_MESSCELL")
    private WebElement ResponseMsg;   
    
    @FindBy(name = "MENU")
    private WebElement Menu_Form;

    @FindBy(xpath = "//td[contains(text(),'Log out')]")
    private WebElement Logout;

    public SideMenu(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		
		action = new Actions(driver);
		SideMenu.driver = driver;
		wait = new WebDriverWait(driver,30);

		driver.switchTo().defaultContent();
		driver.switchTo().frame(Menu_Form);
	}	
	
	public static SideMenu using(WebDriver driver) throws InterruptedException
	{		
		return new SideMenu(driver);
	}
	
	public SideMenu NavigateTo(String NavigationText) throws AWTException
	{		
		String[] FinalText = NavigationText.split("->");		
		for(String Text:FinalText)
		{
			WebElement Menu_Element = driver.findElement(By.xpath("//td[contains(text(),'"+Text.trim()+"')  and not(contains(text(),'Inventory'))]"));
			wait.until(ExpectedConditions.visibilityOf(Menu_Element));
			action.moveToElement(Menu_Element).perform();
		}		
		return this;		
	}
	
	public SideMenu LogOut() {
		Logout.click();
		return this;
	}
	
	public void clickMenu(String Text) throws InterruptedException {
		WebElement Menu_Element = driver.findElement(By.xpath("//td[contains(text(),'"+Text.trim()+"')  and not(contains(text(),'Inventory'))]"));
		wait.until(ExpectedConditions.visibilityOf(Menu_Element));
		Thread.sleep(1000);
		Menu_Element.click();		
//		driver.findElement(By.xpath("//td[(contains(text(),'"+Text.trim()+"') and not(contains(text(),'Inventory'))) and ((@id='MENUITEM') or contains(@onclick,'invoke('))]")).click();
	}
	
	public SideMenu MoveTo_MsgBox() {
		action.moveToElement(this.ResponseMsg).perform();
		return this;
    }
	
	 public Boolean Wait_SuccessMsg(String Msg) throws InterruptedException {
//			action.moveToElement(this.ResponseMsg).perform();
			
			for(int i=0;i<30;i++)
			{
				String ResText = this.ResponseMsg.getText();
		        if(ResText.contains(Msg))
		        	return true;
		        Thread.sleep(1000);
			}
			
	        return false;
	    }
		
}
