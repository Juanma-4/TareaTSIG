package persistencia;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import wrappers.WrapperPropiedadFiltrada;
import dominio.Propiedad;

@Stateless
public class PropiedadDAO implements IPropiedadDAO{

	@PersistenceContext(unitName="Inmo")
    private EntityManager em;	
	
	
	public boolean guardarPropiedad(Propiedad propiedad) {

		boolean guardo = false;
		
		try {
			em.persist(propiedad);
			guardo = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guardo;

	}


	public boolean modificarPropiedad(String calle, double precio,
			Integer cantDorm, Integer cantBanio, double metrosCuadrados,
			boolean parrillero, boolean garage, String tipoPropiedad,
			String tipotransaccion, String tipoEstado, Integer numeroPuerta,
			String fid, String tipoMoneda, String piso, String usuario) {
		
		boolean modifico = false;
		try {
			Propiedad propiedad = (Propiedad) em.createQuery(
							"Select p From Propiedad p Where p.fid = '" + fid + "'").getSingleResult();
			
			propiedad.setCalle(calle);
			propiedad.setPrecio(precio);
			propiedad.setCantDorm(cantDorm);
			propiedad.setCantBanio(cantBanio);
			propiedad.setMetrosCuadrados(metrosCuadrados);
			propiedad.setParrillero(parrillero);
			propiedad.setGarage(garage);
			propiedad.setTipoPropiedad(tipoPropiedad);
			propiedad.setTipotransaccion(tipotransaccion);
			propiedad.setTipoEstado(tipoEstado);
			propiedad.setNumeroPuerta(numeroPuerta);
			propiedad.setTipoMoneda(tipoMoneda);
			propiedad.setPiso(piso);
			//propiedad.setUsuario(usuario);
			//propiedad.setFid(fid);
			em.merge(propiedad);
			em.refresh(propiedad);
			modifico = true;
		} catch (Exception e) {

			e.printStackTrace();

		}
		return modifico;
	}
	
/*
0,tipopropiedad);
	(1,tipotransaccion);
2, tipomoneda);
(3, minimo);
			datos.add(4, maximo);
			datos.add(5, cantbanio);
			datos.add(6, cantdorm);
			datos.add(7, metroscuadrados);
			datos.add(8, barrio);
			datos.add(9, parrillero);
			datos.add(10, garage);
			datos.add(11, distanciaMar);
			datos.add(12, distanciaParada);
			datos.add(13, distanciaPuntoInteres);
 */

	@SuppressWarnings("unchecked")
	public List<Object[]> listarPropiedades(ArrayList<String> filtros) {
		String sql = null;
		List<Object[]> propiedadesFiltradas = null;
		try {			
			
//			sql = "SELECT ";
//			if(!filtros.get(4).equals("")){
//				sql += "propiedad.id";
//			}
//			propiedadesFiltradas = em.createNativeQuery(sql);
			propiedadesFiltradas = em.createNativeQuery("SELECT propiedad.id,propiedad.calle, ST_X(propiedad.geom) AS latitud , ST_Y(propiedad.geom) AS longitud "
															+ "FROM propiedad,barrios " 
															+ "WHERE ST_CONTAINS(barrios.geom,propiedad.geom) AND barrios.barrio = '"+"CIUDAD VIEJA"+"'").getResultList();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return propiedadesFiltradas;
	}
}
