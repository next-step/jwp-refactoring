package kitchenpos.application.sample;

import kitchenpos.domain.OrderTable;

public class OrderTableSample {

    public static OrderTable notEmptyFiveGuestsOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(5);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable emptyTwoGuestsOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable emptyThreeGuestsOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(3L);
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
        return orderTable;
    }
}
