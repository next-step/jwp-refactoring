package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class MockFixture {

	public static Product productForCreate(String name, long price) {
		Product product = mock(Product.class);
		given(product.getName()).willReturn(name);
		given(product.getPrice()).willReturn(new BigDecimal(price));
		return product;
	}

	public static Product product(Long id, String name, long price) {
		Product product = mock(Product.class);
		given(product.getId()).willReturn(id);
		given(product.getName()).willReturn(name);
		given(product.getPrice()).willReturn(new BigDecimal(price));
		return product;
	}

	public static List<Product> anyProducts(int size) {
		List<Product> products = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			products.add(product((long) i, "제품", 1000));
		}
		return products;
	}

	public static List<MenuGroup> anyMenuGroups(int size) {
		List<MenuGroup> menuGroups = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			menuGroups.add(anyMenuGroup());
		}
		return menuGroups;
	}

	public static MenuGroup anyMenuGroup() {
		MenuGroup menuGroup = mock(MenuGroup.class);
		return menuGroup;
	}

	public static void findByIdWillReturn(ProductDao productDao, MenuProduct menuProduct, Product mockProduct) {
		findByIdWillReturn(productDao, menuProduct.getProductId(), mockProduct);
	}

	public static void findByIdWillReturn(ProductDao productDao, Long id, Product mockProduct) {
		given(productDao.findById(id)).willReturn(Optional.of(mockProduct));
	}

	public static MenuProduct menuProduct(Long menuId, Long productId, Long seq, Long quantity) {
		MenuProduct menuProduct = mock(MenuProduct.class);
		given(menuProduct.getMenuId()).willReturn(menuId);
		given(menuProduct.getProductId()).willReturn(productId);
		given(menuProduct.getSeq()).willReturn(seq);
		given(menuProduct.getQuantity()).willReturn(quantity);
		return menuProduct;
	}

	public static List<Menu> anyMenus(int size) {
		List<Menu> menus = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			menus.add(menu(i + 1L, "", 1000L, null));
		}
		return menus;
	}

	public static Menu menuForCreate(String name, Long price, Long menuGroupId, MenuProduct... menuProducts) {
		return menu(null, name, price, menuGroupId, Arrays.asList(menuProducts));
	}

	public static Menu menu(Long id, String name, Long price, Long menuGroupId, MenuProduct... menuProducts) {
		return menu(id, name, price, menuGroupId, Arrays.asList(menuProducts));
	}

	public static Menu menu(Long id, String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
		Menu menu = mock(Menu.class);
		given(menu.getId()).willReturn(id);
		given(menu.getName()).willReturn(name);
		if (price != null) {
			given(menu.getPrice()).willReturn(new BigDecimal(price));
		}
		given(menu.getMenuGroupId()).willReturn(menuGroupId);
		given(menu.getMenuProducts()).willReturn(menuProducts);
		return menu;
	}

	public static OrderTable orderTableForCreate() {
		OrderTable orderTable = mock(OrderTable.class);
		return orderTable;
	}

	public static List<OrderTable> anyOrderTables(int size) {
		List<OrderTable> orderTables = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			orderTables.add(orderTable(null, null, false, 0));
		}
		return orderTables;
	}

	public static OrderTable orderTable(Long id, Long tableGroupId, Boolean empty, int numberOfGuests) {
		OrderTable orderTable = mock(OrderTable.class);
		given(orderTable.getId()).willReturn(id);
		given(orderTable.getTableGroupId()).willReturn(tableGroupId);
		given(orderTable.isEmpty()).willReturn(empty);
		given(orderTable.getNumberOfGuests()).willReturn(numberOfGuests);
		return orderTable;
	}

	public static TableGroup tableGroupForCreate(List<OrderTable> orderTables) {
		TableGroup tableGroup = mock(TableGroup.class);
		given(tableGroup.getOrderTables()).willReturn(orderTables);
		return tableGroup;
	}

	public static OrderLineItem orderLineItemsForCreate(long seq, long menuId, long quantity) {
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		given(orderLineItem.getSeq()).willReturn(seq);
		given(orderLineItem.getMenuId()).willReturn(menuId);
		given(orderLineItem.getQuantity()).willReturn(quantity);
		return orderLineItem;
	}

	public static Order orderForCreate(Long orderTableId, List<OrderLineItem> orderLineItem) {
		Order order = mock(Order.class);
		given(order.getOrderTableId()).willReturn(orderTableId);
		given(order.getOrderLineItems()).willReturn(orderLineItem);
		return order;
	}

	public static void mockSave(OrderDao orderDao, Order order, long id) {
		doAnswer(invocation -> {
			Order savedOrder = invocation.getArgument(0, Order.class);
			given(savedOrder.getId()).willReturn(id);
			return savedOrder;
		}).when(orderDao).save(order);
	}
}
