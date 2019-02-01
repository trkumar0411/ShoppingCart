package com.shoppingcart;

import java.util.HashMap;
import java.util.Map;

public class ItemOffer {

	private static final Map<Item, Offer> itemOfferDetail = new HashMap<>();

	static {
		itemOfferDetail.put(Item.APPLE, Offer.BUY_ONE_GET_ONE);
		itemOfferDetail.put(Item.ORRANGE, Offer.THREE_FOR_PRICE_OF_TWO);
	}

	private ItemOffer() {

	}

	/**
	 * @param item
	 * @return
	 */
	public static Offer getOffer(final Item item) {
		return itemOfferDetail.get(item);
	}

	public static int getEffectiveQuantity(final Offer offer, final int quantity) {
		int effectiveQuantity = quantity;
		
		if(null != offer) {

			switch(offer) {
			case BUY_ONE_GET_ONE:
				final int quot1 = quantity / 2;
				final int remi1 = quantity % 2;
				effectiveQuantity = quot1 + remi1;
				break;
			case THREE_FOR_PRICE_OF_TWO:
				final int quot2 = quantity / 3;
				final int remi2 = quantity % 3;
				effectiveQuantity = quot2 * 2 + remi2;
				break;
			default:
				break;
			}
		}
		return effectiveQuantity;
	}

	public static int getEffectiveQuantity(final Item item, final int quantity) {
		final Offer offer = ItemOffer.getOffer(item);
		return getEffectiveQuantity(offer, quantity);
	}
	
	public static void main(String[] args) {
		for(int index = 1; index <= 10; index ++) {
			//System.out.println(Item.APPLE + "   {Actual Quantity : " + index + " } {Effective Quantity : " + getEffectiveQuantity(Item.APPLE, index) + " } ");
			System.out.println(Item.ORRANGE + " {Actual Quantity : " + index + " } {Effective Quantity : " + getEffectiveQuantity(Item.ORRANGE, index) + " } ");
			System.out.println();
		}
	}

}
