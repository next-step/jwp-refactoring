package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainFactory {
	public static OrderTable createOrderTable(boolean empty) {
		return createOrderTable(0, empty, null);
	}

	public static OrderTable createOrderTable(int numberOfGuests) {
		return createOrderTable(numberOfGuests, true, null);
	}

	public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
		return createOrderTable(numberOfGuests, empty, null);
	}

	public static OrderTable createOrderTable(int numberOfGuests, boolean empty, Long tableGroupId) {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(empty);
		orderTable.setTableGroupId(tableGroupId);

		return orderTable;
	}

	public static List<OrderTable> createOrderTables(Long... ids) {
		List<OrderTable> orderTables = new ArrayList<>();

		for (Long id : ids) {
			OrderTable orderTable = new OrderTable();
			orderTable.setId(id);

			orderTables.add(orderTable);
		}

		return orderTables;
	}

	public static TableGroup createTableGroup(Long... ids) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(createOrderTables(ids));

		return tableGroup;
	}

	public static Product createProduct(String name, int price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(new BigDecimal(price));

		return product;
	}

	public static MenuGroup createMenuGroup(String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);

		return menuGroup;
	}

	public static Menu createMenu(String name, int price, Long menuGroupId, MenuProduct... menuProducts) {
		Menu menu = new Menu();
		menu.setName(name);
		menu.setPrice(new BigDecimal(price));
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(Arrays.asList(menuProducts));

		return menu;
	}

	public static MenuProduct createMenuProduct(Long productId, long quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(quantity);

		return menuProduct;
	}

	public static Order createOrder(Long orderedTableId, OrderLineItem... orderLineItems) {
		return createOrder(orderedTableId, null, orderLineItems);
	}

	public static Order createOrder(String orderStatus) {
		return createOrder(null, orderStatus);
	}

	public static Order createOrder(Long orderedTableId, String orderStatus, OrderLineItem... orderLineItems) {
		Order order = new Order();
		order.setOrderTableId(orderedTableId);
		order.setOrderStatus(orderStatus);
		order.setOrderLineItems(Arrays.asList(orderLineItems));

		return order;
	}

	public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);

		return orderLineItem;
	}
}
