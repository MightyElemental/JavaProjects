package net.mightyelemental.ldsracing.wagon.parts;

public class Wheel extends WagonPart {

	public Wheel() {
		this.setyRelativeToWagon(60);
		this.setxRelativeToWagon(-20);
	}

	public Wheel clone() {
		return (Wheel) new Wheel().setDisplayImage(this.getDisplayImage()).setyRelativeToWagon(this.getyRelativeToWagon())
				.setxRelativeToWagon(this.getxRelativeToWagon());
	}

}
