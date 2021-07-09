package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
	private long orderTableId;
	private OrderStatus orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	public long getOrderTableId() {
		return orderTableId;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public List<Long> getMenuIds() {
		return orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public int getOrderLineItemSize() {
		return orderLineItems.size();
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public OrderLineItems toOrderLineItems(List<Menu> menus) {
		List<OrderLineItem> orderLineItems = menus.stream()
			.map(this::toOrderLineItem)
			.collect(Collectors.toList());
		return OrderLineItems.of(orderLineItems);
	}

	private OrderLineItem toOrderLineItem(Menu menu) {
		OrderLineItemRequest orderLineItemRequest = orderLineItems.stream()
			.filter(orderLineItem -> orderLineItem.isEqualMenuId(menu.getId()))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("주문 메뉴를 찾을 수 없습니다."));
		return orderLineItemRequest.toEntity(menu);
	}
}
