package kitchenpos.domain;

import kitchenpos.dto.OrderTableRequest;

public class OrderTableTestFixture {

    public static OrderTable generateOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(null, tableGroup, numberOfGuests, empty);
    }

    public static OrderTable generateOrderTable(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }

    public static OrderTableRequest generateOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
