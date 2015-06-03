package persistencia;
import java.util.List;

import javax.ejb.Local;

import dominio.Zona;

@Local
public interface IZonaDAO {	
	
	public List<Zona> actualizarZonas();
    
}



 