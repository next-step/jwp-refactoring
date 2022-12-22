package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable create(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

}
