package synergynet.table.apps.threemanipulationExperiment.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class DateTextWritter {
		
	protected String fileName;
	
	@SuppressWarnings("deprecation")
	public String createFile(){
	
		Date currentDate = new Date();
		String dateString = currentDate.getMonth()+1+"-"+currentDate.getDate()+" ["+currentDate.getHours()+"~"+currentDate.getMinutes()+"~"+
			currentDate.getSeconds()+"]";
		fileName = "log//3DExperimentLog/Excel/"+dateString+" (3D Manipulation).txt";
		
		try {
			
			FileWriter out = new FileWriter(fileName);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(currentDate.toString()+" 3D Manipulation)\n");
			writer.write("InputMode Size CompletedTime Actions Successful\n");
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return fileName;
				
	}
	
	public void addRecord(LogRecord logRecord){
		
		long completedTime = logRecord.endTime - logRecord.startTime;
		
		try {		
			FileWriter out = new FileWriter(fileName, true);
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(logRecord.getInputMode()+" "+logRecord.getSize()+" "+completedTime+" "+logRecord.getActions()+" "+logRecord.isSuccessful()+"\n");
			writer.close();		
		} catch (IOException e) {
			e.printStackTrace();
		} 
				
	}
	

}
