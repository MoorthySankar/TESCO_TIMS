package testPages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Admin_FlowTrackReport {
	
	@FindBy(xpath = "//th[@class='AH']")
	private List<WebElement> FTR_Header;

	@FindBy(xpath = "//th[contains(text(),'Action')]/following::td[contains(text(),'New')]")
	private WebElement Pass_Report;
    
    @FindBy(name = "APPL")
    private WebElement Appl_Form;
    
    private WebDriver driver;
    
    public Admin_FlowTrackReport(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(this.Appl_Form);
		this.driver = driver;
	}
	
	public static Admin_FlowTrackReport using(WebDriver driver) throws InterruptedException
	{
		return new Admin_FlowTrackReport(driver);
	}

	public Boolean CheckReports() {
		int i=0;
		for(WebElement Indv:FTR_Header)
		{
			if(Indv.getText().contains("Action"))
			{
				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				if(driver.findElements(By.xpath("//th[contains(text(),'Action')]/../following-sibling::tr/td["+(i+2)+"]")).size()>0)
				{
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//					System.out.println(driver.findElement(By.xpath("//th[contains(text(),'Action')]/../following-sibling::tr/td["+(i+2)+"]")).getText());
					if(driver.findElement(By.xpath("//th[contains(text(),'Action')]/../following-sibling::tr/td["+(i+2)+"]")).getText().contains("New"))
						return true;
					else
						return false;
				}
				else
				{
					driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
					return false;
				}
			}
			i++;
		}
		return false;
	}
	
}
