package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderItems = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderLineItemRequest> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderLineItemRequest> orderItems) {
		this.orderItems = orderItems;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
