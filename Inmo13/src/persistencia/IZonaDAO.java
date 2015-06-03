package persistencia;
import java.util.List;

import javax.ejb.Local;

import wrappers.WrapperZona;
import dominio.Zona;

@Local
public interface IZonaDAO {	
	
	public List<WrapperZona> actualizarZonas();
    
}



 