package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class OrderFactory {
    public static Order create(long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }
}
