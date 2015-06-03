package controladores;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import persistencia.IZonaDAO;
import dominio.Zona;

@Stateless
public class ControladorZona implements IControladorZona {

	@EJB
	private IZonaDAO zonaDAO;

	@Override
	public List<Zona> actualizarInfoZonas() {
		List<Zona> zonas = zonaDAO.actualizarZonas();
		return zonas;
	}
	
	
}
