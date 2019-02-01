package com.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.shoppingcart.util.NumberUtil;

public final class ItemCost {

	public static final String CURRENCY = "";
	private static final boolean cheapestForFree = true;
	private static Map<Item, Double> costDetail = new HashMap<>();

	static {
		costDetail.put(Item.APPLE, 0.60);
		costDetail.put(Item.ORRANGE, 0.25);
		costDetail.put(Item.BANANA, 0.20);
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

	public static double getCost(final Item item, final int quantity, final boolean getEffectiveQuantity) {
		final int effectiveQuantity = getEffectiveQuantity ? ItemOffer.getEffectiveQuantity(item, quantity) : quantity;
		double actualCost = getCost(item);
		//System.out.println("actualCost of Item " + item + " ::>> " + actualCost);
		//System.out.println("effectiveQuantity ::>> " + effectiveQuantity);
		actualCost = actualCost * effectiveQuantity;
		//System.out.println("actualCost of Item " + item + " ::>> " + actualCost);
		Double temp = Double.valueOf(actualCost);
		temp = BigDecimal.valueOf(temp).setScale(2, RoundingMode.HALF_UP).doubleValue();
		//System.out.println("temp of Item " + item + " ::>> " + temp);
		return temp.doubleValue();
	}

	public static double getCost(final Item item, final int quantity) {
		return getCost(item, quantity, true);
	}

	public static double getTotalBill(final Map<Item, ? extends Number> cart, final boolean getEffectiveQuantity) {
		double totalBill;
		Map<Item, Double> costDetails = cart.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> getCost(entry.getKey(), entry.getValue().intValue(), getEffectiveQuantity)));

