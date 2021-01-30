package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	protected OrderRequest() {
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<Long> getMenuIds() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}
}
