package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;

public class OrderResponse {
	private Long id;
	private OrderTable orderTable;
	private String orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItem> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, OrderTable orderTable, String orderStatus, LocalDateTime localDateTime, List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderStatus = orderStatus;
		this.orderTable = orderTable;
		this.orderedTime = localDateTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Order order) {
		return new OrderResponse(order.getId(), order.getOrderTable(), order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());
	}

	public Long getId() {
		return id;
	}

	public List<OrderLineItem> getOrderLineItems() {
		return orderLineItems;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
}
