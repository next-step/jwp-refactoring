package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable 주문_개인테이블 = new OrderTable();
    public static OrderTable 빈_개인테이블 = new OrderTable();
    public static OrderTable 주문1_단체테이블 = new OrderTable();
    public static OrderTable 주문2_단체테이블 = new OrderTable();

    static {
        주문_개인테이블.setId(1L);
        주문_개인테이블.setEmpty(false);

        빈_개인테이블.setId(2L);
        빈_개인테이블.setEmpty(true);

        주문1_단체테이블.setId(3L);
        주문1_단체테이블.setEmpty(false);

        주문2_단체테이블.setId(4L);
        주문2_단체테이블.setEmpty(false);
    }

    private OrderTableFixture() {}
}
