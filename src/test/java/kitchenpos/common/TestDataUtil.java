package kitchenpos.common;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.TableGroupRequest;

public class TestDataUtil {

	public static ProductRequest createProduct(String name, Integer price) {
		ProductRequest product = new ProductRequest();
		product.setName(name);
		if (price != null) {
			product.setPrice(BigDecimal.valueOf(price));
		}
		return product;
	}

	public static ProductRequest createProductById(long productId) {
		ProductRequest product = new ProductRequest();
		product.setId(productId);
		return product;
	}

	public static OrderTableRequest createOrderTable() {
		OrderTableRequest orderTable = new OrderTableRequest();
		orderTable.setEmpty(true);
		return orderTable;
	}

	public static OrderTableRequest createOrderTableById(long id) {
		OrderTableRequest orderTable = new OrderTableRequest();
		orderTable.setId(id);
		orderTable.setEmpty(true);
		return orderTable;
	}

	public static MenuGroupRequest createMenuGroup(String name) {
		MenuGroupRequest menuGroup = new MenuGroupRequest();
		menuGroup.setName(name);
		return menuGroup;
	}

	public static MenuRequest createMenu(String name, Integer price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
		MenuRequest menu = new MenuRequest();
		menu.setName(name);
		if (price != null) {
			menu.setPrice(BigDecimal.valueOf(price));
		}
		menu.setMenuGroupId(menuGroupId);
		menu.setMenuProducts(menuProducts);
		return menu;
	}

	public static TableGroupRequest createTableGroup(List<Long> orderTableIds) {
		TableGroupRequest tableGroup = new TableGroupRequest();
		tableGroup.setOrderTableIds(orderTableIds);
		return tableGroup;
	}

	public static OrderRequest createOrder(Long tableId, List<Long> menuIds) {
		OrderRequest order = new OrderRequest();
		order.setOrderTableId(tableId);
		order.setMenuIds(menuIds);
		return order;
	}

	public static OrderRequest createOrderByIdAndStatus(long id, OrderStatus orderStatus) {
		OrderRequest order = new OrderRequest();
		order.setId(id);
		order.setOrderStatus(orderStatus.name());
		return order;
	}

}
