package kitchenpos.fixture;

import kitchenpos.domain.Quantity;
import java.util.Arrays;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemRequest;

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

    public static OrderLineItemRequest ofRequest(final Long menuId, final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }
}
