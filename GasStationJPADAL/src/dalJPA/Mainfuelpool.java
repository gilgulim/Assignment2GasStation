package dalJPA;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the mainfuelpool database table.
 * 
 */
@Entity
@NamedQuery(name="Mainfuelpool.findAll", query="SELECT m FROM Mainfuelpool m")
public class Mainfuelpool implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int mainFuelPoolID;

	private int maxCapacity;

	//bi-directional many-to-one association to Gasstation
	@OneToMany(mappedBy="mainfuelpool")
	private List<Gasstation> gasstations;

	public Mainfuelpool() {
	}

	public int getMainFuelPoolID() {
		return this.mainFuelPoolID;
	}

	public void setMainFuelPoolID(int mainFuelPoolID) {
		this.mainFuelPoolID = mainFuelPoolID;
	}

	public int getMaxCapacity() {
		return this.maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public List<Gasstation> getGasstations() {
		return this.gasstations;
	}

	public void setGasstations(List<Gasstation> gasstations) {
		this.gasstations = gasstations;
	}

	public Gasstation addGasstation(Gasstation gasstation) {
		getGasstations().add(gasstation);
		gasstation.setMainfuelpool(this);

		return gasstation;
	}

	public Gasstation removeGasstation(Gasstation gasstation) {
		getGasstations().remove(gasstation);
		gasstation.setMainfuelpool(null);

		return gasstation;
	}

}