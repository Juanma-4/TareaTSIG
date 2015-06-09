package presentacion;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.primefaces.context.RequestContext;

import wrappers.WrapperCorreo;
import wrappers.WrapperPropiedad;
import wrappers.WrapperPuntoInteres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


@ManagedBean(name="indexMB")
@javax.faces.bean.ViewScoped
public class IndexMB implements Serializable{
	
private static final long serialVersionUID = -5064414056493502033L;
	
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
	private String fid;
	private String usuario;
	private String piso;
	private String imagen;
	
	
	private Integer distanciaMar;
	private Integer distanciaParada;
	private Integer distanciaPInteres;
	private String calleDestino;
	private String esquinaDestino;
	
   private List<WrapperPuntoInteres> lpuntosInteres;
	
	
public void listarProp(){
		
		String propiedadesJson = null;		
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String tipopropiedad = String.valueOf(params.get("tipopropiedad"));
		String tipotransaccion = String.valueOf(params.get("tipotransaccion"));
		String minimo = String.valueOf(params.get("minimo"));
		String maximo = String.valueOf(params.get("maximo"));
		String cantbanio = String.valueOf(params.get("cantbanio"));
		String cantdorm = String.valueOf(params.get("cantdorm"));
		String metroscuadrados  = String.valueOf(params.get("metroscuadrados"));
		String barrio = String.valueOf(params.get("barrio"));
		String parrillero = String.valueOf(params.get("parrillero"));
		String garage = String.valueOf(params.get("garage"));
		String distanciaMar = String.valueOf(params.get("distanciaMar"));
		String distanciaParada = String.valueOf(params.get("distanciaParada"));
		String distanciaPInteres = String.valueOf(params.get("distanciaPInteres"));
		String calleDestino = String.valueOf(params.get("calleDestino"));
		String esquinaDestino = String.valueOf(params.get("esquinaDestino"));
		
		
		ClientRequest request;
		 try{	
		
			request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioPropiedad/listarPropiedades");		
				
		 	List<String> datos = new ArrayList<String>();
			datos.add(0,tipopropiedad);
			datos.add(1,tipotransaccion);
			datos.add(2, minimo);
			datos.add(3, maximo);
			datos.add(4, cantbanio);
			datos.add(5, cantdorm);
			datos.add(6, metroscuadrados);
			datos.add(7, barrio);
			datos.add(8, parrillero);
			datos.add(9, garage);
			datos.add(10, distanciaMar);
			datos.add(11, distanciaParada);
			datos.add(12, distanciaPInteres);
			datos.add(13, calleDestino);
			datos.add(14, esquinaDestino);
			
			String filtrosJSON = toJSONString(datos);
			
			request.body("application/json", filtrosJSON);
	
			ClientResponse<String> response = request.post(String.class);
			propiedadesJson = response.getEntity(String.class);
			
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		 		 
		 RequestContext.getCurrentInstance().addCallbackParam("PropiedaesFiltradas", propiedadesJson);
		 
	}

public void setearDatos(){
	try{
	Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	this.numPuerta = Integer.valueOf(params.get("numPuerta"));
	this.precio = Double.valueOf(params.get("precio"));
	this.cantDorm = Integer.valueOf(params.get("cantDorm"));
	this.cantBanio = Integer.valueOf(params.get("cantBanio"));
	this.metrosCuadrados = Double.valueOf(params.get("metrosCuadrados"));
	this.parrillero = Boolean.valueOf(params.get("parrillero"));
	this.garage = Boolean.valueOf(params.get("garage"));
	this.tipoPropiedad = String.valueOf(params.get("tipoPropiedad")); 
	this.tipotransaccion = String.valueOf(params.get("tipotransaccion"));  
	this.tipoEstado = String.valueOf(params.get("tipoEstado")); 
	this.calle = String.valueOf(params.get("calle"));  
	this.fid = String.valueOf(params.get("fid")); 
	this.usuario = String.valueOf(params.get("usuario"));   
	this.imagen = String.valueOf(params.get("imagen"));   
	this.piso = String.valueOf(params.get("piso")); 
//	this.distanciaMar = Integer.valueOf(params.get("distanciaMar"));
//	this.distanciaParada = Integer.valueOf(params.get("distanciaParada"));
//	this.distanciaPInteres = Integer.valueOf(params.get("distanciaPInteres"));

	//Cargo lista de puntos de interes para esa propiedad
	this.listarPuntosInteres();
	
	FacesContext.getCurrentInstance().getExternalContext().redirect("descripcionPropiedad.xhtml");
	}catch(Exception e){
		e.printStackTrace();
	}
	
}
private void listarPuntosInteres() {
	ClientRequest request;
	ClientResponse<String> respuesta;
	try{			
		
		request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioPropiedad/listarPuntosInteres");	
		request.header("fid", this.fid);
		
		respuesta = request.get(String.class);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		JsonParser parser = new JsonParser();
		JsonArray jArray = parser.parse(respuesta.getEntity()).getAsJsonArray();			
		
		this.lpuntosInteres = new ArrayList<WrapperPuntoInteres>();
		
		for (JsonElement puntoInteres : jArray) {				
			WrapperPuntoInteres pInteres = new WrapperPuntoInteres();
			pInteres = gson.fromJson(puntoInteres, WrapperPuntoInteres.class);
			this.lpuntosInteres.add(pInteres);
		}
		
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 
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
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public Integer getDistanciaMar() {
		return distanciaMar;
	}
	public void setDistanciaMar(Integer distanciaMar) {
		this.distanciaMar = distanciaMar;
	}
	public Integer getDistanciaParada() {
		return distanciaParada;
	}
	public void setDistanciaParada(Integer distanciaParada) {
		this.distanciaParada = distanciaParada;
	}
	public Integer getDistanciaPInteres() {
		return distanciaPInteres;
	}
	public void setDistanciaPInteres(Integer distanciaPInteres) {
		this.distanciaPInteres = distanciaPInteres;
	}
	public String getCalleDestino() {
		return calleDestino;
	}
	public void setCalleDestino(String calleDestino) {
		this.calleDestino = calleDestino;
	}
	public String getEsquinaDestino() {
		return esquinaDestino;
	}
	public void setEsquinaDestino(String esquinaDestino) {
		this.esquinaDestino = esquinaDestino;
	}
	
	
	
	public List<WrapperPuntoInteres> getLpuntosInteres() {
		return lpuntosInteres;
	}

	public void setLpuntosInteres(List<WrapperPuntoInteres> lpuntosInteres) {
		this.lpuntosInteres = lpuntosInteres;
	}

	public void limpiarDatos(){
		this.calle = "";
		this.numPuerta = 0;
		this.tipoPropiedad = "";
		this.tipotransaccion = "";
		this.precio = 0.0;
		this.piso = "";
		this.cantBanio = 1;
		this.cantDorm = 1;
		this.metrosCuadrados = 0.0;
		this.fid = "";
		this.parrillero = false;
		this.garage = false;
		this.tipoEstado = "";
		this.imagen = "";
	}
	
	public String toJSONString(Object object) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
	}
	
	
	
	

}
