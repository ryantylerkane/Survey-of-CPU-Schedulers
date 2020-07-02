import java.util.ArrayList;
import java.util.Iterator;

public class RunRR extends Run{

static RR queue= new RR();
int quantum;

	public RunRR()
	{
		super();
		queue = new RR();
		quantum = 10;
	}
	
	public RunRR(ArrayList<Process>p, int timeQuantum)
	{
		super(p, queue);
		quantum = timeQuantum;
		
	}
	
@Override
protected void increaseWait() { //Increases the wait time of each process in the waiting queue.
	for(int i = 0; i <= queue.readyQueue.size()-1; i++)
	{
		if(queue.readyQueue.get(i).getId() != currentlyRunning.getId())
			queue.readyQueue.get(i).setwTime(queue.readyQueue.get(i).getwTime() + 1);
	}
}

@Override
protected void increaseTurnaround() //Increases the turnaround time of each unfinished process and the process currently executing on the CPU.
{
	for(int i = 0; i <= queue.readyQueue.size()-1; i++)
	{
		queue.readyQueue.get(i).settTime(queue.readyQueue.get(i).gettTime() + 1);
	}
}
	
	@Override
	protected void checkBurstLength() { //Checks if the last quantum burst caused a process to be complete and sets the CPU to null so that it can service another process.
		if(currentlyRunning.getBurstLength()==0 || currentlyRunning.getBurstLength()<0) //The process on the CPU is finished and needs to be discarded.
		{
			System.out.println("<system time  " + currentTime + "> process "+ currentlyRunning.getId()+" is finished...");
			currentlyRunning.settTime(currentlyRunning.gettTime()+(quantum+currentlyRunning.getBurstLength())); //Add the getBurstLength in the event the actual burst was less than the quantum time. This will reduce the turnaround time added to the process by the proper amount.
			completedProcesses.add(currentlyRunning); //Add the completed process to the ArrayList so it can be used to calculate statistics.
			currentlyRunning = null; //Clear the CPU
		}
		else //Process still requires more time on the CPU.
		{
			currentlyRunning = null;
		}
	}
	
	@Override
	protected void processBurst() //Applies the quantum burst length to a currently running process and 
	{
		
		contactedCPU(currentlyRunning);
		if(currentlyRunning.getBurstLength() < quantum)
		{
			for(int i = 0; i<currentlyRunning.getBurstLength();i++) //Use for loop to write output messages for the length of a burst that is less than the quantum time.
			{
				int temp=currentTime+i;
				System.out.println("<system time  " + temp + "> process " + currentlyRunning.getId()+ " is running.");
			}	
			//Must remove the process from the queue here in order to generate correct stats.
			int burst =currentlyRunning.getBurstLength(); //Record the burst length of the last process prior to subtracting the time quantum from the value so that the statistics for the wait, turnaround, and response times can be properly calculated
			currentlyRunning.setBurstLength(currentlyRunning.getBurstLength()-quantum); //Make this a negative value so that it can be used to calculate the correct turnaround time in the checkBurst function.
			queue.remove(currentlyRunning.getId());
			increaseTime(burst);
		}
		else //burstLength == quantum time or burstLength > quantum time
		{
			currentlyRunning.setBurstLength(currentlyRunning.getBurstLength()-quantum); //Subtract the time quantum from the remaining burstLength.
			if(currentlyRunning.getBurstLength() == 0) //If the process no longer requires time on the CPU, remove it from the ready queue.
				queue.remove(currentlyRunning.getId());
			
			for(int i = 0; i<quantum;i++) //Use a for loop to output messages for the length of a single time quantum.
			{
				int temp = currentTime + i;
				System.out.println("<system time  " + temp + "> process " + currentlyRunning.getId()+ " is running.");
			}
			increaseTime(quantum);
		}
		checkBurstLength(); //Check the process's remaining burst time so that it can either be properly removed or put back onto the ready queue.
	}
	
	
	
	private void increaseTime(int burstLength) { //Increases the statistics, currentTime and checks the job pool every second during a time quantum.
		for(int i= 0; i < burstLength; i++)
		{
			increaseStats();
			currentTime++;
			checkJobPool();
		}
	}
	
	public void scheduleList()
	{
		System.out.println("Scheduling algorithm: Round Robin");
		System.out.println("========================================================");
		while(!processes.isEmpty()||!queue.readyQueue.isEmpty()) //Stop running when there are no processes on the ready queue or waiting to be loaded on the ready queue and the CPU is empty.
		{
			checkJobPool();
			if(!queue.readyQueue.isEmpty()) //If there are jobs on the ready queue, then process them.
			{
				//Process the first element and then use the iterator to cover the rest. 
				currentlyRunning = queue.readyQueue.get(0);
				Process checkFront = currentlyRunning; //Save the front of the list in the event that the first element in the list changes during quantum burst.
				processBurst();
				if(!queue.readyQueue.isEmpty() && queue.readyQueue.get(0).getId() == checkFront.getId())
				{
					for(Iterator<Process> i = queue.readyQueue.iterator(); i.hasNext();)
					{
						currentlyRunning = i.next();
						processBurst();
					}
				}
			}
			else { //The CPU is idle.
					System.out.println("<system time  " + currentTime + "> no process is currently running.");
					cpuNotUsed++; //Increase the CPU not used variable by one.
					currentTime++;
				}
			checkComplete(); //Outputs completion message once system is done running.
		}
		generateStats(); //Calculates the average waiting time, turnaround time, and response time.
	}
}
