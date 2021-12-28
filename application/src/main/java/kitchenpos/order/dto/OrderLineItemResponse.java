package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long seq;
	private Long menuId;
	private Long quantity;

	protected OrderLineItemResponse() {
	}

	private OrderLineItemResponse(Long seq, Long menuId, Long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(),
			orderLineItem.getQuantity());
	}

	public static OrderLineItemResponse of(Long seq, Long menuId, Long quantity) {
		return new OrderLineItemResponse(seq, menuId, quantity);
	}

	public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItemList) {
		return orderLineItemList.stream()
			.map(OrderLineItemResponse::from)
			.collect(Collectors.toList());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}

}
