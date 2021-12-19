package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.OrderStatus;

public class OrderDto {
	private Long id;
	private Long orderTableId;
	private OrderStatus orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemDto> orderLineItems;

	public OrderDto() {
	}

	public OrderDto(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemDto> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemDto> getOrderLineItems() {
		return orderLineItems;
	}
}
