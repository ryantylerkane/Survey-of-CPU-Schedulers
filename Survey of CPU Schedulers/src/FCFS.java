
public class FCFS extends Scheduler{
	
	public FCFS() {
		super();
		
	}
	
	public void add(Process p)
	{
		readyQueue.add(readyQueue.size(),p); //Use the size() method to add to the end of the linked list.
	}
	
	public void remove(int index)
	{
		readyQueue.remove(index);
	}

}
