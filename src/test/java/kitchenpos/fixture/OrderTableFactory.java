package kitchenpos.fixture;

import kitchenpos.dto.OrderTableRequest;

public class OrderTableFactory {
    public static OrderTableRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
