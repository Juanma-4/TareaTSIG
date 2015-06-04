package wrappers;
import com.google.gson.annotations.SerializedName;

public class WrapperCorreo {
	
	@SerializedName("nombre")
	private String nombre;
		
	@SerializedName("origen")
	private String origen;
	
	@SerializedName("asunto")
	private String asunto;
	
	@SerializedName("cuerpo")
	private String cuerpo;
	
	@SerializedName("propid")
	private String propid;

	

	public WrapperCorreo() {
		
	}

	public WrapperCorreo(String nombre, String correo, String asunto, String cuerpo, String id) {

		this.nombre = nombre;
		this.origen = correo;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
	    this.propid = id;
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

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	/*public Integer getPropid() {
		return propid;
	}

	public void setPropid(Integer propid) {
		this.propid = propid;
	}*/
	
	public String getPropid() {
	return propid;
	}
	
	public void setPropid(String propid) {
		this.propid = propid;
	}
}



