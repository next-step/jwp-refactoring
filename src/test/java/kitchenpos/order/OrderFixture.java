package kitchenpos.order;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;

import java.time.LocalDateTime;
import java.util.Collections;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixture {
	public static Order 후라이드후라이드_주문() {
		Order order = new Order();
		order.setId(1L);
		order.setOrderTableId(비어있지않은_주문_테이블().getId());
		order.setOrderStatus(OrderStatus.COOKING.name());
		order.setOrderedTime(LocalDateTime.now());
		order.setOrderLineItems(Collections.singletonList(후라이드후라이드_주문_항목()));
		return order;
	}

	public static Order 후라이드후라이드_주문_상태_변경됨(OrderStatus orderStatus) {
		Order order = new Order();
		order.setId(후라이드후라이드_주문().getId());
		order.setOrderTableId(후라이드후라이드_주문().getOrderTableId());
		order.setOrderStatus(orderStatus.name());
		order.setOrderedTime(후라이드후라이드_주문().getOrderedTime());
		order.setOrderLineItems(후라이드후라이드_주문().getOrderLineItems());
		return order;
	}

	public static OrderLineItem 후라이드후라이드_주문_항목() {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setSeq(1L);
		orderLineItem.setOrderId(1L);
		orderLineItem.setMenuId(후라이드후라이드_메뉴().getId());
		orderLineItem.setQuantity(1);
		return orderLineItem;
	}

	public static OrderRequest 주문_요청(Long orderTableId, Long menuId, int quantity) {
		return new OrderRequest(orderTableId, Collections.singletonList(new OrderLineItemDto(menuId, quantity)));
	}

	public static OrderRequest 주문_항목_없는_주문_요청(Long orderTableId) {
		return new OrderRequest(orderTableId, Collections.emptyList());
	}

	public static OrderRequest 주문_상태_변경_요청(OrderStatus orderStatus) {
		return new OrderRequest(orderStatus);
	}
}
