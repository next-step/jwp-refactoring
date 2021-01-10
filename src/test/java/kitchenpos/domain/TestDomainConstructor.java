package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestDomainConstructor {
	public static Product product(String name, Integer price) {
		Product product = new Product();
		product.setName(name);
		if (price != null) {
			product.setPrice(BigDecimal.valueOf(price));
		}
		return product;
	}

	public static Product productWithId(String name, int price, Long id) {
		Product product = product(name, price);
		product.setId(id);
		return product;
	}

	public static MenuGroup menuGroup(String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);
		return menuGroup;
	}

	public static MenuGroup menuGroupWithId(String name, Long Id) {
		MenuGroup menuGroup = menuGroup(name);
		menuGroup.setId(Id);
		return menuGroup;
	}

	public static Menu menu(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
		Menu menu = new Menu();
		menu.setName(name);
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProducts);
		if (price != null) {
			menu.setPrice(BigDecimal.valueOf(price));
		}
		return menu;
	}

	public static Menu menuWithId(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts, Long Id) {
		Menu menu = menu(name, price, menuGroupId, menuProducts);
		menu.setId(Id);
		return menu;
	}

	public static MenuProduct menuProduct(Long menuId, Long productId, long quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setMenuId(menuId);
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(quantity);
		return menuProduct;
	}

	public static MenuProduct menuProductWithSeq(Long menuId, Long productId, long quantity, Long seq) {
		MenuProduct menuProduct = menuProduct(menuId, productId, quantity);
		menuProduct.setSeq(seq);
		return menuProduct;
	}

	public static OrderTable orderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.setTableGroupId(tableGroupId);
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(empty);
		return orderTable;
	}

	public static OrderTable orderTableWithId(Long tableGroupId, int numberOfGuests, boolean empty, Long id) {
		OrderTable orderTable = orderTable(tableGroupId, numberOfGuests, empty);
		orderTable.setId(id);
		return orderTable;
	}

	public static TableGroup tableGroup(List<OrderTable> orderTables, LocalDateTime createdDate) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		tableGroup.setCreatedDate(createdDate);
		return tableGroup;
	}

	public static TableGroup tableGroupWithId(List<OrderTable> orderTables, LocalDateTime createdDate, Long id) {
		TableGroup tableGroup = tableGroup(orderTables, createdDate);
		tableGroup.setId(id);
		return tableGroup;
	}

	public static Order order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setOrderTableId(orderTableId);
		order.setOrderStatus(orderStatus);
		order.setOrderedTime(orderedTime);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static Order orderWithId(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems, Long id) {
		Order order = order(orderTableId, orderStatus, orderedTime, orderLineItems);
		order.setId(id);
		return order;
	}

	public static OrderLineItem orderLineItem(Long orderId, Long menuId, long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);
		return orderLineItem;
	}

	public static OrderLineItem orderLineItemWithSeq(Long orderId, Long menuId, long quantity, Long seq) {
		OrderLineItem orderLineItem = orderLineItem(orderId, menuId, quantity);
		orderLineItem.setSeq(seq);
		return orderLineItem;
	}
}
