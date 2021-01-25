package kitchenpos.order.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
	private Long orderId;
	private Long menuId;
	private Long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long orderId, Long menuId, Long quantity) {
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
		if(orderLineItem == null) {
			return null;
		}
		Long orderId = Optional.ofNullable(orderLineItem.getOrder())
			.map(Order::getId)
			.orElse(null);
		Long menuId = Optional.ofNullable(orderLineItem.getMenu())
			.map(Menu::getId)
			.orElse(null);
		return new OrderLineItemResponse(orderId, menuId, orderLineItem.getQuantity());
	}

	public static List<OrderLineItemResponse> newList(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::from)
			.collect(Collectors.toList());
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
