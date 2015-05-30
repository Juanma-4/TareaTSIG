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
		
		// Filtros 
		String tipoPropiedad = filtros.get(0);
		String tipoTransaccion = filtros.get(1);
		String tipoMoneda = filtros.get(2); // VER BIEN
		//VER LO DE LOS MINIMOS Y MAXIMOS!
		
		Integer cantBanio = Integer.parseInt(filtros.get(5));
		Integer cantDorm = Integer.parseInt(filtros.get(6));
		String barrio = filtros.get(8);
		
		Boolean parrillero = Boolean.parseBoolean(filtros.get(9));
		Boolean garage = Boolean.parseBoolean(filtros.get(10));
		
		
		Integer distanciaMar = Integer.parseInt(filtros.get(11));
		Integer distanciaParada = Integer.parseInt(filtros.get(12));
		Integer distanciaPuntoInteres = Integer.parseInt(filtros.get(13));
		
		String sql = null;
		List<Object[]> propiedadesFiltradas = null;
		try {			
			sql = "SELECT propiedad.id,propiedad.calle,propiedad.cantbanio,propiedad.cantdorm,propiedad.garage,propiedad.metroscuadrados, "
					+ "propiedad.numeropuerta,propiedad.parrillero,propiedad.precio,propiedad.tipoestado,propiedad.tipopropiedad, "
					+ "propiedad.tipotransaccion,propiedad.usuario,propiedad.fid,propiedad.piso,propiedad.tipomoneda, "
					+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud "
					+ "FROM propiedad,barrios " 
					+ "WHERE propiedad.tipopropiedad = '"+tipoPropiedad+"' AND propiedad.tipotransaccion = '"+tipoTransaccion+"'"
					+ " AND propiedad.cantbanio = "+cantBanio+" AND propiedad.cantdorm = "+cantDorm+" AND propiedad.parrillero = "+parrillero 
					+ " AND propiedad.garage = "+garage+" AND propiedad.tipoestado IN ('Publica','Reservada')"
					+ " AND ST_CONTAINS(barrios.geom,propiedad.geom)";
			
			if(!barrio.equalsIgnoreCase("Todos")){
				sql += " AND barrios.barrio = '"+barrio+"')";
			}
	
			//+ " AND ST_CONTAINS(barrios.geom,propiedad.geom) AND barrios.barrio IN ('CIUDAD VIEJA','POCITOS')").getResultList();

			propiedadesFiltradas = em.createNativeQuery(sql).getResultList();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return propiedadesFiltradas;
	}
}
