package kitchenpos.table.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {
    private OrderTableFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderTable create(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }
}
