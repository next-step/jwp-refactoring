package kitchenpos.common;

import java.math.BigDecimal;
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
		return TableGroup.of(id, orderTables);
	}

	public static Order order(Long id, OrderStatus status) {
		return order(id, null, status, null);
	}

	public static Order order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
		return order(null, orderTable, OrderStatus.COOKING, orderLineItems);
	}

	public static Order order(Long id, OrderTable orderTable, OrderStatus status,
		List<OrderLineItem> orderLineItems) {
		return Order.of(id, orderTable, status, orderLineItems);
	}

	public static OrderTable orderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
		return OrderTable.of(id, tableGroup, numberOfGuests, empty);
	}

	public static OrderLineItem orderLineItem(Menu menu, long quantity) {
		return orderLineItem(null, null, menu, quantity);
	}

	public static OrderLineItem orderLineItem(Long seq, Order order, Menu menu, long quantity) {
		return OrderLineItem.of(seq, order, menu, quantity);
	}

	public static Menu menu(Long id, String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return menu(id, name, BigDecimal.valueOf(price), menuGroup, menuProducts);
	}

	public static Menu menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
		return Menu.of(id, name, price, menuGroup, menuProducts);
	}

	public static MenuGroup menuGroup(Long id, String name) {
		return MenuGroup.of(id, name);
	}

	public static MenuProduct menuProduct(Long seq, Menu menu, Product product, long quantity) {
		return MenuProduct.of(seq, menu, product, quantity);
	}

	public static Product product(Long id, String name, long price) {
		return product(id, name, BigDecimal.valueOf(price));
	}

	public static Product product(Long id, String name, BigDecimal price) {
		return Product.of(id, name, price);
	}
}
