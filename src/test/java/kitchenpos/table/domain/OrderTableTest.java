package kitchenpos.table.domain;

import static kitchenpos.table.domain.TableGroupTest.테이블그룹;

public class OrderTableTest {
    public static final OrderTable 빈자리 = OrderTable.ofEmptyTable();
    public static final OrderTable 이인석 = new OrderTable(1L, 테이블그룹, 2, false);
}
