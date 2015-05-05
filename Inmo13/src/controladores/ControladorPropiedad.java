package controladores;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dominio.Propiedad;
import dominio.Usuario;
import persistencia.IPropiedadDAO;
import enumerados.Estado;
import enumerados.TipoPropiedad;
import enumerados.Transaccion;


@Stateless
public class ControladorPropiedad implements IControladorPropiedad{
	
	@EJB
	private IPropiedadDAO PropiedadDAO;
	
	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta, String Calle, Usuario Usuario){
		
		boolean guardo = false;
		
		try{			
			
			Propiedad p = new Propiedad(Precio,CantDorm,CantBanio,MetrosCuadrados,Parrillero,Garage,TipoPropiedad,Estado,Tipotransaccion,NumeroPuerta,Calle,Usuario);					
			guardo = PropiedadDAO.guardarPropiedad(p);		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return guardo;
		
	}


}

