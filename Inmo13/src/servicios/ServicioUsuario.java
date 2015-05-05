package servicios;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controladores.IControladorUsuario;
import dominio.Usuario;


@Path("/ServicioUsuario") 	
public class ServicioUsuario extends Application {

	@EJB
	private IControladorUsuario iuc;
	
	
		// ---> localhost:8080/Inmo13/rest/UsuarioService/status/
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/status") 	// ---> para mapearlo en la url	
		public Response getStatus() {
			return Response
					.ok("{\"status\":\"El servicio de los usuarios esta funcionando...\"}")
					.build();
		}
		
	    // localhost:8080/Inmo13/UsuarioService/usuario
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/registro")
		public Response guardarUsuario(String datos) {
			boolean creado = false;
			String booleanJSON = null;
			
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();

			Usuario usuario = gson.fromJson(datos, Usuario.class);

			String codigoRetorno = "200";			
			
			try {
				creado = this.iuc.guardarUsuario(usuario.getMail(), usuario.getPassword());
				booleanJSON = gson.toJson(creado);
				
			} catch (Exception err) {
				err.printStackTrace();
				codigoRetorno = "{\"status\":\"500\","
						+ "\"message\":\"Resource not created.\""
						+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
				return Response.status(500).entity(booleanJSON).build();
			}
			return Response.status(201).entity(booleanJSON).build();
		
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/login")
		public Response login(String datos) {

			boolean existe = false;
			String respuesta;
			ArrayList<Boolean> lista = new ArrayList<Boolean>();

			// Create a new Gson object that could parse all passed in elements
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
	
			// Get book Object parsed from JSON string
			Usuario usuario = gson.fromJson(datos, Usuario.class);

			if (iuc.existeUsuario(usuario.getMail(), usuario.getPassword())) {

				existe = true;

			
				lista.add(existe);

				respuesta = toJSONString(lista);

				return Response.ok().entity(respuesta).build();

			} else {
				
				lista.add(existe);

				respuesta = toJSONString(lista);

				return Response.status(404).entity(respuesta).build();
			}
		}

		public String toJSONString(Object object) { // Funcion que convierte de
													// objeto java a JSON
			GsonBuilder gsonBuilder = new GsonBuilder();

			Gson gson = gsonBuilder.create();
			return gson.toJson(object);
		}
		
}