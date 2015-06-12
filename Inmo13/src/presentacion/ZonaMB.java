package presentacion;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import wrappers.WrapperPropiedad;
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
	private String fid;
	
	public String crearZona(){
		limpiaraltazona();
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
	
	public void modificarZona(){
		
		ClientRequest request;
		ClientResponse<String> response;
		try {
			
			request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioZona/modificar");
			WrapperZona zona = new WrapperZona();
			zona.setNombre(nombre);
			zona.setDescripcion(desc);
			zona.setFid(fid);

			String zonaJSON = toJSONString(zona);
			request.body("application/json", zonaJSON);			
			response = request.post(String.class);

			if (response.getStatus() != 201) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Error al ingresar una nueva zona"));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			
			Boolean modifico = Boolean.parseBoolean(response.getEntity(String.class));				
			
			if (modifico)	{
				FacesContext.getCurrentInstance().getExternalContext().redirect("BMZona.xhtml");
			}else{	
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al modificar la zona"));
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setearDatosBM(){
		try{
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

		this.nombre = String.valueOf(params.get("nombre")); 
		this.desc = String.valueOf(params.get("descripcion"));  
		this.fid = (String.valueOf(params.get("fid")));
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("ModificarDatosZona.xhtml");
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	
}
