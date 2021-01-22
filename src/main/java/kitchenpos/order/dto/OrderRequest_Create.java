package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest_Create {
	private List<OrderLineItemRequest> orderLineItems;
	private long orderTableId;


	public OrderRequest_Create() {
	}

	public OrderRequest_Create(List<OrderLineItemRequest> orderLineItems, long orderTableId) {
		this.orderLineItems = orderLineItems;
		this.orderTableId = orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public long getOrderTableId() {
		return orderTableId;
	}
}
