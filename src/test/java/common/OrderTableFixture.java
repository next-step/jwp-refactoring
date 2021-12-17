package common;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 첫번째_테이블() {
        return new OrderTable(1L, null, 1, false);
    }

    public static OrderTable 두번째_테이블() {
        return new OrderTable(2L, null, 2, false);
    }
}
