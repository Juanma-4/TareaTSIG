package persistencia;

import javax.ejb.Local;

import dominio.Propiedad;
import dominio.Usuario;

@Local
public interface IPropiedadDAO  {

	public boolean guardarPropiedad(Propiedad propiedad);
}
