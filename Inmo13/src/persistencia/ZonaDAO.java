package persistencia;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dominio.Usuario;
import dominio.Zona;

@Stateless
public class ZonaDAO implements IZonaDAO{
	
	@PersistenceContext(unitName="Inmo")
    private EntityManager em;		
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Zona> actualizarZonas() {
		// TODO Auto-generated method stub
		return em.createQuery("SELECT z FROM Zona z").getResultList();
	}

	
}
