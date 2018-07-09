package testPages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class CreateASN {
	
	private WebDriver driver;

	@FindBy(css = "input[title='Update']")
	 	private WebElement Update;

	@FindBy(css = "input[title='Items']")
	 	private WebElement ASN_Header_Items;

	@FindBy(css = "input[title='Items']")
	 	private List<WebElement> All_Items;

	@FindBy(css = "input[name^='AP_SSCC_']")
	 	private List<WebElement> SSCC_Value;

	@FindBy(css = "input[title='Generate SSCC']")
	 	private WebElement Generate_SSCC_Btn;

	@FindBy(xpath = "//th[@class='AH']")
		private List<WebElement> FTR_Header;

	@FindBy(css = "input[title='Return']")	
	 	private WebElement Return;

	@FindBy(className = "RO")
	 	private WebElement ASN_ID;

	@FindBy(name = "AH_TYPE")
	 	private WebElement Slt_ASN_Type;

	@FindBy(name = "AH_REFNO")
	 	private WebElement LoadRefID;

	@FindBy(name = "AH_BOOKTIME")
	 	private WebElement Book_Time;

	@FindBy(css = "input[id^='SEL_']")
		private List<WebElement> Slt_List;

	@FindBy(xpath = "//input[contains(@id,'AI_EXPDATE_')]")
		private List<WebElement> Sell_Date;

	@FindBy(css = "input[name^='AP_DOCNO_']")
	 	private List<WebElement> Pallet_Name;

	@FindBy(xpath = "//th[contains(text(),'Pallet Name')]/../following-sibling::tr/td[4]")
	 	private List<WebElement> ASNPallet_Pallet_Name;
	
    @FindBy(name = "APPL")
    	private WebElement Appl_Form;

	public CreateASN(WebDriver driver)
		{
			PageFactory.initElements(driver, this);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(this.Appl_Form);
//			wait = new WebDriverWait(driver,30);
			this.driver = driver;
		}

	public static CreateASN using(WebDriver driver) throws InterruptedException {
		return new CreateASN(driver);
	}

	public CreateASN SltASNType(String Type) {
		Select DocType = new Select(this.Slt_ASN_Type);
		DocType.selectByVisibleText(Type);
		return this;
	}
	
	public CreateASN Set_LoadRefID() {		
		LocalDateTime  today = LocalDateTime.now();
	    String LoadID = today.format(DateTimeFormatter.ofPattern("MMMMddHHmm"));	    
		this.LoadRefID.sendKeys(LoadID);
		return this;
	}

	public CreateASN Set_BookTime() {
		this.Book_Time.sendKeys("04:00");
		return this;
	}

	public CreateASN NavigateTo_Tab(String TabName) {
		driver.findElement(By.xpath("//td[contains(text(),'"+TabName+"')]")).click();
		return this;
	}
	
	public HashSet<String> Get_SSCC_Value() {
		HashSet<String> SSCC = new HashSet<String>();
		int i = 0;
		for(WebElement SSCC_Val : SSCC_Value)
			SSCC.add( ASNPallet_Pallet_Name.get(i++).getText() + " : " +  SSCC_Val.getAttribute("value"));
		return SSCC;
	}
	
	public CreateASN SltAll_SltBtn() {
		for(WebElement SltBtn:Slt_List)
			SltBtn.click();
		return this;
	}

	public CreateASN GenerateSSCC_Btn() {
		Generate_SSCC_Btn.click();
		return this;
	}
	
	public CreateASN Click_Items_Btn() {
		ASN_Header_Items.click();
		return this;
	}
	
	public CreateASN Click_Update_Btn() {
		Update.click();
		return this;
	}

	public CreateASN Click_Return_Btn() {
		Return.click();
		return this;
	}
	
	public String Get_ASN_ID() {
		return ASN_ID.getAttribute("value");
	}
	
	public CreateASN Enter_PalletName_Prod(String PalletName) throws InterruptedException {
			int i=0;
			
			for(WebElement Pall_Name:Pallet_Name)
			{
				Slt_List.get(i++).click();
				Pall_Name.sendKeys(PalletName);
			}
			
			Click_Update_Btn();
			Click_Return_Btn();
		return this;
	}
	
	public CreateASN Enter_PalletName_AllPO(String PalletName) throws InterruptedException {
		for(int i=0;i<All_Items.size(); i++)
		{
			All_Items.get(i).click();
//			Enter_PalletName(PalletName);
			Click_Update_Btn();
			Click_Return_Btn();
		}
		return this;
	}

	public CreateASN Enter_PalletName(String FileName) throws IOException {
		String PO_Prod_PalletName = new String(Files.readAllBytes(Paths.get(FileName)));		
		ArrayList<String> PO = new ArrayList<String>();
		ArrayList<String> Prod = new ArrayList<String>();
		ArrayList<String> Pallet = new ArrayList<String>();		
		String[] TempVar = PO_Prod_PalletName.split("##"); 		
		for(String Detail:TempVar) 
		{
			if(Detail.contains("$$"))
			{
				PO.add(Detail.split("\\$\\$")[0]);
				Prod.add(Detail.split("\\$\\$")[1]);
				Pallet.add(Detail.split("\\$\\$")[2]);
			}
		}
		
//		String[][][] array =
//			        Pattern.compile("##", Pattern.LITERAL)
//			               .splitAsStream(PO_Prod_PalletName)
//			               .map(s -> s.split("\\$\\$"))
//			               .map(a -> String.format(a[0],a[1],a[2]))
//			               .toArray(String[][][]::new);
		
		
		
		int i=0;
		int TPND_ColNum = 0,PO_ColNum = 0;
		for(WebElement Indv:FTR_Header)
		{
			if(Indv.getText().contains("TPND"))
				TPND_ColNum = i;
			else if(Indv.getText().contains("PO Number"))
				PO_ColNum = i;
			i++;
		}
		
		LocalDate SellDate = LocalDate.now().plusDays(10);
		
		for(int j=0;j<Slt_List.size();j++)
		{
			String TPND = driver.findElement(By.xpath("//th[contains(text(),'TPND')]/../following-sibling::tr["+(j+1)+"]/td["+(TPND_ColNum+3)+"]")).getText().trim();
			String PO_Num = driver.findElement(By.xpath("//th[contains(text(),'TPND')]/../following-sibling::tr["+(j+1)+"]/td["+(PO_ColNum+3)+"]")).getText().trim();
//			System.out.println(TPND + "-" + PO_Num);
			for(i=0;i<TempVar.length;i++)
			{
				if(TempVar[i].contains(TPND) && TempVar[i].contains(PO_Num))
				{
					Slt_List.get(j).click();
					Pallet_Name.get(j).sendKeys(Pallet.get(i));					
					
					Sell_Date.get(j).sendKeys(SellDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString());
				}
			}
			
//			Stream<String> array =
//			        Arrays.stream(TempVar)
//			        	.filter(e -> (e.contains(TPND) && e.contains(TPND)));
//			System.out.println(array);
		}
		return this;
	}
}
