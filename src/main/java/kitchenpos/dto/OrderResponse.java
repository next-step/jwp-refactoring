package kitchenpos.dto;

import java.time.LocalDateTime;

import kitchenpos.domain.Order;
import kitchenpos.exception.NotFoundException;

public class OrderResponse {

	private Long id;
	private OrderTableResponse orderTable;
	private String orderStatus;
	private LocalDateTime orderedTime;

	private OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, LocalDateTime orderedTime) {
		this.id = id;
		this.orderTable = orderTable;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
	}

	public static OrderResponse of(Order order) {
		if (order == null) {
			throw new NotFoundException("주문 정보를 찾을 수 없습니다.");
		}
		return new OrderResponse(order.getId(), OrderTableResponse.of(order.getOrderTable()), order.getOrderStatus(), order.getOrderedTime());
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
}
