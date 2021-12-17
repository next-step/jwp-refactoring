package common;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTable 주문_첫번째_1번_테이블() {
        return new OrderTable(1L, 1L, 1, false);
    }

    public static OrderTable 주문_두번째_2번_테이블() {
        return new OrderTable(2L, 2L, 2, false);
    }
}
