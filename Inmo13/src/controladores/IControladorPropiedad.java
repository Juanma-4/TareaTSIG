package controladores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import wrappers.WrapperPropiedadFiltrada;
import wrappers.WrapperPuntoInteres;
import dominio.Propiedad;
import dominio.Usuario;

@Local
public interface IControladorPropiedad {

	public boolean guardarPropiedad(double Precio, Integer CantDorm, Integer CantBanio, double MetrosCuadrados,boolean Parrillero, boolean Garage, String TipoPropiedad, String Estado ,String Tipotransaccion, Integer NumeroPuerta,String Calle ,String fid, String imagen,String piso,Usuario Usuario);
	public boolean modificarPropiedad(String datosPropiedad);
	public List<WrapperPropiedadFiltrada> listarPropiedades(ArrayList<String> filtros);
	public List<WrapperPuntoInteres> listarPuntosInteres(String fid);
	
}
