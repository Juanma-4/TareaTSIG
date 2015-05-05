package controladores;

import javax.ejb.Local;

import dominio.Usuario;
import enumerados.Estado;
import enumerados.TipoPropiedad;
import enumerados.Transaccion;

@Local

public interface IPropiedadController {

	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta,String Calle ,Usuario Usuario);
	
}
