package dal;

import com.google.gson.annotations.Expose;

public class CarObject {
	
	@Expose
	protected int id;
	@Expose
	protected Boolean finishCleaning;
	@Expose
	protected Boolean finishFuel;
	@Expose
	protected Boolean wantCleaning;
	@Expose
	protected Boolean wantFuel;
	@Expose
	protected int numOfLiters;
	@Expose
	protected int pumpNum;
	
	
	public Boolean getFinishCleaning() {
		return finishCleaning;
	}
	public void setFinishCleaning(Boolean finishCleaning) {
		this.finishCleaning = finishCleaning;
	}
	public Boolean getFinishFuel() {
		return finishFuel;
	}
	public void setFinishFuel(Boolean finishFuel) {
		this.finishFuel = finishFuel;
	}
	public int getId() {
		return id;
	}
	public Boolean getWantCleaning() {
		return wantCleaning;
	}
	public Boolean getWantFuel() {
		return wantFuel;
	}
	public int getNumOfLiters() {
		return numOfLiters;
	}
	public int getPumpNum() {
		return pumpNum;
	}
	
	
}
