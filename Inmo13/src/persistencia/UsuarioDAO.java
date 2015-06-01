package persistencia;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
			System.out.println("estoy en modificar usuario DAO");
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
			existe = ((user != null) && (user.getPassword().trim().equals(usuario.getPassword()))) ? true:existe;				
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

	@SuppressWarnings("unchecked")
	public List<Usuario> listarUsuarios() {
		//	List<Usuario> usus = em.createQuery("Select u FROM Usuario u", Usuario.class).getResultList();
		//return usus;
		Query q = em.createQuery("SELECT o FROM Usuario o");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listarUsuariosporProp(Integer id){
		
		 List<Usuario> usuarios = em.createQuery("SELECT o FROM Usuario o").getResultList();
		 List<Usuario> uresult = new LinkedList<Usuario>();	 
	
		 for(Usuario u : usuarios)
			{
				if(u.administraestaPropiedad(id))
					uresult.add(u);
			}
		return uresult;
		
	}

	public void insert(Usuario u)
    {
    	em.persist(u);
    }
    
    public void update(Usuario u)
    {
    	em.merge(u);
    }
    
    public void delete(Usuario u)
    {
    	System.out.println("eliminar Usuarios en dato DAO"+ u.getMail());
    	em.remove(u);
    } 
	
}
