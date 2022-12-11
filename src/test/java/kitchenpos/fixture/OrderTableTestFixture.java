package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableTestFixture {

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(null, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable 주문테이블2() {
        return createOrderTable(2L, null, 20, false);
    }

    public static OrderTable 주문테이블1() {
        return createOrderTable(1L, null, 10, false);
    }
}
