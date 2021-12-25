package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {

    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }
}
