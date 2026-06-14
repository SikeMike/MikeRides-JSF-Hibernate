package businessLogic;

import java.util.Date;
import java.util.List;

import dataAccess.DataAccessHibernate;
import dominio.Driver;
import dominio.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BLFacadeImplementation implements BLFacade {
	
	private DataAccessHibernate da;
	
	public BLFacadeImplementation() {
		System.out.println("BLFacadeImplementation.java CONSTRUCTOR - hashCode: " + this.hashCode());
		da = new DataAccessHibernate();
	}
	
	// En realidad, al parecer CDI no usará nunca este constructor
	public BLFacadeImplementation(DataAccessHibernate da) {
		System.out.println("BLFacadeImplementation.java CONSTRUCTOR (with 'da' parameter)");
		this.da = da;
	}
	
	public Driver getDriver(String username) {
		return da.getDriver(username);
	}

	public void createDriver(String username, String password) {
		da.createDriver(username, password);
	}

	public Ride createRide(String from, String to, double price, int nPlaces, Date date, String driverUsername)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		return da.createRide(from, to, price, nPlaces, date, driverUsername);
	}
	
    public List<String> getDepartCities(){
		return da.getDepartCities();
    }

	public List<String> getDestinationCitiesGivenOrigin(String from) {
		return da.getDestinationCitiesGivenOrigin(from);
	}

	public List<Ride> getSpecificRidesInDate(String from, String to, Date date) {
		return da.getSpecificRidesInDate(from, to, date);
	}
	
}
