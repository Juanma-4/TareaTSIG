package persistencia;

import java.util.ArrayList;
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
			if (!this.existeUsuario(usuario)){
			em.persist(usuario);
			guardo = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guardo;

	}
	
	public boolean modificarUsuario(Usuario usuario) {
		
		boolean modifico = false;
		try {
//			if (this.existeUsuario(usuario.getMail())){
//				System.out.println("estoy en modificar usuario DAO mail: "+ usuario.getMail());
				Usuario user = em.find(Usuario.class, usuario.getMail()); 
				user.setPassword(usuario.getPassword());
				em.persist(user);			
				modifico = true;
//    		}
			
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
		List<Usuario> usuarios = null;
		
		try{	
//			usuarios= em.createQuery("SELECT o FROM Usuario o",Usuario.class).getResultList();
			usuarios = new ArrayList<Usuario>(em.createNativeQuery("SELECT * FROM usuario",Usuario.class).getResultList());
		}catch(Exception e){
			e.printStackTrace();
		}
		return usuarios;
	}	

    
    public Boolean delete(Usuario u)
    {
    	System.out.println("eliminar Usuarios en dato DAO"+ u.getMail());
    	Boolean elimino = false;
    	
    	try{
    		if (this.existeUsuario(u)){
    			em.remove(u);
    			elimino = true;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return elimino;
    } 
    
    public boolean existeUsuario (String mail){
    	//ad.fid = '"+fid.trim()+"'
		return (em.createQuery("SELECT u FROM Usuario u WHERE u.mail='"+mail+"'").getResultList().size()== 1);
	}
	
}
