package dalJPA;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the gasstationhistorylog database table.
 * 
 */
@Entity
@NamedQuery(name="Gasstationhistorylog.findAll", query="SELECT g FROM Gasstationhistorylog g")
public class Gasstationhistorylog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int gasStationHistoryLogID;

	private int actionTypeID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	private int serviceEntityID;

	private int serviceEntityTypeID;

	//bi-directional many-to-one association to Car
	@ManyToOne
	@JoinColumn(name="CarID")
	private Car car;

	public Gasstationhistorylog() {
	}

	public int getGasStationHistoryLogID() {
		return this.gasStationHistoryLogID;
	}

	public void setGasStationHistoryLogID(int gasStationHistoryLogID) {
		this.gasStationHistoryLogID = gasStationHistoryLogID;
	}

	public int getActionTypeID() {
		return this.actionTypeID;
	}

	public void setActionTypeID(int actionTypeID) {
		this.actionTypeID = actionTypeID;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getServiceEntityID() {
		return this.serviceEntityID;
	}

	public void setServiceEntityID(int serviceEntityID) {
		this.serviceEntityID = serviceEntityID;
	}

	public int getServiceEntityTypeID() {
		return this.serviceEntityTypeID;
	}

	public void setServiceEntityTypeID(int serviceEntityTypeID) {
		this.serviceEntityTypeID = serviceEntityTypeID;
	}

	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

}