package kitchenpos.ordertable.testfixture;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableTestFixture {

    public static OrderTable create(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }

    public static OrderTable create(int numberOfGuests, boolean empty) {
        return OrderTable.of(null, numberOfGuests, empty);
    }
}
