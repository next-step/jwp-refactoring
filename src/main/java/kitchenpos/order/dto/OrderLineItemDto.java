package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
	private Long seq;
	private Long menuId;
	private long quantity;

	public OrderLineItemDto() {
	}

	public OrderLineItemDto(Long menuId, long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantity() {
		return quantity;
	}

	public static OrderLineItemDto from(OrderLineItem orderLineItem) {
		OrderLineItemDto dto = new OrderLineItemDto();
		dto.seq = orderLineItem.getSeq();
		dto.menuId = orderLineItem.getMenu().getId();
		dto.quantity = orderLineItem.getQuantity().getValue();
		return dto;
	}
}
