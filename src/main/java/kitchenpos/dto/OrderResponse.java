package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.exception.NotFoundException;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTable;
	private String orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItems;

	private OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Order order) {
		if (order == null) {
			throw new NotFoundException("주문 정보를 찾을 수 없습니다.");
		}
		List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems()
			.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());
		return new OrderResponse(order.getId(), OrderTableResponse.of(order.getOrderTable()), order.getOrderStatus(), order.getOrderedTime(), orderLineItems);
	}

	public Long getId() {
		return id;
	}

	public OrderTableResponse getOrderTable() {
		return orderTable;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getOrderedTime() {
		return orderedTime;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}
}
