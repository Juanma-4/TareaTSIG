package presentacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import controladores.IControladorUsuario;
import wrappers.WrapperUsuario;
	
@ManagedBean
@javax.faces.bean.SessionScoped
public class UsuarioMB implements Serializable {

	
	@EJB
	private IControladorUsuario icu;
	
	private static final long serialVersionUID = 1L;

	private String mail;
	private String password;

	private List<WrapperUsuario> administradores = new ArrayList<WrapperUsuario>();
	
	public void registroUsuario() {

		ClientRequest request = null;

			try {
			
				request = new ClientRequest("http://localhost:8080/Inmo13/rest/ServicioUsuario/registro");
				WrapperUsuario usuario = new WrapperUsuario(this.mail,this.password);

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
				
				Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));				
		
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

	public void ModificarUsuario(String mail, String pass) {
		System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		
		ClientRequest request = null;
		try {
			request = new ClientRequest("http://localhost:8082/Inmo13/rest/ServicioUsuario/modificar");
		
			//WrapperUsuario usuario = new WrapperUsuario(this.mail,this.password);
			WrapperUsuario usuario = new WrapperUsuario(mail,pass);

			String usuarioJSON = toJSONString(usuario);

			request.body("application/json", usuarioJSON);

			ClientResponse<String> respuesta = request.post(String.class);

			if (respuesta.getStatus() != 201) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Error al Modificar Usuario"));
				throw new RuntimeException("Failed : HTTP error code : "
						+ respuesta.getStatus());
			}
			
			Boolean creado = Boolean.parseBoolean(respuesta.getEntity(String.class));	
			
			if (creado)	{
				FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
			}else{			
					FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");				
			};
			
			}
			catch(Exception e){
				e.printStackTrace();
			}finally{
				request.clear();
			}
	
	}
	
	public List<WrapperUsuario> getUsuarios(){		
		//System.out.println("entro get administradores");
				
		ClientRequest request = new ClientRequest("http://localhost:8082/Inmo13/rest/ServicioUsuario/administradores");
		
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
	
	public String eliminarUsuario(String mail) {
		icu.eliminarUsuario(mail);
		return "IndexAdmin.xhtml?faces-redirect=true";
		
	}
	
	public void altaPropiedad(){
		try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("AltaPropiedad.xhtml");					
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
	
	public String irRegistroUsuario(){
		
		return "AltaUsuario.xhtml?faces-redirect=true";
	
	}
	
	public String BMUsuario(){
		
		return "BMUsuario.xhtml?faces-redirect=true";
		
	}
	
	public String irAltaPropiedad(){
		
		return "AltaPropiedad.xhtml?faces-redirect=true";
		
	}
	
	public String irBajaPropiedad(){
		
		return "BajaPropiedad.xhtml?faces-redirect=true";
		
	}
	public String irBMPropiedad(){
		
		return "BMPropiedad.xhtml?faces-redirect=true";
		
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
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
			}
			
			else {				
				request.clear();
								
				response = request.get(String.class);						
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
		// gsonBuilder.setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // ISO8601
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
}

	public List<WrapperUsuario> getAdministradores() {//esto esta mal ver como cambiarlo
		administradores = this.getUsuarios();
		return administradores;
	}

	public void setAdministradores(List<WrapperUsuario> administradores) {
		this.administradores = administradores;
	}
	
	

}
