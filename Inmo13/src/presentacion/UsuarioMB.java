package presentacion;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import wrappers.WrapperUsuario;

@ManagedBean
@javax.faces.bean.SessionScoped
public class UsuarioMB implements Serializable {

	//@EJB
	//private IControladorUsuario icu;
	
	@ManagedProperty(value="#{propiedadMB}")
	private PropiedadMB propiedadMB;
	
	private static final long serialVersionUID = 1L;

	private String mail;
	private String password;
	
	private String mailregistro;
	private String passwordregistro;
	
	private String usuarioSelecmail;
	private String usuarioSelecpass;
	
	
	private List<WrapperUsuario> lusuarios;
	
	public PropiedadMB getPropiedadMB() {
		return propiedadMB;
	}

	public void setPropiedadMB(PropiedadMB propiedadMB) {
		this.propiedadMB = propiedadMB;
	}

	@PostConstruct
	public void iniciar(){
		lusuarios = new ArrayList<WrapperUsuario>();
		this.usuarioSelecmail="";
		this.usuarioSelecpass="";
	}
	
	public void registroUsuario() {

		ClientRequest request = null;

			try {
			
				request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/registro");
				WrapperUsuario usuario = new WrapperUsuario(this.mailregistro,this.passwordregistro);

				// transformo el usuario a ingresar en Json string
				String usuarioJSON = toJSONString(usuario);

				// Seteo el objeto usuario al body del request
				request.body("application/json", usuarioJSON);

				// se obtiene una respuesta por parte del webService
				ClientResponse<String> respuesta = request.post(String.class);

				if (respuesta.getStatus() != 201) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al ingresar un nuevo usuario","Ya se encuentra registrador otro usuario con este email, pruebe con otro"));
					throw new RuntimeException("Failed : HTTP error code : "
							+ respuesta.getStatus());
					
					
					// FacesContext context = FacesContext.getCurrentInstance();
					    //context.addMessage(null, new FacesMessage("Successful",  "Your message: " + message) );
					// context.addMessage(null, new FacesMessage("Error al crear usuario", "Ya se encuentra registrador otro usuario con este email, pruebe con otro"));
				}
				//else FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
				
				Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));				
		
				if (creado)	{
					FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
				}else{			
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al ingresar un nuevo usuario","Ya se encuentra registrador otro usuario con este email, pruebe con otro"));		
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				request.clear();
			}
			
	}

	public void modificarUsuario() {
		System.out.println("estoy en modificar usuarioMB mail: "+this.usuarioSelecmail+" pass: "+ this.usuarioSelecpass);
		
		ClientRequest request = null;
		try {
					
				request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/modificar");
			
				WrapperUsuario usuario = new WrapperUsuario(this.usuarioSelecmail,this.usuarioSelecpass);
	
				String usuarioJSON = toJSONString(usuario);
				request.body("application/json", usuarioJSON);
	
				ClientResponse<String> respuesta = request.post(String.class);
				System.out.println(respuesta.getEntity());
									
				Boolean modifico = Boolean.parseBoolean(respuesta.getEntity(String.class));
				
				if (modifico){
						FacesContext.getCurrentInstance().getExternalContext().redirect("BMUsuario.xhtml");
				}else{			
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al Modificar Usuario","Intentelo nuevamente"));	
				};
				
				
				
		}catch(Exception e){
				e.printStackTrace();
			}
	
	}
	
	public void getUsuarios(){		
				
		ClientRequest request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/getUsuarios");
											
		try {
			request.accept("application/json");
					
			ClientResponse<String> response = request.get(String.class);
			
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			
			System.out.println("USUARIOSSS : "+response.getEntity());
			
			JsonParser parser = new JsonParser();
			JsonArray jArray = parser.parse(response.getEntity()).getAsJsonArray();			
			
			this.lusuarios.clear();
			for (JsonElement Usuario : jArray) {
				WrapperUsuario wu = new WrapperUsuario();
				wu = gson.fromJson(Usuario, WrapperUsuario.class);
				this.lusuarios.add(wu);
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}
	
	public String irRegistroUsuario(){
         this.limpiarRegistro();
		return "AltaUsuario.xhtml?faces-redirect=true";
	
	}
	
	public void limpiarRegistro(){
		this.mailregistro="";
		this.passwordregistro="";
	}
	
	public String BMUsuario(){
		
		this.getUsuarios();		
		return "BMUsuario.xhtml?faces-redirect=true";
		
	}
		
	public void login(){
		
		ClientRequest request;		  
		
		try {

			request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/login");		
			
			WrapperUsuario u = new WrapperUsuario(mail, password);			
			
			String userJson = toJSONString(u);			
			request.body("application/json", userJson);					
			
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			
			ClientResponse<String> response = request.post(String.class);
			
			JsonParser parser = new JsonParser();
		    JsonArray jArray = parser.parse(response.getEntity()).getAsJsonArray();
			
			ArrayList<Boolean> lista = new ArrayList<Boolean>();

		    for(JsonElement obj : jArray)
		    {
		    	Boolean booleano = gson.fromJson(obj , Boolean.class);
		    	lista.add(booleano);
		        
		  
		    }
			
			if (lista.get(0) == false) {				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Credenciales incorrectas"));
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			}
			
			else {				

				this.propiedadMB.setUsuario(this.mail);
				
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mail", this.mail);					
				FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");			
				
			
			}
			
			} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		

	}

	public void logOut() {

		try {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public void eliminarUsuario() {
		System.out.println("eliminar UsuarioMB:::::::"+this.usuarioSelecmail);
			ClientRequest request = null;
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			
				try {
														
					request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/eliminar/"+this.usuarioSelecmail);
					//request.header("mail", this.usuarioSelecmail);
										
					ClientResponse<String> respuesta = request.get(String.class);
					
					System.out.println(respuesta.getEntity());
					
					/*if (respuesta.getStatus() != 201) {
						System.out.println("ERROR !=201");
						FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Error al Eliminar Usuario","No se pudo eliminar"));
						throw new RuntimeException("Failed : HTTP error code : "
								+ respuesta.getStatus());
					//}else{
					
					
					//Boolean elimino = Boolean.parseBoolean(respuesta.getEntity(String.class));	
					}*/
					Boolean elimino = Boolean.parseBoolean(respuesta.getEntity(String.class));
					if (elimino){
						if (this.mail.equals("admin")){
									FacesContext.getCurrentInstance().getExternalContext().redirect("BMUsuario.xhtml");
						//this.irRegistroUsuario();
									System.out.println("es administrador");
						}
						else {
							this.logOut();
							//FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
							System.out.println("NO ES admin");
						}
					}else{		
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al Eliminar Usuario","Intentelo nuevamente, asegurese que el mailes el correcto"));			
							System.out.println("NO ELIMINO");
					};
					
					}
							
				
				catch(Exception e){
					e.printStackTrace();
				}finally{
					request.clear();
				}
	}

	

	public String toJSONString(Object object) { // Funcion que convierte de
		// objeto java a JSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailregistro() {
		return mailregistro;
	}

	public void setMailregistro(String mailregistro) {
		this.mailregistro = mailregistro;
	}

	public String getPasswordregistro() {
		return passwordregistro;
	}

	public void setPasswordregistro(String passwordregistro) {
		this.passwordregistro = passwordregistro;
	}

	public String getUsuarioSelecmail() {
		return usuarioSelecmail;
	}

	public void setUsuarioSelecmail(String usuarioSelecmail) {
		this.usuarioSelecmail = usuarioSelecmail;
	}

	public String getUsuarioSelecpass() {
		return usuarioSelecpass;
	}

	public void setUsuarioSelecpass(String usuarioSelecpass) {
		this.usuarioSelecpass = usuarioSelecpass;
	}
	public List<WrapperUsuario> getLusuarios() {
		return lusuarios;
	}

	public void setLusuarios(List<WrapperUsuario> lusuarios) {
		this.lusuarios = lusuarios;
	}
	
	
	


}
