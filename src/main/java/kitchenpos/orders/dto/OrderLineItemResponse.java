package kitchenpos.orders.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.orders.domain.OrderLineItem;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
public class OrderLineItemResponse {
	private Long seq;
	private Long orderId;
	private Long menuId;
	private long quantity;

	public OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem){
		return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}
}
