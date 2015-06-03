package controladores;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import persistencia.IZonaDAO;
import wrappers.WrapperZona;
import dominio.Zona;

@Stateless
public class ControladorZona implements IControladorZona {

	@EJB
	private IZonaDAO zonaDAO;

	@Override
	public List<WrapperZona> actualizarInfoZonas() {
		List<WrapperZona> zonas = zonaDAO.actualizarZonas();
		return zonas;
	}
	
	
}
