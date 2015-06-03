package controladores;
import java.util.List;

import javax.ejb.Local;

import dominio.Usuario;
import dominio.Zona;

@Local
public interface IControladorZona {
	public List<Zona> actualizarInfoZonas();
}
