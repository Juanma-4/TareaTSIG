package persistencia;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dominio.Propiedad;
import dominio.Usuario;

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
}
