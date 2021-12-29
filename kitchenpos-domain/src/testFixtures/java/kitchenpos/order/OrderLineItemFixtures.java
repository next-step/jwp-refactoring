package kitchenpos.order;

import kitchenpos.order.domain.OrderLineItem;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderLineItemFixtures
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class OrderLineItemFixtures {
    public static OrderLineItem 주문정보() {
        return new OrderLineItem(1L, 1L);
    }
}
