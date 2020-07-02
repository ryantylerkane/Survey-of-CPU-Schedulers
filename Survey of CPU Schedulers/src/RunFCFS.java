import java.util.ArrayList;

public class RunFCFS extends Run{

	static FCFS queue = new FCFS(); 
	
	public RunFCFS()
	{
		super();
		queue = new FCFS();
	}
	
	public RunFCFS(ArrayList<Process>p)
	{
		super(p, queue);
		
	}
	
	public void scheduleList()
	{
		System.out.println("Scheduling algorithm: First Come First Serve");
		System.out.println("========================================================");
		super.scheduleList();
		/*while(!processes.isEmpty() || !queue.readyQueue.isEmpty() || currentlyRunning!=null) //Stop running when there are no processes on the ready queue or waiting to be loaded on the ready queue and the CPU is empty.
		{
			checkLTScheduler();
			if(currentlyRunning == null)
			{
				if(!queue.readyQueue.isEmpty()&&queue.readyQueue.get(0).getArrival() <= currentTime) //If the processor is unoccupied and the readyQueue has a process that is able to run, load it in.
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
		
		generateStats();*/
	}
	
	
}
