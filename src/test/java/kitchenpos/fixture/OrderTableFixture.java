package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static final OrderTable orderTable1 = OrderTable.of(1L, null, 4, true);
    public static final OrderTable orderTable2 = OrderTable.of(2L, null, 6, true);
}
