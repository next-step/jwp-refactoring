package kitchenpos.factory;

import kitchenpos.orderTable.domain.OrderTable;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }
}
