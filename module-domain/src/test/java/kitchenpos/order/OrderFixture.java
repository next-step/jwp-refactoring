package kitchenpos.order;

import static kitchenpos.order.OrderLineItemFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;

import java.util.Collections;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.ValidOrderValidator;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderFixture {
	public static Order 후라이드후라이드_메뉴_주문() {
		return Order.of(
			1L,
			비어있지않은_주문_테이블_1번().getId(),
			Collections.singletonList(OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목())),
			new ValidOrderValidator());
	}

	public static Order 후라이드후라이드_메뉴_주문(OrderTable orderTable) {
		return Order.of(
			2L,
			orderTable.getId(),
			Collections.singletonList(OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목())),
			new ValidOrderValidator());
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
