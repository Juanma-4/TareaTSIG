package wrappers;

import com.google.gson.annotations.SerializedName;

import dominio.Usuario;
import enumerados.Estado;
import enumerados.TipoPropiedad;
import enumerados.Transaccion;

public class WrapperPropiedad {

	@SerializedName("precio")
	private Double precio;

	@SerializedName("cantDorm")
	private Integer cantDorm;

	@SerializedName("cantBanio")
	private Integer cantBanio;

	@SerializedName("metrosCuadrados")
	private Double metrosCuadrados;

	@SerializedName("parrillero")
	private Boolean parrillero;

	@SerializedName("garage")
	private Boolean garage;

	@SerializedName("tipoPropiedad")
	private String tipoPropiedad;

	@SerializedName("tipotransaccion")
	private String tipotransaccion;

	@SerializedName("tipoEstado")
	private String tipoEstado;

	@SerializedName("numeroPuerta")
	private Integer numeroPuerta;

	@SerializedName("calle")
	private String calle;

	@SerializedName("fid")
	private String fid;

	public WrapperPropiedad() {
	}

	public WrapperPropiedad(Double precio, Integer cantDorm, Integer cantBanio,
			Double metrosCuadrados, Boolean parrillero, Boolean garage,
			String tipoPropiedad, String tipoEstado, String tipotransaccion,
			Integer numeroPuerta, String calle, String fid) {

		this.precio = precio;
		this.cantDorm = cantDorm;
		this.cantBanio = cantBanio;
		this.metrosCuadrados = metrosCuadrados;
		this.parrillero = parrillero;
		this.garage = garage;
		this.tipoPropiedad = tipoPropiedad;
		this.tipoEstado = tipoEstado;
		this.tipotransaccion = tipotransaccion;
		this.calle = calle;
		this.fid = fid;
		this.numeroPuerta = numeroPuerta;

	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
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

	public Integer getNumeroPuerta() {
		return numeroPuerta;
	}

	public void setNumeroPuerta(Integer numeroPuerta) {
		this.numeroPuerta = numeroPuerta;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}
}
