import java.util.NoSuchElementException;

public class Assignment2 {
	
	public static void main(String args[])
	{
		ReadFile read = new ReadFile(); //Read the values from the input file.
		
		if(args[0].equals("FCFS"))
		{
			RunFCFS fcfs = new RunFCFS(read.buildList());
			fcfs.scheduleList();
		}
		else if(args[0].equals("SRTF"))
		{
			RunSRTF srtf = new RunSRTF(read.buildList());
			srtf.scheduleList();
		}
		else if(args[0].equals("RR"))
		{
			RunRR rr = new RunRR(read.buildList(), Integer.parseInt(args[1]));
			rr.scheduleList();
		}
		else
		{
			throw new NoSuchElementException("The scheduling algorithm provided is not valid!");
		}
	}

}
