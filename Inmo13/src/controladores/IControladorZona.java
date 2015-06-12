package controladores;
import java.util.List;

import javax.ejb.Local;

import wrappers.WrapperZona;
import dominio.Usuario;
import dominio.Zona;

@Local
public interface IControladorZona {
	public List<WrapperZona> actualizarInfoZonas();

	public boolean modificarZona(String datosZona);
}
