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
		return createOrderTable(null, numberOfGuests, empty, tableGroupId);
	}

	public static OrderTable createOrderTable(Long id, int numberOfGuests, boolean empty, Long tableGroupId) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(id);
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

	public static Product createProduct(String name, BigDecimal price) {
		return createProduct(null, name, price);
	}

	public static Product createProduct(Long id, String name, BigDecimal price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setPrice(price);

		return product;
	}

	public static List<Product> createProducts(Long... ids) {
		List<Product> products = new ArrayList<>();

		for (Long id : ids) {
			Product product = new Product();
			product.setId(id);

			products.add(product);
		}

		return products;
	}

	public static MenuGroup createMenuGroup(String name) {
		return createMenuGroup(null, name);
	}

	public static MenuGroup createMenuGroup(Long id, String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(id);
		menuGroup.setName(name);

		return menuGroup;
	}

	public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, MenuProduct... menuProducts) {
		Menu menu = new Menu();
		menu.setName(name);
		menu.setPrice(price);
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(Arrays.asList(menuProducts));

		return menu;
	}

	public static List<Menu> createMenus(Long... ids) {
		List<Menu> menus = new ArrayList<>();

		for (Long id : ids) {
			Menu menu = new Menu();
			menu.setId(id);

			menus.add(menu);
		}

		return menus;
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
		return createOrder(null, orderedTableId, orderStatus, Arrays.asList(orderLineItems));
	}

	public static Order createOrder(Long orderId, Long orderedTableId, String orderStatus,
		List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setId(orderId);
		order.setOrderTableId(orderedTableId);
		order.setOrderStatus(orderStatus);
		order.setOrderLineItems(orderLineItems);

		return order;
	}

	public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
		return createOrderLineItem(null, null, menuId, quantity);
	}

	public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setSeq(seq);
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);

		return orderLineItem;
	}
}
