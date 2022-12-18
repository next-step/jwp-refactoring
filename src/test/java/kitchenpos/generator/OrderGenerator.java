package kitchenpos.generator;

import static kitchenpos.generator.OrderLineItemGenerator.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderGenerator {
	private OrderGenerator() {
	}

	public static Order 주문(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		return new Order(null, orderTableId, orderStatus.name(), LocalDateTime.now(), orderLineItems);
	}

	public static Order 주문(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
		return new Order(id, orderTableId, orderStatus.name(), LocalDateTime.now(), orderLineItems);
	}


	public static Order 조리중_주문() {
		return 주문(1L, 1L, OrderStatus.COOKING, Collections.singletonList(주문_품목()));
	}

	public static Order 계산_완료_주문() {
		return 주문(1L, 1L, OrderStatus.COMPLETION, Collections.singletonList(주문_품목()));
	}
}
