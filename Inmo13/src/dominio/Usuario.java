package dominio;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L; // Mapping JPA

	@Id
	@Column(name = "mail", nullable = false)
	private String mail;

	@Column(name = "password", nullable = false)
	private String password;
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.LAZY)
//	private List<Propiedad> propiedades;
//	
	public Usuario(){}
	
	public Usuario(String mail,String password){
		this.mail = mail;
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public List<Propiedad> getPropiedades() {
//		return propiedades;
//	}
//
//	public void setPropiedades(List<Propiedad> propiedades) {
//		this.propiedades = propiedades;
//	}	
//	
//	public boolean administraestaPropiedad (Integer id){
//		 for(Propiedad p : propiedades)
//			{
//				if(p.getId()==id)
//					return true;
//			}
//			return false;
//	}	
	
	
}
