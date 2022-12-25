package kitchenpos.order.order.ui.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {

	private final long orderTableId;
	private final List<OrderLineItemRequest> orderLineItems;

	public OrderRequest(
		@JsonProperty("orderTableId") long orderTableId,
		@JsonProperty("orderLineItems") List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}
}
