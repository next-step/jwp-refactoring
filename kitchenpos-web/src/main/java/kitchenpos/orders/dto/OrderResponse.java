package kitchenpos.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.orders.domain.Orders;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
public class OrderResponse {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private LocalDateTime orderedTime;
	private List<OrderLineItemResponse> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
		List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderedTime = orderedTime;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Orders order) {
		return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemResponse.of(order.getOrderLineItems()));
	}

	public Long getId() {
		return id;
	}

	public Long getOrderTableId() {
		return orderTableId;
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
