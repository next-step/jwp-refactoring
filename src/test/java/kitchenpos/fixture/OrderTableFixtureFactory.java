package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {
    private OrderTableFixtureFactory() {
    }

    public static OrderTable createEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createNotEmptyOrderTable(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
