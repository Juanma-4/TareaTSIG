package controladores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dominio.Propiedad;
import dominio.Usuario;
import persistencia.IPropiedadDAO;
import wrappers.WrapperPropiedadFiltrada;


@Stateless
public class ControladorPropiedad implements IControladorPropiedad{
	
	@EJB
	private IPropiedadDAO PropiedadDAO;
	
	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta, String Calle,String fid, String tipoMoneda,String piso ,Usuario Usuario){
		
		boolean guardo = false;
		
		try{			
			
			Propiedad p = new Propiedad(Precio,CantDorm,CantBanio,MetrosCuadrados,Parrillero,Garage,TipoPropiedad,Estado,Tipotransaccion,NumeroPuerta,Calle,fid,tipoMoneda,piso,Usuario);					
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
		String tipoMoneda = jobj.get("tipoMoneda").getAsString();
		String piso = jobj.get("piso").getAsString();
		String usuario = jobj.get("usuario").getAsString();
		
		boolean modifico = false;		
		
		try{		
							
			modifico = PropiedadDAO.modificarPropiedad(calle,precio,cantDorm,cantBanio,metrosCuadrados,parrillero,garage,
														tipoPropiedad,tipotransaccion,tipoEstado,numeroPuerta,fid,
														tipoMoneda,piso,usuario);		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return modifico;
	}


	public List<WrapperPropiedadFiltrada> listarPropiedades(ArrayList<String> filtros) {
		
		List<WrapperPropiedadFiltrada> props = new ArrayList<WrapperPropiedadFiltrada>();;
		
		try{		
			List<Object[]> propiedadesFiltradas = PropiedadDAO.listarPropiedades(filtros);
			
			for (Object[] propiedad: propiedadesFiltradas) {
				WrapperPropiedadFiltrada pf = new WrapperPropiedadFiltrada((Double)propiedad[8], (Integer)propiedad[3], (Integer)propiedad[2], (Double)propiedad[5],
						(Boolean)propiedad[7], (Boolean)propiedad[4], (String)propiedad[10], (String)propiedad[9], (String)propiedad[11],(Integer)propiedad[6],
						(String) propiedad[1],(String) propiedad[13],(String)propiedad[15], (String)propiedad[14], (Double)propiedad[16], ((Double)propiedad[17]),
						(String)propiedad[12]);
				System.out.println("Estoy en listar Propiedad: "+ pf.getCalle()+pf.getPrecio()+pf.getParrillero()+pf.getUsuario());
				props.add(pf);
	//			Datatalcosa = new datatalcosa(propiedad[0],propiedad[1].....)
	//			propiedadesFiltradasString.add(propiedad[0],)
	//			propiedad[0]);
	//			propiedad[1]);
	//			propiedad[2]);
	//			propiedad[3]);
			 }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return props;
	}

}

