package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class TestFixtures {

	public static MenuGroup 메뉴_그룹1 = newMenuGroup(1L, "두마리메뉴");
	public static MenuGroup 메뉴_그룹2 = newMenuGroup(2L, "한마리메뉴");
	public static MenuGroup 메뉴_그룹3 = newMenuGroup(3L, "순살파닭두마리메뉴");
	public static MenuGroup 메뉴_그룹4 = newMenuGroup(4L, "신메뉴");
	public static Product 상품1 = newProduct(1L, 16000L, "후라이드");
	public static Product 상품2 = newProduct(2L, 16000L, "양념치킨");
	public static Product 상품3 = newProduct(3L, 16000L, "반반치킨");
	public static Product 상품4 = newProduct(4L, 16000L, "통구이");
	public static Product 상품5 = newProduct(5L, 17000L, "간장치킨");
	public static Product 상품6 = newProduct(6L, 17000L, "순살치킨");
	public static Menu 메뉴1 = newMenu(1L, "후라이드치킨", 16000L, 2L, null);
	public static Menu 메뉴2 = newMenu(2L, "양념치킨", 16000L, 2L, null);
	public static Menu 메뉴3 = newMenu(3L, "반반치킨", 16000L, 2L, null);
	public static Menu 메뉴4 = newMenu(4L, "통구이", 16000L, 2L, null);
	public static Menu 메뉴5 = newMenu(5L, "간장치킨", 17000L, 2L, null);
	public static Menu 메뉴6 = newMenu(6L, "순살치킨", 17000L, 2L, null);
	public static MenuProduct 메뉴_상품1 = newMenuProduct(1L, 1L, 1L, 1L);
	public static MenuProduct 메뉴_상품2 = newMenuProduct(2L, 2L, 2L, 1L);
	public static MenuProduct 메뉴_상품3 = newMenuProduct(3L, 3L, 3L, 1L);
	public static MenuProduct 메뉴_상품4 = newMenuProduct(4L, 4L, 4L, 1L);
	public static MenuProduct 메뉴_상품5 = newMenuProduct(5L, 5L, 5L, 1L);
	public static MenuProduct 메뉴_상품6 = newMenuProduct(6L, 6L, 6L, 1L);
	public static OrderTable 주문_테이블1 = newOrderTable(1L, null, 0, true);
	public static OrderTable 주문_테이블2 = newOrderTable(2L, null, 0, true);
	public static OrderTable 주문_테이블3 = newOrderTable(3L, null, 0, true);
	public static OrderTable 주문_테이블4 = newOrderTable(4L, null, 0, true);
	public static OrderTable 주문_테이블5 = newOrderTable(5L, null, 0, true);
	public static OrderTable 주문_테이블6 = newOrderTable(6L, null, 0, true);
	public static OrderTable 주문_테이블7 = newOrderTable(7L, null, 0, true);
	public static OrderTable 주문_테이블8 = newOrderTable(8L, null, 0, true);

	public static Menu newMenu(Long id, String name, Long price, Long menuGroupId,
		List<MenuProduct> menuProducts) {
		Menu menu = new Menu();
		menu.setId(id);
		menu.setName(name);
		menu.setPrice(BigDecimal.valueOf(price));
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProducts);
		return menu;
	}

	public static MenuGroup newMenuGroup(Long id, String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(id);
		menuGroup.setName(name);
		return menuGroup;
	}

	public static MenuProduct newMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setSeq(seq);
		menuProduct.setMenuId(menuId);
		menuProduct.setProductId(productId);
		menuProduct.setQuantity(quantity);
		return menuProduct;
	}

	public static Order newOrder(String orderStatus) {
		Order order = new Order();
		order.setOrderStatus(orderStatus);
		return order;
	}

	public static Order newOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setOrderTableId(orderTableId);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static Order newOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItem> orderLineItems) {
		Order order = new Order();
		order.setId(id);
		order.setOrderTableId(orderTableId);
		order.setOrderStatus(orderStatus);
		order.setOrderedTime(orderedTime);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static OrderLineItem newOrderLineItem(Long menuId, long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);
		return orderLineItem;
	}

	public static OrderLineItem newOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setSeq(seq);
		orderLineItem.setOrderId(orderId);
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(quantity);
		return orderLineItem;
	}

	public static OrderTable newOrderTable(boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.setEmpty(empty);
		return orderTable;
	}

	public static OrderTable newOrderTable(int numberOfGuests) {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(numberOfGuests);
		return orderTable;
	}

	public static OrderTable newOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(id);
		orderTable.setTableGroupId(tableGroupId);
		orderTable.setNumberOfGuests(numberOfGuests);
		orderTable.setEmpty(empty);
		return orderTable;
	}

	public static Product newProduct(Long id, Long price, String name) {
		Product product = new Product();
		product.setId(id);
		product.setPrice(BigDecimal.valueOf(price));
		product.setName(name);
		return product;
	}

	public static TableGroup newTableGroup(List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}

	public static TableGroup newTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setId(id);
		tableGroup.setCreatedDate(createdDate);
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}
}
