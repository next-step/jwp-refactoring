package kitchenpos.fixture;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableFactory {
    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(id, tableGroupId, NumberOfGuests.from(numberOfGuests), empty);
        return orderTable;
    }

    public static OrderTableRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }
}
