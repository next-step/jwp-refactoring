package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderItems = new ArrayList<>();

	private OrderRequest() {
	}

	private OrderRequest(Long id, String orderStatus) {
		this.id = id;
		this.orderStatus = orderStatus;
	}

	private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderItems) {
		this.orderTableId = orderTableId;
		this.orderItems = orderItems;
	}

	public static OrderRequest of(Long id, String orderStatus) {
		return new OrderRequest(id, orderStatus);
	}

	public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderItems) {
		return new OrderRequest(orderTableId, orderItems);
	}

	public Long getId() {
		return id;
	}

	public List<OrderLineItemRequest> getOrderItems() {
		return orderItems;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public List<Long> getMenuIds() {
		return this.orderItems
			.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public List<Long> getQuantities() {
		return this.orderItems
			.stream()
			.map(OrderLineItemRequest::getQuantity)
			.collect(Collectors.toList());
	}
}
