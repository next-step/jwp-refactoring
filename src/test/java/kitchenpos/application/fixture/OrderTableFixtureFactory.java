package kitchenpos.application.fixture;


import kitchenpos.domain.table.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable createWithGuest(boolean empty, int numberOfGuest) {
        return OrderTable.of(empty, numberOfGuest);
    }
}
