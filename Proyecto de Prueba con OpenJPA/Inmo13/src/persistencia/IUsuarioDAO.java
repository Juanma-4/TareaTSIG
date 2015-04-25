package persistencia;

import java.util.List;

import javax.ejb.Local;

import dominio.Usuario;

@Local
public interface IUsuarioDAO {	
	public boolean guardarUsuario(Usuario usuario);	
	public boolean existeUsuario(Usuario usuario);
	public Usuario getUsuario(String nick);
	public boolean modificarUsuario(Usuario u);
}



 