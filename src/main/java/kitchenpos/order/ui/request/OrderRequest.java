package kitchenpos.order.ui.request;

import java.util.List;

public class OrderRequest {

	private final long orderTableId;
	private final List<OrderLineItemRequest> orderLineItems;

	public OrderRequest(long orderTableId, List<OrderLineItemRequest> orderLineItems) {
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
