package persistencia;

import javax.ejb.Local;

import dominio.Propiedad;
import dominio.Usuario;

@Local
public interface IPropiedadDAO  {

	public boolean guardarPropiedad(Propiedad propiedad);
	public boolean modificarPropiedad(String calle, double precio,
			Integer cantDorm, Integer cantBanio, double metrosCuadrados,
			boolean parrillero, boolean garage, String tipoPropiedad,
			String tipotransaccion, String tipoEstado, Integer numeroPuerta,
			String fid, String tipoMoneda, String piso, String usuario);
}
