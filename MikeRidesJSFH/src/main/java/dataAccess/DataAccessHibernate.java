package dataAccess;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import dominio.Driver;
import dominio.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.faces.application.FacesMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DataAccessHibernate {

	public DataAccessHibernate() {
	}

	public void createDriver(String username, String password) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Driver d = new Driver(username, password);
			em.persist(d);

			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public Driver getDriver(String username) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			Driver driver = em.find(Driver.class, username);
			return driver;
		} catch (Exception e) {
			throw e;
		} finally {
			em.close();
		}

	}

	public Ride createRide(String from, String to, double price, int nPlaces, Date date, String driverUsername)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			Driver dbDriver = em.find(Driver.class, driverUsername);

			if (dbDriver.doesRideExist(from, to, date)) {
				throw new RideAlreadyExistException();
			}

			if (date != null) {
				LocalDate dateSinHora = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate today = LocalDate.now();
				if (dateSinHora.isBefore(today)) {
					throw new RideMustBeLaterThanTodayException();
				}
			}

			Ride ride = new Ride(from, to, price, nPlaces, date, dbDriver);
			em.persist(ride);

			// TODO TENGO QUE PERSISTEAR TAMBIÉN EL DRIVER CON EL NUEVO RIDE?

			em.getTransaction().commit();
			return ride;

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public List<String> getDepartCities() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from",
					String.class);
			List<String> result = query.getResultList();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;

		} finally {
			em.close();
		}
	}

	public List<String> getDestinationCitiesGivenOrigin(String from) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<String> query = em.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from = :from ORDER BY r.to", String.class);
			query.setParameter("from", from);
			List<String> result = query.getResultList();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		}finally {
			em.close();
		}
	}

	public List<Ride> getSpecificRidesInDate(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Ride> query = em.createQuery("SELECT r FROM Ride r WHERE r.from = :from AND r.to = :to AND r.date = :date", Ride.class);
			query.setParameter("from", from);
			query.setParameter("to", to);
			query.setParameter("date", date);
			List<Ride> result = query.getResultList();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
}