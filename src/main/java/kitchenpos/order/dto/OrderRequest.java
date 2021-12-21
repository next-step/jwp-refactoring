package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.OrderStatus;

public class OrderRequest {
	private Long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemDto> orderLineItems;

	public OrderRequest() {
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public OrderRequest(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public void setOrderedTime(LocalDateTime orderedTime) {
		this.orderedTime = orderedTime;
	}

	public List<OrderLineItemDto> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(List<OrderLineItemDto> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}
}
