package net.minegeek360.animalguesser;

public class AnimalBase {

	public int		Legs;
	public boolean	tail;
	public boolean	milk;
	public int		size;
	public int		color;
	public int		sound;
	public String	name;
	public boolean	wings;
	public boolean	moreColor;
	public int		foodChainPlace;

	public static final int	SIZE_INSECT	= 0;
	public static final int	SIZE_RODENT	= 1;
	public static final int	SIZE_DOG	= 2;
	public static final int	SIZE_CATTLE	= 3;
	public static final int	SIZE_BIGGER	= 4;

	// colors (DON'T CHANGE!!!)
	public static final int	COLOR_PINK	= 1;
	public static final int	COLOR_BROWN	= 2;
	public static final int	COLOR_GREEN	= 3;
	public static final int	COLOR_WHITE	= 4;
	public static final int	COLOR_BLACK	= 5;
	public static final int	COLOR_OTHER	= 6;

	public static final int	BAA			= 1;
	public static final int	MOO			= 2;
	public static final int	WOOF		= 3;
	public static final int	CROAK		= 4;
	public static final int	NONE		= 5;
	public static final int	OINK		= 6;
	public static final int	CLUCK		= 7;
	public static final int	MEOW		= 8;
	public static final int	SQUEAK		= 9;
	public static final int	TWITTWOO	= 10;
	public static final int	CLICK		= 11;
	public static final int	BUZZ		= 12;
	public static final int	OTHER		= 13;

	public static final int	MIDDLE	= 1;
	public static final int	TOP		= 2;

	public AnimalBase( int legs, boolean tail, boolean milk, int size, int color, int sound, String name, boolean wingsThatFly,
			boolean moreThan1Color, int foodChain ) {
		this.Legs = legs;
		this.tail = tail;
		this.milk = milk;
		this.size = size;
		this.color = color;
		this.sound = sound;
		this.name = name;
		this.wings = wingsThatFly;
		this.moreColor = moreThan1Color;
		this.foodChainPlace = foodChain;
	}

	public int getNumOfLegs() {
		return Legs;
	}

	public boolean hasTail() {
		return tail;
	}

	public boolean makesMilkForHumans() {
		return milk;
	}

	public int getSize() {
		return size;
	}

	public int getColor() {
		return color;
	}

	public int getSound() {
		return sound;
	}

	public String getName() {
		return name;
	}

	public boolean hasWings() {
		return wings;
	}

	public boolean hasMoreThan1Color() {
		return moreColor;
	}

	public int getFoodChainPlace() {
		return foodChainPlace;
	}

}
