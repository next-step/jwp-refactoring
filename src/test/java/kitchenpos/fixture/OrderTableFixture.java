package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

}
