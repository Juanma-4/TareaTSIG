package controladores;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

	@Override
	public boolean modificarZona(String datosZona) {
		
			JsonObject jobj = new Gson().fromJson(datosZona, JsonObject.class);
			String nombre = jobj.get("nombre").getAsString();
			String desc = jobj.get("descripcion").getAsString();
			String fid = jobj.get("fid").getAsString();
		
			boolean modifico = false;		
			
			try{
				modifico = zonaDAO.modificarZona(nombre, desc, fid);		
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return modifico;
	}
	
	
}
