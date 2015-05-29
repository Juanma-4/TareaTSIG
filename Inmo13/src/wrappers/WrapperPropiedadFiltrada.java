package wrappers;

import com.google.gson.annotations.SerializedName;

public class WrapperPropiedadFiltrada {

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
	@SerializedName("tipoMoneda")
	private String tipoMoneda;
	@SerializedName("piso")
	private String piso;
	@SerializedName("usuario")
	private String usuario;
	@SerializedName("latitud")
	private String latitud;
	@SerializedName("longitud")
	private String longitud;

	public WrapperPropiedadFiltrada(Double precio, Integer cantDorm,
			Integer cantBanio, Double metrosCuadrados, Boolean parrillero,
			Boolean garage, String tipoPropiedad, String tipoEstado,
			String tipotransaccion, Integer numeroPuerta, String calle,
			String fid, String tipoMoneda, String piso, String latitud,
			String longitud, String usuario) {

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
		this.tipoMoneda = tipoMoneda;
		this.piso = piso;
		this.usuario = usuario;
		this.longitud = longitud;
		this.latitud = latitud;

	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
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

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
