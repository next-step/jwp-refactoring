package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.exception.NotFoundException;

public class OrderLineItemResponse {

	private Long seq;

	private MenuResponse menu;

	private long quantity;

	private OrderLineItemResponse(Long seq, MenuResponse menu, long quantity) {
		this.seq = seq;
		this.menu = menu;
		this.quantity = quantity;
	}

	public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
		if (orderLineItem == null) {
			throw new NotFoundException("주문 아이템 정보를 찾을 수 없습니다.");
		}
		return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.of(orderLineItem.getMenu()), orderLineItem.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public MenuResponse getMenu() {
		return menu;
	}

	public long getQuantity() {
		return quantity;
	}
}
