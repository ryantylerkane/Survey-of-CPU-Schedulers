
public abstract class Scheduler {

	public CircularLinkedList<Process> readyQueue; 
	
	public Scheduler(){
	readyQueue = new CircularLinkedList<Process>();
	}
	abstract public void add(Process p); //Adds a process into a ready queue based on scheduling algorithm.
	abstract public void remove(int index); //Removes a process from the ready queue

		

}
