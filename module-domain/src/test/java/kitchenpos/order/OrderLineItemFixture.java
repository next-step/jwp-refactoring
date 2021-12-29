package kitchenpos.order;

import static kitchenpos.menu.MenuFixture.*;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {
	public static OrderLineItem 후라이드후라이드_메뉴_주문_항목() {
		return OrderLineItem.of(1L, 후라이드후라이드_메뉴().getId(), Quantity.from(1L));
	}
}
