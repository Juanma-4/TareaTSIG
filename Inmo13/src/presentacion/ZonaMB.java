package presentacion;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wrappers.WrapperPropiedad;

@ManagedBean
@javax.faces.bean.SessionScoped
public class ZonaMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String desc;
	
	public String crearZona(){
		
		return "AltaZona.xhtml?faces-redirect=true";
	
	}
	
	public String irIndex(){
			
		return "IndexAdmin.xhtml?faces-redirect=true";
		
	}
	
	public String toJSONString(Object object) { // Funcion que convierte de
		// objeto java a JSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		// gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
