package dominio;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ride implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rideNumber;
	
	@Column(name = "origin") // Para que la consulta SQL no de errores con "from"
	private String from;
	@Column(name = "destination") // Para que la consulta SQL no de errores con "to"
	private String to;
	private double price;
	private int nPlaces;
	private Date date;
	
	// FIXME considerar si se quiere EAGER o LAZY
	@ManyToOne(fetch = FetchType.EAGER)
	private Driver driver;
	
	public Ride() {}
	
	public Ride(String from, String to, double price, int nPlaces, Date date, Driver driver) {
		this.setFrom(from);
		this.setTo(to);
		this.setPrice(price);
		this.setnPlaces(nPlaces);
		this.setDate(date);
		this.driver = driver;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getnPlaces() {
		return nPlaces;
	}

	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Driver getDriver() {
		return driver;
	}
	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}
