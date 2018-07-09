package testPages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CommonPage {
	
	private WebDriver driver;

	@FindBy(name = "V_FT_DOCTYPE")
	private WebElement DocType;

	@FindBy(name = "V_FT_DOCNO")
	private WebElement DocNum;

	@FindBy(css = "input[title='Read']")
	private WebElement ReadBtn;

    @FindBy(css = "input[title='Log out']")
    private WebElement LogOut_Btn;

    @FindBy(css = "input[title='Submit']")
    private WebElement Submit_Btn;

    @FindBy(css = "input[name='V_BATCHTYPEID']")
    private WebElement Identification;
    
    @FindBy(xpath = "//td[contains(text(),'ASNEXP')]/following::input[@title='Start']")
    private WebElement ASNEXP_Start;
    
    @FindBy(xpath = "//input[@title='Start']")
    private WebElement Start_Btn;

    @FindBy(name = "APPL")
    private WebElement Appl_Form;  

	public CommonPage(WebDriver driver)
		{
			this.driver = driver;
			PageFactory.initElements(driver, this);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(this.Appl_Form);
		}

	public static CommonPage using(WebDriver driver) throws InterruptedException {
		return new CommonPage(driver);
	}

	public CommonPage SetDocType(String Type) {
		Select DocType = new Select(this.DocType);
		DocType.selectByVisibleText(Type);
		return this;
	}
	
	public CommonPage SetDocNo(String DocNo) {
		DocNum.sendKeys(Keys.chord(Keys.CONTROL,"a"));
		DocNum.sendKeys(DocNo);
		return this;
	}
	
	public CommonPage Enter_Identification(String Identi) {
		this.Identification.sendKeys(Identi);
		return this;
	}
	
	public CommonPage Click_Start_Btn() throws InterruptedException {
		Thread.sleep(2000);
		this.Start_Btn.click();
		return this;
	}

	public CommonPage Click_Read_Btn() {
		this.ReadBtn.click();
		return this;
	}

	public CommonPage Logout_Btn() {
		this.LogOut_Btn.click();
		return this;
	}
	
	public CommonPage Run_ASNEXP() {
		this.ASNEXP_Start.click();
		return this;
	}	
	
	public CommonPage Click_Submit_Btn() {
		this.Submit_Btn.click();
		return this;
	}		
	
	public void Alert_Accept() {
		Alert alert = driver.switchTo().alert();
		alert.accept();	
	}	
	
	public void Wait_TillRecordDisplay(HashSet<String> PO_Details) throws InterruptedException {
		OuterLoop:
		while(true)
		{			
//			String PO_Num1 = driver.findElement(By.xpath("//input[contains(@id,'SEL_')]/following::td[1]")).getText();
			
			List<WebElement> List_PO = driver.findElements(By.xpath("//input[contains(@id,'SEL_')]/following::td[1]"));			
			HashSet<String> Loc = new HashSet<String>();
			
			for(WebElement Indv_PO:List_PO)
				Loc.add(Indv_PO.getText());
						
			for(String PO_Num:Loc)
			{
				if(!PO_Details.add(PO_Num))
					break OuterLoop;
				PO_Details.remove(PO_Num);
			}
			Click_Read_Btn();
			Thread.sleep(2000);
		}
	}
	
	public ArrayList<String> GetSuppID_TxtFile(String fileName) throws IOException {
		ArrayList<String> ReturnSuppId = new ArrayList<String>();
		Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.startsWith("H|I|LM"));
		lines.forEach(line -> {
			if (line.trim() != null)
				ReturnSuppId.add(line.split("\\|")[5]);
		});
		return ReturnSuppId;
	}
	
	public HashSet<String> GetPO_TxtFile(String fileName) throws IOException {
		HashSet<String> ReturnPO = new HashSet<String>();
		Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.startsWith("H|I|LM"));
		lines.forEach(line -> {
			if (line.trim() != null)
				ReturnPO.add(line.substring(line.indexOf("LM"), line.indexOf("LM") + 13));
		});
		return ReturnPO;
	}
	
	public HashSet<String> GetSupplier_TxtFile(String fileName) throws IOException {
		HashSet<String> ReturnPO = new HashSet<String>();
		Stream<String> lines = Files.lines(Paths.get(fileName)).filter(line -> line.startsWith("H|I|LM"));
		lines.forEach(line -> {
			if (line.trim() != null)
				ReturnPO.add(line.substring(line.indexOf("LM"), line.indexOf("LM") + 13));
		});
		return ReturnPO;
	}
}
