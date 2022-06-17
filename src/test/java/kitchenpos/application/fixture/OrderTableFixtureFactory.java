package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable create(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable create(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createWithGuest(Long id, boolean empty, int numberOfGuest) {
        OrderTable orderTable = create(id, empty);
        orderTable.setNumberOfGuests(numberOfGuest);
        return orderTable;
    }

    public static OrderTable createWithGuest(Long id, Long tableGroupId, boolean empty, int numberOfGuest) {
        OrderTable orderTable = create(id, empty);
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
