package dataObjects;

public class CleaningServiceObject {

	private int id;
	private int numOfInsideTeams;
	private int price;
	
	public CleaningServiceObject(int id, int numOfInsideTeams, int price){
		this.id = id;
		this.numOfInsideTeams = numOfInsideTeams;
		this.price = id; 
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
}
