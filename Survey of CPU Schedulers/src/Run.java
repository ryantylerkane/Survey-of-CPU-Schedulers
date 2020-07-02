import java.util.ArrayList;


public abstract class Run {
	
	protected int currentTime;
	Process currentlyRunning;
	ArrayList<Process>processes;
	Scheduler readyQueue;
	ArrayList<Process>completedProcesses;
	protected int cpuNotUsed;
	
	public Run()
	{
		currentTime = 0;
		currentlyRunning = null;
		processes = new ArrayList<Process>();
		completedProcesses = new ArrayList<Process>();
		cpuNotUsed = 0;
	}
	public Run(ArrayList<Process> p, Scheduler rq)
	{
		currentTime = 0;
		currentlyRunning = null;
		processes = p;
		readyQueue =rq; 
		completedProcesses = new ArrayList<Process>();
		cpuNotUsed = 0;
	}
	
	public int getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}
	public int getCpuNotUsed() {
		return cpuNotUsed;
	}
	public void setCpuNotUsed(int cpuNotUsed) {
		this.cpuNotUsed = cpuNotUsed;
	}
	
	protected void loadCPU() //Since the ready queue added in 'priority' order from the job queue, we can always load the CPU from the front of the ready queue.
	{
		currentlyRunning = readyQueue.readyQueue.get(0);
		readyQueue.remove(0);
	}
	protected void checkJobPool() //Check if there is a process whose arrival time indicates it should be added to the ready queue.
	{
		ArrayList<Process> toRemove = new ArrayList<Process>(); //Make a new ArrayList to hold extracted processes to be removed. 
		for(Process temp: processes) //For all of the jobs not yet on the ready queue.
		{
			if(temp.getArrival() == currentTime) //If there arrival time is the same as the current time.
			{
				readyQueue.add(temp); //Add the process to the readyQueue.
				toRemove.add(temp); //Add the process to be removed from the job pool.
			}
		}
		
		processes.removeAll(toRemove); //Remove the element from the job pool.
		processes.trimToSize(); //Narrow the arrayList down to the correct size so that no empty spaces are left.
	}
	
	protected void checkBurstLength() //Checks if the process currently on the CPU is finished and removes it from currentlyRunning.
	{
		if(currentlyRunning.getBurstLength()==0) //The process on the CPU is finished and needs to be discarded.
		{
			int temp = currentTime + 1;
			System.out.println("<system time  " + temp + "> process "+ currentlyRunning.getId()+" is finished...");
			currentlyRunning.settTime(currentlyRunning.gettTime()+1);
			completedProcesses.add(currentlyRunning); //Add the completed process to the ArrayList so it can be used to calculate statistics.
			currentlyRunning = null; //Clear the CPU
		}
	}
	
	protected void scheduleList()
	{
		while(!processes.isEmpty() || !readyQueue.readyQueue.isEmpty() || currentlyRunning!=null) //Stop running when there are no processes on the ready queue or waiting to be loaded on the ready queue and the CPU is empty.
		{
			checkJobPool();
			if(currentlyRunning == null)
			{
				if(!readyQueue.readyQueue.isEmpty()&& readyQueue.readyQueue.get(0).getArrival() <= currentTime) //If the processor is unoccupied and the readyQueue has a process that is able to run, load it in.
				{
					loadCPU();
					processBurst();
				}
				else {
				System.out.println("<system time  " + currentTime + "> no process is currently running.");
				cpuNotUsed++; //Increase the CPU not used variable by one.
				}
			}
			else
			{
				processBurst();
			}
			
			increaseStats();
			currentTime++; 
			checkComplete();
		}
		
		generateStats();
	}
	
	
	
	protected void processBurst()
	{
		contactedCPU(currentlyRunning); //Indicate that a process has reached the CPU in order to calculate its response time.
		System.out.println("<system time  " + getCurrentTime() + "> process " + currentlyRunning.getId()+ " is running.");
		currentlyRunning.setBurstLength(currentlyRunning.getBurstLength()-1); //Decrease the burst length by one.
		checkBurstLength(); //Check if the process is done
	}
	
	protected void checkComplete() //Checks if all processes in input file has been executed and outputs completed message.
	{
		if(currentlyRunning == null && processes.isEmpty() && readyQueue.readyQueue.isEmpty())
		{
			System.out.println("<system time  " + currentTime + "> All processes are finished.");
			System.out.println("========================================================================");
		}
	}
	
	protected void increaseWait() { //Method will increase the wait time variable for each process in the ready queue.
		for(int i = 0; i <= readyQueue.readyQueue.size()-1; i++)
		{
			readyQueue.readyQueue.get(i).setwTime(readyQueue.readyQueue.get(i).getwTime() + 1);
		}
		
	}
	
	protected void increaseTurnaround() //Method will increase any incomplete process's turnaround time by one.
	{
		for(int i = 0; i <= readyQueue.readyQueue.size()-1; i++) //Increase every process in the ready queue's turnaround time by 1.
		{
			readyQueue.readyQueue.get(i).settTime(readyQueue.readyQueue.get(i).gettTime() + 1);
		}
				
		if(currentlyRunning!=null) //Process currently on CPU still needs to increase turnaround time.
		{
			currentlyRunning.settTime(currentlyRunning.gettTime()+1);
		}
	}
	
	protected void increaseResponse() //Checks to see which processes have yet to reach CPU and increases its response time by 1
	{
		for(int i = 0; i <= readyQueue.readyQueue.size()-1; i++) //Increase every process in the ready queue's turnaround time by 1.
		{
			if(!readyQueue.readyQueue.get(i).hasUsedCPU())
			{
				readyQueue.readyQueue.get(i).setrTime(readyQueue.readyQueue.get(i).getrTime() + 1);
			}
		}
	}
	
	protected void increaseStats() //Increases the fields used to generate average statistics after each CPU burst.
	{
		increaseResponse();
		increaseTurnaround();
		increaseWait();
		
	}
	
	protected void generateStats() //Uses the all of the completed processes' data variables to generate average wait time, turnaround time, responseTime, and CPU utilization percentage.
	{
		double wAverage = 0.0; 
		double tAverage=0.0;
		double rAverage = 0.0;
		double cpuAverage = 0.0;
		
		for(Process temp: completedProcesses)  //Take the sum of the individual categories for all processes.
		{
			wAverage += temp.getwTime();
			tAverage += temp.gettTime();
			rAverage += temp.getrTime();
		}
		
		wAverage = wAverage / completedProcesses.size();
		tAverage = tAverage / completedProcesses.size();
		rAverage = rAverage / completedProcesses.size();
		double temp = currentTime-cpuNotUsed;
		cpuAverage = temp/currentTime * 100;

		System.out.printf("Average CPU Usage: %11.2f%%\n", cpuAverage);
		System.out.printf("Average waiting time: %8.2f\n", wAverage);
		System.out.printf("Average response time: %7.2f\n", rAverage);
		System.out.printf("Average turnaround time: %.2f", tAverage);
	}
	
	protected void contactedCPU(Process currentlyRunning) //Indicates that the process has reached the CPU to help measure resposne time.
	{
		if(!currentlyRunning.hasUsedCPU())
		{
			currentlyRunning.setUsedCPU(true);
		}
	}

}
