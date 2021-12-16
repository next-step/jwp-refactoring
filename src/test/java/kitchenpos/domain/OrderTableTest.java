package kitchenpos.domain;

public class OrderTableTest {
    public static final OrderTable 빈자리 = OrderTable.ofEmptyTable(2);
    public static final OrderTable 이인석 = new OrderTable(1L, 1L, 2, false);
}
