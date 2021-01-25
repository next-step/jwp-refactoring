package kitchenpos.orders.dto;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
public class OrderLineItemRequest {
	private Long menuId;
	private Long quantity;

	public OrderLineItemRequest() {
	}

	public OrderLineItemRequest(Long menuId, Long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
