package kitchenpos;

import kitchenpos.table.dto.OrderTableRequest;

public class TestOrderTableRequestFactory {
    public static OrderTableRequest create(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
