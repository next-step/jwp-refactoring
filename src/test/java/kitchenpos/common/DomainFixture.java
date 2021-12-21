package kitchenpos.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class DomainFixture {

	public static TableGroup tableGroup(Long id, List<OrderTable> orderTables) {
		final TableGroup tableGroup = new TableGroup();
		tableGroup.setId(id);
		tableGroup.setCreatedDate(LocalDateTime.now());
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}

	public static Order order(Long id, OrderStatus status) {
		return order(id, null, status, null, null);
	}

	public static Order order(Long orderTableId, List<OrderLineItem> orderLineItems) {
		return order(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
	}

	public static Order order(Long id, Long orderTableId, OrderStatus status, LocalDateTime orderedAt,
		List<OrderLineItem> orderLineItems) {
		final Order order = new Order();
		order.setId(id);
		order.setOrderTableId(orderTableId);
		order.setOrderStatus(status.name());
		order.setOrderedTime(orderedAt);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static OrderTable orderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		final OrderTable orderTable = new OrderTable();
		orderTable.setId(id);
		orderTable.setTableGroupId(tableGroupId);
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(empty);
		return orderTable;
	}

	public static OrderLineItem orderLineItem(Long menuId, long quantity) {
		return orderLineItem(null, null, menuId, quantity);
	}

	public static OrderLineItem orderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
		final OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setSeq(seq);
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);
		return orderLineItem;
	}

	public static Menu menu(Long id, String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return menu(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
	}

	public static Menu menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		final Menu menu = new Menu();
		menu.setId(id);
		menu.setName(name);
		menu.setPrice(price);
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProducts);
		return menu;
	}

	public static MenuGroup menuGroup(Long id, String name) {
		final MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(id);
		menuGroup.setName(name);
		return menuGroup;
	}

	public static MenuProduct menuProduct(Long seq, Long productId, long quantity) {
		final MenuProduct menuProduct = new MenuProduct();
		menuProduct.setSeq(seq);
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(quantity);
		return menuProduct;
	}

	public static Product product(Long id, String name, long price) {
		return product(id, name, BigDecimal.valueOf(price));
	}

	public static Product product(Long id, String name, BigDecimal price) {
		final Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		return product;
	}
}
