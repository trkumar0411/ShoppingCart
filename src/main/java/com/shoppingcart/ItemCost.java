package com.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ItemCost {

	public static final String CURRENCY = "";
	private static Map<Item, Double> costDetail = new HashMap<>();

	static {
		costDetail.put(Item.APPLE, 0.60);
		costDetail.put(Item.ORRANGE, 0.25);
	}

	private ItemCost() { 

	}

	public static double getCost(final Item item) {
		double cost = 0.00;
		if(null != item) {
			cost = costDetail.get(item);
		}
		return cost;
	}

	public static double getCost(final Item item, final int quantity) {
		final int effectiveQuantity = ItemOffer.getEffectiveQuantity(item, quantity);
		double actualCost = getCost(item);
		actualCost = actualCost * effectiveQuantity;
		return actualCost;
	}

	public static double getTotalBill(final Map<Item, ? extends Number> cart) {
		final Map<Item, Double> costDetails =  cart.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> getCost(entry.getKey(), entry.getValue().intValue())));
		return costDetails.values().stream().mapToDouble(Double::doubleValue).sum();

	}

	public static double getTotalBill(final List<Item> cart) {
		final Map<Item, Long> itemQuantity = cart.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		final double totalCost = getTotalBill(itemQuantity);
		System.out.println(CURRENCY + totalCost);
		return totalCost;

	}

	public static void main(String[] args) {
		List<Item> itemsCart = new ArrayList<>();
		itemsCart.add(Item.APPLE);
		itemsCart.add(Item.APPLE);
		itemsCart.add(Item.ORRANGE);
		itemsCart.add(Item.APPLE);

		System.out.println(CURRENCY+getTotalBill(itemsCart));
	}
}

