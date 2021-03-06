package servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import wrappers.WrapperPropiedadFiltrada;
import wrappers.WrapperPuntoInteres;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
	
	

			@GET
			@Produces(MediaType.APPLICATION_JSON) 
			@Path("/generarFid")
			public Response generarFid(@HeaderParam("fid") String fid) {
		
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				
				String fidGenerado = null;
				
				try {					
					fidGenerado = ipc.generarFid(fid);
					fidGenerado = gson.toJson(fidGenerado);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(fidGenerado).build();
		
				}
				return Response.status(201).entity(fidGenerado).build();
			
			}
			
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
													   propiedad.getNumeroPuerta(),propiedad.getCalle() ,propiedad.getFid(),
													   propiedad.getImagen(),propiedad.getPiso(),usuario);

					booleanJSON = gson.toJson(creado);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(booleanJSON).build();

				}
				return Response.status(201).entity(booleanJSON).build();
			
			}
			
			@POST
			@Produces(MediaType.APPLICATION_JSON) 
			@Consumes(MediaType.APPLICATION_JSON)
			@Path("/modificar")
			public Response modificarPropiedad(String datosPropiedad) {
		
				boolean modificado = false;
				String booleanJSON = null;
			
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();

				try {

					modificado = this.ipc.modificarPropiedad(datosPropiedad);
					booleanJSON = gson.toJson(modificado);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(booleanJSON).build();

				}
				return Response.status(201).entity(booleanJSON).build();
			
			}
		
			@POST
			@Produces(MediaType.APPLICATION_JSON) 
			@Consumes(MediaType.APPLICATION_JSON)
			@Path("/listarPropiedades")
			public Response listarPropiedades(String datosFiltro) {
		
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				
				String respuesta = null;
				List<WrapperPropiedadFiltrada> propiedadesFiltradas = null;
				
				try {
					ArrayList<String> filtros = gson.fromJson(datosFiltro, new TypeToken<ArrayList<String>>() {}.getType());
					propiedadesFiltradas = ipc.listarPropiedades(filtros);
					respuesta = gson.toJson(propiedadesFiltradas);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(respuesta).build();

				}
				return Response.status(201).entity(respuesta).build();
			
			}
			
			@GET
			@Produces(MediaType.APPLICATION_JSON) 
			@Path("/listarPuntosInteres")
			public Response listarPuntosInteres(@HeaderParam("fid") String fid) {
		
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				
				String respuesta = null;
				List<WrapperPuntoInteres> puntosInteres = null;
				
				try {					
					puntosInteres = ipc.listarPuntosInteres(fid);
					respuesta = gson.toJson(puntosInteres);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(respuesta).build();

				}
				return Response.status(201).entity(respuesta).build();
			
			}
			
			@GET
			@Produces(MediaType.APPLICATION_JSON) 
			@Path("/listarCalles")
			public Response listarCalles() {
		
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				
				String respuesta = null;
				ArrayList<String> listCalles;
				
				try {					
					listCalles = ipc.listarCalles();
					respuesta = gson.toJson(listCalles);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(respuesta).build();

				}
				return Response.status(201).entity(respuesta).build();
			
			}
			
			@GET
			@Produces(MediaType.APPLICATION_JSON) 
			@Path("/listarEsquinas")
			public Response listarEsquinas() {
		
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.create();
				
				String respuesta = null;
				ArrayList<String> listEquinas;
				
				try {					
					listEquinas = ipc.listarEsquinas();
					respuesta = gson.toJson(listEquinas);
					
				} catch (Exception err) {
					err.printStackTrace();
					return Response.status(500).entity(respuesta).build();

				}
				return Response.status(201).entity(respuesta).build();
			
			}
			
			
	
}
