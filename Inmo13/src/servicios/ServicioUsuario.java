package servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import utilidades.UsuarioAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import utilidades.Correo;
import wrappers.WrapperCorreo;
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
		
		 // localhost:8080/Inmo13/ServicioUsuario/modificar
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/modificar")
		public Response modificarUsuario(String datos) {
			
			boolean creado = false;
			String booleanJSON = null;
			
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();

			Usuario usuario = gson.fromJson(datos, Usuario.class);
						
			String codigoRetorno = "200";			
			
			try {
				System.out.println("estoy en modificar usuario servicio: mail "+usuario.getMail()+"pass: "+usuario.getPassword());
				creado = iuc.modificarUsuario(usuario);
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
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/administradores")	
		public Response getAdministradores(){
			System.out.println("estoy en listar administradores servicio");
			String response = null;
			try {
				List<Usuario> usuarios = iuc.listarUsuarios();
				response = usuarioToJSONString(usuarios);
					
			} catch (Exception err) {
				response = "{\"status\":\"401\","
				+ "\"message\":\"No content found \""
				+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
				return Response.status(401).entity(response).build();
			}
			return Response.ok(response).build();
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
		
			
		@PUT
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/contacto")
		public Response	 enviarCorreo(String datos) throws AddressException, MessagingException 
		{
			System.out.println("estoy en ENVIAR CORREO");
			boolean creado = false;
			String booleanJSON = null;
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			WrapperCorreo wc = gson.fromJson(datos, WrapperCorreo.class);	
			
			String codigoRetorno = "200";			
		
		try {
			//public WrapperCorreo(String nombre, String correo, String asunto, String cuerpo) {
			Correo c = new Correo();
			//public void enviarMensajeConAuth(String host, Integer puerto, String origen, String destino, String password,
	    	//String asunto, String mensaje) throws AddressException, MessagingException
			
			//SE NOTIFICA AL QUE CONSULTA QUE SU CONSULTA FUE HECHA
			creado = c.enviarMensajeConAuth("smtp.gmail.com", 587,"inmogrupo13@gmail.com", wc.getOrigen(),"inmobiliaria13", "Notificacion de consulta", "Estimado "+wc.getNombre()+":Su Consulta fue recibida con exito y sera respondida a la vedrevedad por nuestro grupo de administradores de Inmo13, Muchas Gracias...");
			booleanJSON = gson.toJson(creado);
			
			//NO BORRAR
			//SE NOTIFICA A TODOS LOS ADMINISTRAORES DE ESA PROPIEDAD
		/*	List<Usuario> usus = iuc.listarUsuariosporPropiedad(wc.getPropid());
			for(Usuario u: usus){
				creado = c.enviarMensajeConAuth("smtp.gmail.com", 587,"inmogrupo13@gmail.com", u.getMail(),"inmobiliaria13", "Consulta realizada sobre la propiedad "+ wc.getPropid(), "El visitante: "+wc.getNombre()+"</br>"+"Se encuentra interesado en la propiedad: "+wc.getPropid()+"</br> Su consulta es: "+wc.getCuerpo());
			}*/
			
			
			
		} catch (Exception err) {
			err.printStackTrace();
			codigoRetorno = "{\"status\":\"500\","
					+ "\"message\":\"Resource not created.\""
					+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
			return Response.status(500).entity(booleanJSON).build();
		}
		return Response.status(201).entity(booleanJSON).build();
		}
	
		
		
		@GET
		//@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/eliminar/{mail}")
		public Response eliminarUsu(@PathParam("mail") String mail) {
			
			System.out.println("Estoy en eliminar servicio usuario"+ mail);
			String booleanoJson = null;

			try {
				Boolean elimino = iuc.eliminarUsuario(mail);
				booleanoJson = toJSONString(elimino);
				
			} catch (Exception err) {
				err.printStackTrace();
				
				return Response.status(500).entity(booleanoJson).build(); 
			}
			return Response.ok(booleanoJson).build();
		}
	
		public String toJSONString(Object object) { 
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			return gson.toJson(object);
		}
		
		public String usuarioToJSONString(List<Usuario> usuarios) {  
		    GsonBuilder gsonBuilder = new GsonBuilder();
		    Gson gson = gsonBuilder.registerTypeAdapter(Usuario.class, new UsuarioAdapter()).create();
		    return gson.toJson(usuarios);
		} 
		
		
}