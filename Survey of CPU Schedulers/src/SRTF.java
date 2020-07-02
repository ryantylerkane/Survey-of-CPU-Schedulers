
public class SRTF extends Scheduler{
	
	public void add(Process p)
	{
		if(readyQueue.isEmpty())
		{
			readyQueue.add(p);
		}
		else{
		for(int i = 0; i <= readyQueue.size()-1; i++) //Traverse the list
		{
			if(p.getBurstLength()<readyQueue.get(i).getBurstLength()) //If the burstLength of the process to be added is less than a value in the list. Add the new process in its place.
			{
				readyQueue.add(i, p);
				break;
			}
			
			else if(p.getBurstLength()==readyQueue.get(i).getBurstLength()) //If the burstLEngth of the process to be added is equal to one of the burst lengths on the queue.
			{
				if(p.getArrival() < readyQueue.get(i).getArrival()) //If the process to be added has a smaller arrival time, add it closer to the front of the queue.
				{
					readyQueue.add(i, p);
					break;
				}
				else if(p.getArrival() > readyQueue.get(i).getArrival())
				{
					readyQueue.add(i+1, p);
					break;
				}
				else //Two processes have the same burst length and arrival time
				{
					readyQueue.add(i+1, p);
					break;
				}
			}
			else //p.getBurstLength() > readyQueue.get(i).burstLength
			{
				if(i==readyQueue.size()-1) //If we reach the end of the linked list and the process still hasn't found a place to be added.
				{	
					readyQueue.add(p);
					break;
				}
			}
		}
	}	
		
}

	public void remove(int id) //Method will locate and a remove a method that was preempted in.
	{
		for(int i = 0; i <= readyQueue.size()-1; i++) //Traverse the list
		{
			if(id == readyQueue.get(i).getId())
			{
				readyQueue.remove(i);
				break;
			}
		}
	}
	
	
}	