package testPages;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(name = "LOGUSER")
    private WebElement userName;

    @FindBy(name = "LOGPASS")
    private WebElement password;

    @FindBy(css = "input[title='Login']")
    private WebElement loginBtn;

    @FindBy(name = "APPL")
    private WebElement Appl_Form;
    
    public LoginPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(this.Appl_Form);
	}
	
	public static LoginPage using(WebDriver driver) throws InterruptedException
	{
		return new LoginPage(driver);
	}
	
	public LoginPage setUsername(String username) throws IOException {		
        this.userName.sendKeys(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        this.password.sendKeys(password);
        return this;
    }

    public void login() throws InterruptedException {
        this.loginBtn.click();
        Thread.sleep(1000);
    }
}
