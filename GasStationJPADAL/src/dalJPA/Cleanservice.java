package dalJPA;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the cleanservices database table.
 * 
 */
@Entity
@Table(name="cleanservices")
@NamedQuery(name="Cleanservice.findAll", query="SELECT c FROM Cleanservice c")
public class Cleanservice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int cleanServiceID;

	private BigDecimal cleanServicePrice;

	private int numOfInsideTeams;

	//bi-directional many-to-one association to Gasstation
	@OneToMany(mappedBy="cleanservice")
	private List<Gasstation> gasstations;

	public Cleanservice() {
	}

	public int getCleanServiceID() {
		return this.cleanServiceID;
	}

	public void setCleanServiceID(int cleanServiceID) {
		this.cleanServiceID = cleanServiceID;
	}

	public BigDecimal getCleanServicePrice() {
		return this.cleanServicePrice;
	}

	public void setCleanServicePrice(BigDecimal cleanServicePrice) {
		this.cleanServicePrice = cleanServicePrice;
	}

	public int getNumOfInsideTeams() {
		return this.numOfInsideTeams;
	}

	public void setNumOfInsideTeams(int numOfInsideTeams) {
		this.numOfInsideTeams = numOfInsideTeams;
	}

	public List<Gasstation> getGasstations() {
		return this.gasstations;
	}

	public void setGasstations(List<Gasstation> gasstations) {
		this.gasstations = gasstations;
	}

	public Gasstation addGasstation(Gasstation gasstation) {
		getGasstations().add(gasstation);
		gasstation.setCleanservice(this);

		return gasstation;
	}

	public Gasstation removeGasstation(Gasstation gasstation) {
		getGasstations().remove(gasstation);
		gasstation.setCleanservice(null);

		return gasstation;
	}

}