package kitchenpos.order.dto;

public class OrderLineItemDto {
	private Long seq;
	private Long orderId;
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
