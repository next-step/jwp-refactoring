package kitchenpos.order.dto;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long id;
	private Long orderId;
	private MenuResponse menu;
	private long quantity;

	protected OrderLineItemResponse() {
	}

	private OrderLineItemResponse(Long id, Long orderId, MenuResponse menu, long quantity) {
		this.id = id;
		this.orderId = orderId;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(
			orderLineItem.getId(),
			orderLineItem.getOrder().getId(),
			MenuResponse.of(orderLineItem.getMenu()),
			orderLineItem.getQuantity()
		);
	}

	public Long getId() {
		return id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public MenuResponse getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}
}
