package kitchenpos.order.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import static kitchenpos.menu.fixture.MenuFixture.기본_메뉴;
import static kitchenpos.order.fixture.OrderFixture.요리중_상태_주문;
import static kitchenpos.order.fixture.OrderFixture.주문완료_상태_주문;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목 = create(1L, 요리중_상태_주문, 기본_메뉴, Quantity.of(2L));

    public static OrderLineItem 완료_주문_항목 = create(1L, 주문완료_상태_주문, 기본_메뉴, Quantity.of(2L));

    public static OrderLineItem create(Long seq, Order order, Menu menu, Quantity quantity) {
        return OrderLineItem.of(seq, order, menu, quantity);
    }
}
