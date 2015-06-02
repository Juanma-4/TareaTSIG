package wrappers;

import com.google.gson.annotations.SerializedName;

public class WrapperPuntoInteres {

	@SerializedName("nombre")
	private String nombre;

	@SerializedName("x")
	private Double x;

	@SerializedName("y")
	private Double y;

	@SerializedName("distancia")
	private Double distancia;

	@SerializedName("tipo")
	private String tipo;
	
	public WrapperPuntoInteres() {
	}
	
	public WrapperPuntoInteres(String nombre, Double x, Double y,
			Double distancia,String tipo) {
		this.nombre = nombre;
		this.x = x;
		this.y = y;
		this.distancia = distancia;
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
}
