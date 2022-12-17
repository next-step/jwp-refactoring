package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.dto.OrderTableRequest;

public class OrderTableTestFixture {

    public static OrderTable generateOrderTable(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTableRequest generateOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
