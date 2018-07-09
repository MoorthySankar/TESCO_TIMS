package testPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Admin_ManualImport {
	
	    @FindBy(name = "FILENEW")
	    private WebElement ImportFile;

	    @FindBy(css = "input[title='Import File']")
	    private WebElement ImportBtn;	   
	    
	    @FindBy(name = "APPL")
	    private WebElement Appl_Form;
	    
	    public Admin_ManualImport(WebDriver driver)
		{
			PageFactory.initElements(driver, this);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(this.Appl_Form);
		}
		
		public static Admin_ManualImport using(WebDriver driver) throws InterruptedException
		{
			return new Admin_ManualImport(driver);
		}
		
		public Admin_ManualImport FileChooser(String FilePath) {
	        this.ImportFile.sendKeys(FilePath);
	        return this;
	    }

	    public void Import() {
	        this.ImportBtn.click();
	    }
	    
	   

}
