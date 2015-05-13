package persistencia;

import javax.ejb.Local;
import javax.ejb.Remote;

import dominio.Usuario;

@Local
public interface IUsuarioDAO {	
	public boolean guardarUsuario(Usuario usuario);	
	public boolean existeUsuario(Usuario usuario);
	public Usuario getUsuario(String nick);
	public boolean modificarUsuario(Usuario u);
}



 