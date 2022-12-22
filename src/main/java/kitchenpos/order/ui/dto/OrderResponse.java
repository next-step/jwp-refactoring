package kitchenpos.order.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;

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
			 order.getOrderStatus()
				  .name(),
			 OrderLineItemResponse.of(order.getOrderLineItems()));
	}

	public static List<OrderResponse> of(List<Order> orders) {
		return orders.stream()
					 .map(OrderResponse::new)
					 .collect(Collectors.toList());
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
		private Long menuId;
		private Integer quantity;

		private OrderLineItemResponse() {
		}

		public OrderLineItemResponse(Long menuId, Integer quantity) {
			this.menuId = menuId;
			this.quantity = quantity;
		}

		public static List<OrderLineItemResponse> of(OrderLineItems orderLineItems) {
			return orderLineItems.stream()
								 .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getMenuId(),
																				 orderLineItem.getQuantity()))
								 .collect(Collectors.toList());
		}

		public Long getMenuId() {
			return menuId;
		}

		public Integer getQuantity() {
			return quantity;
		}
	}
}
