package kitchenpos.dto;

public class OrderItem {

	private Long menuId;
	private Long quantity;

	public OrderItem() {
	}

	private OrderItem(Long menuId, Long quantity) {
		this.menuId = menuId;
		this.quantity = quantity;
	}

	public static OrderItem of(Long menuId, Long quantity) {
		return new OrderItem(menuId, quantity);
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getQuantity() {
		return quantity;
	}
}
