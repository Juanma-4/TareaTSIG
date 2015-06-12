package servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controladores.IControladorZona;


@Path("/ServicioZona") 	
public class ServicioZona extends Application {

	@EJB
	private IControladorZona ipz;
	
	
			@POST
			@Produces(MediaType.APPLICATION_JSON) 
			@Consumes(MediaType.APPLICATION_JSON)
			@Path("/modificar")
			public Response modificarPropiedad(String datosZona) {
		
				boolean modificado = false;
				String booleanJSON = null;
			
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();

				try {

					modificado = ipz.modificarZona(datosZona);
					booleanJSON = gson.toJson(modificado);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(booleanJSON).build();

				}
				return Response.status(201).entity(booleanJSON).build();
			
			}
			
}
