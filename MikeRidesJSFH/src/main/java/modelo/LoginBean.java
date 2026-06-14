package modelo;

import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;

import dominio.Driver;
import businessLogic.BLFacade;

@Named("login") //  // Para #{login...} del XHTML
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private Driver loggedDriver = null;

	@Inject
	private BLFacade facade;

	public LoginBean() {
	}

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

	public Driver getLoggedDriver() {
		return loggedDriver;
	}

	public void setLoggedDriver(Driver loggedDriver) {
		this.loggedDriver = loggedDriver;
	}

	private void sendMessageToScreen(FacesMessage.Severity severity, String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
	}

	public String login() {

		if (facade == null) {
			System.out.println("LoginBean.java => ERROR");
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error en LoginBean.java");
			return "null";
		}
		Driver dbDriver = facade.getDriver(username);

		if (dbDriver != null && dbDriver.getPassword().equals(this.password)) {

			this.loggedDriver = dbDriver;
			System.out.println("LoginBean.java => login() - " + loggedDriver.getUsername());
			// return "ok";
			return "DriverMenu?faces-redirect=true";

		} else {
			sendMessageToScreen(FacesMessage.SEVERITY_ERROR, "Error: username o password incorrectos");
			return "null";
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		// return "out";
		return "Index?faces-redirect=true";
	}

}