package testPages;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class Supp_PurchaseOrder {
	
	@FindBy(css = "input[title='Read']")
	private WebElement ReadBtn;
	
	@FindBy(css = "input[title='Create ASN']")
	private WebElement CreateAsn_Btn;

	@FindBy(id = "v_spi")
	private WebElement SupplierID;

	@FindBy(id = "V_DATDOD")
	private WebElement DeliveryDate;
	
	@FindBy(xpath = "//input[contains(@id,'SEL_')]/following::td[1]")
	private List<WebElement> PO_List;

	@FindBy(xpath = "//input[contains(@id,'SEL_')]")
	private List<WebElement> Slt_List;

	@FindBy(name = "APPL")
	private WebElement Appl_Form;

	public Supp_PurchaseOrder(WebDriver driver) {
		PageFactory.initElements(driver, this);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(this.Appl_Form);
	}

	public static Supp_PurchaseOrder using(WebDriver driver) throws InterruptedException {
		return new Supp_PurchaseOrder(driver);
	}
	
	public Supp_PurchaseOrder Slt_Supplier(String Type) {
		Select DocType = new Select(this.SupplierID);
		DocType.selectByVisibleText(Type);
		return this;
	}
	
	public Supp_PurchaseOrder Click_CreateASNBtn() {
		CreateAsn_Btn.click();
		return this;
	}
	
	public Supp_PurchaseOrder SetDeliveryDate() {
		LocalDate today = LocalDate.now();
		DeliveryDate.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		DeliveryDate.sendKeys(today.toString());
		return this;
	}
	
	public Supp_PurchaseOrder Select_PO(HashSet<String> PO_Number) {
		int i=0;
		for(WebElement Cur_Row:PO_List)
		{
//			System.out.println("-"+Cur_Row.getText()+"-");
			if(!PO_Number.add(Cur_Row.getText()))
				Slt_List.get(i).click();
			i++;
		}

		return this;
	}
}
