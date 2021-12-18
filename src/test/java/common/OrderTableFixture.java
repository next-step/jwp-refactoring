package common;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 첫번째_주문테이블() {
        return new OrderTable(1L, null, 1, false);
    }

    public static OrderTable 두번째_주문테이블() {
        return new OrderTable(2L, null, 2, false);
    }

    public static OrderTable 단체지정_첫번째_주문테이블() {
        return new OrderTable(1L, null, 1, true);
    }

    public static OrderTable 단체지정_두번째_주문테이블() {
        return new OrderTable(2L, null, 2, true);
    }
}
