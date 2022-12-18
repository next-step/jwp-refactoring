package kitchenpos.table.domain;

import kitchenpos.tablegroup.dto.OrderTableRequest;

public class OrderTableFixture {
    private OrderTableFixture() {
    }

    public static OrderTableRequest orderTableParam(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests, false);
    }

    public static OrderTableRequest orderTableParam(boolean empty) {
        return new OrderTableRequest(0, empty);
    }

    public static OrderTableRequest orderTableParam(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTable savedOrderTable(Long id, boolean empty) {
        return new OrderTable(id, null, 0, empty);
    }

    public static OrderTable savedOrderTable(Long id, Long tableGroupId) {
        return new OrderTable(id, tableGroupId, 0, false);
    }

    public static OrderTable savedOrderTable(Long id, Long tableGroupId, boolean empty) {
        return new OrderTable(id, tableGroupId, 0, empty);
    }

    public static OrderTable savedOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
