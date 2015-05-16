package servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controladores.IControladorPropiedad;
import controladores.IControladorUsuario;
import dominio.Propiedad;
import dominio.Usuario;


@Path("/ServicioPropiedad") 	
public class ServicioPropiedad extends Application {

	@EJB
	private IControladorPropiedad ipc;
	
	@EJB
	private IControladorUsuario iuc;
	
			// localhost:8080/Inmo13/PropiedadService/propiedad
			@POST
			@Produces(MediaType.APPLICATION_JSON) 
			@Consumes(MediaType.APPLICATION_JSON)
			@Path("/alta")
			public Response guardarPropiedad(String datos, @HeaderParam("mail") String mail) {
		
				boolean creado = false;
				String booleanJSON = null;
				
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();

				Propiedad propiedad = gson.fromJson(datos, Propiedad.class);

				//String codigoRetorno = "200";

				try {
					Usuario usuario = iuc.buscarUsuario(mail);

					creado = this.ipc.guardarPropiedad(propiedad.getPrecio(), propiedad.getCantDorm(),
													   propiedad.getCantBanio(),propiedad.getMetrosCuadrados(),
													   propiedad.getParrillero(),propiedad.getGarage(),propiedad.getTipoPropiedad(),
													   propiedad.getTipoEstado(),propiedad.getTipotransaccion(), 
													   propiedad.getNumeroPuerta(),propiedad.getCalle() ,propiedad.getFid(),propiedad.getTipoMoneda(),propiedad.getPiso(),usuario);

					booleanJSON = gson.toJson(creado);
					
				} catch (Exception err) {
					err.printStackTrace();
					//					codigoRetorno = "{\"status\":\"500\","
					//							+ "\"message\":\"Resource not created.\""
					//							+ "\"developerMessage\":\"" + err.getMessage() + "\"" + "}";
					return Response.status(500).entity(booleanJSON).build();

				}
				return Response.status(201).entity(booleanJSON).build();
			
			}
		
	
	
}
