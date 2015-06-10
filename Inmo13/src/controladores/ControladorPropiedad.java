package controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dominio.Propiedad;
import dominio.Usuario;
import persistencia.IPropiedadDAO;
import wrappers.WrapperPropiedadFiltrada;
import wrappers.WrapperPuntoInteres;


@Stateless
public class ControladorPropiedad implements IControladorPropiedad{
	
	@EJB
	private IPropiedadDAO PropiedadDAO;
	
	
public String generarFid(String fid) {
		
		String idGenerado = fid;
		List<String> ids = new ArrayList<String>();
		
		Random random = new Random();
		int randomInt = random.nextInt(500 - 100 + 1) + 100; // de 100 a 500
		
		Boolean unico = false;
		try{			
			
			ids = PropiedadDAO.listarIds(fid);	
			
			while(!unico){
				if(ids.contains(idGenerado)){
					idGenerado = "OpenLayers_Feature_Vector_"+randomInt++;
				}else{
					unico = true;
				}		
			}
			
					
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return idGenerado;
	}

	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta, String Calle,String fid, String imagen,String piso ,Usuario Usuario){
		
		boolean guardo = false;
		
		try{			
			
			Propiedad p = new Propiedad(Precio,CantDorm,CantBanio,MetrosCuadrados,Parrillero,Garage,TipoPropiedad,Estado,Tipotransaccion,NumeroPuerta,Calle,fid,piso,Usuario,imagen);					
			guardo = PropiedadDAO.guardarPropiedad(p);		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return guardo;
		
	}

	@Override
	public boolean modificarPropiedad(String datosPropiedad) {
		
	
		JsonObject jobj = new Gson().fromJson(datosPropiedad, JsonObject.class);
		String calle = jobj.get("calle").getAsString();
		double precio = jobj.get("precio").getAsDouble();	
		Integer cantDorm = jobj.get("cantDorm").getAsInt();
		Integer cantBanio = jobj.get("cantBanio").getAsInt();
		double metrosCuadrados = jobj.get("metrosCuadrados").getAsDouble();
		boolean parrillero = jobj.get("parrillero").getAsBoolean();
		boolean garage = jobj.get("garage").getAsBoolean();
		String tipoPropiedad = jobj.get("tipoPropiedad").getAsString();
		String tipotransaccion = jobj.get("tipotransaccion").getAsString();
		String tipoEstado = jobj.get("tipoEstado").getAsString();
		Integer numeroPuerta = jobj.get("numeroPuerta").getAsInt();
		String fid = jobj.get("fid").getAsString();
		String imagen = jobj.get("imagen").getAsString();
		String piso = jobj.get("piso").getAsString();
		String usuario = jobj.get("usuario").getAsString();
		
		boolean modifico = false;		
		
		try{		
							
			modifico = PropiedadDAO.modificarPropiedad(calle,precio,cantDorm,cantBanio,metrosCuadrados,parrillero,garage,
														tipoPropiedad,tipotransaccion,tipoEstado,numeroPuerta,fid,
														imagen,piso,usuario);		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return modifico;
	}

	@Override
	public List<WrapperPropiedadFiltrada> listarPropiedades(ArrayList<String> filtros) {
			
			List<WrapperPropiedadFiltrada> props = new ArrayList<WrapperPropiedadFiltrada>();;
			System.out.println("FILTROS :"+ filtros);
			try{	
				
				 props = PropiedadDAO.listarPropiedades(filtros);			
				
				
	//			List<Object[]> propiedadesFiltradas = PropiedadDAO.listarPropiedades(filtros);
	//			
	//			for (Object[] propiedad: propiedadesFiltradas) {
	//				WrapperPropiedadFiltrada pf = new WrapperPropiedadFiltrada((Double)propiedad[8], (Integer)propiedad[3], (Integer)propiedad[2], (Double)propiedad[5],
	//						(Boolean)propiedad[7], (Boolean)propiedad[4], (String)propiedad[10], (String)propiedad[9], (String)propiedad[11],(Integer)propiedad[6],
	//						(String) propiedad[1],(String) propiedad[13],(String)propiedad[15], (String)propiedad[14], (Double)propiedad[16], ((Double)propiedad[17]),
	//						(String)propiedad[12]);
	//
	//				props.add(pf);
	//			 }
				
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return props;
	}

	@Override
	public List<WrapperPuntoInteres> listarPuntosInteres(String fid) {
		
		List<WrapperPuntoInteres> puntosInteres = null;
		try{		
			puntosInteres = PropiedadDAO.listarPuntosInteres(fid);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return puntosInteres;
	}

	@Override
	public ArrayList<String> listarCalles() {
		ArrayList<String> listCalles = null;
		try{		
			listCalles = PropiedadDAO.listarCalles();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return listCalles;
	}

	
	public ArrayList<String> listarEsquinas() {
		ArrayList<String> listEsquinas = null;
		try{		
			listEsquinas = PropiedadDAO.listarEsquinas();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return listEsquinas;
	}

}

