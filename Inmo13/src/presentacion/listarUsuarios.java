package presentacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import wrappers.WrapperUsuario;

@ManagedBean
@javax.faces.bean.ViewScoped
public class listarUsuarios implements Serializable {
	
	
	private List<WrapperUsuario> listusus= new ArrayList<WrapperUsuario>(); ;
	
	private String usuarioSelecmail;
	private String usuarioSelecpass;
	//private UsuarioMB umb=new UsuarioMB();
	//private UsuarioMB umb = new UsuarioMB();
			
	@PostConstruct
	public void constructor()
	{
		System.out.println("post constructor listarUsuariosMB");
		
			this.listusus = this.getUsuarios();
			
			
	}
	
	public List<WrapperUsuario> getListusus() {
		return listusus;
	}

	public void setListusus(List<WrapperUsuario> listusus) {
		this.listusus = listusus;
	}

/*		public static long getSerialversionuid() {
		return serialVersionUID;
	}
*/
	public List<WrapperUsuario> getUsuarios(){		
		System.out.println("entro get Usuarios del listar usuarios MB");
				
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
			
			this.listusus = lwu;
			return lwu;
			//FacesContext.getCurrentInstance().getExternalContext().redirect("IndexAdmin.xhtml");
		
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return null;
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

	private static final long serialVersionUID = 1L;
}