		costDetails.forEach((item, cost) -> System.out.println(item +  " : " + NumberUtil.formatNumber(cost, ".", 2) ));
		totalBill = costDetails.values().stream().mapToDouble(Double::doubleValue).sum();
		return totalBill;
	}

	public static double getTotalBill(final Map<Item, ? extends Number> cart) {
		return getTotalBill(cart, true);

	}

	public static Entry<Item, Double> getMin(Map<Item, ? extends Number> buyOneGetOneItems, 
			final Map<Item, Double> costDetails) {

		Entry<Item, Double> min = Collections.min(costDetails.entrySet(),
				Comparator.comparing(Entry::getValue));
		
		//System.out.println("~~~~~~~~~~~~~~~~~");
		//System.out.println("MIN ::>> ");
		//System.out.println(min);
		//System.out.println("MIN ::>> ");
		//System.out.println("~~~~~~~~~~~~~~~~~");
		//System.out.println();
		//System.out.println();
		//System.out.println();

		final Map<Item, Double> itemsWithSamePrice = new HashMap<>();
		for(Map.Entry<Item, Double> entry : costDetails.entrySet()) {
			//System.out.println("entry.getValue() ::>> " + entry.getValue());
			//System.out.println("min.getValue() ::>> " + min.getValue());
			//System.out.println();
			if(entry.getValue().equals(min.getValue()) && entry.getKey() != min.getKey()) {
				itemsWithSamePrice.put(entry.getKey(), entry.getValue());
			}
		}
		

		if(!itemsWithSamePrice.isEmpty()) {
			itemsWithSamePrice.put(min.getKey(), min.getValue());
			//System.out.println("~~~~~~~~~~~~~~~");
			//System.out.println("otherItemsWithSamePrice");
			//System.out.println(itemsWithSamePrice);
			//System.out.println("~~~~~~~~~~~~~~~");
			
			Set<Item> items = itemsWithSamePrice.keySet();
			List<Item> itemsList = items.stream()
					.collect(Collectors.toList());
			
			//System.err.println(itemsList);
			
			Map<Item, Double> minValues = new HashMap<>();
			
			for(int i = 0; i < itemsList.size(); i++) {
				double totalCost = 0;
				Item item = itemsList.get(i);
				double cost = getCost(item, buyOneGetOneItems.get(item).intValue());
				totalCost += cost;
				for(int j = 0; j < itemsList.size(); j++) {
					if(i != j) {
						Item item1 = itemsList.get(j);
						double cost1 = getCost(item1, buyOneGetOneItems.get(item1).intValue(), false);
						totalCost += cost1;
					}
				}
				
				Double temp = Double.valueOf(totalCost);
				temp = BigDecimal.valueOf(temp).setScale(2, RoundingMode.HALF_UP).doubleValue();
				minValues.put(item, temp.doubleValue());

			}
			
			min = Collections.min(minValues.entrySet(),
					Comparator.comparing(Entry::getValue));
			
			//System.out.println("~~~~~~~~~~");
			//System.out.println("Min Values details");
			//System.out.println(minValues);
			//System.out.println("~~~~~~~~~~");
			
		}

		//System.out.println("~~~~~~~~~~");
		//System.out.println("Final Min details");
		//System.out.println(min.getKey() +  " : " + NumberUtil.formatNumber(min.getValue(), ".", 2));
		//System.out.println("~~~~~~~~~~");

		return min;
	}

	private static double getTotalBillWithCHeapestForFree(final Map<Item, Long> itemQuantity) {
		double totalCost;
		Map<Item, ? extends Number> buyOneGetOneItems = itemQuantity.entrySet()
				.stream()
				.filter(entry -> Offer.BUY_ONE_GET_ONE == ItemOffer.getOffer(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		Map<Item, Double> costDetails = buyOneGetOneItems.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getKey, entry -> getCost(entry.getKey(), entry.getValue().intValue())));
		//System.out.println("~~~~~~~~~~~~~");
		//System.out.println("BUY ONE GET ONE");
		//System.out.println(costDetails);
		//System.out.println("~~~~~~~~~~~~~");

		Entry<Item, Double> min = getMin(buyOneGetOneItems, costDetails);
		System.out.println(min.getKey() +  " : " + NumberUtil.formatNumber(min.getValue(), ".", 2) );
		buyOneGetOneItems.remove(min.getKey());

		double buyOneGetOneTotalCost  = getTotalBill(buyOneGetOneItems, false);
		buyOneGetOneTotalCost += min.getValue();

		Map<Item, ? extends Number> notBuyOneGetOneItems = itemQuantity.entrySet()
				.stream()
				.filter(entry -> Offer.BUY_ONE_GET_ONE != ItemOffer.getOffer(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		final double notBuyOneGetOneTotalCost  = getTotalBill(notBuyOneGetOneItems);

		totalCost = buyOneGetOneTotalCost + notBuyOneGetOneTotalCost;
		return totalCost;
	}
	
	public static double getTotalBill(final List<Item> cart) {

		double totalCost;
		final Map<Item, Long> itemQuantity = cart.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		//System.out.println(itemQuantity);

		if(cheapestForFree) {
			totalCost = getTotalBillWithCHeapestForFree(itemQuantity);
		}
		else {
			totalCost = getTotalBill(itemQuantity);
		}

		System.out.println("\n\n");
		System.out.println("\n\nTotal Bill :: " + CURRENCY + NumberUtil.formatNumber(totalCost, ".", 2));
		return totalCost;

	}

	public static void main(String[] args) {
		List<Item> itemsCart = new ArrayList<>();
		itemsCart.add(Item.APPLE);
		itemsCart.add(Item.APPLE);
		itemsCart.add(Item.APPLE);

		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);
		itemsCart.add(Item.BANANA);

		itemsCart.add(Item.ORRANGE);
		itemsCart.add(Item.ORRANGE);
		itemsCart.add(Item.ORRANGE);

		System.out.println( "Total Bill :: " + CURRENCY + NumberUtil.formatNumber(getTotalBill(itemsCart),".", 2));


	}
}
