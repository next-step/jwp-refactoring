package kitchenpos.fixture;


import kitchenpos.table.domain.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable createWithGuest(boolean empty, int numberOfGuest) {
        return OrderTable.of(empty, numberOfGuest);
    }
}
