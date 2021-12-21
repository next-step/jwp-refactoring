package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

public class OrderRequest {

	private Long orderTableId;
	private List<OrderLineItemRequest> orderLineItems;

	protected OrderRequest() {
	}

	private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		return new OrderRequest(orderTableId, orderLineItems);
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public Order toEntity(OrderTable orderTable, OrderStatus orderStatus) {
		return Order.of(orderTable, orderStatus);
	}
}
