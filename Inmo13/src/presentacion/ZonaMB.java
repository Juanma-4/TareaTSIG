package presentacion;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import wrappers.WrapperZona;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controladores.IControladorZona;

@ManagedBean
@javax.faces.bean.SessionScoped
public class ZonaMB implements Serializable {
	

	@EJB
	private IControladorZona icz;
	

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String desc;
	private List<WrapperZona> zonas;
	
	public String crearZona(){
		
		return "AltaZona.xhtml?faces-redirect=true";
	
	}
	
	public String irBMZona(){
		limpiaraltazona();
		return "BMZona.xhtml?faces-redirect=true";
	
	}
	
	public void limpiaraltazona(){
		this.nombre="";
		this.desc="";
	}
	
	public String irInfoZonas(){
		
		setZonas(icz.actualizarInfoZonas());
		return "ReporteZonas.xhtml?faces-redirect=true";
	
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

	public List<WrapperZona> getZonas() {
		return zonas;
	}

	public void setZonas(List<WrapperZona> zonas) {
		this.zonas = zonas;
	}

	
}
