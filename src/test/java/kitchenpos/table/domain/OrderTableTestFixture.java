package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableRequest;

public class OrderTableTestFixture {
    public static final OrderTableRequest 비어있는_테이블_요청 = orderTableRequest(0, true);
    public static final OrderTableRequest 주문테이블_요청 = orderTableRequest(2, false);
    public static final OrderTable 비어있는_테이블 = orderTable(1L, null, 0, true);
    public static final OrderTable 비어있는_테이블2 = orderTable(3L, null, 0, true);
    public static final OrderTable 주문테이블 = orderTable(2L, null, 2, false);
    public static final OrderTable 주문테이블2 = orderTable(5L, null, 2, false);
    public static final OrderTable 단체테이블 = orderTable(4L, 1L, 2, false);

    public static OrderTableRequest orderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTable orderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable orderTable(int numberOfGuests, boolean empty) {
        return OrderTable.of(null, null, numberOfGuests, empty);
    }
}
