package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;

public class OrderRequest {

	private Long orderTableId;
	private List<OrderLineItemRequest> orderLineItemRequests;

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
		this.orderTableId = orderTableId;
		this.orderLineItemRequests = orderLineItemRequests;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItemRequests() {
		return orderLineItemRequests;
	}

	public Order toOrder(OrderTable orderTable) {
		return new Order(orderTable);
	}
}
