package common;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableStatus;
import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableFixture {

    public static OrderTable 첫번째_주문테이블() {
        return OrderTable.of(1L, 1, OrderTableStatus.USE);
    }

    public static OrderTable 두번째_주문테이블() {
        return OrderTable.of(2L, 2, OrderTableStatus.USE);
    }

    public static OrderTable 단체지정_첫번째_주문테이블() {
        return OrderTable.of(1L, 1, OrderTableStatus.EMPTY);
    }

    public static OrderTable 단체지정_두번째_주문테이블() {
        return OrderTable.of(2L, 2, OrderTableStatus.EMPTY);
    }

    public static OrderTableRequest from(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests(),
            orderTable.isEmpty());
    }
}
