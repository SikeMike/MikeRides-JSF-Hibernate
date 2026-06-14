package modelo;

import java.io.Serializable;

import businessLogic.BLFacade;
import dominio.Driver;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("register") //  // Para #{register...} del XHTML
@RequestScoped
public class RegisterBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	@Inject
	private BLFacade facade;

	public RegisterBean() {
	} // Hace falta (?)

	public String getUsername() {
		return username;
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

	private void sendMessageToScreen(FacesMessage.Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
	}

	public String register() {
		if (facade == null) {
			System.out.println("RegisterBean.java => ERROR");
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error en RegisterBean.java");
			return "null";
		}

		// Mirar si driver existe
		Driver driverExistente = facade.getDriver(username);
		if (driverExistente != null) {
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error: el usuario ya existe!");
			return "null";
		} else {
			// Crear driver
			facade.createDriver(username, password);
			System.out.println("RegisterBean.java => register() - " + username + " ; " + password);
			sendMessageToScreen(FacesMessage.SEVERITY_INFO, "Registro correcto!");
			return "ok";
		}
	}
}
