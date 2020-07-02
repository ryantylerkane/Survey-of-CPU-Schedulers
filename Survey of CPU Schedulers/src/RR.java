
public class RR extends Scheduler{

	public RR()
	{
		super();
	}
	
	@Override
	public void add(Process p) {
		
		readyQueue.add(readyQueue.size(), p);		
	}


	@Override
	public void remove(int id) {
		
		for(int i =0; i <= readyQueue.size()-1; i++)
		{
			if(readyQueue.get(i).getId() == id)
			{
				readyQueue.remove(i);
				break;
			}
		}
	}

}
