package dominio;

import enumerados.Estado;
import enumerados.TipoPropiedad;
import enumerados.Transaccion;

import java.io.Serializable;
import java.util.List;

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

	/*
	 * @Enumerated(EnumType.STRING) private TipoPropiedad tipoPropiedad;
	 * 
	 * @Enumerated(EnumType.STRING) private Estado tipoEstado;
	 * 
	 * @Enumerated(EnumType.STRING) private Transaccion tipotransaccion;
	 */

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

	public Propiedad() {
	}

	public Propiedad(double precio, Integer cantDorm, Integer cantBanio,
			double metrosCuadrados, boolean parrillero, boolean garage,
			String tipoPropiedad, String estado, String tipotransaccion,
			Integer numeroPuerta, String calle, Usuario usuario) {

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

	/*
	 * public TipoPropiedad getTipoPropiedad() { return tipoPropiedad; }
	 * 
	 * public void setTipoPropiedad(TipoPropiedad tipoPropiedad) {
	 * this.tipoPropiedad = tipoPropiedad; }
	 * 
	 * public Estado getTipoEstado() { return tipoEstado; }
	 * 
	 * public void setTipoEstado(Estado tipoEstado) { this.tipoEstado =
	 * tipoEstado; }
	 * 
	 * public Transaccion getTipotransaccion() { return tipotransaccion; }
	 * 
	 * public void setTipotransaccion(Transaccion tipotransaccion) {
	 * this.tipotransaccion = tipotransaccion; }
	 */
	
	

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

}
