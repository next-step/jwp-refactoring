package kitchenpos.ui.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.EntityNotFoundException;

public class OrderRequest {

	private Long orderTableId;
	private List<OrderLineItemRequest> orderLineItems;

	private OrderRequest() {
	}

	public OrderRequest(Long orderTableId, Map<Long, Integer> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems.entrySet()
			.stream()
			.map(OrderLineItemRequest::new)
			.collect(Collectors.toList());
	}

	public Order toOrder(OrderTable orderTable, List<Menu> menus) {
		return new Order(orderTable, getMenusWithQuantity(menus));
	}

	private Map<Menu, Integer> getMenusWithQuantity(List<Menu> menus) {
		return orderLineItems.stream()
			.collect(Collectors.toMap(
				orderLineItem -> getMenu(menus, orderLineItem.getMenuId()),
				OrderLineItemRequest::getQuantity, Integer::sum));
	}

	private Menu getMenu(List<Menu> menus, Long menuId) {
		return menus.stream().filter(menu -> menu.getId().equals(menuId))
			.findAny()
			.orElseThrow(EntityNotFoundException::new);
	}

	public List<Long> menuIdList() {
		return orderLineItems.stream()
			.map(OrderRequest.OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public static class OrderLineItemRequest {
		private Long menuId;
		private Integer quantity;

		private OrderLineItemRequest() {
		}

		private OrderLineItemRequest(Long menuId, Integer quantity) {
			this.menuId = menuId;
			this.quantity = quantity;
		}

		public OrderLineItemRequest(Map.Entry<Long, Integer> menuQuantity) {
			this(menuQuantity.getKey(), menuQuantity.getValue());
		}

		public Long getMenuId() {
			return menuId;
		}

		public Integer getQuantity() {
			return quantity;
		}
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}
}
