package kitchenpos.application.creator;

import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
public class OrderHelper {

    public static Order create(OrderTable orderTable, OrderLineItem ...orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }

}
