import java.util.ArrayList;

public class RunSRTF extends Run{

	static SRTF queue= new SRTF();
	
	public RunSRTF()
	{
		super();
		queue = new SRTF();
	}
	
	public RunSRTF(ArrayList<Process>p)
	{
		super(p, queue);
		
	}
	
	@Override
	protected void loadCPU()
	{
		currentlyRunning = queue.readyQueue.get(0);
		queue.remove(currentlyRunning.getId());
	}
	
	@Override
	protected void checkJobPool() {
		ArrayList<Process> toRemove = new ArrayList<Process>(); //Make a new ArrayList to hold extracted processes to be removed. 
		for(Process temp: processes)
		{
			if(temp.getArrival() == currentTime)
			{	queue.add(temp);
				toRemove.add(temp);
			}
		}
		
		processes.removeAll(toRemove); //Remove the element from the long term scheduler
		processes.trimToSize(); //Narrow the arrayList down to the correct size so that no empty spaces are left.
		
		if(!toRemove.isEmpty() && currentlyRunning != null)
		{
			for(Process added: toRemove) //Check if the added processes have a burstLength smaller than current.
		{
			if(added.getBurstLength() < currentlyRunning.getBurstLength() || added.getBurstLength() == currentlyRunning.getBurstLength() && added.getArrival()<currentlyRunning.getArrival())
			{
				queue.remove(added.getId());
				queue.add(currentlyRunning);
				currentlyRunning = added;
				contactedCPU(currentlyRunning);	
			}
		}
	}
}
	
	public void scheduleList()
	{
		
		System.out.println("Scheduling algorithm: Shortest Remaining Time First");
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
