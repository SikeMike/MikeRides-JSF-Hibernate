package dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Driver implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	private String username;
	private String password;
	
	@OneToMany(mappedBy = "driver", fetch=FetchType.LAZY)
	private List<Ride> rides = new ArrayList<>();

	public Driver() {}
	
	public Driver(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Ride> getRides() {
		return rides;
	}
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
	
	public boolean doesRideExist(String from, String to, Date date) {
		if (date == null) {
			return false;
		}
		
		LocalDate currentDateSinHora = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Date rideDate;
		for (Ride ride : rides) {
			rideDate = ride.getDate();
			if (ride.getFrom().equalsIgnoreCase(from) && ride.getTo().equalsIgnoreCase(to) && rideDate != null) {
				
				LocalDate rideDateSinHora = rideDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				if (currentDateSinHora.equals(rideDateSinHora)) {
					return true;
				}
			}
		}
		return false;
	}
}
