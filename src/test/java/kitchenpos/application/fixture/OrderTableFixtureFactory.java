package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable create(long id, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    public static OrderTable createWithGuests(long id, boolean isEmpty, int numberOfGuests) {
        OrderTable orderTable = create(id, isEmpty);
        orderTable.setNumberOfGuests(numberOfGuests);

        return orderTable;
    }
}
