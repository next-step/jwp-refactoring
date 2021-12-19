package kitchenpos.order;

import java.util.Collections;

import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixture {
	public static OrderRequest 주문(Long orderTableId, Long menuId, int quantity) {
		return new OrderRequest(orderTableId, Collections.singletonList(new OrderLineItemDto(menuId, quantity)));
	}

	public static OrderRequest 주문_항목_없는_주문(Long orderTableId) {
		return new OrderRequest(orderTableId, Collections.emptyList());
	}
}
