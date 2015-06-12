package persistencia;
import java.util.List;

import javax.ejb.Local;

import wrappers.WrapperZona;
import dominio.Zona;

@Local
public interface IZonaDAO {	
	
	public List<WrapperZona> actualizarZonas();

	public boolean modificarZona(String nombre, String desc, String fid);
    
}



 