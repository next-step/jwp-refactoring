package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long id;
	private MenuResponse menu;
	private long quantity;

	protected OrderLineItemResponse() {
	}

	private OrderLineItemResponse(Long id, MenuResponse menu, long quantity) {
		this.id = id;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(
			orderLineItem.getId(),
			MenuResponse.of(orderLineItem.getMenu()),
			orderLineItem.getQuantity()
		);
	}

	public Long getId() {
		return id;
	}

	public MenuResponse getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}
}
