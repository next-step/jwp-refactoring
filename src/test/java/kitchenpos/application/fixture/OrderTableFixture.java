package kitchenpos.application.fixture;

import kitchenpos.domain.order.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable 한명_주문테이블() {
        return OrderTable.of(1, false);
    }

    public static OrderTable 단체지정된_주문테이블() {
        OrderTable orderTable = OrderTable.of(0, true);
        orderTable.changeTableGroup(단체지정());
        return orderTable;
    }
}
