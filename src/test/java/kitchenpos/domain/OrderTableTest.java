package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;

import static kitchenpos.domain.TableGroupTest.테이블그룹;

public class OrderTableTest {
    public static final OrderTable 빈자리 = OrderTable.ofEmptyTable(2);
    public static final OrderTable 이인석 = new OrderTable(1L, 테이블그룹, 2, false);
}
