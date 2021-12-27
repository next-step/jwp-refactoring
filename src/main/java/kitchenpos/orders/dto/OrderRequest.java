package kitchenpos.orders.dto;

import java.util.List;

import org.springframework.util.CollectionUtils;

import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderLineItems;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderRequest {
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItemRequests;

	public OrderRequest() {
	}

	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderRequest(long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
		this.orderTableId = orderTableId;
		this.orderLineItemRequests = orderLineItemRequests;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItemRequests() {
		return orderLineItemRequests;
	}

	public Order toOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
		return new Order(orderTable, OrderStatus.COOKING, orderLineItems);
	}
}

