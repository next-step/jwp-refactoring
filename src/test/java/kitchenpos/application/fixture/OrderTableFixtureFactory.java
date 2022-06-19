package kitchenpos.application.fixture;


import kitchenpos.domain.table.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable create(boolean empty) {
        return OrderTable.of(empty);
    }

    public static OrderTable createWithGuest(boolean empty, int numberOfGuest) {
        return OrderTable.of(empty, numberOfGuest);
    }
}
