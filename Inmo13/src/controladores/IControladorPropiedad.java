package controladores;

import javax.ejb.Local;

import dominio.Usuario;

@Local
public interface IControladorPropiedad {

	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta,String Calle ,String fid, String tipoMoneda,String piso,Usuario Usuario);
	
}
