package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;

public class OrderResponse {
	private Long id;
	private OrderTable orderTable;
	private LocalDateTime orderedTime;
	private List<OrderLineItem> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, OrderTable orderTable,  LocalDateTime localDateTime, List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderedTime = localDateTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(), order.getOrderTable(), order.getOrderedTime(), order.getOrderLineItems());
	}

	public Long getId() {
		return id;
	}
}
