package kitchenpos.order.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.Orders;

public class OrderResponse {

	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	public OrderResponse() {
	}

	public OrderResponse(Long id, Long orderTableId, String orderStatus,
		  List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderTableId = orderTableId;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public static OrderResponse of(Orders savedOrders) {
		return new OrderResponse(savedOrders.getId(), savedOrders.getOrderTable().getId(),
			  savedOrders.getOrderStatus(), null);
	}

	public static OrderResponse of(List<OrderLineItem> orderLineItems) {
		List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
			  .map(OrderLineItemResponse::of)
			  .collect(Collectors.toList());

		Orders savedOrders = orderLineItems.get(0).getOrder();
		return new OrderResponse(savedOrders.getId(), savedOrders.getOrderTable().getId(),
			  savedOrders.getOrderStatus(), orderLineItemResponses);
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

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderResponse that = (OrderResponse) o;
		return Objects.equals(id, that.id) && Objects
			  .equals(orderLineItems, that.orderLineItems);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderLineItems);
	}
}
