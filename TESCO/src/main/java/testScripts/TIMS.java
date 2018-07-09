package testScripts;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import testPages.Admin_FlowTrackReport;
import testPages.Admin_ManualImport;
import testPages.CommonPage;
import testPages.CreateASN;
import testPages.LoginPage;
import testPages.SideMenu;
import testPages.Supp_PurchaseOrder;

public class TIMS {
 
	@Test
	public static void ASN_Creation(WebDriver driver) throws InterruptedException, AWTException, IOException{
		
		File RootPath = new File(".");
		String Project_Loc = RootPath.getAbsolutePath().substring(0, RootPath.getAbsolutePath().length() - 1);		
		String Import_FileName = Project_Loc + "\\TIMS_PO_Region.txt";
		String TestData = Project_Loc + "\\TIMS_Data.txt";	
		String Output_File = Project_Loc + "\\Test_Output.txt";

		LoginPage.using(driver)
				.setUsername("Testadmin")
				.setPassword("1234567")
				.login();
		SideMenu.using(driver)
				.NavigateTo("Administration -> Manual Import")
				.clickMenu("Manual Import");
		HashSet<String> All_PO = CommonPage.using(driver)
				  .SetDocType("Purchase order")
				  .GetPO_TxtFile(Import_FileName);
		CommonPage.using(driver)
				  .SetDocType("Purchase order")
				  .GetSuppID_TxtFile(Import_FileName);
		if(All_PO.size()>0)
		{
			Admin_ManualImport.using(driver)
					.FileChooser(Import_FileName)
					.Import();
			Boolean Result_Success = SideMenu.using(driver)
					.MoveTo_MsgBox()
					.Wait_SuccessMsg("Import file was placed into the");
			if(Result_Success)
			{
				SideMenu.using(driver)
						.NavigateTo("Subsystems")
						.clickMenu("Subsystems");
				CommonPage.using(driver)
						.Enter_Identification("POIMP")
						.Click_Read_Btn()
						.Click_Start_Btn()
						.Alert_Accept();
				
				
				String PO_Num = All_PO.toArray()[0].toString();				
				SideMenu.using(driver)
						.NavigateTo("Reports -> Flowtrack report")
						.clickMenu("Flowtrack report");

				CommonPage.using(driver)
				      .SetDocType("Purchase order")
				      .SetDocNo(PO_Num);
				Boolean Report_Check = false;
				int Break_Loop = 0;
				while(Break_Loop++ < 100)
				{
					CommonPage.using(driver)
				      	  .SetDocNo(PO_Num)
					      .Click_Read_Btn();
					Report_Check = Admin_FlowTrackReport.using(driver)
										.CheckReports();					
					Thread.sleep(2000);
					if(Report_Check)
						break;
					Thread.sleep(3000);
				}
		
				if(Report_Check)
				{					
					List<String> Split_Eachline_TD = Files.lines(Paths.get(TestData)).collect(Collectors.toList());					
					HashMap<String, HashSet<String>> ASN_Pallet = new HashMap<String, HashSet<String>>();
					HashSet<String> Split_Po = new HashSet<String>();
					
					SideMenu.using(driver)
							.clickMenu("Log out");
					CommonPage.using(driver)
							.Logout_Btn();
					LoginPage.using(driver)
							.setUsername("Testsupp")
							.setPassword("1234567")
							.login();
					
					for(String Indv_Line:Split_Eachline_TD)
					{
						if(Indv_Line.trim()!=null)
							for(String Indv_PO:Indv_Line.split("##"))
								Split_Po.add(Indv_PO.split("\\$\\$")[0]);
						
						SideMenu.using(driver)
								.NavigateTo("Documents -> Orders")
								.clickMenu("Orders");
						Supp_PurchaseOrder.using(driver)
								.SetDeliveryDate();
						CommonPage.using(driver)
							    .Click_Read_Btn()
							    .Wait_TillRecordDisplay(Split_Po);					
						Supp_PurchaseOrder.using(driver)
								.Select_PO(Split_Po)
								.Click_CreateASNBtn();
						Split_Po.clear();
						String ASN_ID = CreateASN.using(driver)
								.SltASNType("Supplier to Tesco")
								.Click_Update_Btn()
								.Get_ASN_ID();
						Boolean ASN_Result = SideMenu.using(driver)
								.Wait_SuccessMsg("ASN was updated");
						CreateASN.using(driver)
								.Click_Items_Btn()
								.Enter_PalletName(TestData)
								.Click_Update_Btn();
						ASN_Result = SideMenu.using(driver)
								.Wait_SuccessMsg("ASN was updated");
						if(ASN_Result)
						{
							CreateASN.using(driver)
									.Click_Return_Btn()
									.NavigateTo_Tab("ASN Pallets")
									.GenerateSSCC_Btn();
							HashSet<String> SSCC_Val = new HashSet<String>();
							SSCC_Val = CreateASN.using(driver)
									.Get_SSCC_Value();
							CreateASN.using(driver)
									.SltAll_SltBtn()
									.Click_Update_Btn();
							ASN_Result = SideMenu.using(driver)
									.Wait_SuccessMsg("ASN was updated");
							if(ASN_Result)
							{
								CreateASN.using(driver)
										.NavigateTo_Tab("ASN Header")
										.Set_LoadRefID()
										.Set_BookTime()
										.Click_Update_Btn();
								ASN_Result = SideMenu.using(driver)
										.Wait_SuccessMsg("ASN was updated");
								CommonPage.using(driver)
										.Click_Submit_Btn()
										.Alert_Accept();
								ASN_Result = SideMenu.using(driver)
										.Wait_SuccessMsg("ASN was submitted");
								
								ASN_Pallet.put(ASN_ID, SSCC_Val);
							}
						}
					}
					
					if(ASN_Pallet.size()>0)
					{
						SideMenu.using(driver)
								.clickMenu("Log out");	
						CommonPage.using(driver)
								.Logout_Btn();	
						LoginPage.using(driver)
								.setUsername("Testadmin")
								.setPassword("1234567")
								.login();
						SideMenu.using(driver)
								.NavigateTo("Subsystems")
								.clickMenu("Subsystems");
						CommonPage.using(driver)
								.Enter_Identification("ASNEXP")
								.Click_Read_Btn()
								.Click_Start_Btn()
								.Alert_Accept();
						Boolean ASN_Result = SideMenu.using(driver)
								.Wait_SuccessMsg("Requested process was started");
						if(ASN_Result)
							Files.write(Paths.get(Output_File), (ASN_Pallet.toString()).getBytes());	
					}
					
				}
			}
		}
	}
}