package dalJPA;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the gasstations database table.
 * 
 */
@Entity
@Table(name="gasstations")
@NamedQuery(name="Gasstation.findAll", query="SELECT g FROM Gasstation g")
public class Gasstation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int gasStationID;

	//bi-directional many-to-one association to Mainfuelpool
	@ManyToOne
	@JoinColumn(name="GasStation_MainFuelPoolID")
	private Mainfuelpool mainfuelpool;

	//bi-directional many-to-one association to Cleanservice
	@ManyToOne
	@JoinColumn(name="GasStation_CleanServiceID")
	private Cleanservice cleanservice;

	public Gasstation() {
	}

	public int getGasStationID() {
		return this.gasStationID;
	}

	public void setGasStationID(int gasStationID) {
		this.gasStationID = gasStationID;
	}

	public Mainfuelpool getMainfuelpool() {
		return this.mainfuelpool;
	}

	public void setMainfuelpool(Mainfuelpool mainfuelpool) {
		this.mainfuelpool = mainfuelpool;
	}

	public Cleanservice getCleanservice() {
		return this.cleanservice;
	}

	public void setCleanservice(Cleanservice cleanservice) {
		this.cleanservice = cleanservice;
	}

}