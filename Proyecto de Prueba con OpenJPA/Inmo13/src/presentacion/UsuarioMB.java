package presentacion;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import controladores.IUsuarioController;


@ManagedBean
@javax.faces.bean.SessionScoped
public class UsuarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private	IUsuarioController controladorUsuario;
	
	private String mail;
	private String password;
		  
		
	public String registroUsuario() {
				
		try{
			controladorUsuario.guardarUsuario(mail, password);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return null;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	
	
	

}
