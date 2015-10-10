package net.mightyelemental.basicengine.inventory;

import net.mightyelemental.basicengine.items.Item;
import net.mightyelemental.basicengine.items.ItemStack;
import net.mightyelemental.basicengine.world.World;

@SuppressWarnings( "rawtypes" )
public class Inventory {

	public ItemStack[] inv = new ItemStack[24];

	public double totalWeight;

	public int selectedSlot = 0;

	public void clearInv() {
		for (int i = 0; i < inv.length; i++) {
			inv[i] = null;
		}
		refreshInventory();
	}

	private void calculateWeight() {
		this.totalWeight = 0;
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				totalWeight += (inv[i].amount * inv[i].item.weightTotal);
			}
		}
	}

	public boolean addItem(Item item, World worldObj) {
		if (item.name.equals("NON-EXISTANT")) {
			refreshInventory();
			return false;
		}
		if (item.weightTotal + this.totalWeight > worldObj.player.maximumWeightCap) {
			refreshInventory();
			return false;
		}
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].item.name.equals(item.name) && inv[i].amount < 99) {
					inv[i].amount++;
					calculateWeight();
					refreshInventory();
					return true;
				}
			}
		}
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] == null) {
				inv[i] = new ItemStack(item, 1);
				calculateWeight();
				refreshInventory();
				return true;
			}
		}
		refreshInventory();
		return false;
	}

	public void addItemStack(ItemStack item) {
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].item.name.equals(item.item.name) && inv[i].amount < 99 - item.amount) {
					inv[i].amount += item.amount;
					calculateWeight();
					refreshInventory();
					return;
				}
			}
		}
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] == null) {
				inv[i] = item;
				refreshInventory();
				return;
			}
		}
		refreshInventory();
		calculateWeight();
	}

	public int contains(Item item) {
		int temp = 0;
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].item.name.equals(item.name)) {
					temp += inv[i].amount;
					refreshInventory();
					return temp;
				}
			}
		}
		refreshInventory();
		return temp;
	}

	public boolean removeItem(Item item) {
		if (contains(item) > 0) {
			for (int i = 0; i < inv.length; i++) {
				if (inv[i] != null) {
					if (inv[i].item.equals(item)) {
						inv[i].amount -= 1;
						refreshInventory();
						return true;
					}
				}
			}
		}
		refreshInventory();
		return false;
	}

	public boolean removeItemStack(ItemStack item) {
		if (contains(item.item) >= item.amount) {
			for (int i = 0; i < inv.length; i++) {
				if (inv[i] != null) {
					if (inv[i].item.name.equals(item.item.name)) {
						inv[i].amount -= item.amount;
						refreshInventory();
						return true;
					}
				}
			}
		}
		refreshInventory();
		return false;
	}

	/** Used after every method that modifies the inventory */
	public void refreshInventory() {
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].amount > 99) {
					inv[i].amount = 99;
				}
				if (inv[i].amount <= 0) {
					inv[i] = null;
				}
			}
		}
		this.calculateWeight();
	}

	public int getItemAmount(String itemName) {
		int amount = 0;
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].item.name.equals(itemName)) {
					amount += inv[i].amount;
				}
			}
		}
		refreshInventory();
		return amount;
	}

	public Inventory() {
		calculateWeight();
		refreshInventory();
	}

}
