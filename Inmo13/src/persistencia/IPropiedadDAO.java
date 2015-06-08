package persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import wrappers.WrapperPropiedadFiltrada;
import wrappers.WrapperPuntoInteres;
import dominio.Propiedad;
import dominio.Usuario;

@Local
public interface IPropiedadDAO  {

	public boolean guardarPropiedad(Propiedad propiedad);
	public boolean modificarPropiedad(String calle, double precio,
			Integer cantDorm, Integer cantBanio, double metrosCuadrados,
			boolean parrillero, boolean garage, String tipoPropiedad,
			String tipotransaccion, String tipoEstado, Integer numeroPuerta,
			String fid, String imagen, String piso, String usuario);
	public List<WrapperPropiedadFiltrada> listarPropiedades(ArrayList<String> filtros);
	public List<WrapperPuntoInteres> listarPuntosInteres(String fid);
	public List<String> listarIds(String fid);
}
