package kitchenpos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections4.CollectionUtils;

import kitchenpos.domain.OrderStatus;

public class OrderRequest {

	private Long orderTableId;
	private OrderStatus orderStatus;

	@NotEmpty(message = "주문 메뉴 정보가 없습니다.")
	private List<OrderItem> orderItems = new ArrayList<>();

	private OrderRequest() {
	}

	private OrderRequest(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	private OrderRequest(Long orderTableId, List<OrderItem> orderItems) {
		this.orderTableId = orderTableId;
		this.orderItems = orderItems;
	}

	public static OrderRequest of(OrderStatus orderStatus) {
		return new OrderRequest(orderStatus);
	}

	public static OrderRequest of(Long orderTableId, List<OrderItem> orderItems) {
		return new OrderRequest(orderTableId, orderItems);
	}

	public List<OrderItem> getOrderItems() {
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
			.map(OrderItem::getMenuId)
			.collect(Collectors.toList());
	}

	public List<Long> getQuantities() {
		return CollectionUtils.emptyIfNull(this.orderItems)
			.stream()
			.map(OrderItem::getQuantity)
			.collect(Collectors.toList());
	}
}
