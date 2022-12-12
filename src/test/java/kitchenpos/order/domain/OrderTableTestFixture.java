package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class OrderTableTestFixture {

    public static OrderTable generateOrderTable(Long id, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }

    public static OrderTableRequest generateOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
