package kitchenpos.tobe.fixture;

import java.util.Arrays;
import kitchenpos.tobe.common.domain.Quantity;
import kitchenpos.tobe.orders.domain.order.OrderLineItem;
import kitchenpos.tobe.orders.domain.order.OrderLineItems;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {
    }

    public static OrderLineItems ofList(final OrderLineItem... orderLineItems) {
        return new OrderLineItems(Arrays.asList(orderLineItems));
    }

    public static OrderLineItem of(final Long seq, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, menuId, new Quantity(quantity));
    }

    public static OrderLineItem of(final Long menuId, final long quantity) {
        return of(null, menuId, quantity);
    }
}
