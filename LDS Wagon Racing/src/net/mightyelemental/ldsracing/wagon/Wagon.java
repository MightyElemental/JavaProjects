package net.mightyelemental.ldsracing.wagon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.ldsracing.wagon.parts.Cart;
import net.mightyelemental.ldsracing.wagon.parts.Cover;
import net.mightyelemental.ldsracing.wagon.parts.Horse;
import net.mightyelemental.ldsracing.wagon.parts.Wheel;

public class Wagon {

	protected float locationX, locationY;

	private Cart	cart;
	private Cover	cover;
	private Wheel[]	wheels;
	private Horse	horse;

	public Wagon( Cart cart, Wheel wheel, Cover cover, Horse horse ) {
		this.cart = cart;
		this.cover = cover;
		this.wheels = new Wheel[] { wheel, (Wheel) wheel.clone().setxRelativeToWagon(200) };
		this.horse = horse;
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		float cartWidth = cart.getDisplayImage().getWidth();
		float cartHeight = cart.getDisplayImage().getHeight();
		g.drawImage(cart.getDisplayImage(), locationX, locationY);
		g.drawImage(wheels[0].getDisplayImage().getScaledCopy(cartWidth / 400f), locationX + wheels[0].getScaledXToWagon(cartWidth / 490f),
				locationY + wheels[0].getScaledYToWagon(cartHeight / 130f));
		g.drawImage(wheels[1].getDisplayImage().getScaledCopy(cartWidth / 400f), locationX + wheels[1].getScaledXToWagon(cartWidth / 490f),
				locationY + wheels[1].getScaledYToWagon(cartHeight / 130f));
	}

	public float getLocationX() {
		return locationX;
	}

	public void setLocationX(float locationX) {
		this.locationX = locationX;
	}

	public float getLocationY() {
		return locationY;
	}

	public void setLocationY(float locationY) {
		this.locationY = locationY;
	}

	public Cart getCart() {
		return cart;
	}

	public Cover getCover() {
		return cover;
	}

	public Wheel[] getWheels() {
		return wheels;
	}

	public Horse getHorse() {
		return horse;
	}

}
