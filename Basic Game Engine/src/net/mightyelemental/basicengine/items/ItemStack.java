package net.mightyelemental.basicengine.items;

@SuppressWarnings( "rawtypes" )
public class ItemStack {

	public Item	item;
	public int	amount;

	public ItemStack( Item item, int amount ) {
		this.item = item;
		this.amount = amount;
	}

	public ItemStack( Item item ) {
		this.item = item;
		this.amount = 1;
	}

	public ItemStack setAmount(int amount) {
		if (amount < 100) {
			this.amount = amount;
		} else {
			this.amount = 99;
		}
		return this;
	}

}
