package dal.dataObjects;

public class CleaningServiceObject {

	protected int id;
	protected int numOfInsideTeams;
	protected int price;
	protected int secondsPerAutoClean;
	
	public CleaningServiceObject(int id, int numOfInsideTeams, int price, int secondsPerAutoClean){
		this.id = id;
		this.numOfInsideTeams = numOfInsideTeams;
		this.price = price; 
		this.secondsPerAutoClean = secondsPerAutoClean;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumOfInsideTeams() {
		return numOfInsideTeams;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getSecondsPerAutoClean() {
		return secondsPerAutoClean;
	}

}
