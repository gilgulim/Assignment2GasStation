package dataObjects;

public class CleaningServiceObject {

	private int id;
	private int numOfInsideTeams;
	private float price;
	
	public CleaningServiceObject(int id, int numOfInsideTeams, int price){
		this.id = id;
		this.numOfInsideTeams = numOfInsideTeams;
		this.price = id; 
	}
	
	public CleaningServiceObject(){
		
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumOfInsideTeams() {
		return numOfInsideTeams;
	}
	
	public float getPrice() {
		return price;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNumOfInsideTeams(int numOfInsideTeams) {
		this.numOfInsideTeams = numOfInsideTeams;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
}
