package dominio;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import enumerados.TipoProcesoEnum;


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
	 	@Enumerated(EnumType.STRING)
 		private TipoPropiedad tipo
	*/
	@Column(name = "tipoPropiedad", nullable = false)
	private String tipoPropiedad;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,optional=true)
	@JoinColumn(name="Propiedad",nullable = true)// Usuario que lo da de alta.
	private Usuario usuario;
	
	@Column(name = "numeroPuerta", nullable = false)
	private Integer numeroPuerta;
	
	@Column(name = "calle", nullable = false)
	private String calle;
	
	public Propiedad(){}

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
