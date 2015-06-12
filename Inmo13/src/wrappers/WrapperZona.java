
package wrappers;

import com.google.gson.annotations.SerializedName;

public class WrapperZona {

	@SerializedName("nombre")
	private String nombre;
	@SerializedName("descripcion")
	private String descripcion;
	@SerializedName("propiedades")
	private Integer propiedades;
	@SerializedName("nivel")
	private String nivel;	
	@SerializedName("fid")
	private String fid;

	public WrapperZona() {
	}

	public WrapperZona(String nombre, String descripcion, Integer propiedades, String nivel, String fid) {

		this.nombre = nombre;
		this.descripcion = descripcion;
		this.propiedades = propiedades;
		this.nivel = nivel;
		this.fid = fid; 

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Integer propiedades) {
		this.propiedades = propiedades;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}


	

}
