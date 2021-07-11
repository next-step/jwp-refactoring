package kitchenpos.dto;

import java.util.Objects;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long seq;
	private Long orderId;
	private Long menuId;
	private Long quantity;

	protected OrderLineItemResponse() {
	}

	public OrderLineItemResponse(Long seq, Long orderId, Long menuId, Long quantity) {
		this.seq = seq;
		this.orderId = orderId;
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(orderLineItem.getSeq(),
			orderLineItem.getOrderId(),
			orderLineItem.getOrderId(),
			orderLineItem.getQuantity());
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Long getSeq() {
		return seq;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderLineItemResponse that = (OrderLineItemResponse)o;
		return Objects.equals(seq, that.seq) && Objects.equals(orderId, that.orderId)
			&& Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq, orderId, menuId, quantity);
	}
}
