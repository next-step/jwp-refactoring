package kitchenpos.ordertable.application.fixture;


import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable 빈_테이블() {
        return OrderTable.of(0, true);
    }

    public static OrderTable 한명_주문테이블() {
        OrderTable orderTable = OrderTable.of(1, false);
        orderTable.group(1L);
        return orderTable;
    }

    public static OrderTable 단체지정된_주문테이블() {
        OrderTable orderTable = OrderTable.of(1, false);
        orderTable.group(1L);
        return orderTable;
    }
}
