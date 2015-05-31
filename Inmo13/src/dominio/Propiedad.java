package dominio;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "propiedad")
public class Propiedad implements Serializable {

	private static final long serialVersionUID = 1L; // Mapping JPA

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "precio", nullable = false)
	private Double precio;

	@Column(name = "cantDorm", nullable = false)
	private Integer cantDorm;

	@Column(name = "cantBanio", nullable = false)
	private Integer cantBanio;

	@Column(name = "metrosCuadrados", nullable = false)
	private Double metrosCuadrados;

	@Column(name = "parrillero", nullable = false)
	private Boolean parrillero;

	@Column(name = "garage", nullable = false)
	private Boolean garage;
	
	@Column(name = "piso", nullable = false)
	private String piso;

	@Column
	private String tipoPropiedad;

	@Column
	private String tipoEstado;

	@Column
	private String tipotransaccion;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "Usuario", nullable = true)
	// Usuario que lo da de alta.
	private Usuario usuario;

	@Column(name = "numeroPuerta", nullable = false)
	private Integer numeroPuerta;

	@Column(name = "calle", nullable = false)
	private String calle;
	
	@Column(name = "fid", nullable = false)
	private String fid;

	@Column(name = "imagen", nullable = false)
	private String imagen;
	
	
	public Propiedad() {
	}

	public Propiedad(double precio, Integer cantDorm, Integer cantBanio,
			double metrosCuadrados, boolean parrillero, boolean garage,
			String tipoPropiedad, String estado, String tipotransaccion,
			Integer numeroPuerta, String calle, String fid,String piso,Usuario usuario, String imagen) {

		this.precio = precio;
		this.cantDorm = cantDorm;
		this.cantBanio = cantBanio;
		this.metrosCuadrados = metrosCuadrados;
		this.parrillero = parrillero;
		this.garage = garage;
		this.tipoEstado = estado;
		this.tipoPropiedad = tipoPropiedad;
		this.tipotransaccion = tipotransaccion;
		this.numeroPuerta = numeroPuerta;
		this.calle = calle;
		this.usuario = usuario;
		this.fid = fid;
        this.piso = piso;
        this.imagen = imagen;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Boolean getGarage() {
		return garage;
	}

	public String getTipoPropiedad() {
		return tipoPropiedad;
	}

	public void setTipoPropiedad(String tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}

	public String getTipoEstado() {
		return tipoEstado;
	}

	public void setTipoEstado(String tipoEstado) {
		this.tipoEstado = tipoEstado;
	}

	public String getTipotransaccion() {
		return tipotransaccion;
	}

	public void setTipotransaccion(String tipotransaccion) {
		this.tipotransaccion = tipotransaccion;
	}

	public void setGarage(Boolean garage) {
		this.garage = garage;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	
	
	

}
