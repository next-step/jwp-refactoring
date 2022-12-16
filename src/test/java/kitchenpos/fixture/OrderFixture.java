package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order create(OrderTable orderTable) {
        return new Order(orderTable);
    }
}
