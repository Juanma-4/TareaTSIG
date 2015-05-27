package persistencia;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
