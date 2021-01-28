package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.Orders;

public class OrderResponse {
	private final Long id;
	private final Long orderTableId;
	private final String orderStatus;
	private final LocalDateTime orderedTime;
	private final List<OrderLineItemResponse> orderLineItems;

	public OrderResponse(final Long id, final Long orderTableId, final String orderStatus,
		final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(final Long id, final Long orderTableId, final String orderStatus,
		final LocalDateTime orderedTime, final List<OrderLineItemResponse> orderLineItems) {
		return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public static OrderResponse of(final List<OrderLineItem> orderLineItems) {
		Orders order = orderLineItems.get(0).getOrder();
		List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());

		return of(order.getId(), order.getOrderTable().getId(), order.orderStatus(), order.getCreatedDate(),
			orderLineItemResponses);
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}

