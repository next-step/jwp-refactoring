package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestDomainConstructor {
	public static Product product(String name, BigDecimal price) {
		return new Product(name, price);
	}

	public static Product productWithId(String name, BigDecimal price, Long id) {
		return new Product(id, name, price);
	}

	public static MenuGroup menuGroup(String name) {
		return new MenuGroup(name);
	}

	public static MenuGroup menuGroupWithId(String name, Long id) {
		return new MenuGroup(id, name);
	}

	public static Menu menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
		return new Menu(name, price, menuGroupId, menuProducts);
	}

	public static Menu menuWithId(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts, Long id) {
		return new Menu(id, name, price, menuGroupId, menuProducts);
	}

	public static MenuProduct menuProduct(Long menuId, Long productId, long quantity) {
		return new MenuProduct(menuId, productId, quantity);
	}

	public static MenuProduct menuProductWithSeq(Long menuId, Long productId, long quantity, Long seq) {
		return new MenuProduct(seq, menuId, productId, quantity);
	}

	public static OrderTable orderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
		return new OrderTable(tableGroupId, numberOfGuests, empty);
	}

	public static OrderTable orderTableWithId(Long tableGroupId, int numberOfGuests, boolean empty, Long id) {
		return new OrderTable(id, tableGroupId, numberOfGuests, empty);
	}

	public static TableGroup tableGroup(List<OrderTable> orderTables, LocalDateTime createdDate) {
		return new TableGroup(createdDate, orderTables);
	}

	public static TableGroup tableGroupWithId(List<OrderTable> orderTables, LocalDateTime createdDate, Long id) {
		return new TableGroup(id, createdDate, orderTables);
	}

	public static Order order(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
		return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static Order orderWithId(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems, Long id) {
		return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static OrderLineItem orderLineItem(Long orderId, Long menuId, long quantity) {
		return new OrderLineItem(orderId, menuId, quantity);
	}

	public static OrderLineItem orderLineItemWithSeq(Long orderId, Long menuId, long quantity, Long seq) {
		return new OrderLineItem(seq, orderId, menuId, quantity);
	}
}
