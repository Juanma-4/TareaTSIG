
package wrappers;

import com.google.gson.annotations.SerializedName;

public class WrapperZona {

	@SerializedName("nombre")
	private String nombre;
	@SerializedName("descripcion")
	private String descripcion;
	@SerializedName("propiedades")
	private Integer propiedades;

	public WrapperZona() {
	}

	public WrapperZona(String nombre, String descripcion, Integer propiedades) {

		this.nombre = nombre;
		this.descripcion = descripcion;
		this.propiedades = propiedades;

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

	

}
