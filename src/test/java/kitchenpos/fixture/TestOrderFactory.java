package kitchenpos.fixture;

import jdk.vm.ci.meta.Local;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestOrderFactory {

    public static Order create(Long id) {
        return create(id, new OrderTable(), "COOKING", null, new ArrayList<>());
    }

    public static Order create(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }
}
