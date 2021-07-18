package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
	private Long id;
	private Long orderId;
	private Long menuId;
	private Long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long id, Long orderId, Long menuId, Long quantity) {
		this.id = id;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(
				orderLineItem.getId(),
				orderLineItem.getOrderId(),
				orderLineItem.getMenuId(),
				orderLineItem.getQuantity());
	}

	public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream().map(OrderLineItemResponse::of)
				.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
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
