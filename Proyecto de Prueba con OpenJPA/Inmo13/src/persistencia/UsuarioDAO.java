package persistencia;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dominio.Usuario;

@Stateless
public class UsuarioDAO implements IUsuarioDAO{
	
	@PersistenceContext(unitName="Inmo")
    private EntityManager em;	

	public boolean guardarUsuario(Usuario usuario) {

		boolean guardo = false;
		
		try {
			em.persist(usuario);
			guardo = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guardo;

	}
	
	
	public boolean modificarUsuario(Usuario usuario) {
		
		boolean modifico = false;
		
		try {
			em.merge(usuario);			
			modifico = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return modifico;
	}


	public boolean existeUsuario(Usuario usuario) {
		
		boolean existe = false;
		try {
			Usuario user = em.find(Usuario.class, usuario.getMail()); // Si no se encuentra retorna NULL
			existe = user == null ? false:true;
		}catch(Exception e){
			e.printStackTrace();
		}

		return existe;
	}

	public Usuario getUsuario(String mail) {		
		
		Usuario usuario = null;
		
		try{			
			usuario = em.find(Usuario.class, mail); // Si no se encuentra retorna NULL
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return usuario;
	}




	
}
