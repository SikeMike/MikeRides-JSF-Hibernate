package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import businessLogic.BLFacade;
import dominio.Driver;

@Named("createRide") // Para #{createRide...} del XHTML
@RequestScoped
public class CreateRideBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;
	private String to;
	private double price;
	private int nPlaces;
	private Date date;

	@Inject
	private BLFacade facade;

	@Inject
	private LoginBean loginBean;

	public CreateRideBean() {
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

	private void sendMessageToScreen(FacesMessage.Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
	}

	public String createRide() {
		try {
			if (facade == null) {
				System.out.println("CreateRideBean.java => ERROR");
				sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error en CreateRideBean.java");
				return "null";
			}
			Driver loggedDriver = loginBean.getLoggedDriver();
			if (loggedDriver == null) {
				sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error: no loggedDriver found");
				// PARA MANTENER MENSAJES AÚN DESPUES DE REDIRECCIÓN
				// FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				return "Index?faces-redirect=true";
			}

			if (from != null && from.equalsIgnoreCase(to)) {
				sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error: origen y destino deben de ser diferentes");
				return "null";
			}

			if (date != null) {
				LocalDate dateSinHora = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate today = LocalDate.now();
				if (dateSinHora.isBefore(today)) {
					sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error: fecha debe de ser anterior a hoy");
					return "null";
				}
			}

			facade.createRide(from, to, price, nPlaces, date, loggedDriver.getUsername());
			sendMessageToScreen(FacesMessage.SEVERITY_INFO, "Create Ride");
			return "DriverMenu?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error al crear viaje: " + e.getClass());
			return null;
		}
	}

}