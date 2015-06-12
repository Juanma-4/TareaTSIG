package persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import wrappers.WrapperPuntoInteres;
import wrappers.WrapperZona;
import dominio.Propiedad;
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
		Integer cantidadPropEnZonas = 0;
		Integer porcentajeZona;
		List<WrapperZona> zonas = null;
		List<WrapperZona> retorno = new ArrayList<WrapperZona>();
		try{
			
			sql = "SELECT zonas.nombre AS nombre, zonas.descripcion AS descripcion, count(propiedad.id) AS propiedades "
						+ " FROM propiedad, zonas"
						+ " WHERE ST_INTERSECTS(ST_BUFFER(propiedad.geom,300), zonas.geom)"
						+ " GROUP BY zonas.nombre, zonas.descripcion"
						+ " ORDER BY propiedades DESC";
			zonas = em.createNativeQuery(sql,WrapperZona.class).getResultList();
	
			for(WrapperZona wz : zonas){
				cantidadPropEnZonas = cantidadPropEnZonas + wz.getPropiedades();  // Total de las propiedades en zonas
			}
			
			for(WrapperZona pi : zonas){
				retorno.add(pi);
			}
			
			for(WrapperZona pi2 : retorno){
				porcentajeZona = ((pi2.getPropiedades()*100)/ cantidadPropEnZonas);
				
				if(porcentajeZona<25){
					pi2.setNivel("Baja");
				}
				else if((porcentajeZona>=25) && (porcentajeZona<84)){
					pi2.setNivel("Media");
				}
				else{
					pi2.setNivel("Alta");
				}
			}
			
			
										
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retorno;
	}


	@Override
	public boolean modificarZona(String nombre, String desc, String fid) {
			
			boolean modifico = false;
			try {
				Zona zona = (Zona) em.createQuery(
								"Select z From Zona z Where z.fid = '" + fid + "'").getSingleResult();
				
				zona.setNombre(nombre);
				zona.setDescripcion(desc);
				em.persist(zona);
				modifico = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return modifico;
		
	}
}
