package kitchenpos.order.domain;

import java.util.List;

public class OrderFactory {
    public static Order create(long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderLineItems);
    }
}
