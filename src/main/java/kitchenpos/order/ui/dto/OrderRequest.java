package kitchenpos.order.ui.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;

public class OrderRequest {

	private Long orderTableId;

	private List<OrderLineItemRequest> orderLineItems;

	private OrderRequest() {
	}

	public OrderRequest(Long orderTableId, Map<Long, Integer> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems.entrySet()
											.stream()
											.map(OrderLineItemRequest::new)
											.collect(Collectors.toList());
	}

	public Order toOrder() {
		return new Order(orderTableId, getMenusWithQuantity());
	}

	private Map<Long, Integer> getMenusWithQuantity() {
		return orderLineItems.stream()
							 .collect(Collectors.toMap(
								 OrderLineItemRequest::getMenuId,
								 OrderLineItemRequest::getQuantity,
								 Integer::sum));
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public static class OrderLineItemRequest {
		private Long menuId;

		private Integer quantity;

		private OrderLineItemRequest() {
		}

		private OrderLineItemRequest(Long menuId, Integer quantity) {
			this.menuId = menuId;
			this.quantity = quantity;
		}

		public OrderLineItemRequest(Map.Entry<Long, Integer> menuQuantity) {
			this(menuQuantity.getKey(), menuQuantity.getValue());
		}

		public Long getMenuId() {
			return menuId;
		}

		public Integer getQuantity() {
			return quantity;
		}
	}
}
