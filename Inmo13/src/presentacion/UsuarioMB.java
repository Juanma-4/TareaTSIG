package presentacion;

import java.io.Serializable;
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
	
	private List<WrapperUsuario> administradores = new ArrayList<WrapperUsuario>();
	
	public PropiedadMB getPropiedadMB() {
		return propiedadMB;
	}

	public void setPropiedadMB(PropiedadMB propiedadMB) {
		this.propiedadMB = propiedadMB;
	}

	@PostConstruct
	public void iniciar(){
		System.out.println("post constructor usuario MB");
//		this.passwordregistro="";
//		this.mailregistro="";
//		this.mail="";
//		this.password="";
//		//this.usuario = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mail")).trim();
//		this.administradores = this.getUsuarios();
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
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Error al ingresar un nuevo usuario"));
					throw new RuntimeException("Failed : HTTP error code : "
							+ respuesta.getStatus());
				}
				
				//Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));				
		
				//if (creado)	{
				//	FacesContext.getCurrentInstance().getExternalContext().redirect("Vista.xhtml");
				//}else{			
				
				
					FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");					
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
/*
			if (respuesta.getStatus() != 201) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Error al Modificar Usuario"));
				throw new RuntimeException("Failed : HTTP error code : "
						+ respuesta.getStatus());
			}
			*/
			
			Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));	
			if (creado)	{
				FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
			}else{			
					FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");				
			};
			
			}
			catch(Exception e){
				e.printStackTrace();
			}finally{
				request.clear();
			}
	
	}
	
	public List<WrapperUsuario> getUsuarios(){		
		System.out.println("entro get Usuarios");
				
		ClientRequest request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/administradores");
																									
		ArrayList<WrapperUsuario> lwu = new ArrayList<WrapperUsuario>();
		
		try {
			request.accept("application/json");
					
			ClientResponse<String> response = request.get(String.class);
			GsonBuilder gsonBuilder = new GsonBuilder();
			
			///////////////////////
			gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Gson gson = gsonBuilder.create();
			////////////////que es esto?
			JsonParser parser = new JsonParser();
			JsonArray jArray = parser.parse(response.getEntity()).getAsJsonArray();
			
			for (JsonElement Usuario : jArray) {
				WrapperUsuario wu = new WrapperUsuario();
				wu = gson.fromJson(Usuario, WrapperUsuario.class);
				lwu.add(wu);
			}
			
			this.administradores = lwu;
			return lwu;
			//FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
		
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return null;
	}
	
	public String irRegistroUsuario(){
		
		return "AltaUsuario.xhtml?faces-redirect=true";
	
	}
	
	public String BMUsuario(){
		
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

		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();

		try {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		// return "Index.xhtml?faces-redirect=true";
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
					
				/*	if (respuesta.getStatus() != 201) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage("Error No se pudo borrar el usuario"));
						throw new RuntimeException("Failed : HTTP error code : "
								+ respuesta.getStatus());
					}*/
							
					/*Boolean elimino = gson.fromJson(respuesta.getEntity(), Boolean.class);
					//Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));	
					
					if (elimino)	{
						//FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
						this.irRegistroUsuario();
					}else{		
							FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");				
					};*/
							
				}
				catch(Exception e){
					e.printStackTrace();
				}finally{
					request.clear();
				}
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

	public String toJSONString(Object object) { // Funcion que convierte de
		// objeto java a JSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
}

	public List<WrapperUsuario> getAdministradores() {
		return administradores;
	}

	public void setAdministradores(List<WrapperUsuario> administradores) {
		this.administradores = administradores;
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

	public String getUsuarioSelecpass() {
		return usuarioSelecpass;
	}

	public void setUsuarioSelecpass(String usuarioSelecpass) {
		this.usuarioSelecpass = usuarioSelecpass;
	}

	public String getUsuarioSelecmail() {
		return usuarioSelecmail;
	}

	public void setUsuarioSelecmail(String usuarioSelecmail) {
		this.usuarioSelecmail = usuarioSelecmail;
	}
}
