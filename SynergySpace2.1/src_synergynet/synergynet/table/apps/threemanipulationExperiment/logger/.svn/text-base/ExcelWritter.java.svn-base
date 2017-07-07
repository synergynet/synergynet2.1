package synergynet.table.apps.threemanipulationExperiment.logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWritter {
	
	public static void txtToExcelFile(String txtFile){
	
		WritableWorkbook workbook;
		WritableSheet sheet;
		
		String excelFileName = "log//3DExperimentLog//Excel//Data-Part1p1.xls";
		
		try {
			workbook = Workbook.createWorkbook(new File(excelFileName));
			sheet = workbook.createSheet("P2", 0);
			
			
			FileInputStream fstream = new FileInputStream(txtFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
					
			//add title of this file
			Label label = new Label(0, 0, br.readLine()); 
			sheet.addCell(label); 
			
			//add column title
			label = new Label(1, 2, "Small"); 
			sheet.addCell(label); 
			label = new Label(2, 2, "Normal"); 
			sheet.addCell(label); 
			label = new Label(3, 2, "Big"); 
			sheet.addCell(label);
			label = new Label(0, 3, "AbsoluteMapping"); 
			sheet.addCell(label); 
			label = new Label(0, 8, "ReletiveMapping"); 
			sheet.addCell(label); 
			
			label = new Label(1+6, 2, "Small"); 
			sheet.addCell(label); 
			label = new Label(2+6, 2, "Normal"); 
			sheet.addCell(label); 
			label = new Label(3+6, 2, "Big"); 
			sheet.addCell(label);
			label = new Label(0+6, 3, "AbsoluteMapping"); 
			sheet.addCell(label); 
			label = new Label(0+6, 8, "ReletiveMapping"); 
			sheet.addCell(label);

					
			String strLine;
			br.readLine();
			int i, j;
			String currentGroup="";
			int position=0;
			while ((strLine = br.readLine()) != null)   {
				  String[] record = strLine.trim().split(" ");
				  
				  if (!currentGroup.equals(record[0]+record[1])){
					  currentGroup=record[0]+record[1];
					  position =0;
				  }
				  
				  if (record[0].equals("Direct")){
					  j = 3+position;
				  }
				  else{
					  j = 8+position;
				  }
				  
				  if (record[1].equals("small")){
					  i = 1;
				  }
				  else if (record[1].equals("middium")){
					  i = 2;
				  }
				  else{
					  i = 3;
				  }
				  
				//add column title
				label = new Label(i, j, record[2]); 
				sheet.addCell(label); 
				
				label = new Label(i+6, j, record[3]); 
				sheet.addCell(label); 
				
				position++;

			}
			
			in.close();
						
			workbook.write(); 
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
				
	}

	
	public static void main(String[] args) {
		ExcelWritter.txtToExcelFile("log//3DExperimentLog//Excel//4-15 [10~6~17] (3D Manipulation).txt");
	}

}
