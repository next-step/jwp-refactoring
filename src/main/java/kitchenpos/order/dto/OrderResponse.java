package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {
	private Long id;
	@JsonIgnore
	private OrderTableResponse orderTable;
	private String orderStatus;
	private LocalDateTime orderedTime;
	@JsonIgnore
	private List<OrderLineItem> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, OrderTable orderTable, String orderStatus, LocalDateTime localDateTime, List<OrderLineItem> orderLineItems) {
		this.id = id;
		this.orderStatus = orderStatus;
		this.orderTable = OrderTableResponse.of(orderTable);
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
