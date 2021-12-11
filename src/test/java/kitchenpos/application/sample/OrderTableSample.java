package kitchenpos.application.sample;

import kitchenpos.domain.OrderTable;

public class OrderTableSample {

    public static OrderTable 채워진_다섯명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable 빈_두명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 빈_세명_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(3L);
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
        return orderTable;
    }
}
