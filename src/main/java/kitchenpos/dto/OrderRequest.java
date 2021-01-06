package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections4.CollectionUtils;

import kitchenpos.domain.OrderStatus;

public class OrderRequest {
	private Long id;
	private Long orderTableId;
	private OrderStatus orderStatus;

	@NotEmpty(message = "주문 메뉴 정보가 없습니다.")
	private List<OrderLineItemRequest> orderItems = new ArrayList<>();

	private OrderRequest() {
	}

	private OrderRequest(Long id, OrderStatus orderStatus) {
		this.id = id;
		this.orderStatus = orderStatus;
	}

	private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderItems) {
		this.orderTableId = orderTableId;
		this.orderItems = orderItems;
	}

	public static OrderRequest of(Long id, OrderStatus orderStatus) {
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<Long> getMenuIds() {
		return CollectionUtils.emptyIfNull(this.orderItems)
			.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public List<Long> getQuantities() {
		return CollectionUtils.emptyIfNull(this.orderItems)
			.stream()
			.map(OrderLineItemRequest::getQuantity)
			.collect(Collectors.toList());
	}
}
