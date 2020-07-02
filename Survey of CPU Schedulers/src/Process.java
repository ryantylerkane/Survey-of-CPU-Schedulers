
public class Process{

	private int id;
	private int arrival;
	private int burstLength;
	private int wTime;
	private int tTime;
	private int rTime;
	private boolean usedCPU; //Used to keep track of whether a process's response time is to increase or not.
	
	public Process()
	{
		id = 0;
		arrival = 0;
		burstLength = 0;
		wTime = 0;
		rTime = 0;
		tTime = 0;
		usedCPU = false;
	}
	public Process(int id, int arrival, int burstLength) {
		super();
		this.id = id;
		this.arrival = arrival;
		this.burstLength = burstLength;
		wTime = 0;
		rTime = 0;
		tTime = 0;
		usedCPU = false;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getArrival() {
		return arrival;
	}
	public void setArrival(int arrival) {
		this.arrival = arrival;
	}
	public int getBurstLength() {
		return burstLength;
	}
	public void setBurstLength(int burstLength) {
		this.burstLength = burstLength;
	}
	public int getwTime() {
		return wTime;
	}
	public void setwTime(int wTime) {
		this.wTime = wTime;
	}
	public int gettTime() {
		return tTime;
	}
	public void settTime(int tTime) {
		this.tTime = tTime;
	}
	public int getrTime() {
		return rTime;
	}
	public void setrTime(int rTime) {
		this.rTime = rTime;
	}
	public boolean hasUsedCPU() {
		return usedCPU;
	}
	public void setUsedCPU(boolean usedCPU) {
		this.usedCPU = usedCPU;
	}
}
