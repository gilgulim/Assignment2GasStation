package dalJPA;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the pumps database table.
 * 
 */
@Entity
@Table(name="pumps")
@NamedQuery(name="Pump.findAll", query="SELECT p FROM Pump p")
public class Pump implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int pumpID;

	private BigDecimal pumpPricePerLiter;

	//bi-directional many-to-one association to Car
	@OneToMany(mappedBy="pump")
	private List<Car> cars;

	public Pump() {
	}

	public int getPumpID() {
		return this.pumpID;
	}

	public void setPumpID(int pumpID) {
		this.pumpID = pumpID;
	}

	public BigDecimal getPumpPricePerLiter() {
		return this.pumpPricePerLiter;
	}

	public void setPumpPricePerLiter(BigDecimal pumpPricePerLiter) {
		this.pumpPricePerLiter = pumpPricePerLiter;
	}

	public List<Car> getCars() {
		return this.cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public Car addCar(Car car) {
		getCars().add(car);
		car.setPump(this);

		return car;
	}

	public Car removeCar(Car car) {
		getCars().remove(car);
		car.setPump(null);

		return car;
	}

}