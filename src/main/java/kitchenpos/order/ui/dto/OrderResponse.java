package kitchenpos.order.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderResponse {
	private Long id;
	private String orderStatus;
	private List<OrderLineItemResponse> orderLineItems;

	private OrderResponse() {
	}

	public OrderResponse(Long id, String orderStatus, List<OrderLineItemResponse> orderLineItems) {
		this.id = id;
		this.orderStatus = orderStatus;
		this.orderLineItems = orderLineItems;
	}

	public OrderResponse(Order order) {
		this(order.getId(),
			 order.getOrderStatus().name(),
			 OrderLineItemResponse.of(order.getOrderLineItems()));
	}

	public static List<OrderResponse> of(List<Order> orders) {
		return orders.stream().map(OrderResponse::new).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemResponse> getOrderLineItems() {
		return orderLineItems;
	}

	static class OrderLineItemResponse {
		private String menu;
		private Integer quantity;

		private OrderLineItemResponse() {
		}

		public OrderLineItemResponse(String menu, Integer quantity) {
			this.menu = menu;
			this.quantity = quantity;
		}

		public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
			return orderLineItems.stream()
				.map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getMenuName(),
																orderLineItem.getQuantity()))
				.collect(Collectors.toList());
		}

		public String getMenu() {
			return menu;
		}

		public Integer getQuantity() {
			return quantity;
		}
	}
}
