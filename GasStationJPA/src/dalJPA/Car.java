package dalJPA;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cars database table.
 * 
 */
@Entity
@Table(name="cars")
@NamedQuery(name="Car.findAll", query="SELECT c FROM Car c")
public class Car implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int carID;

	private int carFuelQuantity;

	private byte isCarReqFuel;

	private byte isCarReqWash;

	//bi-directional many-to-one association to Pump
	@ManyToOne
	@JoinColumn(name="CarPumpID")
	private Pump pump;

	//bi-directional many-to-one association to Gasstationhistorylog
	@OneToMany(mappedBy="car")
	private List<Gasstationhistorylog> gasstationhistorylogs;

	public Car() {
	}

	public int getCarID() {
		return this.carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public int getCarFuelQuantity() {
		return this.carFuelQuantity;
	}

	public void setCarFuelQuantity(int carFuelQuantity) {
		this.carFuelQuantity = carFuelQuantity;
	}

	public byte getIsCarReqFuel() {
		return this.isCarReqFuel;
	}

	public void setIsCarReqFuel(byte isCarReqFuel) {
		this.isCarReqFuel = isCarReqFuel;
	}

	public byte getIsCarReqWash() {
		return this.isCarReqWash;
	}

	public void setIsCarReqWash(byte isCarReqWash) {
		this.isCarReqWash = isCarReqWash;
	}

	public Pump getPump() {
		return this.pump;
	}

	public void setPump(Pump pump) {
		this.pump = pump;
	}

	public List<Gasstationhistorylog> getGasstationhistorylogs() {
		return this.gasstationhistorylogs;
	}

	public void setGasstationhistorylogs(List<Gasstationhistorylog> gasstationhistorylogs) {
		this.gasstationhistorylogs = gasstationhistorylogs;
	}

	public Gasstationhistorylog addGasstationhistorylog(Gasstationhistorylog gasstationhistorylog) {
		getGasstationhistorylogs().add(gasstationhistorylog);
		gasstationhistorylog.setCar(this);

		return gasstationhistorylog;
	}

	public Gasstationhistorylog removeGasstationhistorylog(Gasstationhistorylog gasstationhistorylog) {
		getGasstationhistorylogs().remove(gasstationhistorylog);
		gasstationhistorylog.setCar(null);

		return gasstationhistorylog;
	}

}