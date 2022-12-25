package kitchenpos.generator;

import java.math.BigDecimal;

import kitchenpos.order.order.domain.OrderLineItemMenu;
import kitchenpos.order.order.domain.Price;

public class OrderLineItemMenuGenerator {

	private OrderLineItemMenuGenerator() {
	}

	public static OrderLineItemMenu 주문_품목_메뉴() {
		return OrderLineItemMenu.of(1L, "주문_품목", Price.from(BigDecimal.ONE));
	}

	public static OrderLineItemMenu 후라이드_세트() {
		return OrderLineItemMenu.of(1L, "후라이드_메뉴", Price.from(BigDecimal.ONE));
	}

}
