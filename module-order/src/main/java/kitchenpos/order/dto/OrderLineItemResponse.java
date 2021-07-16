package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.menu.dto.MenuResponse;

public class OrderLineItemResponse {

	private Long seq;
	private MenuResponse menuResponse;
	private long quantity;

	public OrderLineItemResponse(Long seq, MenuResponse menuResponse, long quantity) {
		this.seq = seq;
		this.menuResponse = menuResponse;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public MenuResponse getMenuResponse() {
		return menuResponse;
	}

	public long getQuantity() {
		return quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.of(orderLineItem.getMenu()),
			orderLineItem.getQuantity().value());
	}

	public static List<OrderLineItemResponse> of(OrderLineItems orderLineItems) {
		return orderLineItems.stream()
			.map(OrderLineItemResponse::of)
			.collect(Collectors.toList());
	}
}
