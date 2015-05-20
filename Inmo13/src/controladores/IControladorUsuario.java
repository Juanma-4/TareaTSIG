package controladores;
import java.util.List;

import javax.ejb.Local;

import dominio.Usuario;

@Local
public interface IControladorUsuario {
	public boolean guardarUsuario(String mail, String password);
	public boolean existeUsuario(String mail, String password);
	public Usuario buscarUsuario(String mail);
	public boolean modificarUsuario(Usuario u);
	public List<Usuario> listarUsuarios();
	public void eliminarUsuario(String mail);
}
