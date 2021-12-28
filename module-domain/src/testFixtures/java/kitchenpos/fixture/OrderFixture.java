package kitchenpos.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;

public class OrderFixture {

    public static Order 생성(Long orderTableId) {
        return new Order(orderTableId);
    }

    public static Order 생성(OrderTable orderTable) {
        return new Order(orderTable.getId());
    }

    public static Order 샘플1() {
        OrderTable 테이블1번 = OrderTableFixture.생성(0, true);
        return new Order(테이블1번.getId());
    }

    public static Order 샘플2() {
        OrderTable 테이블1번 = OrderTableFixture.생성(0, true);
        return new Order(테이블1번.getId());
    }

}
