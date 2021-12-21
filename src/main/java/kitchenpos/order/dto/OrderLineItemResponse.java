package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

	private Long seq;
	private MenuResponse menu;
	private Long quantity;

	protected OrderLineItemResponse() {
	}

	private OrderLineItemResponse(Long seq, MenuResponse menu, Long quantity) {
		this.seq = seq;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(Long seq, MenuResponse menu, Long quantity) {
		return new OrderLineItemResponse(seq, menu, quantity);
	}

	public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItemList) {
		return orderLineItemList.stream()
			.map(OrderLineItem::toResDto)
			.collect(Collectors.toList());
	}

	public Long getSeq() {
		return seq;
	}

	public MenuResponse getMenu() {
		return menu;
	}

	public Long getQuantity() {
		return quantity;
	}

}
