import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {
	
	ArrayList<Process> pList;
	
	public ReadFile()
	{
		pList = new ArrayList<Process>();
	}
	public ArrayList<Process> buildList(){
	
		try 
		{
			FileInputStream fstream;
			fstream = new FileInputStream("assignment2.txt");
			//To be able to strings line by line we must used a BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String line;
			while((line = br.readLine()) != null) //Read until the end of file is reached
			{
				String[] parts = line.split("\\s+"); //Split the string into 3 parts using whitespace as the delimiter
				Process temp = new Process(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])); //Extract each column from a row in the input table and assign them to their respective values in the process class object.
				pList.add(temp); //Add hte process object to an arrayList which will be passed to a scheduler,
			}
			br.close();
			fstream.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return pList;
	}
}
