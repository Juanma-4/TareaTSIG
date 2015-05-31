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
			String fid, String imagen, String piso, String usuario) {
		
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
			propiedad.setImagen(imagen);
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
			datos.add(0,tipopropiedad);
			datos.add(1,tipotransaccion);
			datos.add(2, minimo);
			datos.add(3, maximo);
			datos.add(4, cantbanio);
			datos.add(5, cantdorm);
			datos.add(6, metroscuadrados);
			datos.add(7, barrio);
			datos.add(8, parrillero);
			datos.add(9, garage);
			datos.add(10, this.distanciaMar.toString());
			datos.add(11, this.distanciaParada.toString());
			datos.add(12, this.distanciaPInteres.toString());
 */

	@SuppressWarnings("unchecked")
	public List<Object[]> listarPropiedades(ArrayList<String> filtros) {
		
		// Filtros 
		String tipoPropiedad = filtros.get(0);
		String tipoTransaccion = filtros.get(1);
		
		Integer cantBanio = Integer.parseInt(filtros.get(4));
		Integer cantDorm = Integer.parseInt(filtros.get(5));
		String barrio = filtros.get(7);
		
		Boolean parrillero = Boolean.parseBoolean(filtros.get(8));
		Boolean garage = Boolean.parseBoolean(filtros.get(9));
				
		Integer distanciaMar = Integer.parseInt(filtros.get(10));
		Integer distanciaParada = Integer.parseInt(filtros.get(11));
		Integer distanciaPuntoInteres = Integer.parseInt(filtros.get(12));
		
		String from = null;
		String sql = null;
		List<Object[]> propiedadesFiltradas = null;
		try {		
			
			from = "propiedad";
			
			if(!barrio.equalsIgnoreCase("TODOS"))
				from += ",barrios";
			
			if(distanciaMar!= 0)
				from += ",borde_rambla";
			
			if(distanciaParada!= 0)				
				from += ",paradas";
			
			if(distanciaPuntoInteres!= 0)				
				from += ",serv_comerciales,edu_primaria,deportes";
			
			
			sql = "SELECT DISTINCT propiedad.id,propiedad.calle,propiedad.cantbanio,propiedad.cantdorm,propiedad.garage,propiedad.metroscuadrados, "
					+ "propiedad.numeropuerta,propiedad.parrillero,propiedad.precio,propiedad.tipoestado,propiedad.tipopropiedad, "
					+ "propiedad.tipotransaccion,propiedad.usuario,propiedad.fid,propiedad.piso,propiedad.imagen, "
					+ "ST_X(ST_Transform(propiedad.geom, 900913)) AS latitud , ST_Y(ST_Transform(propiedad.geom, 900913)) AS longitud "
					+ "FROM "+from+ " " 
					+ "WHERE propiedad.tipopropiedad = '"+tipoPropiedad+"' AND propiedad.tipotransaccion = '"+tipoTransaccion+"'"
					+ " AND propiedad.cantbanio = "+cantBanio+" AND propiedad.cantdorm = "+cantDorm+" AND propiedad.parrillero = "+parrillero 
					+ " AND propiedad.garage = "+garage+" AND propiedad.tipoestado IN ('Publica','Reservada')";
//					+ " AND ST_CONTAINS(barrios.geom,propiedad.geom)";
			
			if(!barrio.equalsIgnoreCase("TODOS")){
				sql += " AND ST_CONTAINS(barrios.geom,propiedad.geom) AND barrios.barrio = '"+barrio+"'";
			}
			
			if(!filtros.get(2).equalsIgnoreCase("NaN")){
				Double precioMinimo = Double.parseDouble(filtros.get(2));
				sql += " AND propiedad.precio >= "+precioMinimo;
			}
			
			if(!filtros.get(3).equalsIgnoreCase("NaN")){
				Double precioMaximo = Double.parseDouble(filtros.get(3));
				sql += " AND propiedad.precio <= "+precioMaximo;
			}
			
			if(!filtros.get(6).equalsIgnoreCase("NaN")){
				Double metrosCuadrados = Double.parseDouble(filtros.get(6));
				sql += " AND propiedad.metroscuadrados = "+metrosCuadrados;
			}
			
			if(distanciaMar!= 0){
				distanciaMar = (distanciaMar*1000); // para expresarlo en km.
				sql+= " AND ST_Intersects(ST_Buffer(borde_rambla.geom,"+distanciaMar+"),propiedad.geom)";
			}
			
			if(distanciaParada!= 0){
				sql+= " AND ST_Intersects(ST_Buffer(borde_rambla.geom,"+distanciaParada+"),paradas.geom)";
			}
			
			if(distanciaPuntoInteres!= 0){				
				sql+=" AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),serv_comerciales.geom)" 
					 + " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),edu_primaria.geom)"
					 + " AND ST_Intersects(ST_Buffer(propiedad.geom,"+distanciaPuntoInteres+"),deportes.geom)";
			}
			
			propiedadesFiltradas = em.createNativeQuery(sql).getResultList();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return propiedadesFiltradas;
	}
}
