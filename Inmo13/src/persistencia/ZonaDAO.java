package persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import wrappers.WrapperPuntoInteres;
import wrappers.WrapperZona;
import dominio.Usuario;
import dominio.Zona;

@Stateless
public class ZonaDAO implements IZonaDAO{
	
	@PersistenceContext(unitName="Inmo")
    private EntityManager em;		
	

	@SuppressWarnings("unchecked")
	@Override
	public List<WrapperZona> actualizarZonas() {
		String sql = null;
		
		List<WrapperZona> zonas = null;
		List<WrapperZona> retorno = new ArrayList<WrapperZona>();
		try{
			
			sql = "SELECT zonas.nombre AS nombre, zonas.descripcion AS descripcion, count(propiedad.id) AS propiedades "
						+ " FROM propiedad, zonas"
						+ " WHERE ST_INTERSECTS(ST_BUFFER(propiedad.geom,300), zonas.geom)"
						+ " GROUP BY zonas.nombre, zonas.descripcion"
						+ " ORDER BY propiedades DESC";
			zonas = em.createNativeQuery(sql,WrapperZona.class).getResultList();
	
			for(WrapperZona pi : zonas){
				retorno.add(pi);
			}
										
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retorno;
	}
}
