package wrappers;
import com.google.gson.annotations.SerializedName;

public class WrapperCorreo {
//c.enviarMensajeConAuth("smtp.gmail.com", 587,mail, 
	//"gayoso.javier@gmail.com","gestioncatastrofes",
	// asunto, cuerpo);
	
	@SerializedName("correo")
	private String correo;
	
	@SerializedName("pass")
	private String pass;
	
	@SerializedName("asunto")
	private String asunto;
	
	@SerializedName("cuerpo")
	private String cuerpo;

	public WrapperCorreo() {
		
	}

	public WrapperCorreo(String correo, String asunto, String pass, String cuerpo) {

		this.correo = correo;
		this.asunto = asunto;
		this.pass = pass;
		this.cuerpo = cuerpo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
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
}



