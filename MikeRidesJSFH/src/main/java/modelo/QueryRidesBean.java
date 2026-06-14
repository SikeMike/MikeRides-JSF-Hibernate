package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import dominio.Ride;
import businessLogic.BLFacade;

@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;
	private List<String> fromCities;

	private String to;
	private List<String> toCities = new ArrayList<>();

	private Date date;
	private List<Ride> foundRides = new ArrayList<>();

	@Inject
	private BLFacade facade;

	@PostConstruct
	public void init() {
		try {
			this.fromCities = facade.getDepartCities();
		} catch (Exception e) {
			e.printStackTrace();
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "ERROR: QueryRidesBean.init()");
		}
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getFromCities() {
		return fromCities;
	}

	public void setFromCities(List<String> fromCities) {
		this.fromCities = fromCities;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<String> getToCities() {
		return toCities;
	}

	public void setToCities(List<String> toCities) {
		this.toCities = toCities;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Ride> getFoundRides() {
		return foundRides;
	}

	public void setFoundRides(List<Ride> foundRides) {
		this.foundRides = foundRides;
	}

	private void sendMessageToScreen(FacesMessage.Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
	}

	// Cada vez que AJAX ve que el usuario cambia la seleccion de "from"
	public void fromCitySelected() {

		// Limpieza por si hay consultas anteriores (ES NECESARIA?)
		this.to = null;
		this.toCities.clear();
		this.foundRides.clear();

		// IR A LA BASE DE DATOS, BUSCAR VIAJES DE 'from', Y CONSEGUIR LOS 'to' ->
		// llenar el array toCities

		if (this.from != null && !this.from.isEmpty()) {
			try {
				this.toCities = facade.getDestinationCitiesGivenOrigin(this.from);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void doSearch() {
		// BUSCAR VIAJES {FROM, TO, DATE}
		this.foundRides.clear();

		if ((this.from == null || this.from.isEmpty()) || (this.to == null || this.to.isEmpty()) || this.date == null) {
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "ERROR: selecciona todos los campos antes de buscar");
		} else {
			System.out.println("doSearch() => " + this.from + " ; " + this.to + " ; " + this.date.toString());

			try {
				this.foundRides = facade.getSpecificRidesInDate(from, to, date);

				if (this.foundRides.isEmpty()) {
					sendMessageToScreen(FacesMessage.SEVERITY_INFO, "ESE DÍA NO HAY VIAJES");
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "ERROR: " + e.getClass());
			}
		}
	}
}