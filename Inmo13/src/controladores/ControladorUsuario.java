package controladores;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dominio.Usuario;
import persistencia.IUsuarioDAO;

@Stateless
public class ControladorUsuario implements IControladorUsuario {

	@EJB
	private IUsuarioDAO UsuarioDAO;
		
	public boolean guardarUsuario(String mail, String Password) {
		
		boolean guardo = false;
		
		try{			
			
			Usuario u = new Usuario(mail,Password);					
			guardo = UsuarioDAO.guardarUsuario(u);		
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return guardo;
		
	}
	
	public boolean existeUsuario(String nick, String password) {
		boolean existe = false;
		try{
			 existe = UsuarioDAO.existeUsuario(new Usuario(nick,password));
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		return existe;
	}

	public Usuario buscarUsuario(String nick) {
		Usuario usuario = null;
		try{
			usuario = UsuarioDAO.getUsuario(nick);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return usuario;
	}

	public boolean modificarUsuario(Usuario usuario) {
		
		boolean modifico = false;
		try{
//			System.out.println("estoy en modificar usuario Controlador usuario mail: "+ usuario.getMail()+"pass: "+usuario.getPassword());
			modifico = UsuarioDAO.modificarUsuario(usuario);
		}catch(Exception e){
			e.printStackTrace();			
		}
		return modifico;
	}
	
	public List<Usuario> listarUsuarios() {
		List<Usuario> usuarios = null;
		
		try{
			usuarios = UsuarioDAO.listarUsuarios();
		}catch(Exception e){
			e.printStackTrace();			
		}
		return usuarios;
		  
	}	
	
	public Boolean eliminarUsuario(String mail) {
		System.out.println("eliminar Usuarios controlador :"+ mail);
		
		Boolean elimino=false;
		
		Usuario usu = UsuarioDAO.getUsuario(mail);
		
		if (usu!=null){
				elimino = UsuarioDAO.delete(usu);
		}
		return elimino;
	}
	
}
