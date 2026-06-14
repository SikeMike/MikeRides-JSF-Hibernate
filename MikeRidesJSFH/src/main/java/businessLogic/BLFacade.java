package businessLogic;

import java.util.Date;
import java.util.List;

import dominio.Driver;
import dominio.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public interface BLFacade {

	public Driver getDriver(String username);
	
	public void createDriver(String username, String password);
	
	public Ride createRide(String from, String to, double price, int nPlaces, Date date, String driverUsername) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;

	public List<String> getDepartCities();

	public List<String> getDestinationCitiesGivenOrigin(String from);

	public List<Ride> getSpecificRidesInDate(String from, String to, Date date);

}
