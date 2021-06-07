package br.itarocha.cartanatal.core.model.domain;

public class ItemAspecto {

	private PlanetaAspecto planetaA;
	private PlanetaAspecto planetaB;

	public ItemAspecto(){
		this.planetaA = new PlanetaAspecto();
		this.planetaB = new PlanetaAspecto();
	}
	
	private EnumAspecto aspecto;

	public PlanetaAspecto getPlanetaA() {
		return planetaA;
	}
	public void setPlanetaA(PlanetaAspecto planetaA) {
		this.planetaA = planetaA;
	}
	public PlanetaAspecto getPlanetaB() {
		return planetaB;
	}
	public void setPlanetaB(PlanetaAspecto planetaB) {
		this.planetaB = planetaB;
	}
	public EnumAspecto getAspecto() {
		return aspecto;
	}
	public void setAspecto(EnumAspecto aspecto) {
		this.aspecto = aspecto;
	}
}
