package presentacion;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wrappers.WrapperPropiedad;


//import controladores.IPropiedadController;

@ManagedBean
@javax.faces.bean.SessionScoped
public class PropiedadMB implements Serializable {

	

	private static final long serialVersionUID = 1L;

	
	private Integer numPuerta;
	private Double precio;
	private Integer cantDorm;
	private Integer cantBanio;
	private Double metrosCuadrados;
	private Boolean parrillero;
	private Boolean garage;
	private String tipoPropiedad;
	private String tipotransaccion;
	private String tipoEstado;
	private String calle;
	private Integer gid;
	
	
	
/*	
	public ArrayList<SelectItem> getTipoPropiedadList() {
		 for(TipoPropiedad stat :TipoPropiedad.values()){
	    	 tipoPropiedadList.add(new SelectItem(stat,stat.getTipoPropiedad()));
	     }
	     return tipoPropiedadList;
	}


	public void setTipoPropiedadList(ArrayList<SelectItem> tipoPropiedadList2) {
		tipoPropiedadList = tipoPropiedadList2;
	}


	public TipoPropiedad[] getTiposPropiedad() {
		return TipoPropiedad.values();
	}
*/

	public String altaPropiedad() {
		
		
		ClientRequest request;

		try {
			request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioPropiedad/alta");
			WrapperPropiedad propiedad = new WrapperPropiedad(this.precio, this.cantDorm, this.cantBanio,this.metrosCuadrados,this.parrillero,this.garage,this.tipoPropiedad,this.tipoEstado,this.tipotransaccion,	this.numPuerta, this.calle,this.gid);
			
			String mail = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mail");
			
			// transformo el usuario a ingresar en Json string
			String propiedadJSON = toJSONString(propiedad);

			request.header("mail",mail);
			// Seteo el objeto usuario al body del request
			request.body("application/json", propiedadJSON);

			// se obtiene una respuesta por parte del webService
			ClientResponse<String> response = request.post(String.class);

			if (response.getStatus() != 201) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Error al ingresar un nuevo usuario"));
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			
			FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
			
		}catch(Exception e){
			e.printStackTrace();
		}
			
		return null;
	}
	
    public String irReporte(){
		
		return "Reporte.xhtml?faces-redirect=true";
	
	}
	
	
	
	public Integer getNumPuerta() {
		return numPuerta;
	}


	public void setNumPuerta(Integer numPuerta) {
		this.numPuerta = numPuerta;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public Integer getCantDorm() {
		return cantDorm;
	}


	public void setCantDorm(Integer cantDorm) {
		this.cantDorm = cantDorm;
	}


	public Integer getCantBanio() {
		return cantBanio;
	}


	public void setCantBanio(Integer cantBanio) {
		this.cantBanio = cantBanio;
	}


	public Double getMetrosCuadrados() {
		return metrosCuadrados;
	}


	public void setMetrosCuadrados(Double metrosCuadrados) {
		this.metrosCuadrados = metrosCuadrados;
	}


	public Boolean getParrillero() {
		return parrillero;
	}


	public void setParrillero(Boolean parrillero) {
		this.parrillero = parrillero;
	}


	public Boolean getGarage() {
		return garage;
	}


	public void setGarage(Boolean garage) {
		this.garage = garage;
	}


	public String getTipoPropiedad() {
		return tipoPropiedad;
	}


	public void setTipoPropiedad(String tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}




	public String getTipotransaccion() {
		return tipotransaccion;
	}

	public void setTipotransaccion(String tipotransaccion) {
		this.tipotransaccion = tipotransaccion;
	}

	public String getTipoEstado() {
		return tipoEstado;
	}

	public void setTipoEstado(String tipoEstado) {
		this.tipoEstado = tipoEstado;
	}

	public String getCalle() {
		return calle;
	}

	
	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	
	public String toJSONString(Object object) { // Funcion que convierte de
		// objeto java a JSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		// gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
}
	
}
