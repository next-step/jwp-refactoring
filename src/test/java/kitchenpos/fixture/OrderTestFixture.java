package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;

public class OrderTestFixture {
    public static Order 생성(OrderTable orderTable){
        return new Order(orderTable);
    }
}
