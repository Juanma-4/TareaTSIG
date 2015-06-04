
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

	public WrapperZona() {
	}

	public WrapperZona(String nombre, String descripcion, Integer propiedades, String nivel) {

		this.nombre = nombre;
		this.descripcion = descripcion;
		this.propiedades = propiedades;
		this.nivel = nivel;

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

	

}
