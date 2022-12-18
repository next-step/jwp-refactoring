package kitchenpos.order.ui.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

	private final long id;
	private final long orderTableId;
	private final String orderStatus;
	private final LocalDateTime orderedTime;
	private final List<OrderLineItemResponse> orderLineItems;

	private OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
	}

	public long getId() {
		return id;
	}

	public long getOrderTableId() {
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
