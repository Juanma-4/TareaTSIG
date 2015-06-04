package wrappers;
import com.google.gson.annotations.SerializedName;

public class WrapperCorreo {
	
	@SerializedName("nombre")
	private String nombre;
		
	@SerializedName("origen")
	private String origen;
	
	@SerializedName("telefono")
	private String telefono;
	
	@SerializedName("cuerpo")
	private String cuerpo;
	
	@SerializedName("admin")
	private String admin;
	
	@SerializedName("propiedad")
	private String propiedad;
	
	@SerializedName("numeroPuerta")
	private Integer numeroPuerta;
	
	

	

	public WrapperCorreo() {
		
	}

	public WrapperCorreo(String nombre, String correo, String telefono, String cuerpo, String admin, String propiedad, Integer numeroPuerta) {

		this.nombre = nombre;
		this.origen = correo;
		this.telefono = telefono;
		this.cuerpo = cuerpo;
	    this.admin = admin;
	    this.propiedad = propiedad;
	    this.numeroPuerta = numeroPuerta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}

	public Integer getNumeroPuerta() {
		return numeroPuerta;
	}

	public void setNumeroPuerta(Integer numeroPuerta) {
		this.numeroPuerta = numeroPuerta;
	}

	
}



