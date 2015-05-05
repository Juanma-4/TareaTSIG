package controladores;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import dominio.Usuario;

@Local
public interface IUsuarioController {
	public Boolean guardarUsuario(String mail, String password);
	public boolean existeUsuario(String mail, String password);
	public Usuario buscarUsuario(String mail);
	public boolean modificarUsuario(Usuario u);
}
