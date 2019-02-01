package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemCostTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void shouldGetTotalBill() {
		
		assertEquals("£2.05", ItemCost.CURRENCY+ItemCost.getTotalBill(asList(Item.APPLE, Item.APPLE, Item.ORRANGE,Item.APPLE)));
		assertEquals("£0", ItemCost.CURRENCY+ItemCost.getTotalBill(new ArrayList<Item>()));
	}
}
