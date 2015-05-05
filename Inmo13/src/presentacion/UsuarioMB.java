package presentacion;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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

	private static final long serialVersionUID = 1L;

	private String mail;
	private String password;
		  
		

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
					FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");					
				//}
				
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				request.clear();
			}
			
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
				FacesContext.getCurrentInstance().getExternalContext().redirect("AltaPropiedad.xhtml");			
				
			
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
	
	

}
